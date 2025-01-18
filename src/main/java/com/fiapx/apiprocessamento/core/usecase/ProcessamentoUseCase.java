package com.fiapx.apiprocessamento.core.usecase;

import com.fiapx.apiprocessamento.core.domain.VideoProcessor;
import com.fiapx.apiprocessamento.core.util.ValidadorUtil;
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

        // TO-DO ATUALIZAR APICONSULTA - PROCESSANDO

        var video = s3Service.buscarVideo(chaveVideo);
        ValidadorUtil.validarVideo(video.length);

        // TO-DO PROCESSAMENTO

        s3Service.salvarImagens(chaveVideo, video);

        // TO-DO ATUALIZAR APICONSULTA - PROCESSADO + URL
    }

    private List<byte[]> dividirVideoEmPartes(byte[] video) {
        return List.of(video);
    }
}