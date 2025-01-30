package com.fiapx.apiprocessamento.adapter.in.controller;

import com.fiapx.apiprocessamento.adapter.in.controller.dto.StatusProcessamentoDto;
import com.fiapx.apiprocessamento.core.usecase.ProcessamentoUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/processamento")
@RequiredArgsConstructor
public class HttpControllerAdapter {
    private final ProcessamentoUseCase processamentoUseCase;

    @GetMapping("/{chaveVideo}")
    public ResponseEntity<StatusProcessamentoDto> obterStatusProcessamento(@PathVariable String chaveVideo) {
        log.info(String.format("Obtendo status processamento %s", chaveVideo));

        var statusProcessamento = processamentoUseCase.consultarStatusProcessamento(chaveVideo);

        if (statusProcessamento.isEmpty()) {
            log.info((String.format("Não foi encontrado o vídeo com a chave %s", chaveVideo)));
            return ResponseEntity.notFound().build();
        }

        final var statusResponse = StatusProcessamentoDto.builder()
                .status(statusProcessamento.get().name())
                .chaveVideo(chaveVideo)
                .build();

        log.info(String.format("Fim da obtenção do status de processamento %s", statusResponse));
        return ResponseEntity.ok().body(statusResponse);
    }
}
