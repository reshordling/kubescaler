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

@RestController
@RequestMapping(path = "users/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class UserController {

  private final UserRepository userRepository;

  public UserController(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @RequestMapping(method = RequestMethod.GET)
  public List<User> list(){
    return StreamSupport
        .stream(userRepository.findAll().spliterator(), false)
        .map(Convert::api)
        .collect(Collectors.toList());
  }

  @RequestMapping(method = RequestMethod.POST)
  public ResponseEntity<User> create(@RequestBody User user){
    return ResponseEntity.ok().body(Convert.api(userRepository.create(Convert.base(user))));
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.GET)
  public ResponseEntity<User> get(@PathVariable UUID id){
    return userRepository.findById(id).map(user -> ResponseEntity.ok().body(Convert.api(user)))          //200 OK
        .orElseGet(() -> ResponseEntity.notFound().build());  //404 Not found
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
  public ResponseEntity<User> update(@PathVariable UUID id, @RequestBody User user) {
    org.springframework.boot.kubescaler.base.model.User data = Convert.base(user);
    data.setId(id);
    return ResponseEntity.ok().body(Convert.api(userRepository.save(data)));
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
  public void delete(@PathVariable UUID id){
    userRepository.deleteById(id);
  }
}
