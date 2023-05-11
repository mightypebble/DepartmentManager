package com.example.myapp.dtos.UserDtos;

import java.util.Set;

import com.example.myapp.dtos.GetRoleInfoDto;
import com.example.myapp.entities.Department;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class UserRegisterDto {
	
	@NotNull
    private String name;

	@NotNull
    private String surname;

	@NotNull
    private String age;

	@NotNull
    private String UCN;

	@NotNull
    private String position;
    
	@NotNull
    private String username;
    
	@NotNull
    private String password;
	
	private String department;
	
	private Set<GetRoleInfoDto> roles;
}
