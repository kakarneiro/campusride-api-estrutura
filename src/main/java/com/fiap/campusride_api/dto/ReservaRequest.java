package com.fiap.campusride_api.dto;

import jakarta.validation.constraints.NotBlank;

public record ReservaRequest(

        @NotBlank(message = "O passageiro é obrigatório") String passageiro) {
}
