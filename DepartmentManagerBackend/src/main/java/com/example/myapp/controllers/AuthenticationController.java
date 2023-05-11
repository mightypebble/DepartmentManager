package com.example.myapp.controllers;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.myapp.dtos.UserDtos.AuthenticationResponse;
import com.example.myapp.dtos.UserDtos.UserLoginDto;
import com.example.myapp.dtos.UserDtos.UserRegisterDirectorDto;
import com.example.myapp.dtos.UserDtos.UserRegisterDto;
import com.example.myapp.services.AuthenticationService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@CrossOrigin("http://localhost:4200")
@RestController
@RequiredArgsConstructor
public class AuthenticationController {

  private final AuthenticationService service;

  @PostMapping("/register")
  public ResponseEntity<AuthenticationResponse> register(
      @RequestBody UserRegisterDto request
  ) {
    return ResponseEntity.ok(service.register(request));
  }

  @PostMapping("/register-director")
  public ResponseEntity<AuthenticationResponse> registerDirector(
      @RequestBody UserRegisterDirectorDto request
  ) {
    return ResponseEntity.ok(service.registerDirector(request));
  }

  @PostMapping("/authenticate")
  public ResponseEntity<AuthenticationResponse> authenticate(
      @RequestBody UserLoginDto request
  ) {
    return ResponseEntity.ok(service.authenticate(request));
  }

  @PostMapping("/refresh-token")
  public void refreshToken(
      HttpServletRequest request,
      HttpServletResponse response
  ) throws IOException {
    service.refreshToken(request, response);
  }


}
