package com.cancuntravel.app;
import java.time.LocalDateTime;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;
@Entity
public class TravelEvent {
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
	@NotBlank(message="Indica el evento") @Size(max=200) @Column(nullable=false) private String description;
	@NotNull(message="Indica la fecha y hora") @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME) @Column(nullable=false) private LocalDateTime eventAt;
	@Column(nullable=false) private String guide;
	public Long getId(){return id;} public String getDescription(){return description;} public void setDescription(String v){description=v;}
	public LocalDateTime getEventAt(){return eventAt;} public void setEventAt(LocalDateTime v){eventAt=v;}
	public String getGuide(){return guide;} public void setGuide(String v){guide=v;}
}
