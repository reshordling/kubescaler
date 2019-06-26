package org.springframework.boot.kubescaler.base.web;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.springframework.boot.kubescaler.api.Profile;
import org.springframework.boot.kubescaler.api.User;
import org.springframework.boot.kubescaler.base.service.ProfileRepository;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@RestController
@RequestMapping(path = "profiles", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class ProfileController {

  private final ProfileRepository profileRepository;

  public ProfileController(ProfileRepository profileRepository) {
    this.profileRepository = profileRepository;
  }

  @RequestMapping(method = RequestMethod.GET)
  @HystrixCommand(fallbackMethod = "listFailback")
  public ResponseEntity<List<Profile>> list(){
    return ResponseEntity.ok(StreamSupport
        .stream(profileRepository.findAll().spliterator(), false)
        .map(Convert::api)
        .collect(Collectors.toList()));
  }

  public ResponseEntity<List<Profile>> listFailback() {
    return ResponseEntity.notFound().build();
  }

  @RequestMapping(method = RequestMethod.POST)
  @HystrixCommand(fallbackMethod = "createFallback")
  public ResponseEntity<Profile> create(@RequestBody Profile profile){
    return ResponseEntity.ok(Convert.api(profileRepository.create(Convert.base(profile))));
  }

  public ResponseEntity<Profile> createFallback(User user){
    return ResponseEntity.notFound().build();
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.GET)
  @HystrixCommand(fallbackMethod = "getFallback")
  public ResponseEntity<Profile> get(@PathVariable UUID id){
    return profileRepository.findById(id).map(data -> ResponseEntity.ok(Convert.api(data)))          //200 OK
        .orElse(ResponseEntity.notFound().build());  //404 Not found
  }

  public ResponseEntity<Profile> getFallback(UUID id) {
    return ResponseEntity.notFound().build();
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
  @HystrixCommand(fallbackMethod = "updateFallback")
  public ResponseEntity<Profile> update(@PathVariable UUID id, @RequestBody Profile profile) {
    org.springframework.boot.kubescaler.base.model.Profile data = Convert.base(profile);
    data.setId(id);
    return ResponseEntity.ok(Convert.api(profileRepository.save(data)));
  }

  public ResponseEntity<Profile> updateFallback(UUID id, User user) {
    return ResponseEntity.notFound().build();
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
  @HystrixCommand(fallbackMethod = "deleteFallback")
  public ResponseEntity delete(@PathVariable UUID id){
    profileRepository.deleteById(id);
    return ResponseEntity.ok().build();
  }

  public ResponseEntity deleteFallback(UUID id) {
    return ResponseEntity.notFound().build();
  }
}
