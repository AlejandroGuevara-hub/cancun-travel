package com.cancuntravel.app;

import jakarta.persistence.*;

@Entity
@Table(name = "app_users", uniqueConstraints = @UniqueConstraint(columnNames = "username"))
public class AppUser {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false, unique = true, length = 50)
	private String username;
	@Column(nullable = false)
	private String password;
	@Enumerated(EnumType.STRING) @Column(nullable = false)
	private Role role;
	@Column(nullable = false)
	private boolean enabled = true;

	protected AppUser() {}
	public AppUser(String username, String password, Role role) {
		this.username = username;
		this.password = password;
		this.role = role;
	}
	public Long getId() { return id; }
	public String getUsername() { return username; }
	public String getPassword() { return password; }
	public Role getRole() { return role; }
	public boolean isEnabled() { return enabled; }
	public void setEnabled(boolean enabled) { this.enabled = enabled; }
}
