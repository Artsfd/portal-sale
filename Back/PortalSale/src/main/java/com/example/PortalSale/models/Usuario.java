package com.example.PortalSale.models;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String email;
    private String ra;
    private String senha;
    private String role;

    @ManyToMany(mappedBy = "inscritos", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Evento> eventosInscritos = new ArrayList<>();

    public Usuario() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRa() { return ra; }
    public void setRa(String ra) { this.ra = ra; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public List<Evento> getEventosInscritos() { return eventosInscritos; }
    public void setEventosInscritos(List<Evento> eventosInscritos) { this.eventosInscritos = eventosInscritos; }
}
