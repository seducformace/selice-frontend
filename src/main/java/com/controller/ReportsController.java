package com.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired; // Importa para injeção automática

import com.service.ReportsService; // Importa o serviço corretamente
import com.dto.ReportsDTO; // Importa o DTO corretamente

@RestController
@RequestMapping("/api/reports")
public class ReportsController {

    private final ReportsService reportsService;

    @Autowired
    public ReportsController(ReportsService reportsService) {
        this.reportsService = reportsService;
    }

    @GetMapping("/faculties")
    public ReportsDTO getFacultiesReportsData() {
        // Usa o service para buscar/contar faculdades
        return reportsService.getFacultiesStats();
    }
}
