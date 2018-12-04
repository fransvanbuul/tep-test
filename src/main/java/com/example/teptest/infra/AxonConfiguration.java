package com.example.teptest.infra;

import lombok.extern.slf4j.Slf4j;
import org.axonframework.config.EventProcessingConfiguration;
import org.axonframework.eventhandling.TrackingEventProcessorConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class AxonConfiguration {

    @Autowired
    public void configure(EventProcessingConfiguration eventProcessingConfiguration) {
        log.debug("configuring tracking event processorss");
        eventProcessingConfiguration.usingTrackingProcessors();
        eventProcessingConfiguration.usingTrackingProcessors(c ->
                TrackingEventProcessorConfiguration.forSingleThreadedProcessing().andBatchSize(100));
    }

}
