package com.fiapx.apiprocessamento.core.usecase;

import com.fiapx.apiprocessamento.adapter.out.database.entities.ProcessamentoEntity;
import com.fiapx.apiprocessamento.core.domain.VideoProcessor;
import com.fiapx.apiprocessamento.core.enums.StatusProcessamentoEnum;
import com.fiapx.apiprocessamento.core.mapper.ProcessamentoMapper;
import com.fiapx.apiprocessamento.core.model.ProcessamentoDTO;
import com.fiapx.apiprocessamento.port.out.ProcessamentoDbServicePort;
import com.fiapx.apiprocessamento.port.out.S3ServicePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class ProcessamentoUseCaseTest {

    @Mock
    private VideoProcessor videoProcessor;

    @Mock
    private S3ServicePort s3Service;

    @Mock
    private ProcessamentoDbServicePort processamentoDbService;

    @Mock
    private ProcessamentoMapper processamentoMapper;

    @InjectMocks
    private ProcessamentoUseCase processamentoUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testProcessarVideo() throws IOException {
        String chaveVideo = "videos/user123/video.mp4";
        String userId = "user123";
        byte[] videoData = new byte[]{1, 2, 3};
        byte[] processedData = new byte[]{4, 5, 6};

        when(s3Service.buscarVideo(chaveVideo)).thenReturn(videoData);
        when(videoProcessor.processarVideo(videoData)).thenReturn(processedData);

        processamentoUseCase.processarVideo(chaveVideo);

        verify(processamentoDbService).salvarStatusProcessamento(eq(chaveVideo), eq(userId));
        verify(s3Service).buscarVideo(eq(chaveVideo));

        ArgumentCaptor<String> chaveZipCaptor = ArgumentCaptor.forClass(String.class);
        verify(s3Service).salvarImagens(chaveZipCaptor.capture(), eq(processedData));

        String chaveZip = chaveZipCaptor.getValue();

        assertTrue(chaveZip.startsWith("zips/" + userId + "/"));
        assertTrue(chaveZip.endsWith(".zip"));

        verify(processamentoDbService).atualizarStatusProcessamento(eq(chaveVideo), eq(chaveZip), eq(StatusProcessamentoEnum.PROCESSADO));
    }

    @Test
    void testConsultarStatusProcessamento() {
        String userId = "user123";
        var entityList = new ArrayList<ProcessamentoEntity>();
        var dtoList = List.of(new ProcessamentoDTO());

        when(processamentoDbService.obterStatusProcessamento(userId)).thenReturn(entityList);
        when(processamentoMapper.toDTOList(entityList)).thenReturn(dtoList);

        List<ProcessamentoDTO> result = processamentoUseCase.consultarStatusProcessamento(userId);

        assertEquals(dtoList, result);
        verify(processamentoDbService).obterStatusProcessamento(userId);
        verify(processamentoMapper).toDTOList(entityList);
    }

    @Test
    void testGerarUrlDownload() {
        String chaveZip = "zips/user123/abc.zip";
        String expectedUrl = "https://s3.amazonaws.com/zips/user123/abc.zip";

        when(s3Service.gerarUrlPreAssinada(chaveZip)).thenReturn(expectedUrl);

        String result = processamentoUseCase.gerarUrlDownload(chaveZip);

        assertEquals(expectedUrl, result);
        verify(s3Service).gerarUrlPreAssinada(chaveZip);
    }
}