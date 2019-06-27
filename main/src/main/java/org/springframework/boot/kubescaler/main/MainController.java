package org.springframework.boot.kubescaler.main;

import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.boot.kubescaler.api.Profile;
import org.springframework.boot.kubescaler.api.User;
import org.springframework.boot.kubescaler.main.data.DataService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class MainController {

  private final DataService dataService;

  public MainController(DataService dataService) {
    this.dataService = dataService;
  }

  private final String hostName = System.getenv("HOSTNAME");

  @RequestMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
  public ResponseEntity<String> ribbonPing() {
    return ResponseEntity.ok(hostName);
  }

  @RequestMapping(value = "/health", produces = MediaType.TEXT_HTML_VALUE)
  public ResponseEntity health() {
    // warm-up
    dataService.hasAccess(UUID.randomUUID(), UUID.randomUUID());
    return new ResponseEntity(HttpStatus.OK);
  }

  @RequestMapping(value = "/testdata", produces = MediaType.TEXT_HTML_VALUE)
  public ResponseEntity<String> testdata() {
    log.debug("Starting testdata generation");
    dataService.createTestdataAsync();
    log.debug("Don't wait until testdata generation is finished");
    return ResponseEntity.ok("Generating testdata");
  }

  @RequestMapping(value = "/i-swear-i-want-drop-everything", produces = MediaType.TEXT_HTML_VALUE)
  // delete testdata. what should be done about active sessions?
  public ResponseEntity<String> drop() {
    dataService.dropAll();
    return ResponseEntity.ok("DB content successfully dropped, no snapshots");
  }

  @RequestMapping(value = "/users", produces = MediaType.TEXT_HTML_VALUE)
  public ResponseEntity<String> indexUsers() {
    String profilesTextRepresentation = dataService.getUsers().stream().map(User::toString).collect(Collectors.joining("\n"));
    return ResponseEntity.ok(profilesTextRepresentation);
  }

  @RequestMapping(value = "/profiles", produces = MediaType.TEXT_HTML_VALUE)
  public ResponseEntity<String> indexProfiles() {
    String profilesTextRepresentation = dataService.getProfiles().stream().map(Profile::toString).collect(Collectors.joining("\n"));
    return ResponseEntity.ok(profilesTextRepresentation);
  }

  @RequestMapping(value = "/login/{userId}/{profileId}", produces = MediaType.TEXT_HTML_VALUE)
  public ResponseEntity<String> login(@PathVariable UUID userId, @PathVariable  UUID profileId) {
    return dataService.hasAccess(userId, profileId) && dataService.acquireLock(userId.toString() + ":" + profileId.toString()) ?
        ResponseEntity.ok("Profile started") : ResponseEntity.notFound().build();
  }
}

