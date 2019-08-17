package nl.rabobank.customerstatementprocessor.exceptions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
@Builder
public class ResponseError {

  private int status;
  private String message;

}
