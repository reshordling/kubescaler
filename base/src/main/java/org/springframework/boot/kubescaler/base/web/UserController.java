package org.springframework.boot.kubescaler.base.web;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.springframework.boot.kubescaler.api.User;
import org.springframework.boot.kubescaler.base.service.UserRepository;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@RestController
@RequestMapping(path = "users", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class UserController {

  private final UserRepository userRepository;

  public UserController(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @RequestMapping(method = RequestMethod.GET)
  @HystrixCommand(fallbackMethod = "listFailback")
  public ResponseEntity<List<User>> list() {
    return ResponseEntity.ok(StreamSupport
        .stream(userRepository.findAll().spliterator(), false)
        .map(Convert::api)
        .collect(Collectors.toList()));
  }

  public ResponseEntity<List<User>> listFailback() {
    return ResponseEntity.notFound().build();
  }

  @RequestMapping(method = RequestMethod.POST)
  @HystrixCommand(fallbackMethod = "createFallback")
  public ResponseEntity<User> create(@RequestBody User user){
    return ResponseEntity.ok(Convert.api(userRepository.create(Convert.base(user))));
  }

  public ResponseEntity<User> createFallback(User user){
    return ResponseEntity.notFound().build();
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.GET)
  @HystrixCommand(fallbackMethod = "getFallback")
  public ResponseEntity<User> get(@PathVariable UUID id){
    return userRepository.findById(id).map(user -> ResponseEntity.ok(Convert.api(user)))          //200 OK
        .orElse(ResponseEntity.notFound().build());  //404 Not found
  }

  public ResponseEntity<User> getFallback(UUID id) {
    return ResponseEntity.notFound().build();
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
  @HystrixCommand(fallbackMethod = "updateFallback")
  public ResponseEntity<User> update(@PathVariable UUID id, @RequestBody User user) {
    org.springframework.boot.kubescaler.base.model.User data = Convert.base(user);
    data.setId(id);
    return ResponseEntity.ok(Convert.api(userRepository.save(data)));
  }

  public ResponseEntity<User> updateFallback(UUID id, User user) {
    return ResponseEntity.notFound().build();
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
  @HystrixCommand(fallbackMethod = "deleteFallback")
  public ResponseEntity delete(@PathVariable UUID id){
    userRepository.deleteById(id);
    return ResponseEntity.ok().build();
  }

  public ResponseEntity deleteFallback(UUID id) {
    return ResponseEntity.notFound().build();
  }
}
