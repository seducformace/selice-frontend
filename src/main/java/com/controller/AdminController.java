package com.controller;

import com.model.Admin;
import com.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller responsável por expor os endpoints do administrador.
 */
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    /**
     * Construtor para injeção de dependência de AdminService.
     *
     * @param adminService O serviço de administradores injetado pelo Spring.
     */
    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    /**
     * Retorna todos os administradores do sistema.
     *
     * @return Lista de administradores.
     */
    @GetMapping("/all")
    public List<Admin> getAllAdmins() {
        List<Admin> admins = adminService.getAllAdmins();
        System.out.println("Admins retornados pelo controlador: " + admins);
        return admins;
    }

    /**
     * Busca um administrador pelo nome de usuário.
     *
     * @param username Nome de usuário do administrador.
     * @return O administrador encontrado ou null caso não exista.
     */
    @GetMapping("/{username}")
    public Admin getAdminByUsername(@PathVariable String username) {
        Admin admin = adminService.findAdminByUsername(username);
        System.out.println("Admin retornado pelo controlador: " + admin);
        return admin;
    }
}
