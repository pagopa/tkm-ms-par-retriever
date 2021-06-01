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

    @Value("${keyvault.amexClientId}")
    private String clientId;

    @Value("${keyvault.amexClientSecret}")
    private String clientSecret;

    private static final String RETRIEVE_PAR_URL = "https://api.qa.americanexpress.com/payments/digital/v2/product_account_reference";

    public String getPar(String pan) throws IOException {
        Properties properties = new Properties();
      //  properties.put("CLIENT_KEY", clientId);
        properties.put("CLIENT_KEY", "??");
        //properties.put("CLIENT_SECRET", clientSecret);
        properties.put("CLIENT_SECRET", "??");
         properties.put("RETRIEVE_PAR_URL", RETRIEVE_PAR_URL);
        PropertiesConfigurationProvider configurationProvider = new PropertiesConfigurationProvider();
        configurationProvider.setProperties(properties);

        AuthProvider authProvider = HmacAuthBuilder.getBuilder()
                .setConfiguration(configurationProvider)
                .build();

        String url = configurationProvider.getValue("RETRIEVE_PAR_URL");
        HttpUrl httpUrl = HttpUrl.parse(url);
        if (httpUrl == null) {
            throw new IOException("HttpUrl not parsable");
        }

     //   String amexParRequest = mapper.writeValueAsString(new AmexParRequest(pan));
      //  System.out.println("AMEX PAR REQUEST: " + amexParRequest);
        String amexParRequest = "[\"374313476701813\"]";
        Map<String, String> headers = authProvider.generateAuthHeaders(amexParRequest, url, "POST");
        RequestBody body = RequestBody.create(amexParRequest, MediaType.parse("application/json; charset=utf-8"));
        Request.Builder builder = new Request.Builder()
                .url(url)
                .post(body);
        for (Map.Entry<String, String> header : headers.entrySet()) {
            builder.addHeader(header.getKey(), header.getValue());
        }
        Request request = builder.build();
        OkHttpClient httpClient = new OkHttpClient.Builder().build();
        Response response = httpClient.newCall(request).execute();
       // ResponseBody responseBody = response.body();
        String responseBodyString = response.body().string();
        System.out.println(responseBodyString);

        String fakeResponse = "{\"input\":\"374313476701813\",\"par\":\"A000FWD79TSY53DF135Z9H3TESTE2\",\"success\":true}";
        if (responseBodyString != null) {
            AmexParResponse amexParResponse = mapper.readValue(fakeResponse, AmexParResponse.class);
            return amexParResponse.getPar();
        }
        return null;
    }


    public static void main (String... args) throws IOException {
        retrievePar("[\"374313476701813\"]");
    }


    public static String retrievePar(String searchPayload) throws IOException {
        Properties properties = new Properties();
        properties.put("CLIENT_KEY", "PfsU4JWgDJZ4mJu9GgKqFRloIBZ7I3DT");
        properties.put("CLIENT_SECRET", "vF9ABaTV5pG7m92U83EJj5roYBkwAqQJ");
        properties.put("RETRIEVE_PAR_URL", RETRIEVE_PAR_URL);
        PropertiesConfigurationProvider configurationProvider = new PropertiesConfigurationProvider();
        configurationProvider.setProperties(properties);

        AuthProvider authProvider = HmacAuthBuilder.getBuilder()
                .setConfiguration(configurationProvider)
                .build();

        String url = configurationProvider.getValue("RETRIEVE_PAR_URL");

        // This generates the AMEX specific authentication headers needed for this API.
        Map<String, String> headers = authProvider.generateAuthHeaders(searchPayload, url, "POST");

        RequestBody body = RequestBody.create(searchPayload, MediaType.parse("application/json; charset=utf-8"));
        Request.Builder builder = new Request.Builder()
                .url(url)
                .post(body);

        for (Map.Entry<String, String> header : headers.entrySet()) {
            builder.addHeader(header.getKey(), header.getValue());
        }
        Request request = builder.build();

        OkHttpClient httpClient = new OkHttpClient.Builder().build();
        Response response = httpClient.newCall(request).execute();

        String responseBody = response.body().string();

        System.out.println(responseBody);

        return responseBody;
    }


}
