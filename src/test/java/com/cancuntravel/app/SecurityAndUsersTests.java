package com.cancuntravel.app;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityAndUsersTests {
	@Autowired MockMvc mvc;
	@Autowired UserRepository users;
	@Autowired PasswordEncoder encoder;

	@BeforeEach void setup() {
		users.deleteAll();
		users.save(new AppUser("admin", encoder.encode("password123"), Role.ADMINISTRADOR));
		users.save(new AppUser("visitor", encoder.encode("password123"), Role.USUARIO));
	}
	@Test void loginAndDisabledAccount() throws Exception {
		mvc.perform(formLogin().user("admin").password("password123")).andExpect(authenticated().withUsername("admin"));
		mvc.perform(formLogin().user("admin").password("wrong")).andExpect(unauthenticated());
		AppUser user = users.findByUsername("visitor").orElseThrow();
		user.setEnabled(false); users.save(user);
		mvc.perform(formLogin().user("visitor").password("password123")).andExpect(unauthenticated());
	}
	@Test void onlyAdminCanManageUsers() throws Exception {
		mvc.perform(get("/admin/users")).andExpect(status().is3xxRedirection());
		for (String role : new String[]{"DUENO", "EMPLEADO", "GUIA", "USUARIO"}) {
			mvc.perform(get("/admin/users").with(user("other").roles(role))).andExpect(status().isForbidden());
			mvc.perform(post("/admin/users").with(user("other").roles(role)).with(csrf())).andExpect(status().isForbidden());
			mvc.perform(get("/").with(user("other").roles(role))).andExpect(status().isOk());
		}
		mvc.perform(get("/admin/users").with(user("admin").roles("ADMINISTRADOR"))).andExpect(status().isOk());
	}
	@Test void createValidateAndChangeStatus() throws Exception {
		mvc.perform(post("/admin/users").with(user("admin").roles("ADMINISTRADOR")).with(csrf())
			.param("username", "newuser").param("password", "password123").param("role", "GUIA"))
			.andExpect(status().is3xxRedirection());
		AppUser created = users.findByUsername("newuser").orElseThrow();
		assertThat(created.getRole()).isEqualTo(Role.GUIA);
		assertThat(encoder.matches("password123", created.getPassword())).isTrue();
		mvc.perform(post("/admin/users").with(user("admin").roles("ADMINISTRADOR")).with(csrf())
			.param("username", "newuser").param("password", "short").param("role", "GUIA"))
			.andExpect(status().isOk()).andExpect(model().attributeHasFieldErrors("userForm", "username", "password"));
		mvc.perform(post("/admin/users/{id}/status", created.getId()).with(user("admin").roles("ADMINISTRADOR")).with(csrf()).param("enabled", "false"))
			.andExpect(status().is3xxRedirection());
		assertThat(users.findById(created.getId()).orElseThrow().isEnabled()).isFalse();
		mvc.perform(post("/admin/users/{id}/status", created.getId()).with(user("admin").roles("ADMINISTRADOR")).with(csrf()).param("enabled", "true"))
			.andExpect(status().is3xxRedirection());
		assertThat(users.findById(created.getId()).orElseThrow().isEnabled()).isTrue();
	}
}
