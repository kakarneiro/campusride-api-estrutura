package com.fiap.campusride_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// Centraliza o tratamento de erros de toda a API.
// Sem isso, cada controller precisaria de try/catch e o Spring devolveria
// respostas genéricas (ou com stack trace) que vazam detalhes internos.
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Dados de entrada inválidos (@Valid nos DTOs) -> 400
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

    // JSON malformado ou valor de enum inexistente (ex: tipoVeiculo "AVIAO") -> 400
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
}