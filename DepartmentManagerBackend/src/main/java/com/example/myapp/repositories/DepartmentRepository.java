package com.example.myapp.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.myapp.entities.Department;
import com.example.myapp.entities.Directorate;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

	@Query(value = "SELECT * FROM Department WHERE directorate = :dir ORDER BY id DESC LIMIT 3 OFFSET :offset", nativeQuery = true)
    List<Department> find3(@Param("offset") int offset, @Param("dir") Long id);
	
	@Query(value = "SELECT * FROM Department WHERE name LIKE CONCAT('%',:keyword,'%')"
			+ " ORDER BY id DESC LIMIT 3 OFFSET :offset", nativeQuery = true)
	List<Department> searchDepartments(@Param("keyword") String keyword, @Param("offset") int offset);
    
	@Query(value = "SELECT COUNT(DISTINCT id) FROM Department WHERE directorate = :dir", nativeQuery = true)
	int getDepartmentCount(
			@Param("dir") Long id
			);
	
	@Query(value = "SELECT COUNT(DISTINCT id) FROM Department WHERE directorate = :dir"
			+ " AND name LIKE CONCAT(:keyword,'%')", nativeQuery = true)
	int getFilteredDepartmentCount(
			@Param("keyword") String keyword,
			@Param("dir") Long id
			);
	
    Department findByName(String name);
    
    boolean existsByName(String name);
}
