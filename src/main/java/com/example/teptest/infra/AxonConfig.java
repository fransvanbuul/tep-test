package com.example.teptest.infra;

import lombok.extern.slf4j.Slf4j;
import org.axonframework.config.EventProcessingConfiguration;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.TrackingEventProcessorConfiguration;
import org.axonframework.eventsourcing.eventstore.EmbeddedEventStore;
import org.axonframework.eventsourcing.eventstore.EventStorageEngine;
import org.axonframework.eventsourcing.eventstore.jdbc.JdbcEventStorageEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

@Component
@Slf4j
public class AxonConfig {

    @Autowired
    public void configure(EventProcessingConfiguration eventProcessingConfiguration) {
        log.debug("configuring tracking event processorss");
        eventProcessingConfiguration.usingTrackingProcessors(c ->
                TrackingEventProcessorConfiguration.forSingleThreadedProcessing().andBatchSize(10000));
    }

    @Autowired
    public void configure(TransactionTemplate transactionTemplate) {
        transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
    }

    @Profile("jdbc")
    @Bean(name = "eventBus")
    public EventBus eventStore(EventStorageEngine eventStorageEngine) {
        return new EmbeddedEventStore(eventStorageEngine);
    }

}
