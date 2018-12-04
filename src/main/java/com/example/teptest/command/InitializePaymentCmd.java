package com.example.teptest.command;

import lombok.Value;
import org.axonframework.commandhandling.TargetAggregateIdentifier;

import java.math.BigDecimal;
import java.util.UUID;

@Value
public class InitializePaymentCmd {

    @TargetAggregateIdentifier UUID id;
    String receiver;
    BigDecimal amount;

}
