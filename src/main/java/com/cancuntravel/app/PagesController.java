package com.cancuntravel.app;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.security.core.Authentication;

@Controller
public class PagesController {
	@GetMapping("/login") String login() { return "login"; }
	@GetMapping("/access-denied") String denied() { return "access-denied"; }
	@GetMapping("/") ModelAndView home(Authentication auth) {
		String role = auth.getAuthorities().iterator().next().getAuthority().substring(5);
		return new ModelAndView("home").addObject("role", role).addObject("username", auth.getName());
	}
}
