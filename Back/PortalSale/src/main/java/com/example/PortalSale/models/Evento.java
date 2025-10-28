package com.example.PortalSale.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "eventos")
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String palestrante;
    private String descricao;
    private LocalDateTime dataHora;
    private String local;
    private String tipoEvento;
    private int vagas;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "evento_usuario",
            joinColumns = @JoinColumn(name = "evento_id"),
            inverseJoinColumns = @JoinColumn(name = "usuario_id")
    )
    private List<Usuario> inscritos = new ArrayList<>();

    public Evento() {
    }

    public Evento(Long id, String nome, String palestrante, String descricao,
            LocalDateTime dataHora, String local, String tipoEvento, int vagas) {
        this.id = id;
        this.nome = nome;
        this.palestrante = palestrante;
        this.descricao = descricao;
        this.dataHora = dataHora;
        this.local = local;
        this.tipoEvento = tipoEvento;
        this.vagas = vagas;
    }

    // Getters e setters
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

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
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

    public List<Usuario> getInscritos() {
        return inscritos;
    }

    public void setInscritos(List<Usuario> inscritos) {
        this.inscritos = inscritos;
    }
}
