package com.repository;

import com.model.Faculty;
import com.dto.LocationData;
import com.dto.DetailedReportDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FacultyRepository extends JpaRepository<Faculty, Long> {

    // ✅ Contagem por tipo (PÚBLICA ou PRIVADA)
    long countByType(String type);

    // ✅ Contagem por estado (ATIVO ou INATIVO)
    long countByState(String state); // <- uso correto do nome do campo "state"

    // ✅ Contagem de coordenadores por tipo de faculdade
    @Query("SELECT f.type, COUNT(c) FROM Coordinator c JOIN c.faculty f WHERE f IS NOT NULL GROUP BY f.type")
    List<Object[]> getCoordinatorCountsByType();

    // ✅ Estatísticas gerais por faculdade (exemplo simples, pode ser estendido)
    @Query("SELECT new com.dto.DetailedReportDTO(f.id, f.name) FROM Faculty f")
    List<DetailedReportDTO> getGeneralFacultyStatistics();

    // ✅ Localizações para mapa interativo
    @Query("SELECT new com.dto.LocationData(f.name, 'Faculdade', f.address, f.latitude, f.longitude) FROM Faculty f")
    List<LocationData> getAllLocationsForMap();
}
