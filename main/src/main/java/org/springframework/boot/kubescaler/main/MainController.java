package org.springframework.boot.kubescaler.main;

import java.util.stream.Collectors;
import org.springframework.boot.kubescaler.api.Profile;
import org.springframework.boot.kubescaler.api.User;
import org.springframework.boot.kubescaler.main.data.DataService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class MainController {

  private final DataService dataService;

  public MainController(DataService dataService) {
    this.dataService = dataService;
  }

  @RequestMapping(value = "/health", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity health() {
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @RequestMapping(value = "/testdata", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity testdata() {
    log.debug("Starting testdata generation");
    dataService.createTestdataAsync();
    log.debug("Don't wait until testdata generation is finished");
    return ResponseEntity.ok().build();
  }

  @RequestMapping(value = "/i-swear-i-want-drop-everything", produces = MediaType.TEXT_HTML_VALUE)
  // delete testdata. what should be done about active sessions?
  public ResponseEntity<String> drop() {
    dataService.dropAll();
    return ResponseEntity.ok("DB content successfully dropped, no snapshots");
  }

  @RequestMapping(produces = MediaType.TEXT_HTML_VALUE)
  public ResponseEntity<String> indexProfiles() {
    String profilesTextRepresentation = dataService.getProfiles().stream().map(Profile::toString).collect(Collectors.joining("\n"));
    return ResponseEntity.ok(profilesTextRepresentation);
  }

  @RequestMapping(value = "/users", produces = MediaType.TEXT_HTML_VALUE)
  public ResponseEntity<String> indexUsers() {
    String profilesTextRepresentation = dataService.getUsers().stream().map(User::toString).collect(Collectors.joining("\n"));
    return ResponseEntity.ok(profilesTextRepresentation);
  }
}

