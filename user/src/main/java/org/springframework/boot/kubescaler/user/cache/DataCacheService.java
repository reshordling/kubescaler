package org.springframework.boot.kubescaler.user.cache;

import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DataCacheService {

  private final DataCache<String> stringCache;

  @Autowired
  public DataCacheService(DataCache<String> stringCache) {
    this.stringCache = stringCache;
  }

  public String get(String key) {
    return stringCache.getValue(key);
  }

  public void put(String key, String value) {
    stringCache.putValue(key, value);
  }

  public void putValueWithExpireTime(String key, String value, long timeout, TimeUnit unit) {
    stringCache.putValueWithExpireTime(key, value, timeout, unit);
  }
}
