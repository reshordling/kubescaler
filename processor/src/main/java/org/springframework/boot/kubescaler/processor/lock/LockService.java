package org.springframework.boot.kubescaler.processor.lock;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import com.lomagicode.redlock.spring.boot.autoconfigure.RedisLockService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@Service
class LockService {

  private final ValueOperations<Object, Object> valueOperations;
  private final RedisLockService redisLockService;

  public LockService(RedisTemplate<Object, Object> redisTemplate, RedisLockService redisLockService) {
    this.valueOperations = redisTemplate.opsForValue();
    this.redisLockService = redisLockService;
  }

  @HystrixCommand(fallbackMethod = "acquireLockFallback")
  boolean acquireLock(UUID userId, UUID profileId) {
    String key = createKey(userId, profileId);
    // https://redis.io/topics/distlock
    if (redisLockService.acquire(key, 1, TimeUnit.MINUTES)) {
      valueOperations.set(key, Boolean.TRUE, 1, TimeUnit.MINUTES);
      return true;
    }
    return false;
  }

  public boolean acquireLockFallback(UUID userId, UUID profileId) {
    return false;
  }

  private String createKey(UUID userId, UUID profildId) {
    return userId.toString() + ":" + profildId.toString();
  }

  // Redis restart is not handled
  void holdLock(UUID userId, UUID profileId) {
    String key = createKey(userId, profileId);
    redisLockService.hold(key, 1, TimeUnit.MINUTES);
    valueOperations.set(key, Boolean.TRUE, 1, TimeUnit.MINUTES);
  }
}