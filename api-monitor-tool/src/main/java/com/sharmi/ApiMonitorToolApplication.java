package com.sharmi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class ApiMonitorToolApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiMonitorToolApplication.class, args);
    }

    // This creates the tool used to ping other APIs
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}