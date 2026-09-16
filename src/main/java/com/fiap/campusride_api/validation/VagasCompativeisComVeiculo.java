package com.fiap.campusride_api.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// Validação de classe (TYPE), e não de campo: ela precisa olhar dois atributos
// ao mesmo tempo (tipoVeiculo e vagasTotais), algo que anotações como @Min não fazem.
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = VagasCompativeisComVeiculoValidator.class)
public @interface VagasCompativeisComVeiculo {

    String message() default "O número de vagas não é compatível com o tipo de veículo";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}