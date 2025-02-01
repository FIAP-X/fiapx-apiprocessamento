package com.fiapx.apiprocessamento.adapter.in.controller;

import com.fiapx.apiprocessamento.core.model.ProcessamentoDTO;
import com.fiapx.apiprocessamento.core.usecase.ProcessamentoUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/processamento")
@RequiredArgsConstructor
public class HttpControllerAdapter {
    private final ProcessamentoUseCase processamentoUseCase;

    @GetMapping("/{userId}")
    public ResponseEntity<List<ProcessamentoDTO>> obterStatusProcessamento(@PathVariable String userId) {
        log.info(String.format("Obtendo status dos processamentos do usuário %s", userId));

        var processamentos = processamentoUseCase.consultarStatusProcessamento(userId);

        if (processamentos.isEmpty()) {
            log.info((String.format("Não foi encontrado processamento para o usuário %s", userId)));
            return ResponseEntity.notFound().build();
        }

        log.info(String.format("Fim da obtenção dos processamentos do usuário"));
        return ResponseEntity.ok().body(processamentos);
    }

    @GetMapping("/download/{chaveZip}")
    public ResponseEntity<String> gerarUrlDownload(@RequestParam String chaveZip) {
        log.info("Gerando URL de download para o arquivo com chave: {}", chaveZip);

        try {
            String url = processamentoUseCase.gerarUrlDownload(chaveZip);
            return ResponseEntity.ok(url);
        } catch (Exception e) {
            log.error("Erro ao gerar URL de download para chave: {}", chaveZip, e);
            return ResponseEntity.status(500).body("Erro ao gerar URL de download");
        }
    }
}