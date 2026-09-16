package com.fiap.campusride_api.exception;

import java.time.LocalDateTime;

// Formato único de erro devolvido pela API — nunca expõe stack trace nem detalhes internos.
public record ErrorResponse(
        LocalDateTime timestamp,
        int status,
        String erro,
        String mensagem,
        String campo) {

    // Erro de regra de negócio ou recurso não encontrado (sem campo específico)
    public static ErrorResponse de(int status, String erro, String mensagem) {
        return new ErrorResponse(LocalDateTime.now(), status, erro, mensagem, null);
    }

    // Erro de validação de um campo de entrada
    public static ErrorResponse deCampo(int status, String erro, String mensagem, String campo) {
        return new ErrorResponse(LocalDateTime.now(), status, erro, mensagem, campo);
    }
}