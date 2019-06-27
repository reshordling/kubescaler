package org.springframework.boot.kubescaler.common.redis.cache;

import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// trick to share service over multiple applications
public class DataCacheService {

  private final DataCache<Object> stringCache;

  public DataCacheService(DataCache<Object> stringCache) {
    this.stringCache = stringCache;
  }

  public <T>T get(String key) {
    return (T)stringCache.getValue(key);
  }

  public <T>void put(String key, T value) {
    stringCache.putValue(key, value);
  }

  public <T>void putValueWithExpireTime(String key, T value, long timeout, TimeUnit unit) {
    stringCache.putValueWithExpireTime(key, value, timeout, unit);
  }
}
