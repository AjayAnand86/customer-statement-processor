package nl.rabobank.customerstatementprocessor.services;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasProperty;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import java.io.IOException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import nl.rabobank.customerstatementprocessor.config.TestConfiguration;
import nl.rabobank.customerstatementprocessor.factories.TestObjectFactory;
import nl.rabobank.customerstatementprocessor.model.TransactionRecords;
import nl.rabobank.customerstatementprocessor.parsers.ParserResult;
import nl.rabobank.customerstatementprocessor.properties.ProjectConstants;
import nl.rabobank.customerstatementprocessor.validators.ValidationResult;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestConfiguration.class})
public class ValidationServiceTest {
  @Autowired
  @Qualifier("testParserService")
  private ParserService parserService;

  @Autowired
  @Qualifier("testValidationService")
  private ValidationService validationService;

  @Test
  public void whenCsvTransactionFileIsValidThenValidationResultShouldHaveNoErrors()
      throws IOException {
    // Given all fields are valid
    String fileContent = TestObjectFactory.getValidCsvFileContent();

    // Parse file content.
    ParserResult<TransactionRecords> parserResult =
        this.parserService.parseFile(ProjectConstants.MIME_TYPE_TEXT_CSV, fileContent);

    // Validate file content.
    ValidationResult validationResult = validationService.validate(parserResult);

    this.assertValidationResults(validationResult);

    // Due to erroneous records.
    assertFalse("Validation result should have invalid records.", validationResult.isValidated());

    // Due to erroneous records.
    assertThat("Validation result should have exactly specific number of validation errors.",
        validationResult.getValidationErrors().size(), is(6));


    // Should contain transaction #112806 due to uniqueness-violation
    assertThat("Validation errors should contain reference number.",
        validationResult.getValidationErrors(), hasItem(hasProperty("errorKey", is("112806"))));

    assertThat("Validation errors should contain description.",
        validationResult.getValidationErrors(), hasItem(hasProperty("errorDescription",
            is(ProjectConstants.UNIQUE_REFERENCE_VALIDATION_FAILED))));

    // Should contain parser error
    assertThat("Validation errors should contain reference number.",
        validationResult.getValidationErrors(), hasItem(hasProperty("errorKey", is("PARSER"))));

  }

  @Test
  public void whenCsvTransactionFileHasEmptyRecordsThenValidationResultShouldHaveNoErrors()
      throws IOException {
    // Given all fields are valid
    String fileContent = TestObjectFactory.getEmptyCsvFileContent();

    ParserResult<TransactionRecords> parserResult =
        this.parserService.parseFile(ProjectConstants.MIME_TYPE_TEXT_CSV, fileContent);

    // Validate file content.
    ValidationResult validationResult = validationService.validate(parserResult);

    this.assertValidationResults(validationResult);

    assertTrue("Validation result should not have invalid records.",
        validationResult.isValidated());
    assertThat("Validation result should not have invalid records.",
        validationResult.getValidationErrors(), is(empty()));
  }

  @Test
  public void whenXmlTransactionFileIsValidThenValidationResultShouldHaveNoErrors()
      throws IOException {
    // Given all fields are valid
    String fileContent = TestObjectFactory.getValidXmlFileContent();

    ParserResult<TransactionRecords> parserResult =
        this.parserService.parseFile(ProjectConstants.MIME_TYPE_APPLICATION_XML, fileContent);

    // Validate file content.
    ValidationResult validationResult = validationService.validate(parserResult);

    this.assertValidationResults(validationResult);

    assertTrue("Validation result should not have invalid records.",
        validationResult.isValidated());
    assertThat("Validation result should not have invalid records.",
        validationResult.getValidationErrors(), is(empty()));
  }

  @Test
  public void whenXmlTransactionFileHasEmptyRecordsThenValidationResultShouldHaveNoErrors()
      throws IOException {
    // Given all fields are valid
    String fileContent = TestObjectFactory.getEmptyXmlFileContent();

    ParserResult<TransactionRecords> parserResult =
        this.parserService.parseFile(ProjectConstants.MIME_TYPE_APPLICATION_XML, fileContent);

    // Validate file content.
    ValidationResult validationResult = validationService.validate(parserResult);

    this.assertValidationResults(validationResult);

    assertTrue("Validation result should not have invalid records.",
        validationResult.isValidated());
    assertThat("Validation result should not have invalid records.",
        validationResult.getValidationErrors(), is(empty()));
  }

  @Test
  public void whenXmlTransactionFileIsInvalidThenValidationResultShouldHaveNoErrors()
      throws IOException {
    // Given all fields are valid
    String fileContent = TestObjectFactory.getUnparsableXmlFileContent();

    ParserResult<TransactionRecords> parserResult =
        this.parserService.parseFile(ProjectConstants.MIME_TYPE_APPLICATION_XML, fileContent);

    // Validate file content.
    ValidationResult validationResult = validationService.validate(parserResult);

    this.assertValidationResults(validationResult);
  }

  @Test
  public void whenXmlTransactionFileIsValidWithInvalidRecordsThenValidationResultShouldHaveNoErrors()
      throws IOException {
    // Given all fields are valid
    String fileContent = TestObjectFactory.getValidXmlFileContentWithInvalidRecords();

    // Parse file content.
    ParserResult<TransactionRecords> parserResult =
        this.parserService.parseFile(ProjectConstants.MIME_TYPE_APPLICATION_XML, fileContent);

    // Validate file content.
    ValidationResult validationResult = validationService.validate(parserResult);

    this.assertValidationResults(validationResult);


    // Due to erroneous records.
    assertFalse("Validation result should have invalid records.", validationResult.isValidated());

    // Due to erroneous records.
    assertThat("Validation result should have exactly specific number of validation errors.",
        validationResult.getValidationErrors().size(), is(5));

    assertThat("Validation errors should contain reference number.",
        validationResult.getValidationErrors(), hasItem(hasProperty("errorKey", is("147674"))));

    assertThat("Validation errors should contain description.",
        validationResult.getValidationErrors(), hasItem(hasProperty("errorDescription",
            is(ProjectConstants.UNIQUE_REFERENCE_VALIDATION_FAILED))));

  }

  private void assertValidationResults(final ValidationResult validationResult) {
    assertNotNull("Validation result should not be null.", validationResult);
  }
}
