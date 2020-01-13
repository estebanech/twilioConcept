package com.example.test01.demo.client.identitycheck;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "identity")
public class IdentityCheckProperties {
    private String url;
}
