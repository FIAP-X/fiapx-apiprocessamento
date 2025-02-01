package com.fiapx.apiprocessamento.core.usecase;

import com.fiapx.apiprocessamento.core.domain.VideoProcessor;
import com.fiapx.apiprocessamento.core.enums.StatusProcessamentoEnum;
import com.fiapx.apiprocessamento.core.util.ValidadorUtil;
import com.fiapx.apiprocessamento.port.out.ProcessamentoDbServicePort;
import com.fiapx.apiprocessamento.port.out.S3ServicePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProcessamentoUseCase {

    private final VideoProcessor videoProcessor;
    private final S3ServicePort s3Service;
    private final ProcessamentoDbServicePort processamentoDbService;

    public void processarVideo(String chaveVideo) throws IOException {

        var userId = chaveVideo.split("/")[1];

        // listagem por usuário userId
        processamentoDbService.salvarStatusProcessamento(chaveVideo);

        var video = s3Service.buscarVideo(chaveVideo);

        ValidadorUtil.validarVideo(video.length);

        var resultadoProcessamento = videoProcessor.processarVideo(video);

        var chaveZip = String.format("zips/%s/%s.zip", userId, UUID.randomUUID());

        s3Service.salvarImagens(chaveZip, resultadoProcessamento);

        // remover (apenas para verificar funcionamento da url)
        s3Service.gerarUrlPreAssinada(chaveZip);

        // listagem por usuário
        processamentoDbService.atualizarStatusProcessamento(chaveVideo, StatusProcessamentoEnum.PROCESSADO);
    }

    public Optional<StatusProcessamentoEnum> consultarStatusProcessamento(String chaveVideo) {
        return processamentoDbService.obterStatusProcessamento(chaveVideo);
    }
}