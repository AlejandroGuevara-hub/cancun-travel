package com.cancuntravel.app;
import java.time.LocalDate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
@Service
public class TravelService {
	private final TripRepository trips; private final ReservationRepository reservations;
	public TravelService(TripRepository trips, ReservationRepository reservations){this.trips=trips;this.reservations=reservations;}
	@Transactional public boolean reserve(Long id, String username) {
		Trip trip=trips.findForUpdate(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		if (trip.getAvailable() < 1 || trip.getStartDate().isBefore(LocalDate.now())) return false;
		trip.setAvailable(trip.getAvailable()-1); reservations.save(new Reservation(trip, username)); return true;
	}
}
