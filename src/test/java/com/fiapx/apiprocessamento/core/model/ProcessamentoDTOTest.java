package com.fiapx.apiprocessamento.core.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProcessamentoDTOTest {

    @Test
    void testGettersAndSetters() {
        ProcessamentoDTO dto = new ProcessamentoDTO();
        dto.setChaveVideo("video123");
        dto.setChaveZip("zip123");
        dto.setUserId("user123");
        dto.setStatusProcessamentoEnum("PROCESSADO");

        assertEquals("video123", dto.getChaveVideo());
        assertEquals("zip123", dto.getChaveZip());
        assertEquals("user123", dto.getUserId());
        assertEquals("PROCESSADO", dto.getStatusProcessamentoEnum());
    }
}