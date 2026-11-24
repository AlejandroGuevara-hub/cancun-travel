package com.cancuntravel.app;
import java.math.BigDecimal;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
@Entity
public class Budget {
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
	@NotBlank(message="Indica el concepto") @Size(max=200) @Column(nullable=false) private String concept;
	@NotNull(message="Indica el monto") @DecimalMin(value="0.01", message="El monto debe ser mayor a cero") @Digits(integer=9,fraction=2) @Column(nullable=false,precision=11,scale=2) private BigDecimal amount;
	@Column(nullable=false) private String createdBy;
	@Enumerated(EnumType.STRING) @Column(nullable=false) private BudgetStatus status = BudgetStatus.PENDIENTE_ADMIN;
	@Column private String presentedBy;
	@Column private String decidedBy;
	public String getPresentedBy(){return presentedBy;} public void setPresentedBy(String v){presentedBy=v;}
	public String getDecidedBy(){return decidedBy;} public void setDecidedBy(String v){decidedBy=v;}
	public Long getId(){return id;} public String getConcept(){return concept;} public void setConcept(String v){concept=v;}
	public BigDecimal getAmount(){return amount;} public void setAmount(BigDecimal v){amount=v;}
	public String getCreatedBy(){return createdBy;} public void setCreatedBy(String v){createdBy=v;}
	public BudgetStatus getStatus(){return status;} public void setStatus(BudgetStatus v){status=v;}
}
