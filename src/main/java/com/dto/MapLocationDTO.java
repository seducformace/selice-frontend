package com.dto;

/**
 * DTO que representa a localização de uma escola ou faculdade
 * para exibição no mapa.
 */
public class MapLocationDTO {

    private String name; // Nome da instituição
    private String type; // Tipo: Faculdade ou Escola
    private String address;
    private Double latitude;
    private Double longitude;

    public MapLocationDTO() {
    }

    public MapLocationDTO(String name, String type, String address, Double latitude, Double longitude) {
        this.name = name;
        this.type = type;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

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
