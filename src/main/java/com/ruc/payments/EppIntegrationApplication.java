


package com.ruc.payments;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the EPP Integration Spring Boot application.
 *
 * <p>Starts the Spring context and auto-configures all beans.</p>
 */
@SpringBootApplication
public class EppIntegrationApplication {
    /**
     * Application bootstrap method.
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(EppIntegrationApplication.class, args);
    }
}
