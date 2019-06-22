package org.springframework.boot.kubescaler.user;

import java.net.InetAddress;
import java.net.UnknownHostException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@EnableCircuitBreaker
@RibbonClient(name = "db-service", configuration = RibbonConfiguration.class)
@RestController
public class Application {

  @Autowired
  public Application(ClientConfig config) {
    this.config = config;
  }

  @LoadBalanced
  @Bean
  RestTemplate restTemplate() {
    return new RestTemplate();
  }

  private final ClientConfig config;


  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @GetMapping("")
  public String helloWorld() throws UnknownHostException {

    return "Hello from " + InetAddress.getLocalHost().getHostName() + " supported by " + config.getMessage();
  }

}
