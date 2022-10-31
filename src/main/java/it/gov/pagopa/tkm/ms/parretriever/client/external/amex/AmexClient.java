package it.gov.pagopa.tkm.ms.parretriever.client.external.amex;

import com.fasterxml.jackson.databind.*;
import io.github.resilience4j.circuitbreaker.annotation.*;
import it.gov.pagopa.tkm.ms.parretriever.client.external.amex.api.configuration.*;
import it.gov.pagopa.tkm.ms.parretriever.client.external.amex.api.security.authentication.*;
import it.gov.pagopa.tkm.ms.parretriever.client.external.amex.model.response.*;
import lombok.extern.log4j.*;
import okhttp3.*;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import javax.annotation.*;
import java.io.*;
import java.util.*;

@Log4j2
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

    @Value("${circuit-activation.amex}")
    private String isAmexActive;

    private AuthProvider authProvider;

    private final OkHttpClient httpClient = new OkHttpClient.Builder().build();

    @PostConstruct
    public void init() throws IOException {
        if (isAmexActive.equals("true")) {
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
    }

    @CircuitBreaker(name = "amexClientCircuitBreaker", fallbackMethod = "getParFallback")
    public String getPar(String pan) throws IOException {
        String amexParRequest = "[\"" + pan + "\"]";
        Map<String, String> headers = authProvider.generateAuthHeaders(amexParRequest, retrieveParUrl,
                "POST");
        Request.Builder builder = new Request.Builder()
                .url(retrieveParUrl)
                .post(RequestBody.create(amexParRequest, MediaType.parse("application/json; charset=utf-8")));
        for (Map.Entry<String, String> header : headers.entrySet()) {
            builder.addHeader(header.getKey(), header.getValue());
        }
        ResponseBody responseBody = httpClient.newCall(builder.build()).execute().body();
        if (responseBody != null) {
            AmexParResponse[] parResponseArray = mapper.readValue(responseBody.string(), AmexParResponse[].class);
            if (ArrayUtils.isNotEmpty(parResponseArray)) {
                return parResponseArray[0].getPar();
            }
        }
        return null;
    }


    public String getParFallback(Throwable t ){
        log.error("AMEX fallback", t);
        return null;
    }



}