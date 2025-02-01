package com.fiapx.apiprocessamento.core.model;

import lombok.Data;

@Data
public class ProcessamentoDTO {
    private String chaveVideo;
    private String chaveZip;
    private String userId;
    private String statusProcessamentoEnum;
}
