package com.cancuntravel.app;
import java.util.Optional;
import java.util.List;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
public interface TripRepository extends JpaRepository<Trip, Long> {
	@Query("select t from Trip t where lower(t.name) like lower(concat('%', :query, '%')) or lower(t.destination) like lower(concat('%', :query, '%'))")
	List<Trip> search(@Param("query") String query);
	@Lock(LockModeType.PESSIMISTIC_WRITE) @Query("select t from Trip t where t.id = :id") Optional<Trip> findForUpdate(@Param("id") Long id);
}
