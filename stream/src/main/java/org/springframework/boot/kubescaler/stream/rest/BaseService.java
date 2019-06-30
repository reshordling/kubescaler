package org.springframework.boot.kubescaler.stream.rest;

import java.util.UUID;
import org.springframework.boot.kubescaler.api.Profile;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("base-service")
@RequestMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
public interface BaseService {
  @RequestMapping(value = "/profiles/{id}", method = RequestMethod.GET)
  ResponseEntity<Profile> getProfile(@PathVariable UUID id);
}
