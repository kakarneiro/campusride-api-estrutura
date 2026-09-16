package com.fiap.campusride_api.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "reservas")
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "carona_id")
    private Carona carona;

    private String passageiro;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime momentoReserva;

    @Enumerated(EnumType.STRING)
    private SituacaoReserva situacao;

    public Reserva() {
    }

    // ---- getters e setters ----

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Carona getCarona() {
        return carona;
    }

    public void setCarona(Carona carona) {
        this.carona = carona;
    }

    public String getPassageiro() {
        return passageiro;
    }

    public void setPassageiro(String passageiro) {
        this.passageiro = passageiro;
    }

    public LocalDateTime getMomentoReserva() {
        return momentoReserva;
    }

    public void setMomentoReserva(LocalDateTime momentoReserva) {
        this.momentoReserva = momentoReserva;
    }

    public SituacaoReserva getSituacao() {
        return situacao;
    }

    public void setSituacao(SituacaoReserva situacao) {
        this.situacao = situacao;
    }
}