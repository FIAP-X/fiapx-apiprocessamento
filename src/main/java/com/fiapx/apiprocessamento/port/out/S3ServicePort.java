package com.fiapx.apiprocessamento.port.out;

import java.io.IOException;

public interface S3ServicePort {
    byte[] buscarVideo(String chave) throws IOException;
    void salvarVideo(String chave, byte[] videoZipado);
}