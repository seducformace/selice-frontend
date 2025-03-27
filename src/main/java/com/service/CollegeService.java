package com.service;

import com.model.College;
import com.repository.CollegeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Este é o cérebro da operação das faculdades no SELICE!
 * Aqui está toda a mágica que gerencia a vida de uma faculdade no sistema.
 */
@Service // Marca esta classe como um serviço no Spring.
public class CollegeService {

    @Autowired // O motor que conecta o repositório ao serviço.
    private CollegeRepository collegeRepository;

    /**
     * **Cadastro de nova faculdade**: Aqui criamos uma nova história no sistema.
     */
    public College createCollege(College college) {
        return collegeRepository.save(college); // Salva a faculdade e retorna a nova estrela do sistema!
    }

    /**
     * **Busca todas as faculdades**: Como abrir um álbum com todas as páginas.
     */
    public List<College> getAllColleges() {
        return collegeRepository.findAll(); // Retorna todas as faculdades cadastradas.
    }

    /**
     * **Busca faculdade por ID**: Quando queremos encontrar um capítulo específico.
     */
    public Optional<College> getCollegeById(Long id) {
        return collegeRepository.findById(id); // Retorna a faculdade com o ID procurado, se existir.
    }

    /**
     * **Busca faculdade pelo nome**: Procuramos pela identidade especial!
     */
    public College getCollegeByName(String name) {
        return collegeRepository.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("Faculdade não encontrada: " + name)); // Lança exceção se não encontrada.
    }

    /**
     * **Exclusão de uma faculdade**: Despedida de uma faculdade, mas só se ela for encontrada!
     */
    public void deleteCollege(Long id) {
        collegeRepository.deleteById(id); // Deleta pelo ID.
    }
}
