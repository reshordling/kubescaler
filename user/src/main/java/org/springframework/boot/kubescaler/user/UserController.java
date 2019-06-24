package org.springframework.boot.kubescaler.user;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.net.InetAddress;
import java.net.UnknownHostException;
import org.springframework.boot.kubescaler.user.cache.DataCacheService;
import org.springframework.boot.kubescaler.user.configuration.ClientConfig;
import org.springframework.boot.kubescaler.user.data.User;
import org.springframework.boot.kubescaler.user.data.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class UserController {

  private final RestTemplate restTemplate;
  private final ClientConfig config;
  private final MemoryMXBean mbean = ManagementFactory.getMemoryMXBean();
  private final UserRepository userRepository;
  private final DataCacheService cacheService;

  public UserController(RestTemplate restTemplate, ClientConfig config, UserRepository userRepository, DataCacheService cacheService) {
    this.restTemplate = restTemplate;
    this.config = config;
    this.userRepository = userRepository;
    this.cacheService = cacheService;
  }

  @GetMapping("")
  public String list() {
    String host = "";
    try {
      host = InetAddress.getLocalHost().getHostName();
    }
    catch (UnknownHostException e) {
      e.printStackTrace();
    }
    return host + ": Hello from " + userRepository.count() + " user(s) supported by " + config.getMessage() + " cached " + cacheService.get("Lala");
  }

  @GetMapping("/create")
  public String create() {
    User user = new User();
    userRepository.save(user);
    cacheService.put("Lala", "distributed cache");
    return "Created user " + user.getId();
  }

  @ResponseBody
  @RequestMapping(value = "/metrics", produces = "text/plain")
  public String metrics() {
    return String.format("# HELP active users\n# TYPE memory gauge\nmemory %d", mbean.getHeapMemoryUsage().getUsed() / 1024 / 1024);
  }

  @RequestMapping(value = "/health")
  public ResponseEntity health() {
    return new ResponseEntity<>(HttpStatus.OK);
  }

}
