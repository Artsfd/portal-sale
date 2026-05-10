package com.example.PortalSale.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum EventoTipo {
    PRESENCIAL("presencial"),
    ONLINE("online"),
    WORKSHOP("workshop"),
    PALESTRA("palestra"),
    SEMINARIO("seminario");

    private final String value;

    EventoTipo(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static EventoTipo fromValue(String value) {
        if (value == null) {
            return null;
        }
        switch (value.toLowerCase()) {
            case "presencial":
                return PRESENCIAL;
            case "online":
                return ONLINE;
            case "workshop":
                return WORKSHOP;
            case "palestra":
                return PALESTRA;
            case "seminario":
            case "seminário":
                return SEMINARIO;
            default:
                throw new IllegalArgumentException("Tipo de evento desconhecido: " + value);
        }
    }
}
