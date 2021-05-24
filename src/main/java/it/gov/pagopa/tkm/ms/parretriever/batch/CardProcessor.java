package it.gov.pagopa.tkm.ms.parretriever.batch;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.gov.pagopa.tkm.ms.parretriever.visa.VisaApiClient;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
@StepScope
public class CardProcessor implements ItemProcessor</*TODO Card*/Object, String> {

    private static final String MLE_CLIENT_PRIVATE_KEY_PATH = "";
    private static final String MLE_SERVER_PUBLIC_CERTIFICATE_PATH = "";
    private static final String KEY_ID = "";

    @Override
    public String process(/*TODO Card*/Object item) throws Exception {
        //Controllo autorizzazione
        //Controllo PAR assente
        //Switch su circuito, e in caso di VISA:
        String reqPayload = "{\n" +
                "\"clientId\": \"0123456789012345678901234567999\",\n" +
                "\"correlatnId\": \"0123456789012345678901234567000\",\n" +
                "\"primaryAccount\": \"1234567898000000\",\n" +
                "}";
        String encryptedPayload = VisaApiClient.getEncryptedPayload(MLE_SERVER_PUBLIC_CERTIFICATE_PATH, reqPayload, KEY_ID);
        String encryptedResponseStr = VisaApiClient.invokeAPI("/par/v1/inquiry", "POST", encryptedPayload, KEY_ID);
        VisaApiClient.EncryptedResponse encryptedResponse = new ObjectMapper().readValue(encryptedResponseStr, VisaApiClient.EncryptedResponse.class);
        System.out.println("Encrypted Response: \n" + encryptedResponse.getEncData());
        String decryptedResponse = VisaApiClient.getDecryptedPayload(MLE_CLIENT_PRIVATE_KEY_PATH, encryptedResponse);
        System.out.println("Decrypted Response: \n" + decryptedResponse);
        return decryptedResponse;
    }

}
