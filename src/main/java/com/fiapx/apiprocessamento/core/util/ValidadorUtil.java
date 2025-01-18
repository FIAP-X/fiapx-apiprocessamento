package com.fiapx.apiprocessamento.core.util;

import java.io.IOException;

public class ValidadorUtil {

    private ValidadorUtil() {

    }

    public static void validarVideo(long tamanhoArquivo) throws IOException {

        if (tamanhoArquivo > ConstantesUtil.TAMANHO_MAXIMO_VIDEO) {
            throw new IOException("O arquivo excede o tamanho máximo permitido de 100MB.");
        }
    }
}
