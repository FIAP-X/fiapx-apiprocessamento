package com.fiapx.apiprocessamento.adapter.in.controller.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StatusProcessamentoDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1499072426353703609L;
    private String chaveVideo;
    private String status;
}
