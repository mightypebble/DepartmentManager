package com.example.myapp.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.myapp.dtos.DirectorateDto;
import com.example.myapp.dtos.UserDtos.UserRegisterDirectorDto;
import com.example.myapp.entities.Department;
import com.example.myapp.entities.Directorate;
import com.example.myapp.entities.User;
import com.example.myapp.services.DirectorateService;
import com.example.myapp.services.UserService;

@CrossOrigin("http://localhost:4200")
@RestController
public class DirectorateController {
	
	private final DirectorateService directorateService;
	
	public DirectorateController(DirectorateService directorateService) {
		this.directorateService = directorateService;
	}
	
	@RequestMapping("/{offset}")
	public List<Directorate> index(@PathVariable String offset) {
		
		List<Directorate> directorates = directorateService.getAllDirectorates(offset);
		
		return directorates;
	}
	
	@RequestMapping("/count-directorates")
	public int countDirectorates() {
		return directorateService.getDirectorateCount();
	}
	
	@RequestMapping("/count-filtered-directorates/{keyword}")
	public int countFilteredDirectorates(@PathVariable String keyword) {
		return directorateService.getFilteredDirectorateCount(keyword);
	}
	
	@RequestMapping(value = "/add-directorate", method = RequestMethod.POST)
	public void addDirectorate(@RequestBody DirectorateDto directorateToRegister) {
		
		directorateService.register(directorateToRegister);
	}
	
	@RequestMapping(value = "/edit-directorate/{name}", method = RequestMethod.POST)
	public void editDirectorate(@RequestBody DirectorateDto directorateToEdit, @PathVariable String name) {
		Directorate directorate = directorateService.getDirectorateByName(name);
		
		directorateService.update(directorateToEdit, directorate);
	}
	
	@RequestMapping(value = "/search-directorates/{keyword}/{offset}", method = RequestMethod.GET)
	public List<Directorate> searchDepartments(@PathVariable String keyword, @PathVariable int offset) {
		List<Directorate> filteredDirectorates = directorateService.searchDirectorates(keyword, offset);
		
		return filteredDirectorates;
	}
	
	@RequestMapping(value = "/delete-directorate/{id}", method = RequestMethod.DELETE)
	public void deleteDirectorate(@PathVariable String id) {
		directorateService.delete(Long.valueOf(id));
	}
	
	@RequestMapping("validate-directorate/{name}")
	public boolean validateName(@PathVariable String name) {
		return directorateService.validateDirectorate(name);
	}
	
}
