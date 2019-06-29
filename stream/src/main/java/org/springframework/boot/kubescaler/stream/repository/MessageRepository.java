package org.springframework.boot.kubescaler.stream.repository;

import org.springframework.boot.kubescaler.stream.model.Message;

import reactor.core.publisher.Flux;

public interface MessageRepository {

    Flux<Message> findAll();

}
