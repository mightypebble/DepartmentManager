package com.example.myapp.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.myapp.dtos.DirectorateDto;
import com.example.myapp.entities.Directorate;
import com.example.myapp.entities.User;
import com.example.myapp.repositories.DirectorateRepository;

@Service
public class DirectorateService {
	
	private final DirectorateRepository directorateRepository;
	
	@Autowired
	public DirectorateService(DirectorateRepository directorateRepository) {
		this.directorateRepository = directorateRepository;
	}
	
	public void register(DirectorateDto directorateDto) {
		String name = directorateDto.getName();
		String description = directorateDto.getDescription();
		
		if (directorateRepository.existsByName(name)) return;
		
		Directorate directorate = new Directorate();
		directorate.setName(name);
		directorate.setDescription(description);
		directorateRepository.save(directorate);
	}
	
	public void update(DirectorateDto directorateDto, Directorate directorate) {
		directorate.setName(directorateDto.getName());
		if (directorateRepository.existsByName(directorateDto.getName())) return;
		directorate.setDescription(directorateDto.getDescription());
		directorateRepository.save(directorate);
	}
	
	public List<Directorate> searchDirectorates(String keyword, int offset) {
		return directorateRepository.searchDirectorates(keyword, offset);
	}
	
	public void delete(Long id) {
		directorateRepository.deleteById(id);
	}
	
	public void fireDirector(String name) {
		Directorate directorate = directorateRepository.findByName(name);
		directorate.setDirector(null);
		directorateRepository.save(directorate);
	}
	
	public List<Directorate> getAllDirectorates(String offset) {
		return directorateRepository.find3(Integer.parseInt(offset)).stream()
                .collect(Collectors.toUnmodifiableList());
	}
	
	public int getDirectorateCount() {
		return directorateRepository.getDirectorateCount();
	}
	
	public int getFilteredDirectorateCount(String keyword) {
		return directorateRepository.getFilteredDirectorateCount(keyword);
	}
	
	public Directorate getDirectorateByName(String name) {
		return directorateRepository.findByName(name);
	}
	
	public void setDirector(Directorate dir, User user) {
		dir.setDirector(user);
		directorateRepository.save(dir);
	}
	
	public boolean validateDirectorate(String name) {
		return directorateRepository.existsByName(name);
	}
	
}
