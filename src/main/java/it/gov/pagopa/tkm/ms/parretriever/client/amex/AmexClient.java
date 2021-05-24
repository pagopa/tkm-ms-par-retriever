package it.gov.pagopa.tkm.ms.parretriever.client.amex;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import com.fasterxml.jackson.databind.*;
import it.gov.pagopa.tkm.ms.parretriever.client.amex.api.configuration.PropertiesConfigurationProvider;
import it.gov.pagopa.tkm.ms.parretriever.client.amex.api.security.authentication.AuthProvider;
import it.gov.pagopa.tkm.ms.parretriever.client.amex.api.security.authentication.HmacAuthBuilder;
import it.gov.pagopa.tkm.ms.parretriever.client.amex.model.request.*;
import it.gov.pagopa.tkm.ms.parretriever.client.amex.model.response.*;
import okhttp3.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

@Component
public class AmexClient {

    @Autowired
    private ObjectMapper mapper;

    private static final String CLIENT_ID = "";
    private static final String CLIENT_SECRET = "";
    private static final String RETRIEVE_PAR_URL = "https://api.qa.americanexpress.com/payments/digital/v2/product_account_reference";

    public String getPar(String pan) throws IOException {
        Properties properties = new Properties();
        properties.put("CLIENT_KEY", CLIENT_ID);
        properties.put("CLIENT_SECRET", CLIENT_SECRET);
        properties.put("RETRIEVE_PAR_URL", RETRIEVE_PAR_URL);
        PropertiesConfigurationProvider configurationProvider = new PropertiesConfigurationProvider();
        configurationProvider.setProperties(properties);

        AuthProvider authProvider = HmacAuthBuilder.getBuilder()
                .setConfiguration(configurationProvider)
                .build();

        String url = configurationProvider.getValue("RETRIEVE_PAR_URL");

        HttpUrl httpUrl = HttpUrl.parse(url);
        HttpUrl.Builder httpUrlBuilder = httpUrl.newBuilder();
        String amexParRequest = mapper.writeValueAsString(new AmexParRequest(pan));
        Map<String, String> headers = authProvider.generateAuthHeaders(amexParRequest, url, "POST");
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), amexParRequest);
        Request.Builder builder = new Request.Builder()
                .url(httpUrlBuilder.build())
                .post(body);
        for (Map.Entry<String, String> header : headers.entrySet()) {
            builder.addHeader(header.getKey(), header.getValue());
        }
        Request request = builder.build();
        OkHttpClient httpClient = new OkHttpClient.Builder().build();
        Response response = httpClient.newCall(request).execute();
        if (response.body() != null) {
            AmexParResponse amexParResponse = mapper.readValue(response.body().string(), AmexParResponse.class);
            return amexParResponse.getPar();
        }
        return null;
    }

}
