package com.ruc.payments.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * Web configuration to register custom message converters.
 * This allows handling JSON content sent with text/html content type headers.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    private final CustomMessageConverter customMessageConverter;
    
    public WebConfig(CustomMessageConverter customMessageConverter) {
        this.customMessageConverter = customMessageConverter;
    }
    
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        // Add our custom converter at the beginning so it gets priority
        converters.add(0, customMessageConverter);
    }
}
