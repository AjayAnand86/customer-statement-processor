package nl.rabobank.customerstatementprocessor.properties;

/**
 * Project constants
 */
public final class ProjectConstants {
  public static final String MIME_TYPE_APPLICATION_XML = "application/xml";
  public static final String MIME_TYPE_TEXT_XML = "text/xml";
  public static final String MIME_TYPE_TEXT_CSV = "text/csv";
  public static final String MIME_TYPE_TEXT_CSV_EXCEL = "application/vnd.ms-excel";
  public static final String CSV_DELIMITER = ",";
  public static final String END_BALANCE_VALIDATION_FAILED = "End Balance Validation Failed";
  public static final String UNIQUE_REFERENCE_VALIDATION_FAILED ="Unique Reference Validation Failed";
  public static final String IBAN_VALIDATION_FAILED = "Invalid IBAN";
  public static final String INVALID_TRANSACTION_REFERENCE = "Invalid Transaction Reference";
  public static final String INVALID_MUTATION = "Invalid Mutation";

  private ProjectConstants() {}
}
