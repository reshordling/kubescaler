package org.springframework.boot.kubescaler.api;

import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
@Builder
public class Profile implements Serializable {

  @NonNull
  private UUID id;
  @NonNull
  private Set<UUID> users;
}