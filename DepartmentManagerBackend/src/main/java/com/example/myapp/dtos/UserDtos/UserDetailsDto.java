package com.example.myapp.dtos.UserDtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsDto {
	
    private String name;

    private String surname;

    private String age;

    private String UCN;

    private String position;
    
    private String username;
}
