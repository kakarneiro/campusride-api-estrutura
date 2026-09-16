package com.fiap.campusride_api.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "caronas")
public class Carona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String motorista;

    private String origem;

    private String destino;

    private LocalDateTime horarioPartida;

    @Enumerated(EnumType.STRING)
    private TipoVeiculo tipoVeiculo;

    private Integer vagasTotais;

    @Enumerated(EnumType.STRING)
    private SituacaoCarona situacao;

    @OneToMany(mappedBy = "carona", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Reserva> reservas = new ArrayList<>();

    public Carona() {
    }

    // ---- regras de domínio da própria entidade ----

    public long contarVagasOcupadas() {
        return reservas.stream()
                .filter(reserva -> reserva.getSituacao() == SituacaoReserva.CONFIRMADA)
                .count();
    }

    public int getVagasDisponiveis() {
        return vagasTotais - (int) contarVagasOcupadas();
    }

    // ---- getters e setters ----

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMotorista() {
        return motorista;
    }

    public void setMotorista(String motorista) {
        this.motorista = motorista;
    }

    public String getOrigem() {
        return origem;
    }

    public void setOrigem(String origem) {
        this.origem = origem;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public LocalDateTime getHorarioPartida() {
        return horarioPartida;
    }

    public void setHorarioPartida(LocalDateTime horarioPartida) {
        this.horarioPartida = horarioPartida;
    }

    public TipoVeiculo getTipoVeiculo() {
        return tipoVeiculo;
    }

    public void setTipoVeiculo(TipoVeiculo tipoVeiculo) {
        this.tipoVeiculo = tipoVeiculo;
    }

    public Integer getVagasTotais() {
        return vagasTotais;
    }

    public void setVagasTotais(Integer vagasTotais) {
        this.vagasTotais = vagasTotais;
    }

    public SituacaoCarona getSituacao() {
        return situacao;
    }

    public void setSituacao(SituacaoCarona situacao) {
        this.situacao = situacao;
    }

    public List<Reserva> getReservas() {
        return reservas;
    }

    public void setReservas(List<Reserva> reservas) {
        this.reservas = reservas;
    }
}