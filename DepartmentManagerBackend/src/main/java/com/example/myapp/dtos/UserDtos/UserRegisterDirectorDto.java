package com.example.myapp.dtos.UserDtos;

import java.util.Set;

import com.example.myapp.dtos.GetRoleInfoDto;
import com.example.myapp.entities.Directorate;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class UserRegisterDirectorDto {
	
	@NotNull
    private String name;

	@NotNull
    private String surname;

	@NotNull
    private String age;

	@NotNull
    private String UCN;
    
	@NotNull
    private String username;
    
	@NotNull
    private String password;
	
	@NotNull
	private String directorate;
	
	private Set<GetRoleInfoDto> roles;
}