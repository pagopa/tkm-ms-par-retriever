package it.gov.pagopa.tkm.ms.parretriever;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.*;
import org.springframework.boot.autoconfigure.jdbc.*;
import org.springframework.boot.autoconfigure.orm.jpa.*;
import org.springframework.cloud.openfeign.*;

@SpringBootApplication
@EnableFeignClients
//TODO: REMOVE WHEN DB HAS BEEN CREATED
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
public class ParRetrieverApplication {

    public static void main(String[] args) {
        SpringApplication.run(ParRetrieverApplication.class, args);
    }

}
