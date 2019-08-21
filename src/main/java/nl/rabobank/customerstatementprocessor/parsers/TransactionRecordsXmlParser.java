package nl.rabobank.customerstatementprocessor.parsers;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.extern.slf4j.Slf4j;
import nl.rabobank.customerstatementprocessor.model.TransactionRecords;

/**
 * Specific implementation of Generic XML Parser for Transaction Report.
 */
@Slf4j
public class TransactionRecordsXmlParser implements Parser {
  

  /**
   * Parser implementation, parses string content into result object.
   *
   * @param content text content to be parsed.
   * @return parser result.
   */
  
  @Override
  public ParserResult<TransactionRecords> parse(InputStream content) {
    try {
      // Maps the content to an object of type generic class.
      return new ParserResult<>(new XmlMapper().readValue(content, TransactionRecords.class));
    } catch (IOException e) {
      String errorMessage = "Failed to parse input for class: " + TransactionRecords.class.getName();
      log.error(errorMessage);
      return new ParserResult<>(null, Collections.singletonList(errorMessage));
    }
  }
}
