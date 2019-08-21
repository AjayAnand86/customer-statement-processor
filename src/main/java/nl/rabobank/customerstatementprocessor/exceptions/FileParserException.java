package nl.rabobank.customerstatementprocessor.exceptions;

/**
 * Parser Exception class.
 */

public class FileParserException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public FileParserException(String message) {
    super(message);
  }

  public FileParserException(String message, Throwable cause) {
    super(message, cause);
  }
}
