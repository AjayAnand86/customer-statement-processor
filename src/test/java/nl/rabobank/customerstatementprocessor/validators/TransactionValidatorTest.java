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
import nl.rabobank.customerstatementprocessor.properties.ValidationConstants;

public class TransactionValidatorTest {
  private TransactionValidator validator;

  @Before
  public void setUp() {
    this.validator = new TransactionValidator();
  }

  @Test
  public void whenTransactionFieldsAreProperThenValidationResultShouldHaveNoErrors() {
    // Given all fields are valid
    Transaction transaction = TestObjectFactory.createValidTransaction();

    ValidationResult validationResult = this.validator.validate(transaction);
    this.assertValidTransactionValidationResults(validationResult);
  }

  @Test
  public void whenReferenceNumberIsNotPositiveThenValidationResultShouldHaveErrors() {
    // Given all fields are valid
    Transaction transaction = TestObjectFactory.createValidTransaction();

    // Invalidating reference number
    transaction.setTransactionReference(0L);

    ValidationResult validationResult = this.validator.validate(transaction);
    this.assertInvalidTransactionReferenceValidationResults(transaction, validationResult);

    // Given all fields are valid
    transaction = TestObjectFactory.createValidTransaction();

    // Invalidating reference number
    transaction.setTransactionReference(-1L);

    validationResult = this.validator.validate(transaction);
    this.assertInvalidTransactionReferenceValidationResults(transaction, validationResult);
  }

  @Test
  public void whenAccountNumberIsNotValidThenValidationResultShouldHaveErrors() {
    // Given all fields are valid
    Transaction transaction = TestObjectFactory.createValidTransaction();

    // Invalidating account number
    transaction.setAccountNumber("NNNNN31232NNN02");

    ValidationResult validationResult = this.validator.validate(transaction);
    this.assertInvalidIbanValidationResults(transaction, validationResult);
  }

  @Test
  public void whenMutationAmountIsZeroThenValidationResultShouldHaveErrors() {
    // Given all fields are valid
    Transaction transaction = TestObjectFactory.createValidTransaction();

    // Invalidating mutation, fixing the error from end-balance.
    transaction.setMutation(BigDecimal.ZERO);
    transaction.setEndBalance(transaction.getStartBalance().add(transaction.getMutation()));

    ValidationResult validationResult = this.validator.validate(transaction);
    this.assertInvalidMutationValidationResults(transaction, validationResult);
  }

  @Test
  public void whenEndBalanceIsNotValidThenValidationResultShouldHaveErrors() {
    // Given all fields are valid
    Transaction transaction = TestObjectFactory.createValidTransaction();

    // Invalidating end balance, having `end-balance = start-balance + mutation + 10`
    transaction.setEndBalance(
        transaction.getStartBalance().add(transaction.getMutation()).add(BigDecimal.TEN));

    ValidationResult validationResult = this.validator.validate(transaction);
    this.assertInvalidEndBalanceValidationResults(transaction, validationResult);
  }

  private void assertValidTransactionValidationResults(final ValidationResult validationResult) {
    assertNotNull("Validation result should not be null.", validationResult);
    assertTrue("Validation result should be validated.", validationResult.isValidated());
    assertThat("Validation errors should be empty.", validationResult.getValidationErrors(),
        is(empty()));
  }

  private void assertInvalidMutationValidationResults(final Transaction transaction,
      final ValidationResult validationResult) {
    assertNotNull("Validation result should not be null.", validationResult);
    assertFalse("Validation result should not be validated.", validationResult.isValidated());
    assertThat("Validation errors should not be empty.", validationResult.getValidationErrors(),
        is(not(empty())));
    assertThat("Validation errors should contain reference number.",
        validationResult.getValidationErrors(),
        hasItem(hasProperty("errorKey", is(Long.toString(transaction.getTransactionReference())))));
    assertThat("Validation errors should contain description.",
        validationResult.getValidationErrors(),
        hasItem(hasProperty("errorDescription", is(ValidationConstants.INVALID_MUTATION))));

  }

  private void assertInvalidTransactionReferenceValidationResults(final Transaction transaction,
      final ValidationResult validationResult) {
    assertNotNull("Validation result should not be null.", validationResult);
    assertFalse("Validation result should not be validated.", validationResult.isValidated());
    assertThat("Validation errors should not be empty.", validationResult.getValidationErrors(),
        is(not(empty())));
    assertThat("Validation errors should contain reference number.",
        validationResult.getValidationErrors(),
        hasItem(hasProperty("errorKey", is(Long.toString(transaction.getTransactionReference())))));
    assertThat("Validation errors should contain description.",
        validationResult.getValidationErrors(), hasItem(
            hasProperty("errorDescription", is(ValidationConstants.INVALID_TRANSACTION_REFERENCE))));

  }

  private void assertInvalidIbanValidationResults(final Transaction transaction,
      final ValidationResult validationResult) {
    assertNotNull("Validation result should not be null.", validationResult);
    assertFalse("Validation result should not be validated.", validationResult.isValidated());
    assertThat("Validation errors should not be empty.", validationResult.getValidationErrors(),
        is(not(empty())));
    assertThat("Validation errors should contain reference number.",
        validationResult.getValidationErrors(),
        hasItem(hasProperty("errorKey", is(Long.toString(transaction.getTransactionReference())))));
    assertThat("Validation errors should contain description.",
        validationResult.getValidationErrors(),
        hasItem(hasProperty("errorDescription", is(ValidationConstants.IBAN_VALIDATION_FAILED))));

  }

  private void assertInvalidEndBalanceValidationResults(final Transaction transaction,
      final ValidationResult validationResult) {
    assertNotNull("Validation result should not be null.", validationResult);
    assertFalse("Validation result should not be validated.", validationResult.isValidated());
    assertThat("Validation errors should not be empty.", validationResult.getValidationErrors(),
        is(not(empty())));
    assertThat("Validation errors should contain reference number.",
        validationResult.getValidationErrors(),
        hasItem(hasProperty("errorKey", is(Long.toString(transaction.getTransactionReference())))));
    assertThat("Validation errors should contain description.",
        validationResult.getValidationErrors(), hasItem(
            hasProperty("errorDescription", is(ValidationConstants.END_BALANCE_VALIDATION_FAILED))));

  }
}
