package org.springframework.boot.kubescaler.base.model;

import java.io.Serializable;
import java.util.UUID;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;
import com.datastax.driver.core.DataType;

import lombok.AllArgsConstructor;
import lombok.Data;

@Table
@AllArgsConstructor
@Data
public class Profile implements Serializable, UuidSetter {

  @PrimaryKey
  @CassandraType(type = DataType.Name.UUID)
  private UUID id;
}