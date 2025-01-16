package com.fiapx.apiprocessamento;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.*;

import java.util.List;

@Slf4j
@Service
public class SQSConsumer {

    @Value("${cloud.aws.sqs.queue-name}")
    private String queueName;

    public void receiveMessages() {

        try (SqsClient sqsClient = SqsClient.builder()
                .region(Region.US_EAST_1)
                .credentialsProvider(DefaultCredentialsProvider.create()).build()) {

            GetQueueUrlRequest getQueueUrlRequest = GetQueueUrlRequest.builder().queueName(queueName).build();

            GetQueueUrlResponse getQueueUrlResponse = sqsClient.getQueueUrl(getQueueUrlRequest);

            String queueUrl = getQueueUrlResponse.queueUrl();

            ReceiveMessageRequest receiveMessageRequest = ReceiveMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .maxNumberOfMessages(10).build();

            ReceiveMessageResponse receiveMessageResponse = sqsClient.receiveMessage(receiveMessageRequest);

            List<Message> messages = receiveMessageResponse.messages();

            for (Message message : messages) {
                log.info("Mensagem recebida: {}", message.body());

                DeleteMessageRequest deleteMessageRequest = DeleteMessageRequest.builder()
                        .queueUrl(queueUrl)
                        .receiptHandle(message.receiptHandle()).build();

                sqsClient.deleteMessage(deleteMessageRequest);
            }

        } catch (SdkException e) {
            log.info("Erro ao processar mensagens do SQS: " + e.getMessage());
        }
    }
}