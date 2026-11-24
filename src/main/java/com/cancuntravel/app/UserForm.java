package com.cancuntravel.app;

import jakarta.validation.constraints.*;

public class UserForm {
	@NotBlank(message = "Ingrese un usuario")
	@Pattern(regexp = "[a-zA-Z0-9._-]{3,50}", message = "Use de 3 a 50 letras, números, puntos, guiones o guiones bajos")
	private String username;
	@NotBlank(message = "Ingrese una contraseña")
	@Size(min = 8, max = 72, message = "La contraseña debe tener entre 8 y 72 caracteres")
	@Pattern(regexp = "[\\x20-\\x7E]*", message = "Use caracteres ASCII imprimibles en la contraseña")
	private String password;
	@NotNull(message = "Seleccione un rol")
	private Role role;
	public String getUsername() { return username; }
	public void setUsername(String username) { this.username = username; }
	public String getPassword() { return password; }
	public void setPassword(String password) { this.password = password; }
	public Role getRole() { return role; }
	public void setRole(Role role) { this.role = role; }
}
