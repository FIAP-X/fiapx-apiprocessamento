package com.fiapx.apiprocessamento.adapter.out.bucket;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.IOException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class S3ServiceTest {

    @Mock
    private S3Client s3Client;

    @Mock
    private S3Presigner s3Presigner;

    @InjectMocks
    private S3Service s3Service;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(s3Service, "bucketVideos", "test-bucket-videos");
        ReflectionTestUtils.setField(s3Service, "bucketZips", "test-bucket-zips");
    }

    @Test
    void deveRetornarBytesDoVideo() throws IOException {
        String chave = "video.mp4";
        byte[] conteudoEsperado = "conteudo do video".getBytes();

        ResponseBytes responseBytesMock = mock(ResponseBytes.class);
        when(responseBytesMock.asByteArray()).thenReturn(conteudoEsperado);

        when(s3Client.getObject(any(GetObjectRequest.class), any(ResponseTransformer.class)))
                .thenReturn(responseBytesMock);

        byte[] resultado = s3Service.buscarVideo(chave);

        assertArrayEquals(conteudoEsperado, resultado);
        verify(s3Client, times(1)).getObject(any(GetObjectRequest.class), any(ResponseTransformer.class));
    }

    @Test
    void deveSalvarArquivoNoS3() {
        String chave = "imagens.zip";
        byte[] conteudo = "conteudo zip".getBytes();

        when(s3Client.putObject(any(PutObjectRequest.class), any(RequestBody.class))).thenReturn(null);

        assertDoesNotThrow(() -> s3Service.salvarImagens(chave, conteudo));

        verify(s3Client, times(1)).putObject(any(PutObjectRequest.class), any(RequestBody.class));
    }

    @Test
    void deveRetornarUrl() {
        String chave = "arquivo.zip";
        URL urlMock = mock(URL.class);
        when(urlMock.toString()).thenReturn("https://s3.amazonaws.com/test-bucket/arquivo.zip");

        PresignedGetObjectRequest presignedRequestMock = mock(PresignedGetObjectRequest.class);
        when(presignedRequestMock.url()).thenReturn(urlMock);

        when(s3Presigner.presignGetObject(any(GetObjectPresignRequest.class)))
                .thenReturn(presignedRequestMock);

        String urlGerada = s3Service.gerarUrlPreAssinada(chave);

        assertEquals("https://s3.amazonaws.com/test-bucket/arquivo.zip", urlGerada);
        verify(s3Presigner, times(1)).presignGetObject(any(GetObjectPresignRequest.class));
    }
}