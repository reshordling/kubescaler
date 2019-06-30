package org.springframework.boot.kubescaler.stream.lock;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.lomagicode.redlock.spring.boot.autoconfigure.RedisLockService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LockService {

  private final ValueOperations<Object, Object> valueOperations;
  private final RedisLockService redisLockService;
  private final Runnable NOOP_ON_ERROR = () -> {};

  // Key and onHoldError
  private final Map<String, Runnable> localStates = new ConcurrentHashMap<>();

  public LockService(RedisTemplate<Object, Object> redisTemplate, RedisLockService redisLockService) {
    this.valueOperations = redisTemplate.opsForValue();
    this.redisLockService = redisLockService;
  }

  @HystrixCommand(fallbackMethod = "acquireLockFallback")
  public boolean acquireLock(UUID userId, UUID profileId) {
    return acquireLock(createKey(userId, profileId), NOOP_ON_ERROR);
  }

  // call onHoldError if holding lock fails
  @HystrixCommand(fallbackMethod = "acquireLockFallback")
  public boolean acquireLock(UUID userId, UUID profileId, @NonNull Runnable onHoldError /* thread-safe only */) {
    return acquireLock(createKey(userId, profileId), onHoldError);
  }

  public void releaseLock(UUID userId, UUID profileId) {
    releaseLock(createKey(userId, profileId));
  }

  public int countLocks() {
    return localStates.size();
  }

  @Scheduled(fixedRate = 10_000 /* millis */)
  void holdLockWithFixedRate() {
    localStates.keySet().parallelStream().forEach(this::holdLock);
  }

  private boolean acquireLock(String key, Runnable onHoldError) {
    log.debug("Try to evict lock on {}", key);
    // https://redis.io/topics/distlock
    if (redisLockService.acquire(key, 1, TimeUnit.MINUTES)) {
      valueOperations.set(key, Boolean.TRUE, 1, TimeUnit.MINUTES);
      localStates.put(key, onHoldError);
      log.debug("Locked {}", key);
      return true;
    }
    log.debug("Unable to lock {}", key);
    return false;
  }

  boolean acquireLockFallback(UUID userId, UUID profileId) {
    return false;
  }

  boolean acquireLockFallback(UUID userId, UUID profileId, Runnable onHoldError) {
    return false;
  }

  private void holdLock(String key) {
    try {
      redisLockService.hold(key, 1, TimeUnit.MINUTES);
      valueOperations.set(key, Boolean.TRUE, 1, TimeUnit.MINUTES);
      log.debug("Lock held on {}", key);
    }
    catch (Exception e) {
      onHoldError(key, localStates.remove(key));
    }
  }
  
  private void onHoldError(String key, Runnable onHoldError) {
    if (onHoldError != null) {
      try {
        onHoldError.run();
      }
      catch (Exception runnableExc) {
        log.warn(String.format("onHoldError failed for %s", key), runnableExc);
      }
    }
  }

  private void releaseLock(String key) {
    // Task requirement - allow to reconnect after 1 min, not immediately
    // so do not release lock, just let it to expire in Redis
    localStates.remove(key);
    log.debug("Lock released on {}", key);
  }

  private String createKey(@NonNull UUID userId, @NonNull UUID profileId) {
    return userId.toString() + ":" + profileId.toString();
  }
}
