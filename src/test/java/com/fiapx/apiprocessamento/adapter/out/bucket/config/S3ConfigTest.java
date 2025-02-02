package com.fiapx.apiprocessamento.adapter.out.bucket.config;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class S3ConfigTest {

    @Test
    void testS3ClientBean() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        ConfigurableEnvironment environment = context.getEnvironment();

        environment.getSystemProperties().put("cloud.aws.region", "us-east-1");
        environment.setActiveProfiles("default");
        context.register(S3Config.class);
        context.refresh();

        S3Client s3Client = context.getBean(S3Client.class);
        assertNotNull(s3Client);

        context.close();
    }

    @Test
    void testS3PresignerBean() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        ConfigurableEnvironment environment = context.getEnvironment();

        environment.getSystemProperties().put("cloud.aws.region", "us-east-1");
        environment.setActiveProfiles("default");
        context.register(S3Config.class);
        context.refresh();

        S3Presigner s3Presigner = context.getBean(S3Presigner.class);
        assertNotNull(s3Presigner);
    }
}