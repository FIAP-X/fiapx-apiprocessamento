package com.fiapx.apiprocessamento.adapter.out.bucket;

import com.fiapx.apiprocessamento.port.out.S3ServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class S3Service implements S3ServicePort {

    @Value("${cloud.aws.bucket.videos}")
    private String bucketVideos;

    @Value("${cloud.aws.bucket.zips}")
    private String bucketZips;

    private final S3Client s3Client;

    public byte[] buscarVideo(String chave) throws IOException {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder().bucket(bucketVideos).key(chave).build();
        return s3Client.getObject(getObjectRequest, ResponseTransformer.toBytes()).asByteArray();
    }

    public void salvarVideo(String chave, byte[] videoZipado) {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder().bucket(bucketZips).key(chave).build();
        s3Client.putObject(putObjectRequest, software.amazon.awssdk.core.sync.RequestBody.fromBytes(videoZipado));
    }
}
