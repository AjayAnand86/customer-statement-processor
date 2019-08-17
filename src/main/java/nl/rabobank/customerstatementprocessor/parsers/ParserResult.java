package nl.rabobank.customerstatementprocessor.parsers;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Parser result class to store parser result.
 *
 * @param <T> result type.
 */
@Getter
@Setter
@AllArgsConstructor
@ToString
public class ParserResult<T> {
  private T result;
  private List<String> errors;

  public ParserResult() {
    this.errors = new ArrayList<>();
  }

  public ParserResult(final T result) {
    this();
    this.result = result;
  }
}
