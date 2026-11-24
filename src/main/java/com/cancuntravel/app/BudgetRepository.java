package com.cancuntravel.app;
import java.util.Optional;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
public interface BudgetRepository extends JpaRepository<Budget, Long> {
	@Lock(LockModeType.PESSIMISTIC_WRITE) @Query("select b from Budget b where b.id = :id") Optional<Budget> findForUpdate(@Param("id") Long id);
}
