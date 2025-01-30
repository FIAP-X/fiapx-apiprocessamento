package com.fiapx.apiprocessamento.adapter.out.database.repository;

import com.fiapx.apiprocessamento.adapter.out.database.entities.ProcessamentoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcessamentoRepository extends JpaRepository<ProcessamentoEntity, String> {
}
