package org.springframework.boot.kubescaler.main.rest;

import java.util.List;
import org.springframework.boot.kubescaler.api.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("base-service")
@RequestMapping(value = "/users", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
public interface UserService {
  @RequestMapping(method = RequestMethod.GET)
  ResponseEntity<List<User>> getAll();

  @RequestMapping(method = RequestMethod.POST)
  ResponseEntity<User> create(User user);
}
