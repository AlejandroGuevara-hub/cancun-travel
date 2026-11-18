package com.cancuntravel.app;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@ActiveProfiles("dev")
@TestPropertySource(properties = "spring.datasource.url=jdbc:h2:mem:devseed;DB_CLOSE_DELAY=-1")
class DevDataTests {
	@Autowired UserRepository users;
	@Autowired TripRepository trips;
	@Autowired BudgetRepository budgets;
	@Autowired TravelEventRepository events;
	@Autowired PasswordEncoder encoder;
	@Test void createsDemonstrationData() {
		for (Role role : Role.values()) {
			AppUser user = users.findByUsername(role.name().toLowerCase(java.util.Locale.ROOT)).orElseThrow();
			assertThat(user.getRole()).isEqualTo(role);
			assertThat(encoder.matches("Cancun2026!", user.getPassword())).isTrue();
		}
		assertThat(trips.count()).isEqualTo(3);
		assertThat(budgets.count()).isEqualTo(1);
		assertThat(events.count()).isEqualTo(1);
	}
}
