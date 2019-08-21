package nl.rabobank.customerstatementprocessor.validators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.validation.constraints.NotNull;
import nl.rabobank.customerstatementprocessor.model.Transaction;
import nl.rabobank.customerstatementprocessor.model.TransactionRecords;
import nl.rabobank.customerstatementprocessor.properties.ValidationConstants;

/**
 * Validator for transaction report.
 */
public class TransactionRecordsValidator implements Validator<TransactionRecords> {
  private final TransactionValidator transactionValidator;

  public TransactionRecordsValidator() {
    super();

    this.transactionValidator = new TransactionValidator();
  }

  /**
   * Validates the transaction report, by validating each transaction within.
   *
   * @param transactionRecords transaction records to validate.
   *
   * @return validation result.
   */
  @Override
  public ValidationResult validate(final @NotNull TransactionRecords transactionRecords) {
    // Initialization.
    List<ValidationError> errorList = new ArrayList<>();

    // If transactions is not null; then loops through the transactions. Then
    // 1. Groups all the transactions by the reference.
    // 2. Performs the operation for each transactions grouped by the transaction reference.
    Optional.ofNullable(transactionRecords.getTransactions()).orElse(Collections.emptyList())
        .stream().collect(Collectors.groupingBy(Transaction::getTransactionReference))
        .forEach((transactionReference, transactionList) -> {
          if (transactionList.size() > 1) {
            // If there are more than 1 (one) transactions for a transaction, then it breaks the
            // constraint of 'Unique Reference Number'.
            // Therefore, transaction should be added to error list, without even validating.
            errorList.add(new ValidationError(Long.toString(transactionReference),
                ValidationConstants.UNIQUE_REFERENCE_VALIDATION_FAILED));
          } else {
            // Otherwise, meaning that there is exactly 1 (one) transaction for the reference
            // number,
            // Append and validation error for the specific transaction.
            errorList.addAll(
                this.transactionValidator.validate(transactionList.get(0)).getValidationErrors());
          }
        });

    // Returns a new validation result containing the error list.
    return new ValidationResult(errorList);
  }
}
