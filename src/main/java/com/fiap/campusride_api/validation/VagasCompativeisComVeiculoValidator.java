package com.fiap.campusride_api.validation;

import com.fiap.campusride_api.dto.CaronaRequest;
import com.fiap.campusride_api.entity.TipoVeiculo;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Map;

public class VagasCompativeisComVeiculoValidator
        implements ConstraintValidator<VagasCompativeisComVeiculo, CaronaRequest> {

    // Capacidade máxima de passageiros (sem contar o motorista) por tipo de veículo
    private static final Map<TipoVeiculo, Integer> VAGAS_MAXIMAS = Map.of(
            TipoVeiculo.MOTO, 1,
            TipoVeiculo.CARRO, 4,
            TipoVeiculo.SUV, 6,
            TipoVeiculo.VAN, 14);

    @Override
    public boolean isValid(CaronaRequest request, ConstraintValidatorContext context) {
        if (request == null || request.tipoVeiculo() == null || request.vagasTotais() == null) {
            // Campos obrigatórios já são cobertos por @NotNull — aqui não é o lugar de
            // reclamar disso
            return true;
        }

        int vagasMaximas = VAGAS_MAXIMAS.get(request.tipoVeiculo());

        if (request.vagasTotais() <= vagasMaximas) {
            return true;
        }

        // Substitui a mensagem padrão por uma que explica o limite daquele veículo
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(
                "Um veículo do tipo " + request.tipoVeiculo() + " comporta no máximo " + vagasMaximas + " vaga(s)")
                .addPropertyNode("vagasTotais")
                .addConstraintViolation();

        return false;
    }
}