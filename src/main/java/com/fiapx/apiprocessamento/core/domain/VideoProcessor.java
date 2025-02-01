package com.fiapx.apiprocessamento.core.domain;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class VideoProcessor {

    public byte[] processarVideo(byte[] bytes) throws IOException {

        InputStream inputStream = new ByteArrayInputStream(bytes);
        Java2DFrameConverter converter = new Java2DFrameConverter();
        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(inputStream);

        grabber.start();

        var frameRate = grabber.getFrameRate();
        var frameInterval = (int) (10 * frameRate);

        ByteArrayOutputStream zipOutputStream = new ByteArrayOutputStream();

        try (ZipOutputStream zos = new ZipOutputStream(zipOutputStream)) {
            int currentFrame = (int) frameRate;
            int imageIndex = 1;

            while (currentFrame < grabber.getLengthInFrames()) {
                grabber.setFrameNumber(currentFrame);
                Frame frame = grabber.grabImage();

                BufferedImage grabbedImage = converter.convert(frame);

                ByteArrayOutputStream imageOutputStream = new ByteArrayOutputStream();
                ImageIO.write(grabbedImage, "jpg", imageOutputStream);
                imageOutputStream.flush();
                byte[] imageInByte = imageOutputStream.toByteArray();
                imageOutputStream.close();

                String imageName = "imagem_" + String.format("%03d", imageIndex) + ".jpg";
                ZipEntry zipEntry = new ZipEntry(imageName);
                zos.putNextEntry(zipEntry);
                zos.write(imageInByte);
                zos.closeEntry();

                imageIndex++;
                currentFrame += frameInterval;
            }
        }

        grabber.stop();

        return zipOutputStream.toByteArray();
    }
}