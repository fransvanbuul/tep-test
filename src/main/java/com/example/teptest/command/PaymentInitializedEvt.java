package com.example.teptest.command;

import lombok.Value;

import java.math.BigDecimal;
import java.util.UUID;

@Value
public class PaymentInitializedEvt {

    UUID id;
    String receiver;
    BigDecimal amount;

}
