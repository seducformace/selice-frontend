package com.dto;

import java.util.Map;
import java.util.HashMap;

/**
 * Data Transfer Object (DTO) responsável por transportar dados
 * estatísticos relacionados às faculdades no sistema SELICE.
 *
 * Os dados incluem contagens por status (ativo/inativo)
 * e por tipo de instituição (pública/privada).
 */
public class ReportsDTO {

    // Quantidade de faculdades com status "Ativo"
    private long activeCount;

    // Quantidade de faculdades com status "Inativo"
    private long inactiveCount;

    // Mapa com os tipos de faculdade e respectivas quantidades (ex: {"Pública": 4, "Privada": 2})
    private Map<String, Long> byType = new HashMap<>();

    /**
     * Construtor padrão.
     */
    public ReportsDTO() {}

    // Getters e Setters

    public long getActiveCount() {
        return activeCount;
    }

    public void setActiveCount(long activeCount) {
        this.activeCount = activeCount;
    }

    public long getInactiveCount() {
        return inactiveCount;
    }

    public void setInactiveCount(long inactiveCount) {
        this.inactiveCount = inactiveCount;
    }

    public Map<String, Long> getByType() {
        return byType;
    }

    public void setByType(Map<String, Long> byType) {
        this.byType = byType;
    }
}
