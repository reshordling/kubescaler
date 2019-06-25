package org.springframework.boot.kubescaler.base.model;

import java.io.Serializable;
import java.util.Set;
import java.util.UUID;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;
import com.datastax.driver.core.DataType;

import lombok.Builder;
import lombok.Data;

@Table("profile")
@Data
@Builder
public class Profile implements Serializable, UuidSetter {

  @PrimaryKey
  @CassandraType(type = DataType.Name.UUID)
  private UUID id;

  @CassandraType(type = DataType.Name.SET, typeArguments = { DataType.Name.UUID } )
  private Set<UUID> users;
}