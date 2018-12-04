package com.example.teptest.read;

import com.example.teptest.command.PaymentInitializedEvt;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.Timestamp;
import org.axonframework.eventhandling.TrackedEventMessage;
import org.axonframework.eventsourcing.eventstore.GapAwareTrackingToken;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@Slf4j
@ProcessingGroup("readmodel")
public class ReadModel {

    @EventHandler
    public void on(PaymentInitializedEvt evt, @Timestamp Instant instant, TrackedEventMessage message) {
        log.debug("Processing evt: {}", evt);
        log.debug("Timestamp: {}", instant);
        log.debug("EventMessage: {}", message);
        log.debug("Index: {}", ((GapAwareTrackingToken)message.trackingToken()).getIndex());
    }

}
