package org.springframework.boot.kubescaler.processor.lock;

import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LockTimerFactory {

  private final LockService lockService;
  private final AtomicInteger COUNTER = new AtomicInteger();

  public LockTimerFactory(LockService lockService) {
    this.lockService = lockService;
  }

  private class LockTimerTask extends TimerTask {
    private final UUID userId;
    private final UUID profileId;

    private LockTimerTask(UUID userId, UUID profileId) {
      COUNTER.incrementAndGet();
      this.userId = userId;
      this.profileId = profileId;
    }

    @Override
    public void run() {
      try {
        lockService.holdLock(userId, profileId);
      }
      catch (Exception e) {
        cancel();
      }
    }

    @Override
    public boolean cancel() {
      COUNTER.decrementAndGet();
      return super.cancel();
    }
  }

  public Optional<Timer> acquire(UUID userId, UUID profileId) {
    if (lockService.acquireLock(userId, profileId)) {
      try {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new LockTimerTask(userId, profileId), 5000, 30000);
        return Optional.of(timer);
      }
      catch (Exception e) {
        // consider reporting additional metric for autoscaling
      }
    }
    return Optional.empty();
  }

  public int count() {
    return COUNTER.intValue();
  }
}