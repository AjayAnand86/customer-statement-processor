package nl.rabobank.customerstatementprocessor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * Main entry for the application.
 *
 * Enables configuration properties with annotation.
 */
@SpringBootApplication
@EnableConfigurationProperties
public class CustomerStatementProcessorApplication {
  public static void main(String[] args) {
    SpringApplication.run(CustomerStatementProcessorApplication.class, args);
  }
}

