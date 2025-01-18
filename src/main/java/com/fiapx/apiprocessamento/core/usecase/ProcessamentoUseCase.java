package com.fiapx.apiprocessamento.core.usecase;

import com.fiapx.apiprocessamento.core.domain.VideoProcessor;
import com.fiapx.apiprocessamento.port.out.S3ServicePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProcessamentoUseCase {

    private final VideoProcessor videoProcessor;
    private final S3ServicePort s3Service;

    public void processarVideo(String chaveVideo) throws IOException {
        var video = s3Service.buscarVideo(chaveVideo);

        long tamanhoMaximo = 1024 * 1024 * 100;

        if (video.length > tamanhoMaximo) {
            throw new IOException("O arquivo excede o tamanho máximo permitido de 100MB.");
        }

        // TO-DO PROCESSAMENTO

        s3Service.salvarImagens(chaveVideo, video);
    }

    private List<byte[]> dividirVideoEmPartes(byte[] video) {
        return List.of(video);
    }
}