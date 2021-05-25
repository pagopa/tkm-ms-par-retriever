package it.gov.pagopa.tkm.ms.parretriever.client.mastercard.api.util;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.stream.*;
import lombok.*;

import java.io.StringReader;
import java.lang.reflect.Type;

@Data
@NoArgsConstructor
public class JSON {

    private Gson gson = new Gson();

    private boolean isLenientOnJson = false;

    public String serialize(Object obj) {
        return gson.toJson(obj);
    }

    @SuppressWarnings("unchecked")
    public <T> T deserialize(String body, Type returnType) {
        try {
            if (isLenientOnJson) {
                JsonReader jsonReader = new JsonReader(new StringReader(body));
                // see https://google-gson.googlecode.com/svn/trunk/gson/docs/javadocs/com/google/gson/stream/JsonReader.html#setLenient(boolean)
                jsonReader.setLenient(true);
                return gson.fromJson(jsonReader, returnType);
            } else {
                return gson.fromJson(body, returnType);
            }
        } catch (JsonParseException e) {
            // Fallback processing when failed to parse JSON form response body:
            // return the response body string directly for the String return type;
            if (returnType.equals(String.class)) {
                return (T) body;
            } else {
                throw (e);
            }
        }
    }

}
