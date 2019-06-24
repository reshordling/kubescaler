package org.springframework.boot.kubescaler.user.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AvailabilityFilteringRule;
import com.netflix.loadbalancer.IPing;
import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.PingUrl;

public class RibbonConfiguration {

  @SuppressWarnings("SpringJavaAutowiredMembersInspection")
  @Autowired
  IClientConfig ribbonClientConfig;

  @Bean
  public IPing ribbonPing(IClientConfig ignored) {
    return new PingUrl();
  }

  @Bean
  public IRule ribbonRule(IClientConfig ingored) {
    return new AvailabilityFilteringRule();
  }
}
