package nl.rabobank.customerstatementprocessor.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration for JavaDoc generation and serving.
 */
@Configuration
public class JavaDocConfiguration implements WebMvcConfigurer {
  /**
   * Adds the JavaDoc resource directory to the resource handlers.
   *
   * @param registry default resource handler registry.
   */
  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/javadoc/**").addResourceLocations("classpath:/static/javadoc/");
  }
}
