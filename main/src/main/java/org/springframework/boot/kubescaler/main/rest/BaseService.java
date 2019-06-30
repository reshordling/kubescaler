package org.springframework.boot.kubescaler.main.rest;

import java.util.List;
import java.util.UUID;
import org.springframework.boot.kubescaler.api.Profile;
import org.springframework.boot.kubescaler.api.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("base-service")
@RequestMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
public interface BaseService {
  @RequestMapping(value = "/users", method = RequestMethod.GET)
  ResponseEntity<List<User>> getAllUsers();

  @RequestMapping(value = "/users", method = RequestMethod.POST)
  ResponseEntity<User> createUser(@RequestBody User user);

  @RequestMapping(value = "/users/{id}", method = RequestMethod.GET)
  ResponseEntity<User> getUser(@PathVariable UUID id);

  @RequestMapping(value = "/users/drop", method = RequestMethod.DELETE)
  ResponseEntity dropUsers();

  @RequestMapping(value = "/profiles", method = RequestMethod.GET)
  ResponseEntity<List<Profile>> getAllProfiles();

  @RequestMapping(value = "/profiles", method = RequestMethod.POST)
  ResponseEntity<Profile> createProfile(@RequestBody Profile profile);

  @RequestMapping(value = "/profiles/user/{id}", method = RequestMethod.GET)
  ResponseEntity<List<Profile>> getUserProfiles(@PathVariable UUID id);

  @RequestMapping(value = "/profiles/drop", method = RequestMethod.DELETE)
  ResponseEntity dropProfiles();
}
