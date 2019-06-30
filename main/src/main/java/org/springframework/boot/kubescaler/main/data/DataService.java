package org.springframework.boot.kubescaler.main.data;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.springframework.boot.kubescaler.api.Profile;
import org.springframework.boot.kubescaler.api.User;
import org.springframework.boot.kubescaler.main.rest.BaseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.github.javafaker.Faker;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DataService {

  private final BaseService baseService;
  private final Set<UUID> sharedUserIds = ConcurrentHashMap.newKeySet();

  public DataService(BaseService baseService) {
    this.baseService = baseService;
  }

  @Transactional
  @Async
  // No Hystrix - log exception
  // Should not be run by other methods inside this service
  public void createTestdataAsync() {
    log.debug("Generate testdata asynchronously");
    Faker fakeNameGenerator = new Faker();
    // cannot save in bulk - there are chances of non-unique UUIDs (is it possible to move UUID.random() to Cassandra?)
    IntStream.rangeClosed(2, new Random().nextInt(100)).forEach(nr -> {
      log.debug("Creating users for profile nr {}", nr);
      Set<UUID> users = IntStream
          .rangeClosed(2, new Random().nextInt(100))
          .mapToObj(dr -> baseService.createUser(
              User.builder().name(fakeNameGenerator.funnyName().name()).build()
          ))
          // do not explode on failures
          .filter(response -> response.getStatusCode() == HttpStatus.OK)
          .map(this::extractId)
          .collect(Collectors.toSet());

      if (nr == 2) {
        sharedUserIds.addAll(users);
      }
      else {
        // some users with multiple profiles
        users.addAll(sharedUserIds);
      }
      ResponseEntity<Profile> profileResponse = baseService.createProfile(Profile.builder().users(users).build());
      if (profileResponse.getStatusCode() == HttpStatus.OK) {
        log.debug("Created profile {}", profileResponse.getBody());
      }
      else {
        log.warn("Failed to create profile nr {}", nr);
      }
    });
  }

  @Transactional
  // it is OK to explode here
  public void dropAll() {
    sharedUserIds.clear();
    baseService.dropProfiles();
    baseService.dropUsers();
  }

  @HystrixCommand(fallbackMethod = "getProfilesFallback")
  public Collection<Profile> getProfiles() {
    ResponseEntity<List<Profile>> profilesResponse = baseService.getAllProfiles();
    return profilesResponse.getStatusCode() == HttpStatus.OK ? profilesResponse.getBody() : Collections.emptyList();
  }

  public Collection<Profile> getProfilesFallback() {
    return Collections.emptyList();
  }

  @HystrixCommand(fallbackMethod = "getUsersFallback")
  public Collection<User> getUsers() {
    ResponseEntity<List<User>> profilesResponse = baseService.getAllUsers();
    return profilesResponse.getStatusCode() == HttpStatus.OK ? profilesResponse.getBody() : Collections.emptyList();
  }

  public Collection<User> getUsersFallback() {
    return Collections.emptyList();
  }

  public Collection<UUID> getSuggestedUserIds() {
    return sharedUserIds;
  }

  @HystrixCommand(fallbackMethod = "getUserProfilesFallback")
  public Collection<Profile> getUserProfiles(UUID userId) {
    ResponseEntity<List<Profile>> profilesResponse = baseService.getUserProfiles(userId);
    return profilesResponse.getStatusCode() == HttpStatus.OK ?
        profilesResponse.getBody() : Collections.emptyList();
  }

  public Collection<Profile> getUserProfilesFallback(UUID userId) {
    return Collections.emptyList();
  }

  private UUID extractId(ResponseEntity<User> userResponseEntity) {
    User user = userResponseEntity.getBody();
    return user == null ? null : user.getId();
  }
}
