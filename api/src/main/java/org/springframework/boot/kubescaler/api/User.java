package org.springframework.boot.kubescaler.api;

import java.io.Serializable;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User implements Serializable {

  private UUID id;
  private String name;
}