package com.controller;

import com.dto.ReportsDTO;
import com.dto.DetailedReportDTO;
import com.dto.LocationData; // ✅ Correção: importar LocationData corretamente
import com.service.ReportsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller responsável por fornecer dados estatísticos detalhados
 * para a página de relatórios gerais do sistema SELICE.
 */
@RestController
@RequestMapping("/api/reports")
public class ReportsController {

    private final ReportsService reportsService;

    @Autowired
    public ReportsController(ReportsService reportsService) {
        this.reportsService = reportsService;
    }

    /**
     * Endpoint usado pelo dashboard para trazer um resumo geral das entidades.
     * Exemplo: /api/reports
     */
    @GetMapping
    public ReportsDTO getDashboardSummary() {
        return reportsService.getDashboardStats();
    }

    /**
     * Endpoint específico para estatísticas básicas de faculdades.
     * Exemplo: /api/reports/faculties
     */
    @GetMapping("/faculties")
    public ReportsDTO getFacultiesReportsData() {
        return reportsService.getFacultiesStats();
    }

    /**
     * Endpoint detalhado para relatórios completos exigidos pela regra de negócios.
     * Exemplo: /api/reports/full-faculty-report
     */
    @GetMapping("/full-faculty-report")
    public DetailedReportDTO getDetailedFacultyReport() {
        return reportsService.getDetailedFacultyReport();
    }

    /**
     * Endpoint que retorna estatísticas detalhadas por escola, conforme regra de negócio.
     * Exemplo: /api/reports/schools
     */
    @GetMapping("/schools")
    public DetailedReportDTO getSchoolsDetailedReport() {
        return reportsService.getDetailedSchoolsReport();
    }

    /**
     * Endpoint para retornar estatísticas gerais consolidadas por faculdade,
     * com detalhamento de alunos, estagiários, professores e coordenadores.
     * Exemplo: /api/reports/general-faculty-stats
     */
    @GetMapping("/general-faculty-stats")
    public List<DetailedReportDTO> getGeneralFacultyStats() {
        return reportsService.getGeneralFacultyStatistics();
    }

    /**
     * Endpoint para fornecer coordenadas geográficas de faculdades e escolas,
     * utilizadas para montar o mapa interativo do Ceará.
     * Exemplo: /api/reports/locations
     */
    @GetMapping("/locations")
    public List<LocationData> getLocationsData() {
        return reportsService.getLocationsForMap();
    }
}
