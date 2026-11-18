package com.cancuntravel.app;
import java.util.Optional;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
public interface TripRepository extends JpaRepository<Trip, Long> {
	@Lock(LockModeType.PESSIMISTIC_WRITE) @Query("select t from Trip t where t.id = :id") Optional<Trip> findForUpdate(@Param("id") Long id);
}
