package nl.rabobank.customerstatementprocessor.services;

import java.util.Collections;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import nl.rabobank.customerstatementprocessor.model.TransactionRecords;
import nl.rabobank.customerstatementprocessor.parsers.ParserResult;
import nl.rabobank.customerstatementprocessor.properties.SecurityProperties;
import nl.rabobank.customerstatementprocessor.properties.SecurityProperties.Validation;
import nl.rabobank.customerstatementprocessor.validators.TransactionRecordsValidator;
import nl.rabobank.customerstatementprocessor.validators.ValidationError;
import nl.rabobank.customerstatementprocessor.validators.ValidationResult;

/**
 * Validation service to validate Transaction Report object.
 */
@Service
public class ValidationService {
  private final TransactionRecordsValidator validator;
  private final Validation validationProperties;

  @Autowired
  public ValidationService(final TransactionRecordsValidator validator,
      final SecurityProperties projectProperties) {
    this.validator = validator;
    this.validationProperties = projectProperties.getValidation();
  }

  /**
   * Validates the transaction report object.
   *
   * @param parserResult parsed result from the file content and mapped to the transaction Records.
   * @return validation result.
   */
  public ValidationResult validate(final ParserResult<TransactionRecords> parserResult){

    // Validates the result.
    ValidationResult validationResult =
        Optional.ofNullable(parserResult.getResult()).map(this.validator::validate).orElse(
            new ValidationResult(new ValidationError(validationProperties.getParserErrorsKey(),
                "Failed to parse transaction Records.")));

    // If the property (statement-processor.validation.append-parser-errors) is enabled (true),
    // append the unparsed records to the error list.
    if (validationProperties.isAppendParserErrors()) {
      // If the parser had any erroneous records that were not able to be parsed,
      // appends the parser errors to the result list.
      Optional.ofNullable(parserResult.getErrors()).orElse(Collections.emptyList())
          .forEach(error -> validationResult.getValidationErrors()
              .add(new ValidationError(validationProperties.getParserErrorsKey(), error)));
    }
    // Returns the validation result.
    return validationResult;



  }
}
