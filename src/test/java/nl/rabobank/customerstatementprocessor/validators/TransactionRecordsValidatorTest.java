package nl.rabobank.customerstatementprocessor.validators;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasProperty;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import java.math.BigDecimal;
import org.junit.Before;
import org.junit.Test;
import nl.rabobank.customerstatementprocessor.factories.TestObjectFactory;
import nl.rabobank.customerstatementprocessor.model.Transaction;
import nl.rabobank.customerstatementprocessor.model.TransactionRecords;
import nl.rabobank.customerstatementprocessor.properties.ProjectConstants;

public class TransactionRecordsValidatorTest {
  private TransactionRecordsValidator validator;

  @Before
  public void setUp() {
    this.validator = new TransactionRecordsValidator();
  }

  @Test
  public void whenTransactionFieldsAreProperThenValidationResultShouldHaveNoErrors() {
    // Given all fields are valid
    TransactionRecords transactionRecords = TestObjectFactory.createValidTransactionRecords();

    ValidationResult validationResult = this.validator.validate(transactionRecords);
    this.assertValidTransactionRecordsValidationResults(validationResult);
  }

  @Test
  public void whenAccountNumberIsNotValidForATransactionThenValidationResultShouldHaveErrors() {
    // Given all fields are valid
    TransactionRecords transactionRecords = TestObjectFactory.createValidTransactionRecords();

    // Invalidating account number
    Transaction invalidTransaction = transactionRecords.getTransactions().get(0);
    invalidTransaction.setAccountNumber("NNNNN31232NNN02");

    ValidationResult validationResult = this.validator.validate(transactionRecords);
    this.assertInvalidTransactionRecordsValidationResults(validationResult, 1);
    this.assertInvalidIbanShouldExistInValidationResults(invalidTransaction,
        validationResult);

  }

  @Test
  public void whenEndBalanceIsNotValidForAtransactionThenValidationResultShouldHaveErrors() {
    // Given all fields are valid
    TransactionRecords transactionRecords = TestObjectFactory.createValidTransactionRecords();

    // Invalidating end balance, having `end-balance = start-balance + mutation + 10`
    Transaction invalidTransaction = transactionRecords.getTransactions().get(1);
    invalidTransaction.setEndBalance(invalidTransaction.getStartBalance()
        .add(invalidTransaction.getMutation()).add(BigDecimal.TEN));

    ValidationResult validationResult = this.validator.validate(transactionRecords);
    this.assertInvalidTransactionRecordsValidationResults(validationResult, 1);
    this.assertInvalidEndDateShouldExistInValidationResults(invalidTransaction,
        validationResult);
  }

  @Test
  public void whenReferenceNumberIsNotUniqueThenValidationResultShouldHaveErrors() {
    // Given all fields are valid
    TransactionRecords transactionRecords = TestObjectFactory.createValidTransactionRecords();

    // Duplicating reference number
    Transaction invalidTransaction1 = transactionRecords.getTransactions().get(0);
    invalidTransaction1.setTransactionReference(123456L);

    // Duplicating reference number
    Transaction invalidTransaction2 = transactionRecords.getTransactions().get(1);
    invalidTransaction2.setTransactionReference(123456L);

    ValidationResult validationResult = this.validator.validate(transactionRecords);
    this.assertInvalidTransactionRecordsValidationResults(validationResult, 1);
    this.assertInvalidTransactionReferenceShouldExistInValidationResults(invalidTransaction1,
        validationResult);
    this.assertInvalidTransactionReferenceShouldExistInValidationResults(invalidTransaction2,
        validationResult);
  }

  private void assertValidTransactionRecordsValidationResults(
      final ValidationResult validationResult) {
    assertNotNull("Validation result should not be null.", validationResult);
    assertTrue("Validation result should be validated.", validationResult.isValidated());
    assertThat("Validation errors should be empty.", validationResult.getValidationErrors(),
        is(empty()));
  }

  private void assertInvalidTransactionRecordsValidationResults(
      final ValidationResult validationResult, final int errorSize) {
    assertNotNull("Validation result should not be null.", validationResult);
    assertFalse("Validation result should not be validated.", validationResult.isValidated());
    assertThat("Validation errors should not be empty.", validationResult.getValidationErrors(),
        is(not(empty())));
    assertThat("Validation errors should have specified length.",
        validationResult.getValidationErrors().size(), is(errorSize));
  }

  private void assertInvalidTransactionReferenceShouldExistInValidationResults(final Transaction transaction,
      final ValidationResult validationResult) {

    assertThat("Validation errors should contain reference number.",
        validationResult.getValidationErrors(),
        hasItem(hasProperty("errorKey", is(Long.toString(transaction.getTransactionReference())))));
    assertThat("Validation errors should contain description.",
        validationResult.getValidationErrors(), hasItem(hasProperty("errorDescription",
            is(ProjectConstants.UNIQUE_REFERENCE_VALIDATION_FAILED))));

  }

  private void assertInvalidIbanShouldExistInValidationResults(
      final Transaction transaction, final ValidationResult validationResult) {

    assertThat("Validation errors should contain reference number.",
        validationResult.getValidationErrors(),
        hasItem(hasProperty("errorKey", is(Long.toString(transaction.getTransactionReference())))));
    assertThat("Validation errors should contain description.",
        validationResult.getValidationErrors(),
        hasItem(hasProperty("errorDescription", is(ProjectConstants.IBAN_VALIDATION_FAILED))));

  }
  
  private void assertInvalidEndDateShouldExistInValidationResults(
      final Transaction transaction, final ValidationResult validationResult) {

    assertThat("Validation errors should contain reference number.",
        validationResult.getValidationErrors(),
        hasItem(hasProperty("errorKey", is(Long.toString(transaction.getTransactionReference())))));
    assertThat("Validation errors should contain description.",
        validationResult.getValidationErrors(),
        hasItem(
            hasProperty("errorDescription", is(ProjectConstants.END_BALANCE_VALIDATION_FAILED))));

  }
}
