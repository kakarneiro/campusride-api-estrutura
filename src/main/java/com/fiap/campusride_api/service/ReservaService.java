package com.fiap.campusride_api.service;

import com.fiap.campusride_api.entity.Carona;
import com.fiap.campusride_api.entity.Reserva;
import com.fiap.campusride_api.entity.SituacaoCarona;
import com.fiap.campusride_api.entity.SituacaoReserva;
import com.fiap.campusride_api.exception.RecursoNaoEncontradoException;
import com.fiap.campusride_api.exception.RegraDeNegocioException;
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

        validarSeCaronaAceitaReserva(carona);

        Reserva reserva = new Reserva();
        reserva.setCarona(carona);
        reserva.setPassageiro(passageiro);
        reserva.setSituacao(SituacaoReserva.CONFIRMADA);

        Reserva salva = reservaRepository.save(reserva);
        carona.getReservas().add(salva);

        // A carona passa a LOTADA assim que a última vaga é ocupada
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

        if (reserva.getSituacao() == SituacaoReserva.CANCELADA) {
            throw new RegraDeNegocioException("Esta reserva já está cancelada");
        }

        if (reserva.getCarona().getSituacao() == SituacaoCarona.CONCLUIDA) {
            throw new RegraDeNegocioException("Não é possível cancelar uma reserva de carona já concluída");
        }

        reserva.setSituacao(SituacaoReserva.CANCELADA);
        reservaRepository.save(reserva);

        // Liberou vaga numa carona que estava lotada: ela volta a aceitar reservas
        Carona carona = reserva.getCarona();
        if (carona.getSituacao() == SituacaoCarona.LOTADA && carona.getVagasDisponiveis() > 0) {
            carona.setSituacao(SituacaoCarona.ABERTA);
            caronaRepository.save(carona);
        }
    }

    // Concentra num só lugar as condições que impedem uma reserva
    private void validarSeCaronaAceitaReserva(Carona carona) {
        switch (carona.getSituacao()) {
            case CANCELADA -> throw new RegraDeNegocioException("Não é possível reservar em uma carona cancelada");
            case CONCLUIDA -> throw new RegraDeNegocioException("Não é possível reservar em uma carona já concluída");
            case EM_ANDAMENTO ->
                throw new RegraDeNegocioException("Não é possível reservar em uma carona em andamento");
            case LOTADA -> throw new RegraDeNegocioException("Esta carona não possui vagas disponíveis");
            case ABERTA -> {
                if (carona.getVagasDisponiveis() <= 0) {
                    throw new RegraDeNegocioException("Esta carona não possui vagas disponíveis");
                }
            }
        }
    }
}