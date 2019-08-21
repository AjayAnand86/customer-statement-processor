package nl.rabobank.customerstatementprocessor.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.util.ResourceUtils;
import lombok.Getter;
import lombok.Setter;
import nl.rabobank.customerstatementprocessor.factories.YamlPropertySourceFactory;

/**
 * Project configuration grabbed from the properties files.
 */
@Configuration
@ConfigurationProperties("statement-processor")
@PropertySource(factory = YamlPropertySourceFactory.class,
    value = ResourceUtils.CLASSPATH_URL_PREFIX + "application.yml")
@Getter
@Setter
public class SecurityProperties {
  private Security security;
  private Validation validation;

  /**
   * Security Configuration attributes.
   */
  @Getter
  @Setter
  public static class Security {
    private boolean enabled;
    private String defaultUsername;
    private String defaultPassword;
  }

  /**
   * Validation Configuration attributes.
   */
  @Getter
  @Setter
  public static class Validation {
    private boolean appendParserErrors;
    private String parserErrorsKey;
  }
}
