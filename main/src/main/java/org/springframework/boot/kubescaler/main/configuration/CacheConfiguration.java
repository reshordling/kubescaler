package org.springframework.boot.kubescaler.main.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.kubescaler.common.redis.configuration.RedisConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@Configuration
@EnableRedisRepositories
public class CacheConfiguration {

  @Bean
  RedisConfiguration redisConfiguration(@Value("${spring.redis.host}") String host, @Value("${spring.redis.port}") int port) {
    return new RedisConfiguration(host, port);
  }

  @Bean
  public JedisConnectionFactory jedisConnectionFactory(RedisConfiguration redisConfiguration) {
    return redisConfiguration.jedisConnectionFactory();
  }

  @Bean
  public RedisTemplate<Object, Object> redisTemplate(RedisConfiguration redisConfiguration) {
    return redisConfiguration.redisTemplate();
  }

}