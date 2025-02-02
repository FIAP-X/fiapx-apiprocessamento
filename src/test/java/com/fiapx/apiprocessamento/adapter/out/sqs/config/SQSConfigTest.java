package com.fiapx.apiprocessamento.adapter.out.sqs.config;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import software.amazon.awssdk.services.sqs.SqsClient;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class SQSConfigTest {

    @Test
    void testSqsClientBean() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        ConfigurableEnvironment environment = context.getEnvironment();

        environment.getSystemProperties().put("cloud.aws.region", "us-east-1");
        environment.setActiveProfiles("default");
        context.register(SQSConfig.class);
        context.refresh();

        SqsClient sqsClient = context.getBean(SqsClient.class);
        assertNotNull(sqsClient);

        context.close();
    }
}
