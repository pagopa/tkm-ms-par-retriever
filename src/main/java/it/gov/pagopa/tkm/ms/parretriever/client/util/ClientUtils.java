package it.gov.pagopa.tkm.ms.parretriever.client.util;

import org.springframework.core.io.*;
import org.springframework.util.*;

import java.io.*;
import java.nio.charset.*;

public class ClientUtils {

    public static String resourceAsString(Resource resource) throws IOException {
        return StreamUtils.copyToString(resource.getInputStream(), Charset.defaultCharset());
    }

}
