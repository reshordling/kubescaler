package org.springframework.boot.kubescaler.base;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.springframework.boot.kubescaler.base.model.Profile;
import org.springframework.boot.kubescaler.base.model.User;
import org.springframework.boot.kubescaler.base.service.ProfileRepository;
import org.springframework.boot.kubescaler.base.service.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

  final UserRepository userRepository;
  final ProfileRepository profileRepository;

  public MainController(UserRepository userRepository, ProfileRepository profileRepository) {
    this.userRepository = userRepository;
    this.profileRepository = profileRepository;
  }

  @RequestMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
  public ResponseEntity index() {
    User user1 = userRepository.create(User.builder().build());
    User user2 = userRepository.create(User.builder().build());
    User user3 = userRepository.create(User.builder().build());
    Profile profile1 = profileRepository.create(Profile.builder().users(Set.of(user1.getId(), user2.getId(), user3.getId())).build());
    Profile profile2 = profileRepository.create(Profile.builder().users(Set.of(user1.getId(), user2.getId())).build());
    return ResponseEntity.ok(String.format("New profiles: %s and %s, total profiles: %d", profile1, profile2, profileRepository.count()));
  }

  @RequestMapping(value = "/health", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity health() {
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
