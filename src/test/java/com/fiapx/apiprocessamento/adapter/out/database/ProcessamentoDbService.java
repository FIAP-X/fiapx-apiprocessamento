package com.fiapx.apiprocessamento.adapter.out.database;

import com.fiapx.apiprocessamento.adapter.out.database.entities.ProcessamentoEntity;
import com.fiapx.apiprocessamento.adapter.out.database.repository.ProcessamentoRepository;
import com.fiapx.apiprocessamento.core.enums.StatusProcessamentoEnum;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProcessamentoDbServiceTest {

    @Mock
    private ProcessamentoRepository processamentoRepository;

    @InjectMocks
    private ProcessamentoDbService processamentoDbService;

    @Test
    void deveSalvarStatusNoRepositorio() {
        String chaveVideo = "video123";
        String userId = "user123";

        ProcessamentoEntity processamentoEntity = ProcessamentoEntity.builder()
                .chaveVideo(chaveVideo)
                .userId(userId)
                .statusProcessamentoEnum(StatusProcessamentoEnum.EM_PROCESSAMENTO)
                .build();

        when(processamentoRepository.save(any(ProcessamentoEntity.class))).thenReturn(processamentoEntity);

        processamentoDbService.salvarStatusProcessamento(chaveVideo, userId);

        verify(processamentoRepository, times(1)).save(any(ProcessamentoEntity.class));
    }

    @Test
    void deveRetornarListaProcessamento() {
        String userId = "user123";
        ProcessamentoEntity entity = new ProcessamentoEntity("video123", "zip123", "user123", StatusProcessamentoEnum.EM_PROCESSAMENTO);
        List<ProcessamentoEntity> expectedList = List.of(entity);

        when(processamentoRepository.findByUserId(userId)).thenReturn(expectedList);

        List<ProcessamentoEntity> result = processamentoDbService.obterStatusProcessamento(userId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("video123", result.get(0).getChaveVideo());
    }
    @Test
    void deveAtualizarStatusNoRepositorio() {
        String chaveVideo = "video123";
        String chaveZip = "zip123";
        StatusProcessamentoEnum status = StatusProcessamentoEnum.PROCESSADO;

        ProcessamentoEntity processamentoEntity = ProcessamentoEntity.builder()
                .chaveVideo(chaveVideo)
                .userId("user123")
                .statusProcessamentoEnum(StatusProcessamentoEnum.EM_PROCESSAMENTO)
                .build();

        when(processamentoRepository.findById(chaveVideo)).thenReturn(Optional.of(processamentoEntity));
        when(processamentoRepository.save(any(ProcessamentoEntity.class))).thenReturn(processamentoEntity);

        processamentoDbService.atualizarStatusProcessamento(chaveVideo, chaveZip, status);

        verify(processamentoRepository, times(1)).findById(chaveVideo);
        verify(processamentoRepository, times(1)).save(any(ProcessamentoEntity.class));

        assertEquals(status, processamentoEntity.getStatusProcessamentoEnum());
        assertEquals(chaveZip, processamentoEntity.getChaveZip());
    }

    @Test
    void naoDeveAtualizarCasoNaoEncontreProcessamento() {
        String chaveVideo = "video123";
        String chaveZip = "zip123";
        StatusProcessamentoEnum statusProcessamentoEnum = StatusProcessamentoEnum.PROCESSADO;

        when(processamentoRepository.findById(chaveVideo)).thenReturn(Optional.empty());

        processamentoDbService.atualizarStatusProcessamento(chaveVideo, chaveZip, statusProcessamentoEnum);

        verify(processamentoRepository, never()).save(any(ProcessamentoEntity.class));
    }
}