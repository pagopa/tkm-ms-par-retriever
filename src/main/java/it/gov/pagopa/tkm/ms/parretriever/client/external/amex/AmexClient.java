package it.gov.pagopa.tkm.ms.parretriever.client.external.amex;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import com.fasterxml.jackson.databind.*;
import it.gov.pagopa.tkm.ms.parretriever.client.external.amex.api.configuration.PropertiesConfigurationProvider;
import it.gov.pagopa.tkm.ms.parretriever.client.external.amex.api.security.authentication.AuthProvider;
import it.gov.pagopa.tkm.ms.parretriever.client.external.amex.api.security.authentication.HmacAuthBuilder;

import it.gov.pagopa.tkm.ms.parretriever.client.external.amex.model.response.*;
import okhttp3.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import javax.annotation.*;

@Component
public class AmexClient {

    @Autowired
    private ObjectMapper mapper;

    @Value("${keyvault.amexClientId}")
    private String clientId;

    @Value("${keyvault.amexClientSecret}")
    private String clientSecret;

    @Value("${circuit-urls.amex}")
    private String retrieveParUrl;

    private AuthProvider authProvider;

    private final OkHttpClient httpClient = new OkHttpClient.Builder().build();

    @PostConstruct
    public void init() throws IOException {
        Properties properties = new Properties();
        properties.put("CLIENT_KEY", clientId);
        properties.put("CLIENT_SECRET", clientSecret);
        properties.put("RETRIEVE_PAR_URL", retrieveParUrl);
        PropertiesConfigurationProvider configurationProvider = new PropertiesConfigurationProvider();
        configurationProvider.setProperties(properties);
        authProvider = HmacAuthBuilder.getBuilder()
                .setConfiguration(configurationProvider)
                .build();
    }

    public String getPar(String pan) throws IOException {
        String amexParRequest = "[\"" + pan + "\"]";
        Map<String, String> headers = authProvider.generateAuthHeaders(amexParRequest, retrieveParUrl, "POST");
        RequestBody body = RequestBody.create(amexParRequest, MediaType.parse("application/json; charset=utf-8"));
        Request.Builder builder = new Request.Builder()
                .url(retrieveParUrl)
                .post(body);
        for (Map.Entry<String, String> header : headers.entrySet()) {
            builder.addHeader(header.getKey(), header.getValue());
        }
        Request request = builder.build();
        Response response = httpClient.newCall(request).execute();
        ResponseBody responseBody = response.body();
        if (responseBody != null) {
            return mapper.readValue(responseBody.string(), AmexParResponse[].class)[0].getPar();
        }
        return null;
    }

}