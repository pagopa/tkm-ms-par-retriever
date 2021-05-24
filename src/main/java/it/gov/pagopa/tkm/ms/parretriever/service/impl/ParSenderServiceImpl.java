package it.gov.pagopa.tkm.ms.parretriever.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.gov.pagopa.tkm.ms.parretriever.client.cards.ParlessCardsClient;
import it.gov.pagopa.tkm.ms.parretriever.client.cards.model.response.ParlessCardResponse;
import it.gov.pagopa.tkm.ms.parretriever.client.consent.ConsentClient;
import it.gov.pagopa.tkm.ms.parretriever.client.consent.model.response.ConsentRequestEnum;
import it.gov.pagopa.tkm.ms.parretriever.client.consent.model.response.GetConsentResponse;
import it.gov.pagopa.tkm.ms.parretriever.client.mastercard.MastercardParClient;
import it.gov.pagopa.tkm.ms.parretriever.constant.CircuitEnum;
import it.gov.pagopa.tkm.ms.parretriever.model.topic.ReadQueue;
import it.gov.pagopa.tkm.ms.parretriever.model.topic.Token;
import it.gov.pagopa.tkm.ms.parretriever.service.ParSenderService;
import org.bouncycastle.openpgp.PGPException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static it.gov.pagopa.tkm.ms.parretriever.constant.Constants.MAX_NUMBER_OF_CARDS;

public class ParSenderServiceImpl implements ParSenderService {

    @Autowired
    ParlessCardsClient parlessCardsClient;

    @Autowired
    ConsentClient consentClient;

    @Autowired
    MastercardParClient mastercardParClient;

    @Autowired
    ProducerService producerService;

    @Autowired
    ObjectMapper objectMapper;
    //RETRIEVE CARDS LIST FROM S2
    private List<ParlessCardResponse> getParlessCards() {
            return parlessCardsClient.getParlessCards(MAX_NUMBER_OF_CARDS);
    }

    private GetConsentResponse getConsent(ParlessCardResponse parlessCardResponse) {
        return consentClient.getConsent(parlessCardResponse.getTaxCode(), parlessCardResponse.getHpan(), null);

    }

    private String getParValueFromCircuit(CircuitEnum circuit, String pan){
        switch (circuit){
            case MASTERCARD:
                return getParFromMasterCardCircuit(pan);
            default: return null;
        }
    }

    private String getParFromMasterCardCircuit(String pan) {
        try {
            return mastercardParClient.getPar(pan).getEncryptedPayload().getEncryptedData().getPaymentAccountReference();
        } catch (Exception e){
            return null;
        }
    }

    public void addRecordToKafkaQueue() throws PGPException, JsonProcessingException {
        List<ParlessCardResponse> parlessCardResponses = getParlessCards();
        parlessCards:
        for (ParlessCardResponse parlessCardResponse : parlessCardResponses) {
            String par=null;
            String pan = parlessCardResponse.getPan();
            CircuitEnum circuit = parlessCardResponse.getCircuit();
            GetConsentResponse consent = getConsent(parlessCardResponse);

            switch(consent.getConsent()){
                case DENY: continue parlessCards;
                case ALLOW:
                    par= getParValueFromCircuit(circuit,pan);
                    break;
                case PARTIAL:
                    Boolean userAllows = consent.getDetails().stream()
                            .anyMatch(c->c.getConsent().equals(ConsentRequestEnum.ALLOW));
                    if (!userAllows){
                        continue parlessCards;
                    }
                    par= getParValueFromCircuit(circuit,pan);
                    break;
                default:break;
            }

            ReadQueue readQueue = new ReadQueue(parlessCardResponse.getTaxCode(),
                    parlessCardResponse.getPan(),
                    parlessCardResponse.getHpan(),
                    par,
                    circuit,
                    getTokenListFromStringSet(parlessCardResponse.getTokens()));

            producerService.sendMessageV2(objectMapper.writeValueAsString(readQueue));


        }

    }

    private List<Token> getTokenListFromStringSet(Set<String> tokens){
       return  tokens.stream().map(z-> new Token(z, null)).collect(Collectors.toList());
    }

}