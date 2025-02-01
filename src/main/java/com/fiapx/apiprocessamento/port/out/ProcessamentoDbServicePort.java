package com.fiapx.apiprocessamento.port.out;

import com.fiapx.apiprocessamento.adapter.out.database.entities.ProcessamentoEntity;
import com.fiapx.apiprocessamento.core.enums.StatusProcessamentoEnum;

import java.util.List;

public interface ProcessamentoDbServicePort {

    void salvarStatusProcessamento(String chaveVideo, String userId);

    List<ProcessamentoEntity> obterStatusProcessamento(String userId);

    void atualizarStatusProcessamento(String chave, String chaveZip, StatusProcessamentoEnum statusProcessamentoEnum);
}
