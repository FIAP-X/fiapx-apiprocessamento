package com.fiapx.apiprocessamento.adapter.out.sqs;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

class SQSMessageSchedulerTest {

    @Mock
    private SQSConsumer sqsConsumer;

    @InjectMocks
    private SQSMessageScheduler sqsMessageScheduler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveConsumirMensagensPeriodicamente() {
        sqsMessageScheduler.consumeMessages();
        verify(sqsConsumer, times(1)).receiveMessages();
    }
}