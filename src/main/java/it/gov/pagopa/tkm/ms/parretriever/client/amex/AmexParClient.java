package it.gov.pagopa.tkm.ms.parretriever.client.amex;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import it.gov.pagopa.tkm.ms.parretriever.client.amex.configuration.PropertiesConfigurationProvider;
import it.gov.pagopa.tkm.ms.parretriever.client.amex.security.authentication.AuthProvider;
import it.gov.pagopa.tkm.ms.parretriever.client.amex.security.authentication.HmacAuthBuilder;
import okhttp3.*;

public class AmexParClient {

    public static void main(String[] args) throws Exception {
     //   retrievePar("378282246310005"); //400 Bad Request
     //   retrievePar("4582822466310005"); //400 Bad Request
     //   retrievePar("ORTX123RQGGD6Z1"); //400 Bad Request

     //   retrievePar("\"378282246310005\""); //400 Bad Request
     //   retrievePar("\"4582822466310005\""); //400 Bad Request
     //   retrievePar("\"ORTX123RQGGD6Z1\""); //400 Bad Request

     //   retrievePar("[374313476701813]"); //500 Internal Server Error
     //   retrievePar("[4582822466310005]"); //500 Internal Server Error
     //   retrievePar("[ORTX123RQGGD6Z1]"); //400 Bad Request

     //   retrievePar("[\"374313476701813\"]"); //500 Internal Server Error
     //   retrievePar("[\"074313476701813\"]"); //200 OK - Must start with 34 or 37
     //   retrievePar("[\"4582822466310005\"]"); //207 Multi Status
     //   retrievePar("[\"ORTX123RQGGD6Z1\"]"); //500 Internal Server Error

     //   retrievePar("{378282246310005}"); //400 Bad Request
     //   retrievePar("{4582822466310005}"); //400 Bad Request
     //   retrievePar("{ORTX123RQGGD6Z1}"); //400 Bad Request

     //   retrievePar("{\"378282246310005}\""); //400 Bad Request
     //   retrievePar("{\"4582822466310005\"}"); //400 Bad Request
     //   retrievePar("{\"pan\":\"ORTX123RQGGD6Z1\"}"); //500 Internal Server Error
        retrievePar("{\"pan\":\"378282246310005\"}"); //500 Internal Server Error
     //   retrievePar("{\"pan\":\"ORTX123RQGGD6Z1\"}"); //500 Internal Server Error

    }

    //QA
    private static final String CLIENT_ID = "";
    private static final String CLIENT_SECRET = "";
    private static final String RETRIEVE_PAR_URL = "https://api.qa.americanexpress.com/payments/digital/v2/product_account_reference";

    private static final MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");

    public static String retrievePar(String searchPayload) throws IOException {
        Properties properties = new Properties();
        properties.put("CLIENT_KEY", CLIENT_ID);
        properties.put("CLIENT_SECRET", CLIENT_SECRET);
        /*?*/ properties.put("RETRIEVE_PAR_URL", RETRIEVE_PAR_URL);
        PropertiesConfigurationProvider configurationProvider = new PropertiesConfigurationProvider();
        configurationProvider.setProperties(properties);

        AuthProvider authProvider = HmacAuthBuilder.getBuilder()
                .setConfiguration(configurationProvider)
                .build();

        String url = configurationProvider.getValue("RETRIEVE_PAR_URL");

        HttpUrl httpUrl = HttpUrl.parse(url);
        HttpUrl.Builder httpUrlBuilder = httpUrl.newBuilder();

        // This generates the AMEX specific authentication headers needed for this API.
        Map<String, String> headers = authProvider.generateAuthHeaders(searchPayload, url, "POST");

        RequestBody body = RequestBody.create(JSON_MEDIA_TYPE, searchPayload);
        Request.Builder builder = new Request.Builder()
                .url(httpUrlBuilder.build())
                .post(body);

        for (Map.Entry<String, String> header : headers.entrySet()) {
            builder.addHeader(header.getKey(), header.getValue());
        }
        Request request = builder.build();

        OkHttpClient httpClient = new OkHttpClient.Builder().build();
        Response response = httpClient.newCall(request).execute();
        Headers requestHeaders = request.headers();
        for (int i = 0, size = requestHeaders.size(); i < size; i++) {
            System.out.println(requestHeaders.name(i) + ": " + requestHeaders.value(i));
        }

        String xxc = response.body().string();
        System.out.println(xxc);
        return xxc;
    }

}
