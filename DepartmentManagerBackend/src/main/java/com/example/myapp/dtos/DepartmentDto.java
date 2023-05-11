package com.example.myapp.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DepartmentDto {
	
	@NotNull
	private String name;
	
	@NotNull
	private String description;
	
	@NotNull
	private String directorate;
}