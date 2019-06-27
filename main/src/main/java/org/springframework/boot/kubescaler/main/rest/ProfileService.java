package org.springframework.boot.kubescaler.main.rest;

import java.util.List;
import java.util.UUID;
import org.springframework.boot.kubescaler.api.Profile;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

//@FeignClient
//@RequestMapping(value = "/profiles", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
public interface ProfileService {
  @RequestMapping(method = RequestMethod.GET)
  ResponseEntity<List<Profile>> getAll();

  @RequestMapping(method = RequestMethod.POST)
  ResponseEntity<Profile> create(@RequestBody Profile profile);

  @RequestMapping(value = "/{id}", method = RequestMethod.GET)
  ResponseEntity<Profile> get(@PathVariable UUID id);

  @RequestMapping(value = "/drop", method = RequestMethod.DELETE)
  ResponseEntity drop();
}
