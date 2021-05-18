package it.gov.pagopa.tkm.ms.parretriever.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.gov.pagopa.tkm.ms.parretriever.service.ParRetrieverService;
import it.gov.pagopa.tkm.ms.parretriever.visa.VisaApiClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class ParRetrieverServiceImpl implements ParRetrieverService {

    private static final String MLE_CLIENT_PRIVATE_KEY_PATH = "";
    private static final String MLE_SERVER_PUBLIC_CERTIFICATE_PATH = "";
    private static final String KEY_ID = "";

    @Override
    @Scheduled(cron = "${core.parRetrieverService.getPar.scheduler}")
    public void getPar() throws Exception {
        invokeVisaApi();
    }

    private void invokeVisaApi() throws Exception {
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
    }

}
