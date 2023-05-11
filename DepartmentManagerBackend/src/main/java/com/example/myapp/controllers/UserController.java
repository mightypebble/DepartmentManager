package com.example.myapp.controllers;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.myapp.dtos.DepartmentDto;
import com.example.myapp.dtos.UserDtos.UserLoginDto;
import com.example.myapp.dtos.UserDtos.UserRegisterDirectorDto;
import com.example.myapp.dtos.UserDtos.UserRegisterDto;
import com.example.myapp.entities.Department;
import com.example.myapp.entities.User;
import com.example.myapp.repositories.DepartmentRepository;
import com.example.myapp.repositories.UserRepository;
import com.example.myapp.services.DirectorateService;
import com.example.myapp.services.UserService;

@CrossOrigin("http://localhost:4200")
@RestController
public class UserController {
	
	private final UserRepository userRepository;
	private final DirectorateService directorateService;
	private final UserService userService;
	private final DepartmentRepository departmentRepository;
	
	public UserController(UserRepository userRepository, UserService userService, DepartmentRepository departmentRepository, 
			DirectorateService directorateService) {
		this.userRepository = userRepository;
		this.userService = userService;
		this.departmentRepository = departmentRepository;
		this.directorateService = directorateService;
	}
	
	@RequestMapping("directorate/{dir}/department/{name}/{offset}")
	public List<User> UserIndex(@PathVariable String offset, @PathVariable String name) {
		Department department = departmentRepository.findByName(name);
		List<User> users = userService.getAllUsers(offset, department.getId());
		
		return users;
	}

	@RequestMapping("/count-users/{name}")
	public int countUsers(@PathVariable String name) {
		Department department = departmentRepository.findByName(name);
		
		return userService.getUserCount(department.getId());
	}
	
	@RequestMapping("/count-filtered-users/{keyword}/{name}")
	public int countFilteredUsers(@PathVariable String keyword, @PathVariable String name) {
		Department department = departmentRepository.findByName(name);
		
		return userService.getFilteredUserCount(keyword, department.getId());
	}
	
	@RequestMapping(value = "/edit-user/{name}", method = RequestMethod.POST)
	public void editUser(@RequestBody UserRegisterDto userToEdit, @PathVariable String name) {
		User user = userService.getUserByName(name);
		
		userService.update(userToEdit, user);
	}
	
	@RequestMapping(value = "/search-users/{keyword}/{offset}/{dir}", method = RequestMethod.GET)
	public List<User> searchUsers(@PathVariable String keyword, @PathVariable int offset, 
			@PathVariable String dir) {
		Department department = departmentRepository.findByName(dir);
		List<User> filteredUsers = userService.searchUsers(keyword, offset, department.getId());
		
		return filteredUsers;
	}
	
	@RequestMapping(value = "/promote-user/", method = RequestMethod.POST)
	public void promote(@RequestBody Long id) {
		userService.promote(id);
	}
	
	@RequestMapping(value = "/demote-user/", method = RequestMethod.POST)
	public void demote(@RequestBody Long id) {
		userService.demote(id);
	}
	
	@RequestMapping(value = "/delete-user/{id}", method = RequestMethod.DELETE)
	public void deleteUser(@PathVariable String id) {
		userService.delete(Long.valueOf(id));
	}
	
	@RequestMapping(value = "/fire-director/{directorate}/{id}", method = RequestMethod.DELETE)
	public void deleteUser(@PathVariable String directorate, @PathVariable String id) {
		directorateService.fireDirector(directorate);
		userService.delete(Long.valueOf(id));
	}
	
	@RequestMapping(value = "/unblock-users", method = RequestMethod.POST)
	public void unblockUsersIp() {
		userService.unblockAllIp();
	}
	
	@RequestMapping(value = "/unblock-user/{id}", method = RequestMethod.POST)
	public void unblockUser(@PathVariable Long id) {
		userService.unblockUser(id);
	}
	
	@RequestMapping("validate-username/{username}")
	public boolean validateUsername(@PathVariable String username) {
		return userService.validateUsername(username);
	}
	
	@RequestMapping("validate-ucn/{ucn}")
	public boolean validateName(@PathVariable String ucn) {
		return userService.validateUCN(ucn);
	}
	
	
}
