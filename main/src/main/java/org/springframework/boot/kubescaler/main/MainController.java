package org.springframework.boot.kubescaler.main;

import java.util.List;
import org.springframework.boot.kubescaler.api.User;
import org.springframework.boot.kubescaler.main.rest.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

  private final UserService userService;

  public MainController(UserService userService) {
    this.userService = userService;
  }

  @RequestMapping(value = "/health", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity health() {
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @RequestMapping(produces = MediaType.TEXT_HTML_VALUE)
  public ResponseEntity<String> index() {
    ResponseEntity<List<User>> users = userService.getAll();
    if (users.getStatusCode() == HttpStatus.OK) {
      return ResponseEntity.ok(users.getBody().toString());
    }
    return ResponseEntity.notFound().build();
  }
//  final UserRepository userRepository;
//  final ProfileRepository profileRepository;
//
//  public MainController(UserRepository userRepository, ProfileRepository profileRepository) {
//    this.userRepository = userRepository;
//    this.profileRepository = profileRepository;
//  }
//
//  // Populate users and profiles
//  @RequestMapping(value = "testdata", produces = MediaType.TEXT_HTML_VALUE)
//  public ResponseEntity testdata() {
//    Faker fakeNameGenerator = new Faker();
//    // cannot save at one go - there are chances of non-unique UUIDs (is it possible to move it to Cassandra?)
//    IntStream.rangeClosed(1, new Random().nextInt(100)).forEach(nr -> {
//      Set<UUID> users = IntStream.rangeClosed(1, new Random().nextInt(100))
//          .mapToObj(dr -> userRepository.create(User.builder().name(fakeNameGenerator.funnyName().name()).build()).getId())
//          .collect(Collectors.toSet());
//      profileRepository.create(Profile.builder().users(users).build());
//    });
//
//    String testProfiles = StreamSupport
//        .stream(profileRepository.findAll().spliterator(), false)
//        .map(Profile::toString)
//        .collect(Collectors.joining(","));
//
//    return ResponseEntity.ok(testProfiles);
//  }
//
//  // Drop everything
//  @RequestMapping(value = "drop", produces = MediaType.TEXT_HTML_VALUE)
//  public ResponseEntity drop() {
//    profileRepository.deleteAll();
//    userRepository.deleteAll();
//    return ResponseEntity.ok("Dropped");
//  }
//
//  @RequestMapping(value = "health", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//  public ResponseEntity health() {
//    return new ResponseEntity<>(HttpStatus.OK);
//  }
}
