package org.springframework.boot.kubescaler.api;

import java.io.Serializable;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
@Builder
public class User implements Serializable {

  @NonNull
  private UUID id;
  private String name;
}