package org.springframework.boot.kubescaler.user.data;

import java.io.Serializable;
import java.util.UUID;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;
import com.datastax.driver.core.DataType;

import lombok.Data;

@Table
@Data
public class User implements Serializable {

  @PrimaryKey
  @CassandraType(type = DataType.Name.UUID)
  private UUID id;

  public User() {
    id = UUID.randomUUID();
  }
}