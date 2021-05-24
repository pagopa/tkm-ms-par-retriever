package it.gov.pagopa.tkm.ms.parretriever.client.amex.configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesConfigurationProvider implements  ConfigurationProvider{

    private Properties properties;

    public PropertiesConfigurationProvider() {
        properties = new Properties();
    }


    public Properties getProperties() {
        return this.properties;
    }

    public void setProperties(Properties p){
        this.properties = p;
    }


    @Override
    public String getValue(String key) {
        return (String)this.properties.get(key);
    }

    /**
     * Loads property details from the specified file path.
     * @throws IOException
     */
    public void loadProperties(String propertyFilePath) throws IOException {
        InputStream inputStream = new FileInputStream(propertyFilePath);
        loadProperties(inputStream);
    }

    /**
     * Loads property details from the specified input stream.
     * @throws IOException
     */
    public void loadProperties(InputStream inputStream) throws IOException {
        properties.load(inputStream);
    }
}
