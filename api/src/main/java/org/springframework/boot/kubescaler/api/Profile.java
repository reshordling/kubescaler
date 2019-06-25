package org.springframework.boot.kubescaler.api;

import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Profile implements Serializable {

  private UUID id;
  private Set<UUID> users;
}