package it.gov.pagopa.tkm.ms.parretriever.client.amex.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import it.gov.pagopa.tkm.ms.parretriever.client.amex.exceptions.JsonException;


import java.util.Map;

public class JsonUtility {
    private final ObjectMapper mapper;
    private final static JsonUtility INSTANCE = new JsonUtility();

    private JsonUtility() {
        mapper = new ObjectMapper();

        mapper.setPropertyNamingStrategy(
                PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public static JsonUtility getInstance() {
        return INSTANCE;
    }

    public <T> T getObject(String jsonString, Class<T> objectClass) {
        try {
            return mapper.readValue(jsonString, objectClass);
        } catch (Exception e) {
            throw new JsonException("Exception mapping string to class, caused by " + e.getMessage(), e);
        }
    }

    public String getString(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (Exception e) {
            throw new JsonException("Exception writing object as string, caused by " + e.getMessage(), e);
        }
    }

    public String prettyString(String jsonString) {
        try {
            return getString(getObject(jsonString, Map.class));
        } catch(Exception e) {
            return jsonString;  //on error, just return the string passed in
        }
    }
}