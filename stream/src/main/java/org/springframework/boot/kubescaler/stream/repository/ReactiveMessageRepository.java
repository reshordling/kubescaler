package org.springframework.boot.kubescaler.stream.repository;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.boot.kubescaler.stream.model.Message;
import org.springframework.boot.kubescaler.stream.utils.MessageGenerator;
import org.springframework.stereotype.Repository;

import reactor.core.publisher.Flux;

@Repository
public class ReactiveMessageRepository implements MessageRepository {

    @Override
    public Flux<Message> findAll() {
        //simulate data streaming every 100 millis.
        return Flux.interval(Duration.ofMillis(100))
                .onBackpressureDrop()
                .map(this::generateMessage)
                .flatMapIterable(x -> x);
    }

    private List<Message> generateMessage(long interval) {

        Message obj = new Message(MessageGenerator.host(), MessageGenerator.randomMessage(), MessageGenerator.getCurrentTimeStamp());
        return Collections.singletonList(obj);

    }

}
