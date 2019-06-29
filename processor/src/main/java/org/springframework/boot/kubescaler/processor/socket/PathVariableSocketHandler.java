package org.springframework.boot.kubescaler.processor.socket;

import java.net.URI;
import java.time.Duration;
import java.util.Map;
import java.util.Optional;
import java.util.Timer;
import java.util.UUID;
import org.apache.commons.lang.StringUtils;
import org.springframework.boot.kubescaler.processor.lock.LockTimerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import org.springframework.web.reactive.socket.adapter.ReactorNettyWebSocketSession;
import org.springframework.web.util.UriTemplate;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

//@Component
@Slf4j
public class PathVariableSocketHandler implements WebSocketHandler {

  private Flux<String> result = Flux.just("");

  public Flux<String> reverseString(String userInput)
  {
    result = Flux.just(new StringBuilder(userInput).reverse().toString());
    return result;
  }

  @Override
  public Mono<Void> handle(WebSocketSession session) {
    log.error("URRRA");
    return session.send(session.receive()
        .doOnNext(WebSocketMessage::retain)
        .delayElements(Duration.ofSeconds(1)).log());
  }

  private final String dummyResponse = StringUtils.repeat("e", 1_024 * 512);
  private final UriTemplate template = new UriTemplate(getMappingTemplate());

//  private final LockTimerFactory lockTimerFactory;
//
//  public PathVariableSocketHandler(LockTimerFactory lockTimerFactory) {
//    this.lockTimerFactory = lockTimerFactory;
//  }

//  @Override
  public Mono<Void> handles(WebSocketSession webSocketSession) {
    log.error("Websocket connected");
    final Map<String, String> pathVariables = getPathVariables(webSocketSession);
    Optional<UUID> userIdOpt = getUuid(pathVariables, "userId");
    Optional<UUID> profileIdOpt = getUuid(pathVariables, "profileId");
    if (userIdOpt.isEmpty() || profileIdOpt.isEmpty()) {
      log.error("Cannot parse args");
      return webSocketSession.send(Mono.error(new IllegalArgumentException("Wrong args")));
    }
    Optional<Timer> lockTimerOpt = null;//lockTimerFactory.acquire(userIdOpt.get(), profileIdOpt.get());
    if (lockTimerOpt.isEmpty()) {
      log.error("Cannot acquire lock");
      return webSocketSession.send(Mono.error(new IllegalArgumentException("Cannot acquire lock, try again later")));
    }

    try {
      log.error("Start handling");
      return handleSession(webSocketSession);
    }
    finally {
      log.error("End handling");
      lockTimerOpt.get().cancel();
    }
  }

  public Mono<Void> handleSession(WebSocketSession webSocketSession) {
    return webSocketSession.receive()
        .map(msg -> {
          log.error("Message {}", msg.getPayloadAsText());
          return webSocketSession.send(Mono.just(webSocketSession.textMessage(dummyResponse)));
        })
        .then();
  }

  public String getMappingTemplate() {
    return "/pipe/{userId}/{profileId}";
  }

  private Map<String, String> getPathVariables(WebSocketSession session) {
    ReactorNettyWebSocketSession nettySession = (ReactorNettyWebSocketSession) session;
    URI uri = nettySession.getHandshakeInfo().getUri();
    return template.match(uri.getPath());
  }

  private Optional<UUID> getUuid(Map<String, String> pathVariables, String variable) {
    try {
      return Optional.of(UUID.fromString(pathVariables.get(variable)));
    }
    catch (Exception e) {
      log.trace("Cannot parse variable", e);
      return Optional.empty();
    }
  }
}
