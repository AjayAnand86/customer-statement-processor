package nl.rabobank.customerstatementprocessor.parsers;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import java.io.IOException;
import org.junit.Before;
import org.junit.Test;
import nl.rabobank.customerstatementprocessor.factories.TestObjectFactory;
import nl.rabobank.customerstatementprocessor.model.TransactionRecords;

public class TransactionRecordsXmlParserTest {
  private TransactionRecordsXmlParser parser;

  @Before
  public void setUp() {
    this.parser = new TransactionRecordsXmlParser();
  }

  @Test
  public void whenTransactionFileIsValidThenParserResultShouldHaveNoErrors() throws IOException {
    // Given all fields are valid
    String fileContent = TestObjectFactory.getValidXmlFileContent();

    ParserResult<TransactionRecords> parserResult = this.parser.parse(fileContent);

    this.assertValidParserResults(parserResult);
  }

  @Test
  public void whenTransactionFileHasEmptyRecordsThenParserResultShouldHaveNoErrors()
      throws IOException {
    // Given all fields are valid
    String fileContent = TestObjectFactory.getEmptyXmlFileContent();

    ParserResult<TransactionRecords> parserResult = this.parser.parse(fileContent);

    this.assertValidParserResults(parserResult);
  }

  @Test
  public void whenTransactionFileIsInvalidThenParserResultShouldHaveNoErrors() throws IOException {
    // Given all fields are valid
    String fileContent = TestObjectFactory.getUnparsableXmlFileContent();

    ParserResult<TransactionRecords> parserResult = this.parser.parse(fileContent);

    this.assertInvalidParserResults(parserResult, 1);
  }

  private <T> void assertValidParserResults(final ParserResult<T> parserResult) {
    assertNotNull("Parser result should not be null.", parserResult);
    assertThat("Parser result errors should be empty.", parserResult.getErrors(), is(empty()));
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
