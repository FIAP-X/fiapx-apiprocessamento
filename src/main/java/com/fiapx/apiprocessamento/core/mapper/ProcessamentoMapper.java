package com.fiapx.apiprocessamento.core.mapper;

import com.fiapx.apiprocessamento.core.model.ProcessamentoDTO;
import com.fiapx.apiprocessamento.adapter.out.database.entities.ProcessamentoEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProcessamentoMapper {

    List<ProcessamentoDTO> toDTOList(List<ProcessamentoEntity> entities);

    ProcessamentoDTO toDTO(ProcessamentoEntity entity);
}