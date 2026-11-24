package com.cancuntravel.app;

import java.util.Optional;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<AppUser, Long> {
	List<AppUser> findByRoleAndEnabledTrue(Role role);
	Optional<AppUser> findByUsername(String username);
	boolean existsByUsername(String username);
}
