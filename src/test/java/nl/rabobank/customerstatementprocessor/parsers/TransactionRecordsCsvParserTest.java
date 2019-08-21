package nl.rabobank.customerstatementprocessor.parsers;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import java.io.IOException;
import java.io.InputStream;
import org.junit.Before;
import org.junit.Test;
import nl.rabobank.customerstatementprocessor.factories.TestObjectFactory;
import nl.rabobank.customerstatementprocessor.model.TransactionRecords;

public class TransactionRecordsCsvParserTest {
  private TransactionRecordsCsvParser parser;

  @Before
  public void setUp() {
    this.parser = new TransactionRecordsCsvParser();
  }

  @Test
  public void whenTransactionFileIsValidThenParserResultShouldHaveNoErrors() throws IOException {
    // Given all fields are valid
    InputStream fileContent = TestObjectFactory.getValidCsvFileContent();

    ParserResult<TransactionRecords> parserResult = this.parser.parse(fileContent);

    this.assertValidParserResults(parserResult);
    assertThat("Transaction list should not be empty.", parserResult.getResult().getTransactions(),
        is(not(empty())));
  }

  @Test
  public void whenTransactionFileHasEmptyRecordsThenParserResultShouldHaveNoErrors()
      throws IOException {
    // Given all fields are valid
    InputStream fileContent = TestObjectFactory.getEmptyCsvFileContent();

    ParserResult<TransactionRecords> parserResult = this.parser.parse(fileContent);

    this.assertValidParserResults(parserResult);
    assertThat("Transaction list should be empty.", parserResult.getResult().getTransactions(),
        is(empty()));
  }

  private <T> void assertValidParserResults(final ParserResult<T> parserResult) {
    assertNotNull("Parser result should not be null.", parserResult);
    assertNotNull("Result object should not be null.", parserResult.getResult());
  }
}
