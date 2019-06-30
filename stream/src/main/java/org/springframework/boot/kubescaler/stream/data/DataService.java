package org.springframework.boot.kubescaler.stream.data;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import org.springframework.boot.kubescaler.api.Profile;
import org.springframework.boot.kubescaler.stream.rest.BaseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@Service
public class DataService {

  private final BaseService baseService;

  public DataService(BaseService baseService) {
    this.baseService = baseService;
  }

  @HystrixCommand(fallbackMethod = "hasAccessFallback")
  public boolean hasAccess(UUID userId, UUID profileId) {
    ResponseEntity<Profile> profilesResponse = baseService.getProfile(profileId);
    return profilesResponse.getStatusCode() == HttpStatus.OK && extractUserIds(profilesResponse).contains(userId);
  }

  public boolean hasAccessFallback(UUID userId, UUID profileId) {
    return false;
  }

  private Set<UUID> extractUserIds(ResponseEntity<Profile> profilesResponse) {
    Profile profile = profilesResponse.getBody();
    if (profile != null) {
      Set<UUID> userIds = profile.getUsers();
      return userIds != null ? userIds : Collections.emptySet();
    }
    return Collections.emptySet();
  }
}
