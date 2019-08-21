package nl.rabobank.customerstatementprocessor.factories;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.springframework.util.ResourceUtils;
import nl.rabobank.customerstatementprocessor.model.Transaction;
import nl.rabobank.customerstatementprocessor.model.TransactionRecords;

public final class TestObjectFactory {
  private TestObjectFactory() {}

  public static Transaction createValidTransaction() {
    Transaction transaction = new Transaction();

    transaction.setTransactionReference(123456L);
    transaction.setAccountNumber("NL43AEGO0773393871");
    transaction.setDescription("Transaction description");
    transaction.setStartBalance(BigDecimal.valueOf(100));
    transaction.setMutation(BigDecimal.valueOf(25));
    transaction.setEndBalance(BigDecimal.valueOf(125));

    return transaction;
  }

  public static TransactionRecords createValidTransactionRecords() {
    TransactionRecords transactionRecords = new TransactionRecords();

    Transaction transaction1 = createValidTransaction();
    transaction1.setDescription("Transaction description 1");

    Transaction transaction2 = createValidTransaction();
    transaction2.setTransactionReference(98765L);
    transaction2.setDescription("Transaction description 2");

    Transaction transaction3 = createValidTransaction();
    transaction3.setTransactionReference(581923L);
    transaction3.setDescription("Transaction description 3");

    List<Transaction> transactions = new ArrayList<>();
    transactions.add(transaction1);
    transactions.add(transaction2);
    transactions.add(transaction3);

    transactionRecords.setTransactions(transactions);

    return transactionRecords;
  }

  public static InputStream getUnparsableXmlFileContent() throws IOException {
    return FileUtils.openInputStream(ResourceUtils.getFile("src/test/resources/data/records_unparsable.xml"));
  }

  public static InputStream getEmptyXmlFileContent() throws IOException {
    return FileUtils.openInputStream(ResourceUtils.getFile("src/test/resources/data/records_empty.xml"));
  }

  public static InputStream getValidXmlFileContent() throws IOException {
    return FileUtils.openInputStream(ResourceUtils.getFile("src/test/resources/data/records_valid.xml"));
  }

  public static InputStream getValidXmlFileContentWithInvalidRecords() throws IOException {
    return FileUtils.openInputStream(ResourceUtils.getFile("src/test/resources/data/records_invalid.xml"));
  }

  public static InputStream getEmptyCsvFileContent() throws IOException {
    return FileUtils.openInputStream(ResourceUtils.getFile("src/test/resources/data/records_empty.csv"));
  }

  public static InputStream getValidCsvFileContent() throws IOException {
    return FileUtils.openInputStream(ResourceUtils.getFile("src/test/resources/data/records_valid.csv"));
     
  }
  public static InputStream getInValidCsvFileContent() throws IOException {
    return FileUtils.openInputStream(ResourceUtils.getFile("src/test/resources/data/records_invalid.csv"));
     
  }
}
