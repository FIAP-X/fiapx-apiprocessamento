package com.fiapx.apiprocessamento.port.out;

import com.fiapx.apiprocessamento.adapter.out.database.entities.ProcessamentoEntity;
import com.fiapx.apiprocessamento.core.enums.StatusProcessamentoEnum;

import java.util.Optional;

public interface ProcessamentoDbServicePort {

    void salvarStatusProcessamento(String chave);

    Optional<StatusProcessamentoEnum> obterStatusProcessamento(String chave);

    Optional<ProcessamentoEntity> atualizarStatusProcessamento(String chave, StatusProcessamentoEnum statusProcessamentoEnum);
}
