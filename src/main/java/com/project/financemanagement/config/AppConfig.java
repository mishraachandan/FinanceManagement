package com.project.financemanagement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class AppConfig {

    @Bean
    @Profile("dev")
    public String devMessage() {
        return "This is the Development environment.";
    }

    @Bean
    @Profile("prod")
    public String prodMessage() {
        return "This is the Production environment.";
    }

    @Bean
    @Profile("test")
    public String testMessage() {
        return "This is the Test environment.";
    }
}
