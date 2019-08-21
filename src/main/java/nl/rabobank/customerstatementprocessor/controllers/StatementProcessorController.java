package nl.rabobank.customerstatementprocessor.controllers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import nl.rabobank.customerstatementprocessor.model.TransactionRecords;
import nl.rabobank.customerstatementprocessor.parsers.ParserResult;
import nl.rabobank.customerstatementprocessor.services.ParserService;
import nl.rabobank.customerstatementprocessor.services.ValidationService;
import nl.rabobank.customerstatementprocessor.validators.ValidationResult;

/**
 * Statement Processor Controller
 */
@RestController
@Api(value = "Statement Controller for statement processing.")
@CrossOrigin
@Slf4j
public class StatementProcessorController {

  private final ParserService parserService;
  private final ValidationService validationService;

  @Autowired
  public StatementProcessorController(final ParserService parserService,
      final ValidationService validationService) {
    this.parserService = parserService;
    this.validationService = validationService;
  }

  /**
   * Validates the statement file.
   *
   * @param statementFile statement records file.
   *
   * @return validation result of the file content.
   * @throws IOException 
   */
  @PostMapping("/validateFile")
  public ResponseEntity<ValidationResult> validateStatementFile(
      @RequestParam("file") final MultipartFile statementFile) throws IOException {

      //check if file is empty
      if(statementFile.isEmpty()) {
        throw new IllegalArgumentException("File not found or empty.") ;
      }
      
      //Check if file type is valid 
      if (!parserService.isParsable(statementFile.getContentType())) {
        throw new FileNotFoundException("Invalid file format exception, kindly check the file format");
      }
      
      //Check if file is valid or readable
      InputStream fileContent;
      try {
        fileContent=statementFile.getResource().getInputStream();
      }catch(IOException ioEx) {
        throw new IOException("Failed to read file");
      }
      // Parse the file content. 
      ParserResult<TransactionRecords> parserResult =parserService.parseFile(statementFile.getContentType(),fileContent);
      
      // Validate file content. 
      ValidationResult validationResult =validationService.validate(parserResult);
      
      // Return the validation result. 
      return ResponseEntity.ok(validationResult);
       }
}
