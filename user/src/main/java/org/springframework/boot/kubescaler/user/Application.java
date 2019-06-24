package org.springframework.boot.kubescaler.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.kubescaler.user.configuration.RibbonConfiguration;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableDiscoveryClient
@EnableCircuitBreaker
@RibbonClient(name = "db-service", configuration = RibbonConfiguration.class)
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @LoadBalanced
  @Bean
  RestTemplate restTemplate() {
    return new RestTemplate();
  }


}
