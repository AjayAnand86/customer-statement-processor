package nl.rabobank.customerstatementprocessor.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import nl.rabobank.customerstatementprocessor.parsers.TransactionRecordsCsvParser;
import nl.rabobank.customerstatementprocessor.parsers.TransactionRecordsXmlParser;
import nl.rabobank.customerstatementprocessor.properties.SecurityProperties;
import nl.rabobank.customerstatementprocessor.services.ParserService;
import nl.rabobank.customerstatementprocessor.services.ValidationService;
import nl.rabobank.customerstatementprocessor.validators.TransactionRecordsValidator;

@Configuration
@ComponentScan(basePackages = {"nl.rabobank.customerstatementprocessor.factories",
    "nl.rabobank.customerstatementprocessor.properties"})

@EnableConfigurationProperties
public class TestConfiguration {
  @Bean(name = "testParserService")
  public ParserService parserService(final TransactionRecordsXmlParser xmlParser,
      final TransactionRecordsCsvParser csvParser) {
    return new ParserService(xmlParser, csvParser);
  }

  @Bean(name = "testValidationService")
  public ValidationService validationService(final TransactionRecordsValidator validator,
      final SecurityProperties securityProperties) {
    return new ValidationService(validator, securityProperties);
  }
}
