package nl.rabobank.customerstatementprocessor.exceptions;

/**
 * Validation Exception class.
 */
public class FileValidationException extends Exception {

  private static final long serialVersionUID = 1L;

    public FileValidationException(String message) {
        super(message);
    }
    
    public FileValidationException(String message, Throwable cause) {
      super(message, cause);
  }
}
