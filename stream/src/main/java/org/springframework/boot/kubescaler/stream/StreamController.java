package org.springframework.boot.kubescaler.stream;

import java.util.UUID;
import org.springframework.boot.kubescaler.stream.data.DataService;
import org.springframework.boot.kubescaler.stream.lock.LockService;
import org.springframework.boot.kubescaler.stream.model.Message;
import org.springframework.boot.kubescaler.stream.repository.MessageRepository;
import org.springframework.boot.kubescaler.stream.utils.MessageGenerator;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;

@RestController
public class StreamController {

  private final Message NONE = MessageGenerator.generate("Unable to acquire lock");

  private final MessageRepository messageRepository;
  private final LockService lockService;
  private final DataService dataService;

  public StreamController(MessageRepository messageRepository, LockService lockService, DataService dataService) {
    this.messageRepository = messageRepository;
    this.lockService = lockService;
    this.dataService = dataService;
  }

  @GetMapping(path = "/stream/{userId}/{profileId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public Flux<Message> feed(@PathVariable final UUID userId, @PathVariable final UUID profileId) {
    return dataService.hasAccess(userId, profileId) && lockService.acquireLock(userId, profileId) ?
        messageRepository.findAll().doOnCancel(() -> lockService.releaseLock(userId, profileId)) : Flux.just(NONE);
  }

  @ResponseBody
  @RequestMapping(value = "/metrics", produces = MediaType.TEXT_PLAIN_VALUE)
  public String metrics() {
    return String.format("# HELP active users\n# TYPE user count gauge\nusers %d", lockService.countLocks());
  }
}

