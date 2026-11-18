package com.cancuntravel.app;
import java.time.LocalDateTime;
import jakarta.persistence.*;
@Entity
public class Reservation {
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
	@ManyToOne(optional=false, fetch=FetchType.EAGER) private Trip trip;
	@Column(nullable=false) private String username;
	@Column(nullable=false) private LocalDateTime createdAt;
	protected Reservation() {}
	public Reservation(Trip trip, String username) { this.trip=trip; this.username=username; this.createdAt=LocalDateTime.now(); }
	public Long getId(){return id;} public Trip getTrip(){return trip;} public String getUsername(){return username;} public LocalDateTime getCreatedAt(){return createdAt;}
}
