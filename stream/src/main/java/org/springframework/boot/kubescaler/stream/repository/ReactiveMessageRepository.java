package org.springframework.boot.kubescaler.stream.repository;

import java.time.Duration;
import java.util.Collections;
import org.springframework.boot.kubescaler.stream.model.Message;
import org.springframework.boot.kubescaler.stream.utils.MessageGenerator;
import org.springframework.stereotype.Repository;

import reactor.core.publisher.Flux;

@Repository
public class ReactiveMessageRepository implements MessageRepository {

  @Override
  public Flux<Message> findAll() {
    //simulate data streaming every 1000 millis.
    return Flux.interval(Duration.ofMillis(1000))
        .onBackpressureDrop()
        .map(interval -> Collections.singletonList(MessageGenerator.generate()))
        .flatMapIterable(x -> x);
  }
}
