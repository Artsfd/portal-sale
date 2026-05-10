package com.example.PortalSale.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "eventos")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Evento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String palestrante;
    private String descricao;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime dataHora;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime horaFim;

    private String local;

    @Enumerated(EnumType.STRING)
    private EventoTipo tipoEvento = EventoTipo.PRESENCIAL;

    @Column(name = "capacidade_maxima")
    private int capacidadeMaxima;

    private Double latitude;
    private Double longitude;

    private int vagas;

    @OneToMany(mappedBy = "evento", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<InscricaoEvento> inscricoes = new ArrayList<>();

    public Evento() {}

    public Evento(Long id, String nome, String palestrante, String descricao,
                  LocalDateTime dataHora, String local, EventoTipo tipoEvento,
                  int capacidadeMaxima, Double latitude, Double longitude) {
        this.id = id;
        this.nome = nome;
        this.palestrante = palestrante;
        this.descricao = descricao;
        this.dataHora = dataHora;
        this.local = local;
        this.tipoEvento = tipoEvento;
        this.capacidadeMaxima = capacidadeMaxima;
        this.latitude = latitude;
        this.longitude = longitude;
        this.vagas = capacidadeMaxima;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getPalestrante() {
        return palestrante;
    }

    public void setPalestrante(String palestrante) {
        this.palestrante = palestrante;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public LocalDateTime getHoraFim() {
        return horaFim;
    }

    public void setHoraFim(LocalDateTime horaFim) {
        this.horaFim = horaFim;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public EventoTipo getTipoEvento() {
        return tipoEvento;
    }

    public void setTipoEvento(EventoTipo tipoEvento) {
        this.tipoEvento = tipoEvento;
    }

    public int getCapacidadeMaxima() {
        return capacidadeMaxima > 0 ? capacidadeMaxima : vagas;
    }

    public void setCapacidadeMaxima(int capacidadeMaxima) {
        this.capacidadeMaxima = capacidadeMaxima;
        this.vagas = capacidadeMaxima;
    }

    public int getVagas() {
        return getCapacidadeMaxima();
    }

    public void setVagas(int vagas) {
        this.vagas = vagas;
        if (this.capacidadeMaxima <= 0) {
            this.capacidadeMaxima = vagas;
        }
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public List<InscricaoEvento> getInscricoes() {
        return inscricoes;
    }

    public void setInscricoes(List<InscricaoEvento> inscricoes) {
        this.inscricoes = inscricoes;
    }

    @JsonProperty("inscritos")
    public int getInscritos() {
        return inscricoes.size();
    }
}
