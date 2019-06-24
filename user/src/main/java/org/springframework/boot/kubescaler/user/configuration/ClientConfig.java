package org.springframework.boot.kubescaler.user.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "bean")
public class ClientConfig {

  private String message = "Local config state";

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}