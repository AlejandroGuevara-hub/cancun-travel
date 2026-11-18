package com.cancuntravel.app;

import org.springframework.http.HttpMethod;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http.authorizeHttpRequests(auth -> auth
				.requestMatchers("/login", "/welcome", "/css/**", "/images/**").permitAll()
				.requestMatchers(HttpMethod.GET, "/trips", "/trips/*").permitAll()
				.requestMatchers("/admin/**").hasRole("ADMINISTRADOR")
				.requestMatchers("/manage/**").hasAnyRole("ADMINISTRADOR", "DUENO")
				.requestMatchers("/reservations/**", "/reservations").hasRole("USUARIO")
				.requestMatchers(HttpMethod.POST, "/budgets/*/decision").hasRole("DUENO")
				.requestMatchers(HttpMethod.POST, "/budgets").hasRole("EMPLEADO")
				.requestMatchers("/budgets/new").hasRole("EMPLEADO")
				.requestMatchers("/budgets/**", "/budgets").hasAnyRole("EMPLEADO", "DUENO")
				.requestMatchers("/events/**", "/events").hasRole("GUIA")
				.anyRequest().authenticated())
			.formLogin(login -> login.loginPage("/login").defaultSuccessUrl("/", true).permitAll())
			.logout(logout -> logout.logoutSuccessUrl("/login?logout"))
			.exceptionHandling(errors -> errors.accessDeniedPage("/access-denied"))
			.build();
	}
	@Bean
	PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }
	@Bean
	UserDetailsService userDetailsService(UserRepository users) {
		return username -> users.findByUsername(username)
			.map(user -> User.withUsername(user.getUsername()).password(user.getPassword())
				.roles(user.getRole().name()).disabled(!user.isEnabled()).build())
			.orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
	}
}
