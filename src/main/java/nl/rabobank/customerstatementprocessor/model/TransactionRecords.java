package nl.rabobank.customerstatementprocessor.model;

import java.util.List;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Transaction Report object that stores a list of Transaction objects, which corresponds to the records of the statement file.
 */
@Getter
@Setter
@JacksonXmlRootElement(localName = "records")
@ToString
public class TransactionRecords {
    @JacksonXmlProperty(localName = "record")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<Transaction> transactions;
}
