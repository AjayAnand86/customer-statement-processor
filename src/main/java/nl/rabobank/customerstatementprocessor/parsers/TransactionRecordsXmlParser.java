package nl.rabobank.customerstatementprocessor.parsers;

import nl.rabobank.customerstatementprocessor.model.TransactionRecords;

/**
 * Specific implementation of Generic XML Parser for Transaction Report.
 */
public class TransactionRecordsXmlParser extends GenericXmlParser<TransactionRecords> {
  public TransactionRecordsXmlParser() {
    super(TransactionRecords.class);
  }
}
