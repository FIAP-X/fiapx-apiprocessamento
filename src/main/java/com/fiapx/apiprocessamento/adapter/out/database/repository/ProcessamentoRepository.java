package com.fiapx.apiprocessamento.adapter.out.database.repository;

import com.fiapx.apiprocessamento.adapter.out.database.entities.ProcessamentoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProcessamentoRepository extends JpaRepository<ProcessamentoEntity, String> {

    List<ProcessamentoEntity> findByUserId(String userId);
}
