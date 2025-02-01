package com.fiapx.apiprocessamento.adapter.in.controller;

import com.fiapx.apiprocessamento.core.model.ProcessamentoDTO;
import com.fiapx.apiprocessamento.core.usecase.ProcessamentoUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}