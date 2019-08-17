package nl.rabobank.customerstatementprocessor.parsers;

import java.io.IOException;
import java.util.Collections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

/**
 * Generic XML Parser to parse strings into the generic object type.
 *
 * @param <T> target object type.
 */
public class GenericXmlParser<T> implements Parser<String, T> {
  private static final Logger logger = LoggerFactory.getLogger(GenericXmlParser.class);

  private final Class<T> clazz;
  private final XmlMapper mapper;

  /**
   * Constructor to define the class type of the generic variable.
   *
   * @param clazz class of the generic type.
   */
  public GenericXmlParser(final Class<T> clazz) {
    super();

    this.clazz = clazz;
    this.mapper = new XmlMapper();
  }

  /**
   * Parser implementation, parses string content into result object.
   *
   * @param content text content to be parsed.
   * @return parser result.
   */
  @Override
  public ParserResult<T> parse(final String content) {
    try {
      // Maps the content to an object of type generic class.
      return new ParserResult<>(mapper.readValue(content, clazz));
    } catch (IOException e) {
      // Exception handling.
      String errorMessage = "Failed to parse input for class: " + clazz.getSimpleName();
      logger.error(errorMessage, e);
      return new ParserResult<>(null, Collections.singletonList(errorMessage));
    }
  }
}
