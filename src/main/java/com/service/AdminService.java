package com.service;

import com.model.Admin;
import com.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    /**
     * Retorna todos os administradores.
     */
    public List<Admin> getAllAdmins() {
        List<Admin> admins = adminRepository.findAll();
        System.out.println("Admins encontrados: " + admins); // Log para verificar os dados
        return admins;
    }

    /**
     * Busca um administrador pelo nome de usuário.
     */
    public Admin findAdminByUsername(String username) {
        Optional<Admin> optionalAdmin = adminRepository.findByUsername(username);
        return optionalAdmin.orElse(null); // Retorna o Admin ou null se não encontrado
    }
}
