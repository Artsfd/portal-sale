package com.example.PortalSale.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "presenca_evento", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"inscricao_evento_id"})
})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class PresencaEvento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "inscricao_evento_id")
    private InscricaoEvento inscricaoEvento;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dataHoraCheckin;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dataHoraCheckout;

    @Enumerated(EnumType.STRING)
    private StatusPresenca statusPresenca = StatusPresenca.AUSENTE;

    private String ipRequisicao;
    private String userAgent;
    private String geolocalizacao;
    private String codigoUsado;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public InscricaoEvento getInscricaoEvento() {
        return inscricaoEvento;
    }

    public void setInscricaoEvento(InscricaoEvento inscricaoEvento) {
        this.inscricaoEvento = inscricaoEvento;
    }

    public LocalDateTime getDataHoraCheckin() {
        return dataHoraCheckin;
    }

    public void setDataHoraCheckin(LocalDateTime dataHoraCheckin) {
        this.dataHoraCheckin = dataHoraCheckin;
    }

    public LocalDateTime getDataHoraCheckout() {
        return dataHoraCheckout;
    }

    public void setDataHoraCheckout(LocalDateTime dataHoraCheckout) {
        this.dataHoraCheckout = dataHoraCheckout;
    }

    public StatusPresenca getStatusPresenca() {
        return statusPresenca;
    }

    public void setStatusPresenca(StatusPresenca statusPresenca) {
        this.statusPresenca = statusPresenca;
    }

    public String getIpRequisicao() {
        return ipRequisicao;
    }

    public void setIpRequisicao(String ipRequisicao) {
        this.ipRequisicao = ipRequisicao;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getGeolocalizacao() {
        return geolocalizacao;
    }

    public void setGeolocalizacao(String geolocalizacao) {
        this.geolocalizacao = geolocalizacao;
    }

    public String getCodigoUsado() {
        return codigoUsado;
    }

    public void setCodigoUsado(String codigoUsado) {
        this.codigoUsado = codigoUsado;
    }
}
