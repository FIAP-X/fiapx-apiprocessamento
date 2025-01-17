package com.fiapx.apiprocessamento.adapter.out.bucket;

import com.fiapx.apiprocessamento.port.out.S3ServicePort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;

@Slf4j
@Component
public class S3Service implements S3ServicePort {

    @Value("${cloud.aws.bucket.videos}")
    private String bucketVideos;

    @Value("${cloud.aws.bucket.zips}")
    private String bucketZips;

    public byte[] buscarVideo(String chave) throws IOException {
        S3Client s3Client = S3Client.builder()
                .region(Region.US_EAST_1)
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();

        log.info("Conexao estabelecida");

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketVideos)
                .key(chave)
                .build();

        log.info("Objeto capturado");

        return s3Client.getObject(getObjectRequest, ResponseTransformer.toBytes()).asByteArray();
    }

    public void salvarVideo(String chave, byte[] videoZipado) {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder().bucket(bucketZips).key(chave).build();
        S3Client.builder()
                .region(Region.US_EAST_1)
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build().putObject(putObjectRequest, software.amazon.awssdk.core.sync.RequestBody.fromBytes(videoZipado));
    }
}
