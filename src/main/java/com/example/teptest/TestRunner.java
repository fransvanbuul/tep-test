package com.example.teptest;

import com.example.teptest.command.InitializePaymentCmd;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class TestRunner implements CommandLineRunner {

    private final CommandGateway commandGateway;

    @Override
    public void run(String... args) throws Exception {
        for(int i = 0; i < 1000; i++) {
            Object cmd = new InitializePaymentCmd(UUID.randomUUID(), "Frans" + i, new BigDecimal(Math.random()*1000 - 200.).setScale(2, BigDecimal.ROUND_HALF_EVEN));
            log.debug("Sending {}", cmd);
            commandGateway.send(cmd);
        }
    }
}
