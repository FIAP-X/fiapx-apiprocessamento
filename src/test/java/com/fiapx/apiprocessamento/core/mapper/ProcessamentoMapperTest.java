package com.fiapx.apiprocessamento.core.mapper;

import com.fiapx.apiprocessamento.core.enums.StatusProcessamentoEnum;
import com.fiapx.apiprocessamento.core.model.ProcessamentoDTO;
import com.fiapx.apiprocessamento.adapter.out.database.entities.ProcessamentoEntity;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class ProcessamentoMapperTest {

    private final ProcessamentoMapper mapper = Mappers.getMapper(ProcessamentoMapper.class);

    @Test
    void testToDTO() {
        ProcessamentoEntity entity = new ProcessamentoEntity();
        entity.setChaveVideo("video123");
        entity.setChaveZip("zip123");
        entity.setUserId("user123");
        entity.setStatusProcessamentoEnum(StatusProcessamentoEnum.PROCESSADO);

        ProcessamentoDTO dto = mapper.toDTO(entity);

        assertNotNull(dto);
        assertEquals("video123", dto.getChaveVideo());
        assertEquals("zip123", dto.getChaveZip());
        assertEquals("user123", dto.getUserId());
        assertEquals("PROCESSADO", dto.getStatusProcessamentoEnum());
    }

    @Test
    void testToDTOList() {
        ProcessamentoEntity entity1 = new ProcessamentoEntity();
        entity1.setChaveVideo("video1");
        entity1.setChaveZip("zip1");
        entity1.setUserId("user1");
        entity1.setStatusProcessamentoEnum(StatusProcessamentoEnum.ERRO_NO_PROCESSAMENTO);

        ProcessamentoEntity entity2 = new ProcessamentoEntity();
        entity2.setChaveVideo("video2");
        entity2.setChaveZip("zip2");
        entity2.setUserId("user2");
        entity2.setStatusProcessamentoEnum(StatusProcessamentoEnum.EM_PROCESSAMENTO);

        List<ProcessamentoDTO> dtoList = mapper.toDTOList(List.of(entity1, entity2));

        assertNotNull(dtoList);
        assertEquals(2, dtoList.size());

        ProcessamentoDTO dto1 = dtoList.get(0);
        assertEquals("video1", dto1.getChaveVideo());
        assertEquals("zip1", dto1.getChaveZip());
        assertEquals("user1", dto1.getUserId());
        assertEquals("ERRO_NO_PROCESSAMENTO", dto1.getStatusProcessamentoEnum());

        ProcessamentoDTO dto2 = dtoList.get(1);
        assertEquals("video2", dto2.getChaveVideo());
        assertEquals("zip2", dto2.getChaveZip());
        assertEquals("user2", dto2.getUserId());
        assertEquals("EM_PROCESSAMENTO", dto2.getStatusProcessamentoEnum());
    }
}