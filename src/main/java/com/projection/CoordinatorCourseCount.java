package com.projection;

/**
 * Projeção usada para retornar a quantidade de coordenadores por curso.
 * Essa interface será usada diretamente nas queries do Spring Data JPA.
 */
public interface CoordinatorCourseCount {

    String getCourse(); // Nome do curso (department)

    Long getTotal();    // Quantidade de coordenadores vinculados ao curso
}
