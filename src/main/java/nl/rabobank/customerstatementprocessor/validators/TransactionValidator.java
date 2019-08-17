package nl.rabobank.customerstatementprocessor.validators;

import java.math.BigDecimal;
import java.util.Collections;
import javax.validation.constraints.NotNull;
import org.apache.commons.validator.routines.IBANValidator;
import nl.rabobank.customerstatementprocessor.model.Transaction;
import nl.rabobank.customerstatementprocessor.properties.ProjectConstants;

/**
 * Validator for transaction class objects.
 */
public class TransactionValidator implements Validator<Transaction> {
  /**
   * Validates transaction object.
   *
   * @param transaction transaction to be validated.
   *
   * @return validation result.
   */
  @Override
  public ValidationResult validate(final @NotNull Transaction transaction) {
    if (!validateEndBalance(transaction)) {
      return new ValidationResult(
          new ValidationError(Long.toString(transaction.getTransactionReference()),
              ProjectConstants.END_BALANCE_VALIDATION_FAILED));
    }
    if (!validateAccountNumber(transaction)) {
      return new ValidationResult(
          new ValidationError(Long.toString(transaction.getTransactionReference()),
              ProjectConstants.IBAN_VALIDATION_FAILED));
    }
    if (!validateTransactionReference(transaction)) {
      return new ValidationResult(
          new ValidationError(Long.toString(transaction.getTransactionReference()),
              ProjectConstants.INVALID_TRANSACTION_REFERENCE));
    }
    if (!validateMutation(transaction)) {
      return new ValidationResult(new ValidationError(
          Long.toString(transaction.getTransactionReference()), ProjectConstants.INVALID_MUTATION));
    }

    return new ValidationResult(Collections.emptyList());
  }


  /**
   * Validates the account number. It needs to be valid IBAN number according to the Apache IBAN
   * Validator.
   * 
   * @param transaction transaction object.
   * @return <code>true</code> if the field is valid, <code>false</code> otherwise.
   */
  private boolean validateAccountNumber(final @NotNull Transaction transaction) {
    return IBANValidator.getInstance().isValid(transaction.getAccountNumber());
  }

  /**
   * Validates the ending balance. It needs to be equal to the sum of starting balance amount and
   * the mutation amount.
   * 
   * @param transaction transaction object.
   * @return <code>true</code> if the field is valid, <code>false</code> otherwise.
   */
  private boolean validateEndBalance(final @NotNull Transaction transaction) {
    return (transaction.getEndBalance()
        .compareTo(transaction.getStartBalance().add(transaction.getMutation())) == 0);
  }

  /**
   * Validates the transaction reference. It needs to be greater than zero.
   * 
   * @param transaction transaction object.
   * @return <code>true</code> if the field is valid, <code>false</code> otherwise.
   */
  private boolean validateTransactionReference(final @NotNull Transaction transaction) {
    return (transaction.getTransactionReference() > 0);
  }

  /**
   * Validates the mutation amount. It needs to be non-zero. (This constraint is based on an
   * assumption, not from the assessment.)
   * 
   * @param transaction transaction object.
   * @return <code>true</code> if the field is valid, <code>false</code> otherwise.
   */
  private boolean validateMutation(final @NotNull Transaction transaction) {
    return (transaction.getMutation().compareTo(BigDecimal.ZERO) != 0);
  }
}
