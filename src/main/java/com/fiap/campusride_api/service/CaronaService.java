package com.fiap.campusride_api.service;

import com.fiap.campusride_api.entity.Carona;
import com.fiap.campusride_api.entity.SituacaoCarona;
import com.fiap.campusride_api.entity.SituacaoReserva;
import com.fiap.campusride_api.exception.RecursoNaoEncontradoException;
import com.fiap.campusride_api.exception.RegraDeNegocioException;
import com.fiap.campusride_api.repository.CaronaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CaronaService {

    private final CaronaRepository caronaRepository;

    // Injeção por construtor (o Spring injeta automaticamente quando há só um
    // construtor)
    public CaronaService(CaronaRepository caronaRepository) {
        this.caronaRepository = caronaRepository;
    }

    public Carona publicar(Carona carona) {
        return caronaRepository.save(carona);
    }

    public List<Carona> listarDisponiveis() {
        return caronaRepository.findBySituacao(SituacaoCarona.ABERTA);
    }

    public Carona buscarPorId(Long id) {
        return caronaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Carona não encontrada com id " + id));
    }

    @Transactional
    public void cancelar(Long id) {
        Carona carona = buscarPorId(id);

        if (carona.getSituacao() == SituacaoCarona.CONCLUIDA) {
            throw new RegraDeNegocioException("Não é possível cancelar uma carona já concluída");
        }

        if (carona.getSituacao() == SituacaoCarona.CANCELADA) {
            throw new RegraDeNegocioException("Esta carona já está cancelada");
        }

        // O cancelamento da carona reflete nas reservas associadas
        carona.setSituacao(SituacaoCarona.CANCELADA);
        carona.getReservas().forEach(reserva -> {
            if (reserva.getSituacao() == SituacaoReserva.CONFIRMADA) {
                reserva.setSituacao(SituacaoReserva.CANCELADA);
            }
        });

        caronaRepository.save(carona);
    }
}