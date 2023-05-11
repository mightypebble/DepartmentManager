package com.example.myapp.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.myapp.entities.Directorate;
import com.example.myapp.entities.User;

@Repository
public interface DirectorateRepository extends JpaRepository<Directorate, Long> {

	@Query(value = "SELECT * FROM Directorate ORDER BY id DESC LIMIT 3 OFFSET :offset", nativeQuery = true)
    List<Directorate> find3(@Param("offset") int offset);
	
	@Query(value = "SELECT * FROM Directorate WHERE name LIKE CONCAT('%',:keyword,'%')"
			+ " ORDER BY id DESC LIMIT 3 OFFSET :offset", nativeQuery = true)
	List<Directorate> searchDirectorates(@Param("keyword") String keyword, @Param("offset") int offset);
    
	@Query(value = "SELECT COUNT(DISTINCT id) FROM Directorate", nativeQuery = true)
	int getDirectorateCount();
	
	@Query(value = "SELECT COUNT(DISTINCT id) FROM Directorate WHERE name LIKE CONCAT(:keyword,'%')", nativeQuery = true)
	int getFilteredDirectorateCount(
			@Param("keyword") String keyword
			);
	
    Directorate findByName(String name);
    
    boolean existsByName(String name);
}
