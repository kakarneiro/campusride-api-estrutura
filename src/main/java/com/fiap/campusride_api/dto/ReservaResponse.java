package com.fiap.campusride_api.dto;

import com.fiap.campusride_api.entity.Reserva;
import com.fiap.campusride_api.entity.SituacaoReserva;

import java.time.LocalDateTime;

public record ReservaResponse(
        Long id,
        Long caronaId,
        String passageiro,
        LocalDateTime momentoReserva,
        SituacaoReserva situacao) {

    public static ReservaResponse fromEntity(Reserva reserva) {
        return new ReservaResponse(
                reserva.getId(),
                reserva.getCarona().getId(),
                reserva.getPassageiro(),
                reserva.getMomentoReserva(),
                reserva.getSituacao());
    }
}