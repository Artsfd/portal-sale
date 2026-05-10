package com.example.PortalSale.dto;

public class ValidacaoCodigoRequest {

    private Long eventoId;
    private String email;
    private String codigo;
    private Double latitude;
    private Double longitude;

    public Long getEventoId() {
        return eventoId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setEventoId(Long eventoId) {
        this.eventoId = eventoId;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
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
}
