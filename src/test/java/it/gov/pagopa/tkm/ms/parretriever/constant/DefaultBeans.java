package it.gov.pagopa.tkm.ms.parretriever.constant;

import it.gov.pagopa.tkm.ms.parretriever.client.external.amex.model.response.*;
import it.gov.pagopa.tkm.ms.parretriever.client.external.mastercard.api.model.*;
import it.gov.pagopa.tkm.ms.parretriever.client.external.visa.model.response.*;
import it.gov.pagopa.tkm.ms.parretriever.client.internal.cardmanager.model.response.*;
import org.springframework.batch.item.ExecutionContext;

import java.time.*;
import java.util.*;

public class DefaultBeans {

    public final String PAN_1 = "111111111111";
    public final String PAN_2 = "211111111111";
    public final String PAN_AMEX = "374477742523440";
    public final String PAN_MASTERCARD = "5340160579206608";
    public final String PAN_VISA = "4024007111341771";

    public final String PAR_1 = "abc11111111111";
    public final String PAR_2 = "xyz11111111111";
    public final String TAX_CODE = "PCCRLE04M24L219D";
    public final String HPAN_1 = "92fc472e8709cf61aa2b6f8bb9cf61aa2b6f8bd8267f9c14f58f59cf61aa2b6f";
    public final String HPAN_2 = "15fc472e8709cf61aa2b6f8bb9cf61aa2b6f8bd8267f9c14f58f59cf61aa2b6a";
    public final String HPAN_3 = "00fc472e8709cf61aa2b6f8bb9cf61aa2b6f8bd8267f9c14f58f59cf61aa2b00";

    public static final Instant INSTANT = Instant.EPOCH;

    public final AmexParResponse[] AMEX_PAR_RESPONSE = new AmexParResponse[]{new AmexParResponse(PAN_1, PAR_1, true)};

    public final VisaParEncryptedResponse VISA_PAR_ENC_RESPONSE = new VisaParEncryptedResponse("eyJlbmMiOiJBMTI4R0NNIiwiaWF0IjowLCJhbGciOiJSU0EtT0FFUC0yNTYiLCJraWQiOiJURVNUX0tFWV9JRCJ9.UdBI3Dy8AFg3PDPRjI453g7CK5Nq5YWnsaSOj199O9YhWRDd9Rf55b4ytF9pJ9jz_4MdhzzHSvVCbsTkQ5QAYir6tr9IbTtkn8eozGecUfKRowpztsFoWdN8mcr5bpot1bK5OVI2xQFmNixrj_ZPxkDk9FS1cxHIcMHiPm3K_YiaS_K8tMLng1oTH-XQkFE0AEM6P2HFyzun0SwkQP4nkYMI3wYgDPTEAos4orG2vgmFBR3CEXkQkkgodQu0YuOqwD8lhmp8fFTdHe-3nQ9u8k12z5whcOmcyFo7g8fDiGggTXZFbc_a5Unt9yvrzGiS4IHQk01RkK1eykh-rRCAuQ.eLGi-GPm_OJBv3jR.gjixjPlSXJhgjHm8OwxPPGfx343sy18NesE6ECtMKgtKUD7hEckmwHdYhPj_5DJ8srk1r3rgs35CWK_vX95fi2UrejQRG3vJZoKfqaeUCLfZwnI8xOQIGIO-J93WlXEkEcPl9jauF35aPWZx1ucGXxrheGnTnlJGWuHCYiUYQa8KD9x6TOpUzqk.TRwZRUVpEQW78ux2BnnA9g");
    public final VisaParDecryptedResponse VISA_PAR_DEC_RESPONSE = new VisaParDecryptedResponse(PAR_1, INSTANT.toString(), PAN_1);

    public final MastercardParResponse MASTERCARD_PAR_RESPONSE = new MastercardParResponse("RESPONSE_ID_TEST", new ParResponseEncryptedPayload(new ParResponseEncryptedData(PAR_1, INSTANT.toString())));

    public final String TOKEN_1 = "abcde123";
    public final String TOKEN_2 = "xyz6543";
    public final String TOKEN_3 = "aerr126";

    public final Set<String> TOKEN_SET_1 = new HashSet<>(Arrays.asList(TOKEN_1, TOKEN_2));
    public final Set<String> TOKEN_SET_2 = new HashSet<>(Arrays.asList(TOKEN_2, TOKEN_3));

    public final ParlessCard PARLESS_CARD_1 = new ParlessCard(
            TAX_CODE, PAN_1, HPAN_1, TOKEN_SET_1, CircuitEnum.AMEX
    );
    public final ParlessCard PARLESS_CARD_2 = new ParlessCard(
            TAX_CODE, PAN_2, HPAN_2, TOKEN_SET_2, CircuitEnum.VISA
    );

    public final ParlessCard PARLESS_AMEX_CARD = new ParlessCard(
            TAX_CODE, PAN_AMEX, HPAN_1, TOKEN_SET_1, CircuitEnum.AMEX
    );
    public final ParlessCard PARLESS_VISA_CARD = new ParlessCard(
            TAX_CODE, PAN_VISA, HPAN_2, TOKEN_SET_2, CircuitEnum.VISA
    );
    public final ParlessCard PARLESS_VISA_ELECTRON_CARD = new ParlessCard(
            TAX_CODE, PAN_VISA, HPAN_2, TOKEN_SET_2, CircuitEnum.VISA_ELECTRON
    );
    public final ParlessCard PARLESS_VPAY_CARD = new ParlessCard(
            TAX_CODE, PAN_VISA, HPAN_2, TOKEN_SET_2, CircuitEnum.VPAY
    );

