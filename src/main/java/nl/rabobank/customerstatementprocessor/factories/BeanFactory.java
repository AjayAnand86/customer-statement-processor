package nl.rabobank.customerstatementprocessor.factories;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import nl.rabobank.customerstatementprocessor.parsers.TransactionRecordsCsvParser;
import nl.rabobank.customerstatementprocessor.parsers.TransactionRecordsXmlParser;
import nl.rabobank.customerstatementprocessor.validators.TransactionRecordsValidator;

/**
 * Bean Factory to expose bean definitions.
 */
@Component
public class BeanFactory {
  /**
   * XML Parser for transaction report parsing.
   *
   * @return XML Parser.
   */
  @Bean
  public TransactionRecordsXmlParser xmlParser() {
    return new TransactionRecordsXmlParser();
  }

  /**
   * CSV Parser for transaction report parsing.
   *
   * @return CSV Parser.
   */
  @Bean
  public TransactionRecordsCsvParser csvParser() {
    return new TransactionRecordsCsvParser();
  }

  /**
   * Validator for transaction report parsing.
   *
   * @return Validator.
   */
  @Bean
  public TransactionRecordsValidator validator() {
    return new TransactionRecordsValidator();
  }

  /**
   * Password Encoder for basic in-memory authentication.
   *
   * @return B2 Password Encoder.
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
