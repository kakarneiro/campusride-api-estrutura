package com.fiap.campusride_api.dto;

import com.fiap.campusride_api.entity.Carona;
import com.fiap.campusride_api.entity.SituacaoCarona;
import com.fiap.campusride_api.entity.TipoVeiculo;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CaronaRequest(

        @NotBlank(message = "O motorista é obrigatório") String motorista,

        @NotBlank(message = "A origem é obrigatória") String origem,

        @NotBlank(message = "O destino é obrigatório") String destino,

        @NotNull(message = "O horário de partida é obrigatório") @Future(message = "O horário de partida deve ser no futuro") LocalDateTime horarioPartida,

        @NotNull(message = "O tipo de veículo é obrigatório") TipoVeiculo tipoVeiculo,

        @NotNull(message = "O número de vagas é obrigatório") @Min(value = 1, message = "A carona deve ter no mínimo 1 vaga") Integer vagasTotais) {

    // Converte o DTO de entrada na entidade, já nascendo com situação ABERTA
    public Carona toEntity() {
        return Carona.builder()
                .motorista(motorista)
                .origem(origem)
                .destino(destino)
                .horarioPartida(horarioPartida)
                .tipoVeiculo(tipoVeiculo)
                .vagasTotais(vagasTotais)
                .situacao(SituacaoCarona.ABERTA)
                .build();
    }
}