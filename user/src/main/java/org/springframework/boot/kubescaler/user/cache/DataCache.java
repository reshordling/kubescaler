package org.springframework.boot.kubescaler.user.cache;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

@Configuration
class DataCache<T> {
  private final RedisTemplate<String, T> redisTemplate;
  private final HashOperations<String, Object, T> hashOperation;
  private final ListOperations<String, T> listOperation;
  private final ValueOperations<String, T> valueOperations;

  @Autowired
  DataCache(RedisTemplate<String, T> redisTemplate) {
    this.redisTemplate = redisTemplate;
    this.hashOperation = redisTemplate.opsForHash();
    this.listOperation = redisTemplate.opsForList();
    this.valueOperations = redisTemplate.opsForValue();
  }

  void putMap(String redisKey, Object key, T data) {
    hashOperation.put(redisKey, key, data);
  }

  T getMapAsSingleEntry(String redisKey, Object key) {
    return hashOperation.get(redisKey, key);
  }

  Map<Object, T> getMapAsAll(String redisKey) {
    return hashOperation.entries(redisKey);
  }

  void putValue(String key, T value) {
    valueOperations.set(key, value);
  }

  void putValueWithExpireTime(String key, T value, long timeout, TimeUnit unit) {
    valueOperations.set(key, value, timeout, unit);
  }

  T getValue(String key) {
    return valueOperations.get(key);
  }

  void setExpire(String key, long timeout, TimeUnit unit) {
    redisTemplate.expire(key, timeout, unit);
  }
}
