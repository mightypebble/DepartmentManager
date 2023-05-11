package com.example.myapp.services;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.myapp.dtos.UserDtos.AuthenticationResponse;
import com.example.myapp.dtos.UserDtos.UserLoginDto;
import com.example.myapp.dtos.UserDtos.UserRegisterDirectorDto;
import com.example.myapp.dtos.UserDtos.UserRegisterDto;
import com.example.myapp.entities.Directorate;
import com.example.myapp.entities.Role;
import com.example.myapp.entities.Token;
import com.example.myapp.entities.TokenType;
import com.example.myapp.entities.User;
import com.example.myapp.repositories.DepartmentRepository;
import com.example.myapp.repositories.DirectorateRepository;
import com.example.myapp.repositories.TokenRepository;
import com.example.myapp.repositories.UserRepository;
import com.example.myapp.security.JwtService;
import com.example.myapp.security.LoginAttemptService;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
  private final UserRepository repository;
  private final DepartmentRepository departmentRepository;
  private final DirectorateRepository directorateRepository;
  private final DirectorateService directorateService;
  private final TokenRepository tokenRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;
  private final LoginAttemptService loginAttemptService;

  public AuthenticationResponse register(UserRegisterDto request) {
    var user = User.builder()
        .name(request.getName())
        .surname(request.getSurname())
        .age(request.getAge())
        .UCN(request.getUCN())
        .position(request.getPosition())
        .department(departmentRepository.findByName(request.getDepartment()))
        .username(request.getUsername())
        .password(passwordEncoder.encode(request.getPassword()))
        .role(Role.valueOf(request.getPosition().toUpperCase()))
        .failedLoginAttempts(0)
        .isBlocked(false)
        .banExpirationDate(null)
        .build();
    if (repository.existsByUCN(request.getUCN())) return null;
    var savedUser = repository.save(user);
    var jwtToken = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);
    saveUserToken(savedUser, jwtToken);
    return AuthenticationResponse.builder()
        .accessToken(jwtToken)
            .refreshToken(refreshToken)
        .build();
  }

  public AuthenticationResponse registerDirector(UserRegisterDirectorDto request) {
    var director = User.builder()
        .name(request.getName())
        .surname(request.getSurname())
        .age(request.getAge())
        .UCN(request.getUCN())
        .directorate(directorateRepository.findByName(request.getDirectorate()))
        .username(request.getUsername())
        .password(passwordEncoder.encode(request.getPassword()))
        .role(Role.DIRECTOR)
        .failedLoginAttempts(0)
        .isBlocked(false)
        .banExpirationDate(null)
        .build();
    if (repository.existsByUCN(request.getUCN())) return null;
    var savedUser = repository.save(director);
    var jwtToken = jwtService.generateToken(director);
    var refreshToken = jwtService.generateRefreshToken(director);
    saveUserToken(savedUser, jwtToken);
    directorateService.setDirector(director.getDirectorate(), director);
    return AuthenticationResponse.builder()
        .accessToken(jwtToken)
            .refreshToken(refreshToken)
        .build();
  }

  public AuthenticationResponse authenticate(UserLoginDto request) {
	  if (loginAttemptService.isBlocked(repository.findByUsername(request.getUsername())) == "temp") {
		  return AuthenticationResponse.builder()
					.route("/login")
			        .status("temp")
			        .build();
      } else if (loginAttemptService.isBlocked(repository.findByUsername(request.getUsername())) == "blocked") {
    	  return AuthenticationResponse.builder()
					.route("/login")
			        .status("blocked")
			        .build();
      }
	var user = repository.findByUsername(request.getUsername());
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            request.getUsername(),
            request.getPassword()
        )
    );
    user.setFailedLoginAttempts(0);
    var route = "";
    if (user.getRole() == Role.ADMIN) route = "/";
    else if (user.getRole() == Role.DIRECTOR) {
    	for (Directorate dir : directorateRepository.findAll()) {
    		if (dir.getDirector() == user) route = "directorate/" + dir.getName();
    	}
    }
    else if (user.getRole() == Role.HEAD) route = "directorate/" +
    user.getDepartment().getDirectorate().getName() + "/department/" + user.getDepartment().getName();
    var jwtToken = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);
    revokeAllUserTokens(user);
    saveUserToken(user, jwtToken);
    repository.save(user);
    return AuthenticationResponse.builder()
        .accessToken(jwtToken)
            .refreshToken(refreshToken)
            .route(route)
            .name(user.getName())
        .build();
  }

  private void saveUserToken(User user, String jwtToken) {
    var token = Token.builder()
        .user(user)
        .token(jwtToken)
        .tokenType(TokenType.BEARER)
        .expired(false)
        .revoked(false)
        .build();
    tokenRepository.save(token);
  }

  private void revokeAllUserTokens(User user) {
    var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
    if (validUserTokens.isEmpty())
      return;
    validUserTokens.forEach(token -> {
      token.setExpired(true);
      token.setRevoked(true);
    });
    tokenRepository.saveAll(validUserTokens);
  }

  public void refreshToken(
          HttpServletRequest request,
          HttpServletResponse response
  ) throws IOException {
    final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    final String refreshToken;
    final String username;
    if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
      return;
    }
    refreshToken = authHeader.substring(7);
    username = jwtService.extractUsername(refreshToken);
    if (username != null) {
      var user = this.repository.findByUsername(username);
      if (jwtService.isTokenValid(refreshToken, user)) {
        var accessToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, accessToken);
        var authResponse = AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
        new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
      }
    }
  }
}
