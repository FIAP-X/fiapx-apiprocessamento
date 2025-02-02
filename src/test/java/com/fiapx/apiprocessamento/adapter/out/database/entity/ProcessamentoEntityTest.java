package com.fiapx.apiprocessamento.adapter.out.database.entity;

import com.fiapx.apiprocessamento.adapter.out.database.entities.ProcessamentoEntity;
import com.fiapx.apiprocessamento.core.enums.StatusProcessamentoEnum;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProcessamentoEntityTest {

    @Test
    void testBuilder() {
        ProcessamentoEntity entity = ProcessamentoEntity.builder()
                .chaveVideo("video123")
                .chaveZip("zip123")
                .userId("user123")
                .statusProcessamentoEnum(StatusProcessamentoEnum.ERRO_NO_PROCESSAMENTO)
                .build();

        assertNotNull(entity);
        assertEquals("video123", entity.getChaveVideo());
        assertEquals("zip123", entity.getChaveZip());
        assertEquals("user123", entity.getUserId());
        assertEquals(StatusProcessamentoEnum.ERRO_NO_PROCESSAMENTO, entity.getStatusProcessamentoEnum());
    }

    @Test
    void testNoArgsConstructor() {
        ProcessamentoEntity entity = new ProcessamentoEntity();

        assertNotNull(entity);
        assertNull(entity.getChaveVideo());
        assertNull(entity.getChaveZip());
        assertNull(entity.getUserId());
        assertNull(entity.getStatusProcessamentoEnum());
    }

    @Test
    void testAllArgsConstructor() {
        ProcessamentoEntity entity = new ProcessamentoEntity(
                "video123", "zip123", "user123", StatusProcessamentoEnum.EM_PROCESSAMENTO
        );

        assertNotNull(entity);
        assertEquals("video123", entity.getChaveVideo());
        assertEquals("zip123", entity.getChaveZip());
        assertEquals("user123", entity.getUserId());
        assertEquals(StatusProcessamentoEnum.EM_PROCESSAMENTO, entity.getStatusProcessamentoEnum());
    }

    @Test
    void testEqualsAndHashCode() {
        ProcessamentoEntity entity1 = new ProcessamentoEntity("video123", "zip123", "user123", StatusProcessamentoEnum.EM_PROCESSAMENTO);
        ProcessamentoEntity entity2 = new ProcessamentoEntity("video123", "zip123", "user123", StatusProcessamentoEnum.EM_PROCESSAMENTO);

        assertEquals(entity1, entity2);
        assertEquals(entity1.hashCode(), entity2.hashCode());
    }
}