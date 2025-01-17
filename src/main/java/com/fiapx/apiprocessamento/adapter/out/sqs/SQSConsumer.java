package com.fiapx.apiprocessamento.adapter.out.sqs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiapx.apiprocessamento.core.usecase.ProcessamentoUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.*;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SQSConsumer {

    @Value("${cloud.aws.sqs.queue-name}")
    private String queueName;

    private final SqsClient sqsClient;
    private final ObjectMapper objectMapper;
    private final ProcessamentoUseCase processamentoUseCase;

    public void receiveMessages() {
        try {
            var queueUrl = getQueueUrl();
            var messages = getMessages(queueUrl);

            for (Message message : messages) {
                log.info("Mensagem recebida: {}", message.body());
                processarMessage(queueUrl, message);
            }
        } catch (SdkException e) {
            log.error("Erro ao processar mensagens do SQS: " + e.getMessage());
        }
    }

    private String getQueueUrl() {
        var getQueueUrlRequest = GetQueueUrlRequest.builder().queueName(queueName).build();
        var getQueueUrlResponse = sqsClient.getQueueUrl(getQueueUrlRequest);
        return getQueueUrlResponse.queueUrl();
    }

    private List<Message> getMessages(String queueUrl) {
        var receiveMessageRequest = ReceiveMessageRequest.builder().queueUrl(queueUrl).maxNumberOfMessages(10).build();
        var receiveMessageResponse = sqsClient.receiveMessage(receiveMessageRequest);
        return receiveMessageResponse.messages();
    }

    private void processarMessage(String queueUrl, Message message) {
        try {

            var jsonNode = objectMapper.readTree(message.body());
            var chave = jsonNode.get("Records").get(0).get("s3").get("object").get("key").asText();

            log.info("Iniciando o processamento do vídeo: {}", chave);

            processamentoUseCase.processarVideo(chave);

            var deleteMessageRequest = DeleteMessageRequest.builder().queueUrl(queueUrl).receiptHandle(message.receiptHandle()).build();
            sqsClient.deleteMessage(deleteMessageRequest);

            log.info("Mensagem processada e deletada com sucesso.");
        } catch (Exception e) {
            log.error("Erro ao processar a mensagem: " + e.getMessage());
        }
    }
}