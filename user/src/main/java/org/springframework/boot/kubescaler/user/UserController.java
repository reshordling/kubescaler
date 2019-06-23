package org.springframework.boot.kubescaler.user;

import java.lang.management.ManagementFactory;
import com.sun.management.OperatingSystemMXBean;
import java.lang.management.MemoryMXBean;
import java.net.InetAddress;
import java.net.UnknownHostException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class UserController {

  public UserController(RestTemplate restTemplate, ClientConfig config) {
    this.restTemplate = restTemplate;
    this.config = config;
  }

  private final RestTemplate restTemplate;
  private final ClientConfig config;
  private final MemoryMXBean mbean = ManagementFactory.getMemoryMXBean();

  @GetMapping("")
  public String helloWorld() throws UnknownHostException {

    return "Hola from " + InetAddress.getLocalHost().getHostName() + " supported by " + config.getMessage();
  }

  @ResponseBody
  @RequestMapping(value="/metrics", produces="text/plain")
  public String metrics() {
    return String.format("# HELP active users\n# TYPE memory gauge\nmemory %d",mbean.getHeapMemoryUsage().getUsed() / 1024 / 1024);
  }

  @RequestMapping(value="/health")
  public ResponseEntity health() {
    return new ResponseEntity<>(HttpStatus.OK);
  }

}
