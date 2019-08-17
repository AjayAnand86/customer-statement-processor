package nl.rabobank.customerstatementprocessor.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import javax.xml.bind.UnmarshalException;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import io.swagger.annotations.Api;
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
public class StatementProcessorController {
  private static final Logger logger = LoggerFactory.getLogger(StatementProcessorController.class);

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
   */
  @PostMapping("/validateFile")
  public ResponseEntity<ValidationResult> validateStatementFile(
      @RequestParam("file") final MultipartFile statementFile) throws Exception {
    if ((statementFile == null) || statementFile.isEmpty()) {
      throw new IllegalArgumentException("File is empty");
    }

    String contentType = statementFile.getContentType();

    // Checks if the file is parsable or not.
    if (!parserService.isParsable(contentType)) {
      throw new UnmarshalException("Unmarchal exception, kindly check the file format");
    }

    // Read the uploaded file content onto string.
    String content;
    try (InputStream inputStream = statementFile.getResource().getInputStream()) {
      content = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
    } catch (IOException ex) {
      throw new IOException("Failed to read file content.");
    }

    // Parse the file content.
    ParserResult<TransactionRecords> parserResult = parserService.parseFile(contentType, content);


    // Validate file content.
    ValidationResult validationResult = validationService.validate(parserResult);

    // Return the validation result.
    return ResponseEntity.ok(validationResult);
  }
}
