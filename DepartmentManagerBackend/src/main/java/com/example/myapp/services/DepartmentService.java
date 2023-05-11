package com.example.myapp.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.myapp.dtos.DepartmentDto;
import com.example.myapp.entities.Department;
import com.example.myapp.entities.User;
import com.example.myapp.repositories.DepartmentRepository;
import com.example.myapp.repositories.DirectorateRepository;

@Service
public class DepartmentService {
	
	private final DepartmentRepository departmentRepository;
	private final DirectorateRepository directorateRepository;
	
	@Autowired
	public DepartmentService(DepartmentRepository DepartmentRepository, DirectorateRepository directorateRepository) {
		this.departmentRepository = DepartmentRepository;
		this.directorateRepository = directorateRepository;
	}
	
	public void register(DepartmentDto departmentDto) {
		String name = departmentDto.getName();
		String description = departmentDto.getDescription();
		String directorate = departmentDto.getDirectorate();
		
		if (departmentRepository.existsByName(name)) return;
		
		Department department = new Department();
		department.setName(name);
		department.setDescription(description);
		department.setDirectorate(directorateRepository.findByName(directorate));
		departmentRepository.save(department);
	}
	
	public void update(DepartmentDto departmentDto, Department department) {
		department.setName(departmentDto.getName());
		if (departmentRepository.existsByName(departmentDto.getName())) return;
		department.setDescription(departmentDto.getDescription());
		departmentRepository.save(department);
	}
	
	public List<Department> searchDepartments(String keyword, int offset) {
		return departmentRepository.searchDepartments(keyword, offset);
	}
	
	public void delete(Long id) {
		departmentRepository.deleteById(id);
	}
	
	public List<Department> getAllDepartments(String offset, Long id) {
		return departmentRepository.find3(Integer.parseInt(offset), id).stream()
                .collect(Collectors.toUnmodifiableList());
	}
	
	public int getDepartmentCount(Long id) {
		return departmentRepository.getDepartmentCount(id);
	}
	
	public int getFilteredDepartmentCount(String keyword, Long id) {
		return departmentRepository.getFilteredDepartmentCount(keyword, id);
	}
	
	public Department getDepartmentByName(String name) {
		return departmentRepository.findByName(name);
	}
	
	public boolean validateDepartment(String name) {
		return departmentRepository.existsByName(name);
	}
	
}