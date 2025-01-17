package com.fiapx.apiprocessamento.adapter.out.sqs;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SQSMessageScheduler {

    private final SQSConsumer sqsConsumer;

    @Scheduled(fixedRate = 5000)
    public void consumeMessages() {
        sqsConsumer.receiveMessages();
    }
}