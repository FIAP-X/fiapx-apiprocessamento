package com.fiapx.apiprocessamento.adapter.out.sqs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiapx.apiprocessamento.core.usecase.ProcessamentoUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.*;

import java.io.IOException;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SQSConsumerTest {

    @Mock
    private SqsClient sqsClient;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private ProcessamentoUseCase processamentoUseCase;

    @InjectMocks
    private SQSConsumer sqsConsumer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveProcessarMensagensComSucesso() throws Exception {
        String queueUrl = "https://sqs.us-east-1.amazonaws.com/123456789012/my-queue";
        String messageBody = "{\"Records\":[{\"s3\":{\"object\":{\"key\":\"video.mp4\"}}}]}";

        when(sqsClient.getQueueUrl(any(GetQueueUrlRequest.class)))
                .thenReturn(GetQueueUrlResponse.builder().queueUrl(queueUrl).build());

        Message message = Message.builder().body(messageBody).receiptHandle("receipt-handle").build();
        when(sqsClient.receiveMessage(any(ReceiveMessageRequest.class)))
                .thenReturn(ReceiveMessageResponse.builder().messages(List.of(message)).build());

        when(objectMapper.readTree(messageBody))
                .thenReturn(new ObjectMapper().readTree(messageBody));

        sqsConsumer.receiveMessages();

        verify(processamentoUseCase, times(1)).processarVideo("video.mp4");
        verify(sqsClient, times(1)).deleteMessage(any(DeleteMessageRequest.class));
    }

    @Test
    void deveLidarComErroNoProcessamento() throws Exception {
        String queueUrl = "https://sqs.us-east-1.amazonaws.com/123456789012/my-queue";
        String messageBody = "{\"Records\":[{\"s3\":{\"object\":{\"key\":\"video.mp4\"}}}]}";

        when(sqsClient.getQueueUrl(any(GetQueueUrlRequest.class)))
                .thenReturn(GetQueueUrlResponse.builder().queueUrl(queueUrl).build());

        Message message = Message.builder().body(messageBody).receiptHandle("receipt-handle").build();
        when(sqsClient.receiveMessage(any(ReceiveMessageRequest.class)))
                .thenReturn(ReceiveMessageResponse.builder().messages(List.of(message)).build());

        when(objectMapper.readTree(messageBody))
                .thenReturn(new ObjectMapper().readTree(messageBody));

        doThrow(new RuntimeException("Erro no processamento")).when(processamentoUseCase).processarVideo("video.mp4");

        sqsConsumer.receiveMessages();

        verify(sqsClient, never()).deleteMessage(any(DeleteMessageRequest.class));
    }

    @Test
    void deveNaoProcessarQuandoNaoHouverMensagens() throws IOException {
        String queueUrl = "https://sqs.us-east-1.amazonaws.com/123456789012/my-queue";

        when(sqsClient.getQueueUrl(any(GetQueueUrlRequest.class)))
                .thenReturn(GetQueueUrlResponse.builder().queueUrl(queueUrl).build());

        when(sqsClient.receiveMessage(any(ReceiveMessageRequest.class)))
                .thenReturn(ReceiveMessageResponse.builder().messages(List.of()).build());

        sqsConsumer.receiveMessages();

        verify(processamentoUseCase, never()).processarVideo(any());
        verify(sqsClient, never()).deleteMessage(any(DeleteMessageRequest.class));
    }
}