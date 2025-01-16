package com.fiapx.apiprocessamento;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.services.sqs.model.*;

import java.util.List;

@Service
public class SQSConsumer {

    @Value("${cloud.aws.credentials.access-key-id}")
    private String awsAccessKeyId;

    @Value("${cloud.aws.credentials.secret-access-key}")
    private String awsSecretAccessKey;

    @Value("${cloud.aws.region.static}")
    private String awsRegion;

    @Value("${cloud.aws.sqs.queue-name}")
    private String queueName;

    public void receiveMessages() {

        AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(awsAccessKeyId, awsSecretAccessKey);

        try (var sqsClient = software.amazon.awssdk.services.sqs.SqsClient.builder()
                .region(software.amazon.awssdk.regions.Region.of(awsRegion))
                .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                .build()) {

            GetQueueUrlRequest getQueueUrlRequest = GetQueueUrlRequest.builder()
                    .queueName(queueName)
                    .build();

            GetQueueUrlResponse getQueueUrlResponse = sqsClient.getQueueUrl(getQueueUrlRequest);

            String queueUrl = getQueueUrlResponse.queueUrl();

            ReceiveMessageRequest receiveMessageRequest = ReceiveMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .maxNumberOfMessages(10)
                    .build();

            ReceiveMessageResponse receiveMessageResponse = sqsClient.receiveMessage(receiveMessageRequest);

            List<Message> messages = receiveMessageResponse.messages();

            for (Message message : messages) {
                System.out.println("Mensagem recebida: " + message.body());

                DeleteMessageRequest deleteMessageRequest = DeleteMessageRequest.builder()
                        .queueUrl(queueUrl)
                        .receiptHandle(message.receiptHandle())
                        .build();

                sqsClient.deleteMessage(deleteMessageRequest);
            }
        } catch (SdkException e) {
            System.err.println("Erro ao processar mensagens do SQS: " + e.getMessage() + awsAccessKeyId + awsSecretAccessKey);
        }
    }
}