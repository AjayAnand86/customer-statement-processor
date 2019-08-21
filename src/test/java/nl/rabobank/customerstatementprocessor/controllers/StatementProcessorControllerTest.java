package nl.rabobank.customerstatementprocessor.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.multipart.MultipartFile;
import nl.rabobank.customerstatementprocessor.config.TestConfiguration;
import nl.rabobank.customerstatementprocessor.services.ParserService;
import nl.rabobank.customerstatementprocessor.services.ValidationService;
import nl.rabobank.customerstatementprocessor.validators.ValidationResult;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestConfiguration.class})
public class StatementProcessorControllerTest {

  private StatementProcessorController statementProcessorController;
  
  @Autowired
  @Qualifier("testParserService")
  private ParserService parserService;

  @Autowired
  @Qualifier("testValidationService")
  private ValidationService validationService;
  @Before
  public void setUp() {
    this.statementProcessorController = new StatementProcessorController(parserService,validationService);
  }
  
  @Test
  public void shouldProcessXMLFile() throws Exception {

      MultipartFile multipartFile = new MockMultipartFile("records_valid.xml",
              "records_valid.xml", "application/xml",new FileInputStream(new File("src/test/resources/data/records_valid.xml")));

      ResponseEntity<ValidationResult> response =  statementProcessorController.validateStatementFile(multipartFile);

      Assert.assertNotNull(response);
      Assert.assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
  }
  
  @Test
  public void shouldProcessCSVFile() throws Exception {

      MultipartFile multipartFile = new MockMultipartFile("records_valid.csv",
              "records_valid.csv", "text/csv",
              new FileInputStream(new File("src/test/resources/data/records_valid.csv")));

      ResponseEntity<ValidationResult> response =  statementProcessorController.validateStatementFile(multipartFile);

      Assert.assertNotNull(response);
      Assert.assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
  }

 
  @Test(expected = FileNotFoundException.class)
  public void shouldProcessWrongFormatFile() throws Exception {

      MultipartFile multipartFile = new MockMultipartFile("records.txt",
              "records.txt", "text",
              new FileInputStream(new File("src/test/resources/data/records.txt")));

      ResponseEntity<ValidationResult> response = statementProcessorController.validateStatementFile(multipartFile);
  }
  

  @Test(expected = IllegalArgumentException.class)
  public void shouldProcessEmptyCSVFile() throws Exception {

    MultipartFile multipartFile = new MockMultipartFile("records_empty.csv",
        "records_empty.csv", "text/csv",new FileInputStream(new File("src/test/resources/data/records_empty.csv")));

    statementProcessorController.validateStatementFile(multipartFile);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void shouldProcessEmptyXMLFile() throws Exception {

    MultipartFile multipartFile = new MockMultipartFile("records_empty.xml",
        "records_empty.xml", "application/xml",new FileInputStream(new File("src/test/resources/data/records_empty.xml")));

    statementProcessorController.validateStatementFile(multipartFile);
  }
  
  
}
