package com.fiap.campusride_api.dto;

import com.fiap.campusride_api.entity.Carona;
import com.fiap.campusride_api.entity.SituacaoCarona;
import com.fiap.campusride_api.entity.TipoVeiculo;

import java.time.LocalDateTime;
import java.util.List;

public record CaronaResponse(
        Long id,
        String motorista,
        String origem,
        String destino,
        LocalDateTime horarioPartida,
        TipoVeiculo tipoVeiculo,
        Integer vagasTotais,
        Integer vagasDisponiveis,
        SituacaoCarona situacao,
        List<ReservaResponse> reservas) {

    // Monta o DTO de saída a partir da entidade — quem consome a API nunca vê a
    // entidade JPA direto
    public static CaronaResponse fromEntity(Carona carona) {
        List<ReservaResponse> reservas = carona.getReservas().stream()
                .map(ReservaResponse::fromEntity)
                .toList();

        return new CaronaResponse(
                carona.getId(),
                carona.getMotorista(),
                carona.getOrigem(),
                carona.getDestino(),
                carona.getHorarioPartida(),
                carona.getTipoVeiculo(),
                carona.getVagasTotais(),
                carona.getVagasDisponiveis(),
                carona.getSituacao(),
                reservas);
    }
}