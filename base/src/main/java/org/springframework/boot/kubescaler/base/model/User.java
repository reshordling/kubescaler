package org.springframework.boot.kubescaler.base.model;

import java.io.Serializable;
import java.util.UUID;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;
import com.datastax.driver.core.DataType;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Table("user")
@Data
@Builder
public class User implements Serializable, UuidSetter {

  @PrimaryKey
  @CassandraType(type = DataType.Name.UUID)
  public UUID id;

  @CassandraType(type = DataType.Name.TEXT)
  public String name;
}