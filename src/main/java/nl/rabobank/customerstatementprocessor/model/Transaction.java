package nl.rabobank.customerstatementprocessor.model;

import java.math.BigDecimal;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvNumber;
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
  @CsvBindByPosition(position = 0)
  private long transactionReference;
  
  @JacksonXmlProperty(localName = "accountNumber")
  @CsvBindByPosition(position = 1)
  private String accountNumber;
  
  @JacksonXmlProperty(localName = "description")
  @CsvBindByPosition(position = 2)
  private String description;
  
  @JacksonXmlProperty(localName = "startBalance")
  @CsvBindByPosition(position = 3)
  private BigDecimal startBalance;
  
  @JacksonXmlProperty(localName = "mutation")
  @CsvBindByPosition(position = 4)
  private BigDecimal mutation;
  
  @JacksonXmlProperty(localName = "endBalance")
  @CsvBindByPosition(position = 5)
  private BigDecimal endBalance;
}

