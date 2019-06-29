package org.springframework.boot.kubescaler.stream;

import org.springframework.boot.kubescaler.stream.configuration.RibbonConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class MainController {

  private final String hostName = System.getenv("HOSTNAME");

  @RequestMapping(value = RibbonConfiguration.PING_URL, produces = MediaType.TEXT_HTML_VALUE)
  public ResponseEntity<String> ribbonPing() {
    return ResponseEntity.ok(hostName);
  }

  @RequestMapping(value = "/health", produces = MediaType.TEXT_HTML_VALUE)
  public ResponseEntity health() {
    return new ResponseEntity(HttpStatus.OK);
  }
}

