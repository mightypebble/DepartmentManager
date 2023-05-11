package com.example.myapp.services;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.myapp.entities.Department;
import com.example.myapp.entities.Role;
import com.example.myapp.entities.User;
import com.example.myapp.repositories.UserRepository;
import com.example.myapp.security.LoginAttemptService;
import com.example.myapp.dtos.UserDtos.*;

@EnableScheduling
@Service
public class UserService{
	
	private final UserRepository userRepository;
	private final DirectorateService directorateService;
	private final DepartmentService departmentService;
	private final PasswordEncoder passwordEncoder;
	private final LoginAttemptService loginAttemptService;
	
	public UserService(UserRepository userRepository,PasswordEncoder passwordEncoder,
			DirectorateService directorateService, DepartmentService departmentService,
			LoginAttemptService loginAttemptService) {
		this.userRepository = userRepository;
		this.directorateService = directorateService;
		this.departmentService = departmentService;
		this.passwordEncoder = passwordEncoder;
		this.loginAttemptService = loginAttemptService;
	}
	
	public void update(UserRegisterDto userRegisterDto, User user) {
		user.setName(userRegisterDto.getName());
		user.setSurname(userRegisterDto.getSurname());
		user.setAge(userRegisterDto.getAge());
		user.setUCN(userRegisterDto.getUCN());
		if (userRepository.existsByUCN(userRegisterDto.getUCN()) && user.getUCN() != userRegisterDto.getUCN()) return;
		user.setUsername(userRegisterDto.getUsername());
		user.setPassword(passwordEncoder.encode(userRegisterDto.getPassword()));
		userRepository.save(user);
	}
	
	public List<User> searchUsers(String keyword, int offset, Long id) {
		return userRepository.searchUsers(keyword, offset, id);
	}
	
	public void promote(Long id) {
		User user = userRepository.findById(id).get();
		if (user == null) return;
		user.setPosition("head");
		user.setRole(Role.valueOf("HEAD"));
		userRepository.save(user);
	}
	
	public void demote(Long id) {
		User user = userRepository.findById(id).get();
		if (user == null) return;
		user.setPosition("employee");
		user.setRole(Role.valueOf("EMPLOYEE"));
		userRepository.save(user);
	}
	
	public void delete(Long id) {
		userRepository.deleteById(id);
	}
	
	public List<User> getAllUsers(String offset, Long id) {
		return userRepository.find3(Integer.parseInt(offset), id).stream()
                .collect(Collectors.toUnmodifiableList());
	}
	
	public int getUserCount(Long id) {
		return userRepository.getUserCount(id);
	}
	
	public int getFilteredUserCount(String keyword, Long id) {
		return userRepository.getFilteredUserCount(keyword, id);
	}
	
	public User getUserByName(String name) {
		return userRepository.findByName(name);
	}

	public void unblockAllIp() {
		loginAttemptService.deleteCache();
	}
	
	public void unblockUser(Long id)  {
		User user = userRepository.findById(id).get();
		
		user.setBanExpirationDate(null);
		user.setFailedLoginAttempts(0);
		userRepository.save(user);
	}
	
	public boolean validateUsername(String username) {
		return userRepository.existsByUsername(username);
	}
	
	public boolean validateUCN(String UCN) {
		return userRepository.existsByUCN(UCN);
	}
	
	@Scheduled(fixedDelay = 1000 * 60 * 10)
	private void banChecker() {
		for (User user : userRepository.findBannedUsers()) {
			Date date = new Date();
			if (date.after(user.getBanExpirationDate())) {
				user.setFailedLoginAttempts(0);
				user.setBanExpirationDate(null);
				userRepository.save(user);
			}
		}
	}
	
}
