package com.fiap.campusride_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> tratarValidacao(MethodArgumentNotValidException ex) {
        FieldError erroDeCampo = ex.getBindingResult().getFieldErrors().get(0);

        ErrorResponse resposta = ErrorResponse.deCampo(
                HttpStatus.BAD_REQUEST.value(),
                "Erro de validação",
                erroDeCampo.getDefaultMessage(),
                erroDeCampo.getField());

        return ResponseEntity.badRequest().body(resposta);
    }

   
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> tratarJsonInvalido(HttpMessageNotReadableException ex) {
        ErrorResponse resposta = ErrorResponse.de(
                HttpStatus.BAD_REQUEST.value(),
                "Requisição inválida",
                "O corpo da requisição está mal formatado ou contém um valor inválido");

        return ResponseEntity.badRequest().body(resposta);
    }

    // Carona ou reserva inexistente -> 404
    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ErrorResponse> tratarNaoEncontrado(RecursoNaoEncontradoException ex) {
        ErrorResponse resposta = ErrorResponse.de(
                HttpStatus.NOT_FOUND.value(),
                "Recurso não encontrado",
                ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resposta);
    }


    @ExceptionHandler(RegraDeNegocioException.class)
    public ResponseEntity<ErrorResponse> tratarRegraDeNegocio(RegraDeNegocioException ex) {
        ErrorResponse resposta = ErrorResponse.de(
                HttpStatus.CONFLICT.value(),
                "Operação não permitida",
                ex.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(resposta);
    }
}