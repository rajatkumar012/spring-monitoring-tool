package com.brillio.poc.component;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SyncScheduler {
    @Scheduled(fixedDelayString = "PT1M")
    public void run() {
        // sync logic here.

    }
}
