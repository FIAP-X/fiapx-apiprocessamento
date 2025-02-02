package com.fiapx.apiprocessamento.adapter.in.controller;

import com.fiapx.apiprocessamento.core.model.ProcessamentoDTO;
import com.fiapx.apiprocessamento.core.usecase.ProcessamentoUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class HttpControllerAdapterTest {

    @Mock
    private ProcessamentoUseCase processamentoUseCase;

    @InjectMocks
    private HttpControllerAdapter httpControllerAdapter;

    private final String userId = "user123";
    private final String chaveZip = "chaveTest";
    private ProcessamentoDTO processamentoDTO;

    @BeforeEach
    void setUp() {
        processamentoDTO = new ProcessamentoDTO();
    }

    @Test
    void obterStatusProcessamentoSucesso() {
        when(processamentoUseCase.consultarStatusProcessamento(userId))
                .thenReturn(List.of(processamentoDTO));

        ResponseEntity<List<ProcessamentoDTO>> response = httpControllerAdapter.obterStatusProcessamento(userId);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(processamentoUseCase, times(1)).consultarStatusProcessamento(userId);
    }

    @Test
    void obterStatusProcessamentoNaoEncontrado() {
        when(processamentoUseCase.consultarStatusProcessamento(userId))
                .thenReturn(List.of());

        ResponseEntity<List<ProcessamentoDTO>> response = httpControllerAdapter.obterStatusProcessamento(userId);

        assertEquals(404, response.getStatusCodeValue());
        verify(processamentoUseCase, times(1)).consultarStatusProcessamento(userId);
    }

    @Test
    void gerarUrlDownloadSucesso() {
        String url = "http://example.com/download";
        when(processamentoUseCase.gerarUrlDownload(chaveZip)).thenReturn(url);

        ResponseEntity<String> response = httpControllerAdapter.gerarUrlDownload(chaveZip);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(url, response.getBody());
        verify(processamentoUseCase, times(1)).gerarUrlDownload(chaveZip);
    }

    @Test
    void gerarUrlDownloadErro() {
        when(processamentoUseCase.gerarUrlDownload(chaveZip)).thenThrow(new RuntimeException("Erro"));

        ResponseEntity<String> response = httpControllerAdapter.gerarUrlDownload(chaveZip);

        assertEquals(500, response.getStatusCodeValue());
        assertEquals("Erro ao gerar URL de download", response.getBody());
        verify(processamentoUseCase, times(1)).gerarUrlDownload(chaveZip);
    }
}
