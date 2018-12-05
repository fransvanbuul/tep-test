package com.example.teptest.load;

import com.example.teptest.events.AnotherEvt;
import com.example.teptest.events.SomethingHappened;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.GenericEventMessage;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@RequiredArgsConstructor
@Slf4j
@Profile("load")
public class EventStoreLoader implements CommandLineRunner {

    private final LoaderConfiguration loaderConfiguration;
    private final EventBus eventBus;
    private final TransactionTemplate transactionTemplate;
    private final Random random;
    private final AtomicInteger counter = new AtomicInteger();

    @Override
    public void run(String... args) throws Exception {
        log.info("Start publishing events, config: {}", loaderConfiguration);
        while(counter.get() < loaderConfiguration.getNumber()) {
            transactionTemplate.execute(new TransactionCallbackWithoutResult() {
                @Override
                public void doInTransactionWithoutResult(TransactionStatus status) {
                        for(int i = 0; i < loaderConfiguration.getBatchSize(); i++) {
                            eventBus.publish(GenericEventMessage.asEventMessage(randomEventMessage()));
                            maybeCreateGap();
                        }
                }
            });
            counter.addAndGet(loaderConfiguration.getBatchSize());
        }
        log.info("Event publishing complete, {} events published", counter.getAndSet(-1));
    }

    private void maybeCreateGap() {
        if(random.nextFloat() < loaderConfiguration.getGapLikelihood()) {
            int gapSize = random.nextInt(loaderConfiguration.getGapMaxSize()) + 1;
            createGap(gapSize);
        }
    }

    private static class GapException extends RuntimeException {
    }

    private void createGap(int gapSize) {
        try {
            transactionTemplate.execute(new TransactionCallbackWithoutResult() {
                @Override
                public void doInTransactionWithoutResult(TransactionStatus status) {
                    for (int i = 0; i < gapSize; i++) {
                        eventBus.publish(GenericEventMessage.asEventMessage(""));
                    }
                    throw new GapException();
                }
            });
        } catch(GapException ex) {
            log.debug("created gap of size {}", gapSize);
        }
    }

    public Object randomEventMessage() {
        if(random.nextBoolean()) {
            return new SomethingHappened(randomString(random.nextInt(60) + 3));
        } else {
            return new AnotherEvt(randomString(random.nextInt(60) + 3));
        }
    }

    private String randomString(int len) {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            sb.append(loaderConfiguration.getAlphabet().charAt(random.nextInt(loaderConfiguration.getAlphabet().length())));
        return sb.toString();
    }

    @Scheduled(fixedRate=1000)
    public void on() {
        int count = counter.get();
        if(count >= 0) log.debug("Published {} events", count);
    }

}