    public final ParlessCard PARLESS_MASTERCARD_CARD = new ParlessCard(
            TAX_CODE, PAN_MASTERCARD, HPAN_3, TOKEN_SET_2, CircuitEnum.MASTERCARD
    );

    public final List<ParlessCard> PARLESS_CARDS_LIST = Arrays.asList(PARLESS_CARD_1, PARLESS_CARD_2);

    public final List<ParlessCard> PARLESS_CARD_LIST_6000 = createParlessCardList600();
    private  List<ParlessCard> PARLESS_AMEX_CARD_LIST_1800;
    private List<ParlessCard> PARLESS_VISA_CARD_LIST_2000;
    private List<ParlessCard> PARLESS_MASTERCARD_CARD_LIST_2200;

    public Map<String, ExecutionContext> EXECUTION_CONTEXT_MAP_15_THREADS = createExecutionsContextMap15Threads();


    private List<ParlessCard> createParlessCardList600(){
        List<ParlessCard> parlessCards = new ArrayList<>();
        PARLESS_AMEX_CARD_LIST_1800=createAmexParlessCardList180();
        PARLESS_VISA_CARD_LIST_2000=createVisaParlessCardList200();
        PARLESS_MASTERCARD_CARD_LIST_2200=createMastercardParlessCardList220();
        parlessCards.addAll(PARLESS_AMEX_CARD_LIST_1800);
        parlessCards.addAll(PARLESS_VISA_CARD_LIST_2000);
        parlessCards.addAll(PARLESS_MASTERCARD_CARD_LIST_2200);

        return parlessCards;
    }

    private List<ParlessCard> createAmexParlessCardList180(){
        List<ParlessCard> parlessCards = new ArrayList<>();

        for (int i=0; i<1800; i++){
            parlessCards.add(PARLESS_AMEX_CARD);
        }
        return parlessCards;
    }

    private List<ParlessCard> createVisaParlessCardList200(){
        List<ParlessCard> parlessCards = new ArrayList<>();

        for (int i=0; i<900; i++){
            parlessCards.add(PARLESS_VISA_CARD);
        }
        for (int i=0; i<600; i++){
            parlessCards.add(PARLESS_VISA_ELECTRON_CARD);
        }
        for (int i=0; i<500; i++){
            parlessCards.add(PARLESS_VPAY_CARD);
        }
        return parlessCards;
    }

    private List<ParlessCard> createMastercardParlessCardList220(){
        List<ParlessCard> parlessCards = new ArrayList<>();

        for (int i=0; i<2200; i++){
            parlessCards.add(PARLESS_MASTERCARD_CARD);
        }

        return parlessCards;
    }

    public final Map<String, ExecutionContext> createExecutionsContextMap15Threads(){
        Map<String, ExecutionContext> executionContextMap = new HashMap<>();
        executionContextMap=createAmexExecutionContextsMap(executionContextMap, 0);
        executionContextMap=createVisaExecutionContextsMap(executionContextMap, 5);
        executionContextMap=createMastercardExecutionContextsMap(executionContextMap, 10);

        return executionContextMap;

    };

    private Map<String, ExecutionContext> createAmexExecutionContextsMap(Map<String, ExecutionContext> executionContextMap, int circuitIndex){
        List<ParlessCard> amexCircuitCards= PARLESS_AMEX_CARD_LIST_1800;
        for (int i=0; i<5; i++){
            int step = 360;
            ExecutionContext value = new ExecutionContext();
            int from= i*step;
            int to= (i+1)*step;
            int j = 1+ i + circuitIndex;

            value.putInt("from", from);
            value.putInt("to", to);
            value.putString("name", "Thread" + j);
            value.put("cardList", new ArrayList<>(amexCircuitCards.subList(from,  to)));
            value.put("rateLimit", 1d);
            executionContextMap.put("partition" + j, value);
        }
        return executionContextMap;
    }


    private Map<String, ExecutionContext> createVisaExecutionContextsMap(Map<String, ExecutionContext> executionContextMap, int circuitIndex){
        List<ParlessCard> visaCircuitCards= PARLESS_VISA_CARD_LIST_2000;
        for (int i=0; i<5; i++){
            int step = 400;
            ExecutionContext value = new ExecutionContext();
            int from= i*step;
            int to= (i+1)*step;
            int j = 1+ i + circuitIndex;

            value.putInt("from", from);
            value.putInt("to", to);
            value.putString("name", "Thread" + j);
            value.put("cardList", new ArrayList<>(visaCircuitCards.subList(from,  to)));
            value.put("rateLimit", 1d);
            executionContextMap.put("partition" + j, value);
        }
        return executionContextMap;
    }

    private Map<String, ExecutionContext> createMastercardExecutionContextsMap(Map<String, ExecutionContext> executionContextMap, int circuitIndex){
        List<ParlessCard> mastercardCircuitCards= PARLESS_MASTERCARD_CARD_LIST_2200;
        for (int i=0; i<5; i++){
            int step = 440;
            ExecutionContext value = new ExecutionContext();
            int from= i*step;
            int to= (i+1)*step;
            int j = 1+ i + circuitIndex;

            value.putInt("from", from);
            value.putInt("to", to);
            value.putString("name", "Thread" + j);
            value.put("cardList", new ArrayList<>(mastercardCircuitCards.subList(from,  to)));
            value.put("rateLimit", 1d);
            executionContextMap.put("partition" + j, value);
        }
        return executionContextMap;
    }

}
