package com.controller;

// Importa a entidade College (modelo de dados)
import com.model.College;

// Importa o serviço que contém a lógica de negócio
import com.service.CollegeService;

// Importações do Spring Framework para injeção de dependência, HTTP e rotas
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// Importa lista para retornar várias faculdades
import java.util.List;

/**
 * Controlador responsável pelas operações com faculdades no sistema SELICE.
 * Permite criar, buscar, listar e excluir faculdades via API REST.
 */
@RestController // Indica que essa classe é um controlador REST
@RequestMapping("/api/colleges") // Define o caminho base da API: http://localhost:8080/api/colleges
public class CollegeController {

    // Injeta automaticamente o serviço que contém a lógica de manipulação das faculdades
    @Autowired
    private CollegeService collegeService;

    /**
     * Endpoint POST para cadastrar uma nova faculdade.
     * @param college Objeto JSON da faculdade enviado pelo frontend
     * @return A faculdade cadastrada com código 200 OK
     */
    @PostMapping
    public ResponseEntity<College> createCollege(@RequestBody College college) {
        // Chama o serviço para salvar a nova faculdade
        College newCollege = collegeService.createCollege(college);

        // Retorna a resposta com a faculdade salva no corpo
        return ResponseEntity.ok(newCollege);
    }

    /**
     * Endpoint GET para listar todas as faculdades cadastradas.
     * @return Lista de faculdades com código 200 OK
     */
    @GetMapping
    public ResponseEntity<List<College>> getAllColleges() {
        // Recupera todas as faculdades através do serviço
        List<College> colleges = collegeService.getAllColleges();

        // Retorna a lista
        return ResponseEntity.ok(colleges);
    }

    /**
     * Endpoint GET para buscar uma faculdade específica pelo seu ID.
     * @param id Identificador da faculdade
     * @return Faculdade encontrada ou 404 caso não exista
     */
    @GetMapping("/{id}")
    public ResponseEntity<College> getCollegeById(@PathVariable Long id) {
        return collegeService.getCollegeById(id)
                .map(ResponseEntity::ok) // Se encontrada, retorna 200 com a faculdade
                .orElse(ResponseEntity.notFound().build()); // Se não, retorna 404 Not Found
    }

    /**
     * Endpoint de teste simples para verificar se o controlador está funcionando.
     * @return Mensagem de sucesso
     */
    @GetMapping("/test")
    public ResponseEntity<String> testEndpoint() {
        return ResponseEntity.ok("CollegeController is working!");
    }

    /**
     * Endpoint DELETE para excluir uma faculdade pelo seu ID.
     * @param id Identificador da faculdade
     * @return Código 204 No Content em caso de sucesso
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCollege(@PathVariable Long id) {
        // Solicita ao serviço que exclua a faculdade com o ID fornecido
        collegeService.deleteCollege(id);

        // Retorna 204 indicando que a exclusão foi bem-sucedida
        return ResponseEntity.noContent().build();
    }
}
