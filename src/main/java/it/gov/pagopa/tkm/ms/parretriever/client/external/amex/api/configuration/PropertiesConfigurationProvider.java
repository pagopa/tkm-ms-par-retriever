package it.gov.pagopa.tkm.ms.parretriever.client.external.amex.api.configuration;

import java.util.*;

public class PropertiesConfigurationProvider implements ConfigurationProvider {

    private Properties properties;

    public PropertiesConfigurationProvider() {
        properties = new Properties();
    }

    public void setProperties(Properties p) {
        this.properties = p;
    }

    @Override
    public String getValue(String key) {
        return (String) this.properties.get(key);
    }

}
