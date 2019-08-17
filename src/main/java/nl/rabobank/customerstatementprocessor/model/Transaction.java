package nl.rabobank.customerstatementprocessor.model;

import java.math.BigDecimal;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Transaction object which stores the details for each record.
 *
 * Note: BigDecimal is used to store currency amounts with better accuracy.
 */
@Getter
@Setter
@JacksonXmlRootElement(localName = "record")
@ToString
public class Transaction {
  @JacksonXmlProperty(localName = "reference", isAttribute = true)
  private long transactionReference;
  @JacksonXmlProperty(localName = "accountNumber")
  private String accountNumber;
  @JacksonXmlProperty(localName = "description")
  private String description;
  @JacksonXmlProperty(localName = "startBalance")
  private BigDecimal startBalance;
  @JacksonXmlProperty(localName = "mutation")
  private BigDecimal mutation;
  @JacksonXmlProperty(localName = "endBalance")
  private BigDecimal endBalance;
}
