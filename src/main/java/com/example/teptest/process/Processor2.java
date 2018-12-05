package com.example.teptest.process;

import lombok.extern.slf4j.Slf4j;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.TrackedEventMessage;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@ProcessingGroup("processor2")
@Slf4j
@Profile("process")
public class Processor2 {
    @EventHandler
    public void on(Object evt, TrackedEventMessage message) {
        log.debug("tracking token {} event {}", message.trackingToken(), evt);
    }
}
