package com.fiapx.apiprocessamento.core.usecase;

import com.fiapx.apiprocessamento.core.domain.VideoProcessor;
import com.fiapx.apiprocessamento.port.out.S3ServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProcessamentoUseCase {

    private final VideoProcessor videoProcessor;
    private final S3ServicePort s3Service;

    public void processarVideo(String chaveVideo) {
        try {
            var video = s3Service.buscarVideo(chaveVideo);

            var videoSegregado = dividirVideoEmPartes(video);

            byte[] zipVideo = videoProcessor.processarVideo(videoSegregado);

            s3Service.salvarVideo("TESTE", zipVideo);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<byte[]> dividirVideoEmPartes(byte[] video) {
        return List.of(video);
    }
}