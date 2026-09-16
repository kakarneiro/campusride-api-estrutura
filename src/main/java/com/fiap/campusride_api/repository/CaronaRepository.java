package com.fiap.campusride_api.repository;

import com.fiap.campusride_api.entity.Carona;
import com.fiap.campusride_api.entity.SituacaoCarona;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CaronaRepository extends JpaRepository<Carona, Long> {

    // @EntityGraph faz o JPA trazer as reservas na mesma consulta da carona.
    // Sem isso, a lista só seria buscada depois (LAZY), quando a sessão com o banco
    // já foi fechada — o que causa LazyInitializationException ao montar o DTO.

    @EntityGraph(attributePaths = "reservas")
    List<Carona> findBySituacao(SituacaoCarona situacao);

    @Override
    @EntityGraph(attributePaths = "reservas")
    Optional<Carona> findById(Long id);
}