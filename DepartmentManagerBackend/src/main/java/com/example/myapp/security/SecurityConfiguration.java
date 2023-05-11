package com.example.myapp.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.bind.annotation.CrossOrigin;

import lombok.RequiredArgsConstructor;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@CrossOrigin(origins = "/**")
public class SecurityConfiguration {
	
	private final JwtAuthenticationFilter jwtAuthFilter;
	private final AuthenticationProvider authenticationProvider;
	private final LogoutHandler logoutHandler;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	  http
	  .cors(withDefaults())
	      .csrf()
	      .disable()
	      .authorizeHttpRequests()
	      .requestMatchers("/authenticate").permitAll()
	      .requestMatchers("/add-department").hasAnyAuthority("ADMIN", "DIRECTOR")
		  .requestMatchers("/{}", "/unblock-user").hasAnyAuthority("ADMIN")
		  .requestMatchers("/directorate/{}/department/**").hasAnyAuthority("HEAD", "DIRECTOR", "ADMIN")
		  .requestMatchers("/directorate/**").hasAnyAuthority("DIRECTOR", "ADMIN")
	      .anyRequest()
	        .authenticated()
	      .and()
	        .sessionManagement()
	        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
	      .and()
	      .authenticationProvider(authenticationProvider)
	      .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
	      .logout()
	      .logoutUrl("/logout")
	      .addLogoutHandler(logoutHandler)
	      .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext());
	  return http.build();
 }
}
