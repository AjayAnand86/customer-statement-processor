package nl.rabobank.customerstatementprocessor.parsers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import nl.rabobank.customerstatementprocessor.model.Transaction;
import nl.rabobank.customerstatementprocessor.model.TransactionRecords;
import nl.rabobank.customerstatementprocessor.properties.ProjectConstants;

/**
 * CSV parser for transaction report objects, to be parsed from string text content.
 */
public class TransactionRecordsCsvParser implements Parser<String, TransactionRecords> {
  private static final Logger logger = LoggerFactory.getLogger(TransactionRecordsCsvParser.class);

  /**
   * Parses the CSV content into transaction report.
   * 
   * @param content content to be parsed.
   * @return parser result.
   */
  @Override
  public ParserResult<TransactionRecords> parse(final String content) {

    // Initializes error list.
    List<String> parserErrorList = new ArrayList<>();

    // Splits the content string into lines.
    String[] records = content.split("\\r?\\n");

    // Iterating through the record lines.
    // 1. Convert array into stream
    // 2. Skip the first 1 (one) row, which is the header line.
    // 3. Convert line data into Transaction object if no error occurs during parsing.
    // If an error occurs, erroneous line is added to the error list and maps a null.
    // 4. Filter non-null transactions, which are the successfully parsed ones only.
    // 5. Collect the transaction list.
    List<Transaction> transactionList =
        Arrays.stream(records).skip(1).map(line -> this.populateRow(line, parserErrorList))
            .filter(Objects::nonNull).collect(Collectors.toList());

    // Creates a new transaction report.
    TransactionRecords transactionRecords = new TransactionRecords();

    // Set the transaction list of the transaction report to the newly collected list.
    transactionRecords.setTransactions(transactionList);

    // Returns the parser result.
    return new ParserResult<>(transactionRecords, parserErrorList);

  }

  private Transaction parseRow(final String[] dataTokens) {
    // Allocates space and create transaction object.
    Transaction transaction = new Transaction();

    // Sets the fields from the tokens.
    transaction.setTransactionReference(Long.parseLong(dataTokens[0]));
    transaction.setAccountNumber(dataTokens[1]);
    transaction.setDescription(dataTokens[2]);
    transaction.setStartBalance(new BigDecimal(dataTokens[3]));
    transaction.setMutation(new BigDecimal(dataTokens[4]));
    transaction.setEndBalance(new BigDecimal(dataTokens[5]));

    return transaction;
  }

  private Transaction populateRow(final String dataLine, final List<String> parserErrorList) {
    // Delimits the record string line into tokens.
    String[] tokens = dataLine.split(ProjectConstants.CSV_DELIMITER);

    try {
      return this.parseRow(tokens);

    } catch (Exception e) {
      // If an error occurs during the conversion, or out-of-index, etc.
      // add the record to error list and return null, to continue execution of remaining records.
      logger.error("Unable to parse record: " + dataLine);
      parserErrorList.add("Unable to parse record: " + dataLine);
      return null;
    }
  }
}
