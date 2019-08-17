package nl.rabobank.customerstatementprocessor.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import nl.rabobank.customerstatementprocessor.properties.ProjectProperties;
import nl.rabobank.customerstatementprocessor.properties.ProjectProperties.Security;

/**
 * Security configuration for the application.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
  private static final Logger logger = LoggerFactory.getLogger(SecurityConfiguration.class);

  private final RequestMatcher permitAllRequestMatcher;
  private final Security securityProperties;
  private final PasswordEncoder passwordEncoder;

  @Autowired
  public SecurityConfiguration(final ProjectProperties projectProperties,
      final PasswordEncoder passwordEncoder) {
    this.securityProperties = projectProperties.getSecurity();
    this.passwordEncoder = passwordEncoder;

    this.permitAllRequestMatcher = new AntPathRequestMatcher("/public/**");
  }

  /**
   * Configures the in-memory authentication for basic authentication.
   *
   * @param auth authentication manager builder.
   *
   * @throws Exception when an error occurs.
   */
  @Autowired
  public void configureGlobal(final AuthenticationManagerBuilder auth) throws Exception {
    auth.inMemoryAuthentication().withUser(securityProperties.getDefaultUsername())
        .password(passwordEncoder.encode(securityProperties.getDefaultPassword()))
        .authorities("ROLE_USER");
  }

  /**
   * Disables user-password authorization for permitted endpoints where the user with wrong
   * credentials should have the same level of authorization as anonymous users.
   *
   * @param webSecurity web security configuration.
   *
   * @throws Exception when an error occurs.
   */
  @Override
  public void configure(final WebSecurity webSecurity) throws Exception {
    // Ignoring authentication validation for permitAll requests.
    webSecurity.ignoring().requestMatchers(permitAllRequestMatcher);
  }

  /**
   * Configures HttpSecurity for the application.
   *
   * @param httpSecurity http security.
   *
   * @throws Exception when an error occurs.
   */
  @Override
  protected void configure(final HttpSecurity httpSecurity) throws Exception {
    //@formatter:off

		// Disabling CSRF support for this application.
		httpSecurity.csrf().disable();

        logger.debug("Security.enabled: {}", securityProperties.isEnabled());
        
		if (!securityProperties.isEnabled()) {
            // If the security is disabled, permit all requests.
			httpSecurity
					.authorizeRequests().anyRequest().permitAll()
					.and()
					.httpBasic().disable();
		} else {
			// Otherwise, prevent access to non-permitted endpoints without credentials.
			httpSecurity
					.authorizeRequests().requestMatchers(permitAllRequestMatcher).permitAll()
				    .anyRequest().authenticated()
				    .and()
				    .httpBasic();
		}

		// To enable JavaDoc page. Otherwise, has 'X-Frame-Options: DENY' by default and frames are not displayed.
		httpSecurity.headers().frameOptions().sameOrigin();
		//@formatter:on
  }
}
