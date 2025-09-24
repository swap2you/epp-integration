package com.ruk.payments.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * CORS configuration to allow the EPP test utility to call our endpoints.
 */
@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(false); // Set to false for file:// origins
        config.addAllowedOriginPattern("*"); // Allow all origins for testing
        config.addAllowedOrigin("file://"); // Explicitly allow file protocol
        config.addAllowedOrigin("null"); // Allow null origin for local files
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.setMaxAge(3600L); // Cache preflight for 1 hour
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        
        return new CorsFilter(source);
    }
}