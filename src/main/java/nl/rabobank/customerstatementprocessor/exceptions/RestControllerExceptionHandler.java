package nl.rabobank.customerstatementprocessor.exceptions;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.UnmarshalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class RestControllerExceptionHandler extends ResponseEntityExceptionHandler {

  private static final Logger LOG = LoggerFactory.getLogger(RestControllerExceptionHandler.class);

  /**
   * Error handler for {@link IllegalArgumentException} being thrown from the backend code.
   * 
   * @param request the {@link HttpServletRequest} current request.
   * @param ex the {@link IllegalArgumentException} thrown exception.
   * @return the {@link ResponseEntity} with error message.
   */
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ResponseError> illigalArgumentExceptionHandler(
      final HttpServletRequest request, final IllegalArgumentException ex) {

    LOG.error("Invalid input", ex);
    final ResponseError error =
        ResponseError.builder().status(400).message(ex.getMessage()).build();
    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
  }

  /**
   * Error handler for {@link IOException} being thrown from the backend code.
   * 
   * @param request the {@link HttpServletRequest} current request.
   * @param ex the {@link IOException} thrown exception.
   * @return the {@link ResponseEntity} with error message.
   */
  @ExceptionHandler(IOException.class)
  public ResponseEntity<ResponseError> ioExceptionHandler(final HttpServletRequest request,
      final IOException ex) {

    LOG.error("Failed to read file", ex);
    final ResponseError error =
        ResponseError.builder().status(400).message(ex.getMessage()).build();
    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
  }


  /**
   * Error handler for {@link Exception} being thrown from backend.
   * 
   * @param request the {@link HttpServletRequest} current request.
   * @param ex the {@link Exception} thrown exception.
   * @return the {@link ResponseEntity} with error message.
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ResponseError> defaultErrorHandler(final HttpServletRequest request,
      final Exception ex) {
    LOG.error("Unhandled Exception caught", ex);
    final ResponseError error = ResponseError.builder().status(500)
        .message("Unhandled exception caught, please check logs for more details.").build();
    return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  /**
   * Error handler for {@link UnmarshalException} being thrown from the backend code.
   * 
   * @param request the {@link HttpServletRequest} current request.
   * @param ex the {@link UnmarshalException} thrown exception.
   * @return the {@link ResponseEntity} with error message.
   */
  @ExceptionHandler(UnmarshalException.class)
  public ResponseEntity<ResponseError> unmarshalExeptionHandler(final HttpServletRequest request,
      final UnmarshalException ex) {

    LOG.error("Unmarchal exception, kindly check the file format", ex);
    final ResponseError error =
        ResponseError.builder().status(400).message(ex.getMessage()).build();
    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
  }

}
