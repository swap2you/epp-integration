package com.ruc.payments.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(EppProperties.class)
public class AppConfig {
    // Additional beans/config if needed
}
