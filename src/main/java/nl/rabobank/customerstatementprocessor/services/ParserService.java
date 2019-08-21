package nl.rabobank.customerstatementprocessor.services;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import nl.rabobank.customerstatementprocessor.model.TransactionRecords;
import nl.rabobank.customerstatementprocessor.parsers.Parser;
import nl.rabobank.customerstatementprocessor.parsers.ParserResult;
import nl.rabobank.customerstatementprocessor.parsers.TransactionRecordsCsvParser;
import nl.rabobank.customerstatementprocessor.parsers.TransactionRecordsXmlParser;
import nl.rabobank.customerstatementprocessor.properties.ValidationConstants;

/**
 * Parser service to perform parsing in the files uploaded.
 */
@Service
public class ParserService {
  private static final Logger logger = LoggerFactory.getLogger(ParserService.class);

  private final TransactionRecordsXmlParser xmlParser;
  private final TransactionRecordsCsvParser csvParser;

  @Autowired
  public ParserService(final TransactionRecordsXmlParser xmlParser,
      final TransactionRecordsCsvParser csvParser) {
    this.xmlParser = xmlParser;
    this.csvParser = csvParser;
  }

  /**
   * Returns the associated parser for the specified file.
   *
   * @param contentType content type.
   *
   * @return associated parser for the specified file.
   */
  public Optional<Parser> getAssociatedParser(
      final String contentType) {
    // If the type is not XML or CSV, returns null.
    switch (contentType) {
      case ValidationConstants.MIME_TYPE_APPLICATION_XML:
      case ValidationConstants.MIME_TYPE_TEXT_XML:
        return Optional.of(xmlParser);
      case ValidationConstants.MIME_TYPE_TEXT_CSV:
        return Optional.of(csvParser);
      case ValidationConstants.MIME_TYPE_TEXT_CSV_EXCEL:
        return Optional.of(csvParser);
      default:
        return Optional.empty();
    }
  }

  /**
   * Returns if the file is parsable or not.
   *
   * @param contentType content type.
   *
   * @return TRUE if the file is parsable; FALSE otherwise.
   */
  public boolean isParsable(final String contentType) {
    return this.getAssociatedParser(contentType).isPresent();
  }

  /**
   * Returns the parsing result for the specified file.
   *
   * @param contentType content type.
   * @param content content.
   *
   * @return parser result for the file content.
   */
  public ParserResult<TransactionRecords> parseFile(final String contentType, final InputStream content) {
    ParserResult<TransactionRecords> transactionRecordsParseResult;

    // Get the associated parser for the specified file.
    Optional<Parser> parserOpt = this.getAssociatedParser(contentType);

    try {
      if (parserOpt.isPresent()) {
        // If there is a parser, parses the content.
        transactionRecordsParseResult = parserOpt.get().parse(content);
      } else {
        // Adds error message otherwise.
        transactionRecordsParseResult = new ParserResult<>(null,
            Collections.singletonList("Unable to find an associated parser for the file."));
      }
    } catch (Exception e) {
      // Exception occurs when parsing fails.
      String errorMessage = "Unable to parse the uploaded file content.";
      logger.error(errorMessage, e);
      transactionRecordsParseResult =
          new ParserResult<>(null, Collections.singletonList(errorMessage));
    }

    // Returns the parser result.
    return transactionRecordsParseResult;
  }
}
