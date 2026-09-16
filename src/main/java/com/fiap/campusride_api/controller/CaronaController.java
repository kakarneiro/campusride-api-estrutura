package com.fiap.campusride_api.controller;

import com.fiap.campusride_api.dto.CaronaRequest;
import com.fiap.campusride_api.dto.CaronaResponse;
import com.fiap.campusride_api.entity.Carona;
import com.fiap.campusride_api.service.CaronaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/caronas")
public class CaronaController {

    private final CaronaService caronaService;

    public CaronaController(CaronaService caronaService) {
        this.caronaService = caronaService;
    }

    @PostMapping
    public ResponseEntity<CaronaResponse> publicar(@RequestBody @Valid CaronaRequest request) {
        Carona carona = caronaService.publicar(request.toEntity());
        return ResponseEntity.status(HttpStatus.CREATED).body(CaronaResponse.fromEntity(carona));
    }

    @GetMapping
    public List<CaronaResponse> listarDisponiveis() {
        return caronaService.listarDisponiveis().stream()
                .map(CaronaResponse::fromEntity)
                .toList();
    }

    @GetMapping("/{id}")
    public CaronaResponse detalhar(@PathVariable Long id) {
        return CaronaResponse.fromEntity(caronaService.buscarPorId(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelar(@PathVariable Long id) {
        caronaService.cancelar(id);
        return ResponseEntity.noContent().build();
    }
}