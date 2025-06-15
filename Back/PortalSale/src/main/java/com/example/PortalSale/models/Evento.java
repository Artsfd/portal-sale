package com.example.PortalSale.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity //Indica que essa classe será tratada como uma entidade no banco
@Table(name = "eventos")
public class Evento {
    @Id //Define a PK
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Auto incremento do ID
    private Long id;

    private String nome;
    private String palestrante;
    private String descricao;
    private LocalDateTime dataHora;
    private String local;
    private String tipoEvento;
    private int vagas;
    private int inscritos;

    public Evento() {
    }

    public Evento(Long id, String nome, String palestrante, String descricao, LocalDateTime dataHora, String local, String tipoEvento, int vagas, int inscritos) {
        this.id = id;
        this.nome = nome;
        this.palestrante = palestrante;
        this.descricao = descricao;
        this.dataHora = dataHora;
        this.local = local;
        this.tipoEvento = tipoEvento;
        this.vagas = vagas;
        this.inscritos = inscritos;
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

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getPalestrante() {
        return palestrante;
    }

    public void setPalestrante(String palestrante) {
        this.palestrante = palestrante;
    }

    public String getTipoEvento() {
        return tipoEvento;
    }

    public void setTipoEvento(String tipoEvento) {
        this.tipoEvento = tipoEvento;
    }

    public int getVagas() {
        return vagas;
    }

    public void setVagas(int vagas) {
        this.vagas = vagas;
    }

    public int getInscritos() {
        return inscritos;
    }

    public void setInscritos(int inscritos) {
        this.inscritos = inscritos;
    }
}
