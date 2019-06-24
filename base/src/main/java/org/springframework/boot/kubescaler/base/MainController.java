package org.springframework.boot.kubescaler.base;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {
  @RequestMapping(value = "/health", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity health() {
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
