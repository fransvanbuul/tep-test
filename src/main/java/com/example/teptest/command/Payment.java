package com.example.teptest.command;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.spring.stereotype.Aggregate;

import java.math.BigDecimal;
import java.util.UUID;

import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;

@Aggregate
@NoArgsConstructor
@Slf4j
public class Payment {

    @AggregateIdentifier
    private UUID id;

    @CommandHandler
    public Payment(InitializePaymentCmd cmd) {
        log.debug("processing {}", cmd);
        if(cmd.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("amount <= 0");
        }
        apply(new PaymentInitializedEvt(cmd.getId(), cmd.getReceiver(), cmd.getAmount()));
    }

    @EventSourcingHandler
    public void on(PaymentInitializedEvt evt) {
        log.debug("processing {}", evt);
        id = evt.getId();
    }

}
