package com.fiapx.apiprocessamento.adapter.out.sqs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiapx.apiprocessamento.core.usecase.ProcessamentoUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.*;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class SQSConsumer {

    @Value("${cloud.aws.sqs.queue-name}")
    private String queueName;

    @Value("${sqs.max.number.messages}")
    private int maxNumberOfMessages;

    private final SqsClient sqsClient;
    private final ObjectMapper objectMapper;
    private final ProcessamentoUseCase processamentoUseCase;

    public void receiveMessages() {
            var queueUrl = getQueueUrl();
            var messages = getMessages(queueUrl);

            for (Message message : messages) {
                log.info("Mensagem recebida: {}", message.body());
                processarMessage(queueUrl, message);
            }
    }

    private void processarMessage(String queueUrl, Message message) {
        try {
            var chave = objectMapper.readTree(message.body())
                    .get("Records").get(0).get("s3").get("object").get("key")
                    .asText();

            log.info("Iniciando o processamento do vídeo: {}", chave);

            processamentoUseCase.processarVideo(chave);
            deleteMessage(queueUrl, message);

            log.info("Mensagem processada com sucesso: {}", chave);
        } catch (Exception e) {
            log.error("Erro ao processar mensagem: {}", message.body(), e);
        }
    }

    private String getQueueUrl() {
        var getQueueUrlRequest = GetQueueUrlRequest.builder()
                .queueName(queueName)
                .build();

        var getQueueUrlResponse = sqsClient.getQueueUrl(getQueueUrlRequest);
        return getQueueUrlResponse.queueUrl();
    }

    private List<Message> getMessages(String queueUrl) {
        var receiveMessageRequest = ReceiveMessageRequest.builder()
                .queueUrl(queueUrl)
                .maxNumberOfMessages(maxNumberOfMessages)
                .build();

        var receiveMessageResponse = sqsClient.receiveMessage(receiveMessageRequest);
        return receiveMessageResponse.messages();
    }

    private void deleteMessage(String queueUrl, Message message) {
        var deleteMessageRequest = DeleteMessageRequest.builder()
                .queueUrl(queueUrl)
                .receiptHandle(message.receiptHandle())
                .build();

        sqsClient.deleteMessage(deleteMessageRequest);
    }
}