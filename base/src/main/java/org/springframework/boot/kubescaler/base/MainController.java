package org.springframework.boot.kubescaler.base;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

  private final String hostName = System.getenv("HOSTNAME");

  @RequestMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
  public ResponseEntity<String> ribbonPing() {
    return ResponseEntity.ok(hostName);
  }

  @RequestMapping(value = "/health", produces = MediaType.TEXT_HTML_VALUE)
  public ResponseEntity health() {
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
