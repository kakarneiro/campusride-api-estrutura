package com.fiap.campusride_api.exception;

// Lançada quando uma Carona ou Reserva não é encontrada pelo id.
// O tratamento formal dela (virar resposta 404 padronizada) é feito no GlobalExceptionHandler, no CP3.
public class RecursoNaoEncontradoException extends RuntimeException {

    public RecursoNaoEncontradoException(String message) {
        super(message);
    }
}