package com.fiapx.apiprocessamento.adapter.out.bucket.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.net.URI;

@Profile("!local")
@Configuration
public class S3Config {

    @Value("${cloud.aws.region}")
    private String region;

    @Value("${cloud.aws.s3.endpoint:}")
    private String endpoint;

    @Bean
    public S3Client s3Client() {

        // Cria o DefaultCredentialsProvider
        AwsCredentialsProvider credentialsProvider = DefaultCredentialsProvider.create();

        // Resolve as credenciais
        AwsCredentials credentials = credentialsProvider.resolveCredentials();

        // Exibe as credenciais capturadas
        System.out.println("Access Key ID: " + credentials.accessKeyId());
        System.out.println("Secret Access Key: " + credentials.secretAccessKey());

        // Verifica se é do tipo SessionCredentials
        if (credentials instanceof software.amazon.awssdk.auth.credentials.AwsSessionCredentials) {
            String sessionToken = ((software.amazon.awssdk.auth.credentials.AwsSessionCredentials) credentials).sessionToken();
            System.out.println("Session Token: " + sessionToken);
        }

        return S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(credentialsProvider)
                .endpointOverride(URI.create(endpoint))
                .build();
    }
}