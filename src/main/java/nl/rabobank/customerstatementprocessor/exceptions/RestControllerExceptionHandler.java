package nl.rabobank.customerstatementprocessor.exceptions;

import java.io.FileNotFoundException;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class RestControllerExceptionHandler extends ResponseEntityExceptionHandler {

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

    log.error(ex.getMessage());
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

    log.error("Unmarchal exception, Unable to read file", ex);
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
    log.error(ex.getMessage());
    final ResponseError error = ResponseError.builder().status(500)
        .message(ex.getMessage()).build();
    return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  /**
   * Error handler for {@link FileNotFoundException} being thrown from the backend code.
   * 
   * @param request the {@link HttpServletRequest} current request.
   * @param ex the {@link FileNotFoundException} thrown exception.
   * @return the {@link ResponseEntity} with error message.
   */
  @ExceptionHandler(FileNotFoundException.class)
  public ResponseEntity<ResponseError> InvalidFileTypeExeptionHandler(final HttpServletRequest request,
      final FileNotFoundException ex) {

    log.error(ex.getMessage());
    final ResponseError error =
        ResponseError.builder().status(400).message(ex.getMessage()).build();
    return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
  }
  
  

}
