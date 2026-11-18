package com.cancuntravel.app;

import java.math.BigDecimal;
import java.time.LocalDate;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class Trip {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
	@NotBlank(message="Indica el nombre") @Size(max=100) @Column(nullable=false) private String name;
	@NotBlank(message="Indica el destino") @Size(max=100) @Column(nullable=false) private String destination;
	@NotBlank(message="Agrega una descripci?n") @Size(max=1000) @Column(nullable=false, length=1000) private String description;
	@NotNull(message="Indica la fecha de salida") @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) @Column(nullable=false) private LocalDate startDate;
	@NotNull(message="Indica la fecha de regreso") @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) @Column(nullable=false) private LocalDate endDate;
	@NotNull(message="Indica el precio") @DecimalMin(value="0.01", message="El precio debe ser mayor a cero") @Digits(integer=9, fraction=2) @Column(nullable=false, precision=11, scale=2) private BigDecimal price;
	@NotNull(message="Indica los cupos") @Min(value=0, message="Los cupos no pueden ser negativos") @Max(10000) @Column(nullable=false) private Integer available;
	@AssertTrue(message="La fecha de regreso debe ser igual o posterior a la salida") public boolean isDatesValid() { return startDate == null || endDate == null || !endDate.isBefore(startDate); }
	public Long getId(){return id;} public String getName(){return name;} public void setName(String v){name=v;}
	public String getDestination(){return destination;} public void setDestination(String v){destination=v;}
	public String getDescription(){return description;} public void setDescription(String v){description=v;}
	public LocalDate getStartDate(){return startDate;} public void setStartDate(LocalDate v){startDate=v;}
	public LocalDate getEndDate(){return endDate;} public void setEndDate(LocalDate v){endDate=v;}
	public BigDecimal getPrice(){return price;} public void setPrice(BigDecimal v){price=v;}
	public Integer getAvailable(){return available;} public void setAvailable(Integer v){available=v;}
}
