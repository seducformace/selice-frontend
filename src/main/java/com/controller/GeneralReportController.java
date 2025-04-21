package com.controller;

import com.dto.GeneralReportDTO;
import com.service.GeneralReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports/general")
@CrossOrigin(origins = "*")
public class GeneralReportController {

    private final GeneralReportService generalReportService;

    @Autowired
    public GeneralReportController(GeneralReportService generalReportService) {
        this.generalReportService = generalReportService;
    }

    @GetMapping
    public GeneralReportDTO getGeneralReport() {
        return generalReportService.getGeneralReport();
    }
}
