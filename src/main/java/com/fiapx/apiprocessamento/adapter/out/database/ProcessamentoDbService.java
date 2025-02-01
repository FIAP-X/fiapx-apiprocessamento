package com.fiapx.apiprocessamento.adapter.out.database;

import com.fiapx.apiprocessamento.adapter.out.database.entities.ProcessamentoEntity;
import com.fiapx.apiprocessamento.adapter.out.database.repository.ProcessamentoRepository;
import com.fiapx.apiprocessamento.core.enums.StatusProcessamentoEnum;
import com.fiapx.apiprocessamento.port.out.ProcessamentoDbServicePort;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.fiapx.apiprocessamento.core.util.ConstantesUtil.FIM;
import static com.fiapx.apiprocessamento.core.util.ConstantesUtil.INICIO;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProcessamentoDbService implements ProcessamentoDbServicePort {
    private static final String SERVICE_NAME = "ProcessamentoDbService";
    private static final String SALVAR_STATUS_PROCESSAMENTO_METHOD_NAME = "salvarStatusProcessamento";
    private static final String OBTER_STATUS_PROCESSAMENTO_METHOD_NAME = "obterStatusProcessamento";
    private static final String ATUALIZAR_STATUS_PROCESSAMENTO_METHOD_NAME = "atualizarStatusProcessamento";
    private static final String STRING_LOG_FORMAT = "%s_%s_%s {}";
    private final ProcessamentoRepository processamentoRepository;

    @Override
    public void salvarStatusProcessamento(String chaveVideo, String userId) {
        log.info(String.format(STRING_LOG_FORMAT, SERVICE_NAME, SALVAR_STATUS_PROCESSAMENTO_METHOD_NAME, INICIO), chaveVideo);

        final var processamento = ProcessamentoEntity.builder()
                .chaveVideo(chaveVideo)
                .userId(userId)
                .statusProcessamentoEnum(StatusProcessamentoEnum.EM_PROCESSAMENTO)
                .build();

        processamentoRepository.save(processamento);

        log.info(String.format(STRING_LOG_FORMAT, SERVICE_NAME, SALVAR_STATUS_PROCESSAMENTO_METHOD_NAME, FIM), chaveVideo);
    }

    @Override
    @SneakyThrows
    public List<ProcessamentoEntity> obterStatusProcessamento(String userId) {
        log.info(String.format(STRING_LOG_FORMAT, SERVICE_NAME, OBTER_STATUS_PROCESSAMENTO_METHOD_NAME, INICIO), userId);

        final var processamentos = processamentoRepository.findByUserId(userId);

        log.info(String.format(STRING_LOG_FORMAT, SERVICE_NAME, OBTER_STATUS_PROCESSAMENTO_METHOD_NAME, FIM), processamentos);

        return processamentos;
    }

    @Override
    @SneakyThrows
    public void atualizarStatusProcessamento(String chaveVideo, String chaveZip, StatusProcessamentoEnum statusProcessamentoEnum) {
        log.info(String.format(STRING_LOG_FORMAT, SERVICE_NAME, ATUALIZAR_STATUS_PROCESSAMENTO_METHOD_NAME, INICIO), chaveVideo);

        var processamento = processamentoRepository.findById(chaveVideo);

        if (processamento.isEmpty()) {
            return;
        }

        processamento.get().setStatusProcessamentoEnum(statusProcessamentoEnum);
        processamento.get().setChaveZip(chaveZip);

        processamentoRepository.save(processamento.get());

        log.info(String.format(STRING_LOG_FORMAT, SERVICE_NAME, ATUALIZAR_STATUS_PROCESSAMENTO_METHOD_NAME, FIM), processamento);
    }
}