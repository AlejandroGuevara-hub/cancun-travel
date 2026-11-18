package com.cancuntravel.app;

import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/users")
public class UserController {
	private final UserRepository users;
	private final PasswordEncoder encoder;
	public UserController(UserRepository users, PasswordEncoder encoder) { this.users = users; this.encoder = encoder; }
	@GetMapping
	String list(Model model) {
		model.addAttribute("users", users.findAll());
		return "users";
	}
	@GetMapping("/new")
	String createForm(Model model) {
		model.addAttribute("userForm", new UserForm());
		model.addAttribute("roles", Role.values());
		return "user-form";
	}
	@PostMapping
	String create(@Valid @ModelAttribute UserForm userForm, BindingResult result, Model model, RedirectAttributes flash) {
		if (!result.hasFieldErrors("username") && users.existsByUsername(userForm.getUsername()))
			result.rejectValue("username", "duplicate", "Este usuario ya existe");
		if (!result.hasErrors()) {
			try {
				users.saveAndFlush(new AppUser(userForm.getUsername(), encoder.encode(userForm.getPassword()), userForm.getRole()));
				flash.addFlashAttribute("message", "Usuario creado correctamente");
				return "redirect:/admin/users";
			} catch (DataIntegrityViolationException ex) {
				result.rejectValue("username", "duplicate", "Este usuario ya existe");
			}
		}
		userForm.setPassword(null);
		model.addAttribute("roles", Role.values());
		return "user-form";
	}
	@PostMapping("/{id}/status")
	String status(@PathVariable Long id, @RequestParam boolean enabled, Authentication auth, RedirectAttributes flash) {
		AppUser user = users.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		if (!enabled && user.getUsername().equals(auth.getName())) {
			flash.addFlashAttribute("message", "No puede desactivar su propia cuenta");
		} else {
			user.setEnabled(enabled);
			users.save(user);
			flash.addFlashAttribute("message", "Estado actualizado correctamente");
		}
		return "redirect:/admin/users";
	}
}
