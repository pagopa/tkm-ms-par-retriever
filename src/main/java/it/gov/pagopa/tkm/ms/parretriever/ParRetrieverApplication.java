package it.gov.pagopa.tkm.ms.parretriever;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.*;
import org.springframework.boot.autoconfigure.jdbc.*;
import org.springframework.boot.autoconfigure.orm.jpa.*;
import org.springframework.cloud.openfeign.*;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
@EnableFeignClients
public class ParRetrieverApplication {

    public static void main(String[] args) {
        SpringApplication.run(ParRetrieverApplication.class, args);
    }

}
