package org.springframework.boot.kubescaler.base.service;

import java.util.UUID;
import org.springframework.boot.kubescaler.base.model.UuidSetter;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public interface UuidCrudRepository<T extends UuidSetter> extends CrudRepository<T, UUID> {

  @Transactional
  default T create(T data) {
    for (int retries = 0; retries < 3; retries++) {
      UUID uid = UUID.randomUUID();
      data.setId(uid);
      if (!existsById(uid)) {
        return save(data);
      }
    }
    throw new IllegalStateException("Should never reach this point");
  }
}
