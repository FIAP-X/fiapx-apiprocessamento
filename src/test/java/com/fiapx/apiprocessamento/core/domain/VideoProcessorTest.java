package com.fiapx.apiprocessamento.core.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.io.InputStream;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class VideoProcessorTest {

    private byte[] videoBytes;
    private VideoProcessor videoProcessor;

    @BeforeEach
    void setUp() throws IOException {
        InputStream videoStream = getClass().getClassLoader()
                .getResourceAsStream("videos/video_teste.mp4");

        if (videoStream == null) {
            throw new IOException("Arquivo teste de vídeo não encontrado");
        }

        videoBytes = videoStream.readAllBytes();
        videoStream.close();

        videoProcessor = new VideoProcessor();
    }

    @Test
    void testProcessarVideo() throws IOException {
        byte[] result = videoProcessor.processarVideo(videoBytes);

        assertNotNull(result);
        assertTrue(result.length > 0);
    }

    @Test
    void testProcessarVideoInvalid() {
        byte[] invalidBytes = new byte[]{0, 1, 2, 3, 4, 5};
        try {
            videoProcessor.processarVideo(invalidBytes);
        } catch (IOException e) {
            assertTrue(e.getMessage().contains("Could not open input"));
        }
    }
}