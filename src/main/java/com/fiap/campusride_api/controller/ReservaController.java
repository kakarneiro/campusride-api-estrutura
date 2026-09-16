package com.fiap.campusride_api.controller;

import com.fiap.campusride_api.dto.ReservaRequest;
import com.fiap.campusride_api.dto.ReservaResponse;
import com.fiap.campusride_api.entity.Reserva;
import com.fiap.campusride_api.service.ReservaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ReservaController {

    private final ReservaService reservaService;

    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    @PostMapping("/caronas/{caronaId}/reservas")
    public ResponseEntity<ReservaResponse> reservar(@PathVariable Long caronaId,
            @RequestBody @Valid ReservaRequest request) {
        Reserva reserva = reservaService.reservar(caronaId, request.passageiro());
        return ResponseEntity.status(HttpStatus.CREATED).body(ReservaResponse.fromEntity(reserva));
    }

    @DeleteMapping("/reservas/{id}")
    public ResponseEntity<Void> cancelar(@PathVariable Long id) {
        reservaService.cancelar(id);
        return ResponseEntity.noContent().build();
    }
}