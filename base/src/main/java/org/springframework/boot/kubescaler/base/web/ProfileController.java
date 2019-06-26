package org.springframework.boot.kubescaler.base.web;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.springframework.boot.kubescaler.api.Profile;
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
@RequestMapping(path = "profiles/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class ProfileController {

  private final ProfileRepository profileRepository;

  public ProfileController(ProfileRepository profileRepository) {
    this.profileRepository = profileRepository;
  }

  @RequestMapping(method = RequestMethod.GET)
  @HystrixCommand(fallbackMethod = "notFound")
  public ResponseEntity<List<Profile>> list(){
    return ResponseEntity.ok(StreamSupport
        .stream(profileRepository.findAll().spliterator(), false)
        .map(Convert::api)
        .collect(Collectors.toList()));
  }

  @RequestMapping(method = RequestMethod.POST)
  @HystrixCommand(fallbackMethod = "notFound")
  public ResponseEntity<Profile> create(@RequestBody Profile profile){
    return ResponseEntity.ok(Convert.api(profileRepository.create(Convert.base(profile))));
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.GET)
  @HystrixCommand(fallbackMethod = "notFound")
  public ResponseEntity<Profile> get(@PathVariable UUID id){
    return profileRepository.findById(id).map(data -> ResponseEntity.ok(Convert.api(data)))          //200 OK
        .orElseGet(() -> ResponseEntity.notFound().build());  //404 Not found
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
  @HystrixCommand(fallbackMethod = "notFound")
  public ResponseEntity<Profile> update(@PathVariable UUID id, @RequestBody Profile profile) {
    org.springframework.boot.kubescaler.base.model.Profile data = Convert.base(profile);
    data.setId(id);
    return ResponseEntity.ok().body(Convert.api(profileRepository.save(data)));
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
  @HystrixCommand(fallbackMethod = "notFound")
  public ResponseEntity delete(@PathVariable UUID id){
    profileRepository.deleteById(id);
    return ResponseEntity.ok().build();
  }

  // fallback method
  public<T> ResponseEntity<T> notFound() {
    return ResponseEntity.notFound().build();
  }
}
