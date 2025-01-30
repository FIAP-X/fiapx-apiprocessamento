package com.fiapx.apiprocessamento.adapter.out.database.entities;

import com.fiapx.apiprocessamento.core.enums.StatusProcessamentoEnum;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@Builder
@Entity(name = "tb_processamento")
@AllArgsConstructor
@NoArgsConstructor
public class ProcessamentoEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 4181172446495001465L;
    @Id
    private String chave;
    @Enumerated(EnumType.STRING)
    private StatusProcessamentoEnum statusProcessamentoEnum;
}
