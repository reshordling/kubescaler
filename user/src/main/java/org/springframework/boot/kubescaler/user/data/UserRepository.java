package org.springframework.boot.kubescaler.user.data;

import java.util.UUID;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, UUID> {
}
