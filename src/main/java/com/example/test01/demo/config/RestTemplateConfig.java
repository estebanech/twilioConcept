package com.example.test01.demo.config;

import com.example.test01.demo.client.lord.errorhandler.LordErrorHandler;
import lombok.AllArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestTemplate;

@Configuration
@AllArgsConstructor
public class RestTemplateConfig {

    private final RestTemplateBuilder builder;

    @Bean(name = "lordRestTemplate")
    public RestTemplate lordRestTemplate() {
        return builder
                .errorHandler(new LordErrorHandler())
                .build();
    }

    @Primary
    @Bean(name = "identityRestTemplate")
    public RestTemplate identityRestTemplate(){
        return builder.build();
    }
}
