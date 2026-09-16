package com.fiap.campusride_api.service;

import com.fiap.campusride_api.entity.Carona;
import com.fiap.campusride_api.entity.Reserva;
import com.fiap.campusride_api.entity.SituacaoCarona;
import com.fiap.campusride_api.entity.SituacaoReserva;
import com.fiap.campusride_api.exception.RecursoNaoEncontradoException;
import com.fiap.campusride_api.repository.CaronaRepository;
import com.fiap.campusride_api.repository.ReservaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final CaronaRepository caronaRepository;

    public ReservaService(ReservaRepository reservaRepository, CaronaRepository caronaRepository) {
        this.reservaRepository = reservaRepository;
        this.caronaRepository = caronaRepository;
    }

    @Transactional
    public Reserva reservar(Long caronaId, String passageiro) {
        Carona carona = caronaRepository.findById(caronaId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Carona não encontrada com id " + caronaId));

        // TODO CP5: impedir reservar em carona sem vaga, cancelada, em andamento ou
        // concluída

        Reserva reserva = new Reserva();
        reserva.setCarona(carona);
        reserva.setPassageiro(passageiro);
        reserva.setSituacao(SituacaoReserva.CONFIRMADA);

        Reserva salva = reservaRepository.save(reserva);

        carona.getReservas().add(salva);

        if (carona.getVagasDisponiveis() <= 0) {
            carona.setSituacao(SituacaoCarona.LOTADA);
            caronaRepository.save(carona);
        }

        return salva;
    }

    @Transactional
    public void cancelar(Long reservaId) {
        Reserva reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Reserva não encontrada com id " + reservaId));

        // TODO CP5: impedir cancelar reserva de carona já concluída

        reserva.setSituacao(SituacaoReserva.CANCELADA);
        reservaRepository.save(reserva);

        Carona carona = reserva.getCarona();
        if (carona.getSituacao() == SituacaoCarona.LOTADA && carona.getVagasDisponiveis() > 0) {
            carona.setSituacao(SituacaoCarona.ABERTA);
            caronaRepository.save(carona);
        }
    }
}