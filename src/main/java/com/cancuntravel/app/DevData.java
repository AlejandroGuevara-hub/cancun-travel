package com.cancuntravel.app;

import java.util.Locale;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Profile("dev")
public class DevData {
	@Bean
	CommandLineRunner seedUsers(UserRepository users, PasswordEncoder encoder, TripRepository trips, BudgetRepository budgets, TravelEventRepository events) {
		return args -> {
			for (Role role : Role.values()) {
				String name = role.name().toLowerCase(Locale.ROOT);
				if (!users.existsByUsername(name)) users.save(new AppUser(name, encoder.encode("Cancun2026!"), role));
			}
			if (budgets.count() == 0) {
				Budget budget = new Budget(); budget.setConcept("Transporte para excursi?n a cenotes"); budget.setAmount(new BigDecimal("3200.00")); budget.setCreatedBy("empleado"); budgets.save(budget);
			}
			if (events.count() == 0) {
				TravelEvent event = new TravelEvent(); event.setDescription("Salida de ejemplo hacia Isla Mujeres"); event.setEventAt(LocalDateTime.now().plusDays(30).withSecond(0).withNano(0)); event.setGuide("guia"); events.save(event);
			}
			if (trips.count() == 0) {
				String[][] samples = {{"Escapada Caribe", "Canc?n", "Playas de arena blanca, aguas turquesas y tiempo para descubrir la zona hotelera.", "1850.00"}, {"Aventura Maya", "Chich?n Itz? y cenotes", "Historia maya, sabores locales y un refrescante ba?o en un cenote.", "2400.00"}, {"Isla Mujeres", "Isla Mujeres", "Navega el Caribe y disfruta un d?a de playa en una isla inolvidable.", "1300.00"}};
				for (int i = 0; i < samples.length; i++) {
					Trip trip = new Trip(); trip.setName(samples[i][0]); trip.setDestination(samples[i][1]); trip.setDescription(samples[i][2]);
					trip.setPrice(new BigDecimal(samples[i][3])); trip.setStartDate(LocalDate.now().plusDays(30 + i * 15)); trip.setEndDate(trip.getStartDate().plusDays(3)); trip.setAvailable(12); trips.save(trip);
				}
			}
		};
	}
}
