package nl.rabobank.customerstatementprocessor.validators;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * Validation Error class.
 */
@Getter
@AllArgsConstructor
@ToString
@Builder
public class ValidationError {
  private String errorKey;
  private String errorDescription;
}
