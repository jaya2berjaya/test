package com.arism;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "com.arism.model")
@EnableJpaRepositories(basePackages = "com.arism.repository")
public class SpringstoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringstoreApplication.class, args);
    }

}
