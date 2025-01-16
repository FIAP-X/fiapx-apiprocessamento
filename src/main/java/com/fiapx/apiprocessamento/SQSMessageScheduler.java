package com.fiapx.apiprocessamento;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SQSMessageScheduler {

    private final SQSConsumer sqsConsumer;

    public SQSMessageScheduler(SQSConsumer sqsConsumer) {
        this.sqsConsumer = sqsConsumer;
    }

    @Scheduled(fixedRate = 5000)
    public void consumeMessages() {
        sqsConsumer.receiveMessages();
    }
}
