package org.springframework.boot.kubescaler.main.lock;

import java.util.UUID;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@Service
public class LockService {

  private final ValueOperations<Object, Object> valueOperations;

  public LockService(RedisTemplate<Object, Object> redisTemplate) {
    this.valueOperations = redisTemplate.opsForValue();
  }

  @HystrixCommand(fallbackMethod = "isActiveProfileFallback")
  public boolean isActiveProfile(UUID userId, UUID profileId) {
    return valueOperations.get(createKey(userId, profileId)) != null;
  }

  public boolean isActiveProfileFallback(UUID userId, UUID profileId) {
    return false;
  }

  private String createKey(UUID userId, UUID profildId) {
    return userId.toString() + ":" + profildId.toString();
  }
}
