package nl.rabobank.customerstatementprocessor.parsers;

import java.io.InputStream;
import nl.rabobank.customerstatementprocessor.model.TransactionRecords;

/**
 * Parser interface to parse input files.
 * 
 */
@FunctionalInterface
public interface Parser {
  /**
   * Function to be performed to parse of type String into TransactionRecords.
   * 
   * @param content content to be parsed.
   * @return parser result.
   */
  ParserResult<TransactionRecords> parse(InputStream content);
}
