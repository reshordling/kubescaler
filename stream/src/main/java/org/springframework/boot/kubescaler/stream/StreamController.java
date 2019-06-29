package org.springframework.boot.kubescaler.stream;

import java.util.UUID;
import org.springframework.boot.kubescaler.stream.model.Message;
import org.springframework.boot.kubescaler.stream.repository.MessageRepository;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@RestController
@Slf4j
public class StreamController {

  private final MessageRepository messageRepository;

  public StreamController(MessageRepository messageRepository) {
    this.messageRepository = messageRepository;
  }

  @GetMapping(path = "/stream/{userId}/{profileId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public Flux<Message> feed(@PathVariable UUID userId, @PathVariable UUID profileId) {
    return this.messageRepository.findAll();
  }
}

