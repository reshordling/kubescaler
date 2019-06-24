package org.springframework.boot.kubescaler.base.configuration;

import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.CassandraCqlClusterFactoryBean;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.data.cassandra.core.cql.keyspace.CreateKeyspaceSpecification;
import org.springframework.data.cassandra.core.cql.keyspace.KeyspaceOption;
import com.datastax.driver.core.policies.Policies;

@Configuration
public class CassandraConfiguration extends AbstractCassandraConfiguration {

  @Value("${spring.data.cassandra.keyspace-name}")
  private String keyspaceName;

  @Value("${spring.data.cassandra.port}")
  private Integer port;

  @Value("${spring.data.cassandra.contact-points}")
  private String contactPoints;

  @Override
  protected String getKeyspaceName() {
    return keyspaceName;
  }

  @Override
  public SchemaAction getSchemaAction() {
    return SchemaAction.CREATE_IF_NOT_EXISTS;
  }

  @Bean
  public CassandraOperations cassandraOperations() {
    return new CassandraTemplate(session().getObject());
  }

  @Override
  public String[] getEntityBasePackages() {
    return new String[]{"org.springframework.boot.kubescaler.base.data"};
  }

  @Bean
  @Override
  public CassandraCqlClusterFactoryBean cluster() {
    CassandraCqlClusterFactoryBean bean = new CassandraCqlClusterFactoryBean();
    bean.setKeyspaceCreations(getKeyspaceCreations());
    bean.setContactPoints(contactPoints);
    bean.setReconnectionPolicy(Policies.defaultReconnectionPolicy());
    bean.setRetryPolicy(Policies.defaultRetryPolicy());
    bean.setPort(port);
    bean.setJmxReportingEnabled(false);
    return bean;
  }

  @Override
  protected List<CreateKeyspaceSpecification> getKeyspaceCreations() {
    return Collections.singletonList(CreateKeyspaceSpecification.createKeyspace(getKeyspaceName())
        .ifNotExists()
        .with(KeyspaceOption.DURABLE_WRITES, true)
        .withSimpleReplication());
  }

}
