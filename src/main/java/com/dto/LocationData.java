package com.dto;

import java.io.Serializable;

/**
 * DTO utilizado para representar dados geográficos
 * de faculdades e escolas em relatórios e mapas.
 */
public class LocationData implements Serializable {

    private String name;       // Nome da instituição (faculdade ou escola)
    private String type;       // Tipo: "Faculdade" ou "Escola"
    private String address;    // Endereço completo
    private double latitude;   // Latitude para o mapa
    private double longitude;  // Longitude para o mapa

    public LocationData() {
    }

    public LocationData(String name, String type, String address, double latitude, double longitude) {
        this.name = name;
        this.type = type;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Getters e Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
