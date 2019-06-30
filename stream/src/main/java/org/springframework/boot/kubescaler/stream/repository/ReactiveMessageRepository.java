package org.springframework.boot.kubescaler.stream.repository;

import java.time.Duration;
import java.util.Collections;
import org.springframework.boot.kubescaler.stream.model.Message;
import org.springframework.boot.kubescaler.stream.utils.MessageGenerator;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Repository
@Slf4j
public class ReactiveMessageRepository implements MessageRepository {

  @Override
  public Flux<Message> findAll() {
    //simulate data streaming every 1000 millis.
    return Flux.interval(Duration.ofMillis(1000))
        .onBackpressureDrop()
        .map(interval -> Collections.singletonList(MessageGenerator.generate()))
        .flatMapIterable(x -> x);


//    MonoProcessor<String> processor = MonoProcessor.create();
//    Executors.newSingleThreadScheduledExecutor().schedule(() -> processor.onNext("STOP"), 10, TimeUnit.SECONDS);
//    return result.takeUntilOther(processor);
  }
}
