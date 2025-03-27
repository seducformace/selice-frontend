package com.service;

import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.HashMap;

import com.repository.FacultyRepository;
import com.dto.ReportsDTO;

/**
 * Serviço responsável por gerar relatórios e estatísticas
 * relacionadas às faculdades cadastradas no sistema SELICE.
 */
@Service
public class ReportsService {

    // Repositório que permite acesso ao banco de dados das faculdades
    private final FacultyRepository facultyRepository;

    /**
     * Injeção do repositório via construtor.
     *
     * @param facultyRepository Repositório da entidade Faculty.
     */
    public ReportsService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    /**
     * Gera um resumo com dados estatísticos das faculdades.
     * OBS: Neste momento, campos como "status" e "type" não
     * existem mais na entidade Faculty, portanto os valores
     * são fixados em zero.
     *
     * @return DTO contendo os dados estatísticos atuais.
     */
    public ReportsDTO getFacultiesStats() {
        // Cria objeto DTO para resposta
        ReportsDTO dto = new ReportsDTO();

        // Define contagens zeradas (por ora, os campos não existem mais)
        dto.setActiveCount(0L);
        dto.setInactiveCount(0L);

        // Preenche estrutura de tipo (público/privado) também com zeros
        Map<String, Long> byType = new HashMap<>();
        byType.put("Pública", 0L);
        byType.put("Privada", 0L);

        dto.setByType(byType);

        return dto;
    }
}
