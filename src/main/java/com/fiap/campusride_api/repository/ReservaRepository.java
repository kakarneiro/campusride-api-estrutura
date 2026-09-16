package com.fiap.campusride_api.repository;

import com.fiap.campusride_api.entity.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {
}