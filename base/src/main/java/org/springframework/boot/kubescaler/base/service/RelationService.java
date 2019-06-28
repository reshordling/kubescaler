package org.springframework.boot.kubescaler.base.service;

import static org.springframework.data.cassandra.core.query.Criteria.where;
import static org.springframework.data.cassandra.core.query.Query.query;

import java.util.List;
import java.util.UUID;
import org.springframework.boot.kubescaler.base.model.Profile;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.stereotype.Service;

@Service
public class RelationService {

  private final CassandraTemplate cassandraTemplate;

  public RelationService(CassandraTemplate cassandraTemplate) {
    this.cassandraTemplate = cassandraTemplate;
  }

  public List<Profile> getUserProfiles(UUID userId) {
    return cassandraTemplate.select(query(where("users").contains(userId)).withAllowFiltering(), Profile.class);
  }
}
