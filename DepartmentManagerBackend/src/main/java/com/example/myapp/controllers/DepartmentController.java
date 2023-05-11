package com.example.myapp.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.myapp.dtos.DepartmentDto;
import com.example.myapp.dtos.DirectorateDto;
import com.example.myapp.entities.Department;
import com.example.myapp.entities.Directorate;
import com.example.myapp.entities.User;
import com.example.myapp.repositories.DirectorateRepository;
import com.example.myapp.services.DepartmentService;

@CrossOrigin("http://localhost:4200")
@RestController
public class DepartmentController {
	
	private final DepartmentService departmentService;
	private final DirectorateRepository directorateRepository;
	
	public DepartmentController(DepartmentService departmentService, DirectorateRepository directorateRepository) {
		this.departmentService = departmentService;
		this.directorateRepository = directorateRepository;
	}
	
	@RequestMapping("/directorate/{name}/{offset}")
	public List<Department> DepratmentIndex(@PathVariable String name, @PathVariable String offset) {
		Directorate directorate = directorateRepository.findByName(name);
		List<Department> departments = departmentService.getAllDepartments(offset, directorate.getId());
		
		return departments;
	}
	
	@RequestMapping("/count-departments/{name}")
	public int countDepartments(@PathVariable String name) {
		Directorate directorate = directorateRepository.findByName(name);
		
		return departmentService.getDepartmentCount(directorate.getId());
	}
	
	@RequestMapping("/count-filtered-departments/{keyword}/{name}")
	public int countFilteredDepartments(@PathVariable String keyword, @PathVariable String name) {
		Directorate directorate = directorateRepository.findByName(name);
		
		return departmentService.getFilteredDepartmentCount(keyword, directorate.getId());
	}
	
	@RequestMapping(value = "/add-department", method = RequestMethod.POST)
	public void addDepartment(@RequestBody DepartmentDto departmentToRegister) {
		
		departmentService.register(departmentToRegister);
	}
	
	@RequestMapping(value = "/edit-department/{name}", method = RequestMethod.POST)
	public void editDirectorate(@RequestBody DepartmentDto departmentToEdit, @PathVariable String name) {
		Department department = departmentService.getDepartmentByName(name);
		
		departmentService.update(departmentToEdit, department);
	}
	
	@RequestMapping(value = "/search-departments/{keyword}/{offset}", method = RequestMethod.GET)
	public List<Department> searchDepartments(@PathVariable String keyword, @PathVariable int offset) {
		List<Department> filteredDepartments = departmentService.searchDepartments(keyword, offset);
		
		return filteredDepartments;
	}
	
	@RequestMapping(value = "/delete-department/{id}", method = RequestMethod.DELETE)
	public void deleteDepartment(@PathVariable String id) {
		departmentService.delete(Long.valueOf(id));
	}
	
	@RequestMapping("validate-department/{name}")
	public boolean validateName(@PathVariable String name) {
		return departmentService.validateDepartment(name);
	}
}
