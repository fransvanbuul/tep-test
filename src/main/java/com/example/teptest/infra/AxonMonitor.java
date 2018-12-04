package com.example.teptest.infra;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.config.Configuration;
import org.axonframework.eventhandling.EventTrackerStatus;
import org.axonframework.eventhandling.TrackingEventProcessor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class AxonMonitor {

    private final Configuration configuration;

    @Scheduled(fixedRate=5000)
    public void on() {
        TrackingEventProcessor processor = (TrackingEventProcessor)configuration.eventProcessingConfiguration().eventProcessorByProcessingGroup("readmodel").get();
        Map<Integer, EventTrackerStatus> statusMap = processor.processingStatus();
        for(EventTrackerStatus status: statusMap.values())
        log.debug("processing tracking token: {}", status.getTrackingToken());

    }

}
