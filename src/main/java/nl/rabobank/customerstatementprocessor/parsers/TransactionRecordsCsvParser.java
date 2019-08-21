package nl.rabobank.customerstatementprocessor.parsers;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.enums.CSVReaderNullFieldIndicator;
import lombok.extern.slf4j.Slf4j;
import nl.rabobank.customerstatementprocessor.model.Transaction;
import nl.rabobank.customerstatementprocessor.model.TransactionRecords;

/**
 * CSV parser for transaction report objects, to be parsed from string text content.
 */
@Slf4j
public class TransactionRecordsCsvParser implements Parser {


  /**
   * Parses the CSV content into transaction report.
   * 
   * @param inputStream content to be parsed.
   * @return parser result.
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  @Override
  public ParserResult<TransactionRecords> parse(final InputStream inputStream) {
    List<Transaction> transactionList = new ArrayList<>();
    List<String> parserErrorList = new ArrayList<>();

    try {
      Reader reader = new InputStreamReader(inputStream);
      CsvToBean<Transaction> csvToBean = new CsvToBeanBuilder(reader)
          .withType(Transaction.class)
          .withSkipLines(1)
          .withSeparator(',')
          .withQuoteChar('"')
          .withFieldAsNull(CSVReaderNullFieldIndicator.EMPTY_SEPARATORS).build();

      // Get CSV data into List of Transaction
      transactionList = csvToBean.parse();

      // Creates a new transaction report.
      TransactionRecords transactionRecords = new TransactionRecords();

      // Set the transaction list of the transaction report to the newly collected list.
      transactionRecords.setTransactions(transactionList);

      // Returns the parser result.
      return new ParserResult<>(transactionRecords, parserErrorList);

    } catch (Exception e) {
      log.error("Unable to Parse CSV File {}",e.getMessage());
      return new ParserResult<>(null, Collections.singletonList("Unable to parse the uploaded file content"));
    }



  }
}
