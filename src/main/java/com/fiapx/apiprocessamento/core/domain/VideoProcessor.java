package com.fiapx.apiprocessamento.core.domain;

import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class VideoProcessor {

    public byte[] processarVideo(List<byte[]> videoParts) throws IOException {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try (ZipOutputStream zipOut = new ZipOutputStream(byteArrayOutputStream)) {

            zipOut.putNextEntry(new ZipEntry("video"));

            for (byte[] part : videoParts) {
                zipOut.write(part);
            }

            zipOut.closeEntry();
        }

        return byteArrayOutputStream.toByteArray();
    }
}