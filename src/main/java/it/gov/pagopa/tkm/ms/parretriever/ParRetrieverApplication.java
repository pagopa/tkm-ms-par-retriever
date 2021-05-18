package it.gov.pagopa.tkm.ms.parretriever;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class ParRetrieverApplication {

    public static void main(String[] args) {
        SpringApplication.run(ParRetrieverApplication.class, args);
    }

}
