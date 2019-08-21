package nl.rabobank.customerstatementprocessor.services;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import java.io.IOException;
import java.io.InputStream;
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
import nl.rabobank.customerstatementprocessor.properties.ValidationConstants;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestConfiguration.class})
public class ParserServiceTest {
  @Autowired
  @Qualifier("testParserService")
  private ParserService parserService;

  @Test
  public void whenCsvTransactionFileIsValidThenParserResultShouldHaveNoErrors() throws IOException {
    // Given all fields are valid
    InputStream fileContent = TestObjectFactory.getValidCsvFileContent();

    ParserResult<TransactionRecords> parserResult =
        this.parserService.parseFile(ValidationConstants.MIME_TYPE_TEXT_CSV, fileContent);

    this.assertValidParserResults(parserResult);

    // Successfully parsed transactions
    assertThat("Transaction list should not be empty.", parserResult.getResult().getTransactions(),
        is(not(empty())));

    // Due to erroneous records.
    assertThat("Error list should empty.", parserResult.getErrors(), is((empty())));

  }

  @Test
  public void whenCsvTransactionFileHasEmptyRecordsThenParserResultShouldHaveNoErrors()
      throws IOException {
    // Given all fields are valid
    InputStream fileContent = TestObjectFactory.getEmptyCsvFileContent();

    ParserResult<TransactionRecords> parserResult =
        this.parserService.parseFile(ValidationConstants.MIME_TYPE_TEXT_CSV, fileContent);

    this.assertValidParserResults(parserResult);
    assertThat("Transaction list should be empty.", parserResult.getResult().getTransactions(),
        is(empty()));
  }

  @Test
  public void whenXmlTransactionFileIsValidThenParserResultShouldHaveNoErrors() throws IOException {
    // Given all fields are valid
    InputStream fileContent = TestObjectFactory.getValidXmlFileContent();

    ParserResult<TransactionRecords> parserResult =
        this.parserService.parseFile(ValidationConstants.MIME_TYPE_APPLICATION_XML, fileContent);

    this.assertValidParserResults(parserResult);
    assertThat("Transaction list should not be empty.", parserResult.getResult().getTransactions(),
        is(not(empty())));
  }

  @Test
  public void whenXmlTransactionFileHasEmptyRecordsThenParserResultShouldHaveNoErrors()
      throws IOException {
    // Given all fields are valid
    InputStream fileContent = TestObjectFactory.getEmptyXmlFileContent();

    ParserResult<TransactionRecords> parserResult =
        this.parserService.parseFile(ValidationConstants.MIME_TYPE_APPLICATION_XML, fileContent);

    assertNotNull("Parser result should not be null.",parserResult);
    assertThat("Parser errors should be not be empty.", parserResult.getErrors(), is(not(empty())));
  }

  @Test
  public void whenXmlTransactionFileIsInvalidThenParserResultShouldHaveNoErrors()
      throws IOException {
    // Given all fields are valid
    InputStream fileContent = TestObjectFactory.getUnparsableXmlFileContent();

    ParserResult<TransactionRecords> parserResult =
        this.parserService.parseFile(ValidationConstants.MIME_TYPE_APPLICATION_XML, fileContent);

    this.assertInvalidParserResults(parserResult, 1);
  }

  private <T> void assertValidParserResults(final ParserResult<T> parserResult) {
    assertNotNull("Parser result should not be null.", parserResult);
    assertNotNull("Result object should not be null.", parserResult.getResult());
  }

  private <T> void assertInvalidParserResults(final ParserResult<T> parserResult,
      final int errorSize) {
    assertNotNull("Parser result should not be null.", parserResult);
    assertThat("Parser result errors should not be empty.", parserResult.getErrors(),
        is(not(empty())));
    assertThat("Parser errors should have specified length.", parserResult.getErrors().size(),
        is(errorSize));
  }
}
