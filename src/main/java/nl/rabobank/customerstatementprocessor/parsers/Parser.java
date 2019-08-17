package nl.rabobank.customerstatementprocessor.parsers;

/**
 * Parser interface to parse of type <code>T</code> into <code>U</code>.
 * 
 * @param <T> Source type.
 * @param <U> Target type.
 */
@FunctionalInterface
public interface Parser<T, U> {
  /**
   * Function to be performed to parse of type <code>T</code> into <code>U</code>.
   * 
   * @param content content to be parsed.
   * @return parser result.
   */
  ParserResult<U> parse(T content);
}
