package com.cancuntravel.app;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class TravelWorkflowTests {
	@Autowired MockMvc mvc;
	@Autowired TripRepository trips;
	@Autowired UserRepository users;
	@Autowired ReservationRepository reservations;
	@Autowired BudgetRepository budgets;
	@Autowired TravelEventRepository events;
	private Trip sample() {
		Trip t=new Trip(); t.setName("Prueba Caribe"); t.setDestination("Canc?n"); t.setDescription("Un viaje de prueba");
		t.setStartDate(LocalDate.now().plusDays(30)); t.setEndDate(LocalDate.now().plusDays(33)); t.setPrice(new BigDecimal("500.00")); t.setAvailable(1); return trips.save(t);
	}
	@Test void publicCatalogAndReservationAvailability() throws Exception {
		Trip t=sample();
		mvc.perform(get("/welcome")).andExpect(status().isOk());
		mvc.perform(get("/trips/"+t.getId())).andExpect(status().isOk());
		mvc.perform(post("/reservations/"+t.getId()).with(user("empleado").roles("EMPLEADO")).with(csrf())).andExpect(status().isForbidden());
		mvc.perform(post("/reservations/"+t.getId()).with(user("visitante").roles("USUARIO")).with(csrf())).andExpect(status().is3xxRedirection());
		mvc.perform(post("/reservations/"+t.getId()).with(user("visitante").roles("USUARIO")).with(csrf())).andExpect(status().is3xxRedirection());
		assertThat(trips.findById(t.getId()).orElseThrow().getAvailable()).isZero();
		mvc.perform(post("/manage/trips/"+t.getId()).with(user("owner").roles("DUENO")).with(csrf()).param("version", t.getVersion().toString()).param("name","Antiguo").param("destination","Canc?n").param("description","Viaje").param("startDate","2035-01-01").param("endDate","2035-01-10").param("price","100").param("available","1")).andExpect(status().isConflict());
		assertThat(reservations.findByUsernameOrderByCreatedAtDesc("visitante")).hasSize(1);
	}
	@Test void rolePagesAndEditing() throws Exception {
		Trip t = sample();
		mvc.perform(get("/manage/trips/"+t.getId()+"/edit").with(user("d").roles("DUENO"))).andExpect(status().isOk());
		mvc.perform(post("/manage/trips/"+t.getId()).with(user("d").roles("DUENO")).with(csrf()).param("version",t.getVersion().toString()).param("name","Actualizado").param("destination","Canc?n").param("description","Un viaje").param("startDate","2035-01-01").param("endDate","2035-01-10").param("price","100").param("available","2")).andExpect(status().is3xxRedirection());
		assertThat(trips.findById(t.getId()).orElseThrow().getName()).isEqualTo("Actualizado");
		mvc.perform(get("/budgets/new").with(user("e").roles("EMPLEADO"))).andExpect(status().isOk());
		mvc.perform(get("/budgets").with(user("d").roles("DUENO"))).andExpect(status().isOk());
		mvc.perform(get("/events").with(user("g").roles("GUIA"))).andExpect(status().isOk());
		mvc.perform(get("/reservations").with(user("u").roles("USUARIO"))).andExpect(status().isOk());
	}
	@Test void searchAndOrganizeTrip() throws Exception {
		Trip trip = sample();
		AppUser guide = users.save(new AppUser("guide_test", "encoded", Role.GUIA));
		mvc.perform(get("/trips").param("q", "prueba caribe")).andExpect(status().isOk()).andExpect(content().string(org.hamcrest.Matchers.containsString("Prueba Caribe")));
		mvc.perform(get("/organization/"+trip.getId()).with(user("e").roles("EMPLEADO"))).andExpect(status().isOk());
		mvc.perform(post("/organization/"+trip.getId()).with(user("e").roles("EMPLEADO")).with(csrf()).param("guideId",guide.getId().toString()).param("notes","Recoger en hotel" )).andExpect(status().is3xxRedirection());
		assertThat(trips.findById(trip.getId()).orElseThrow().getGuide().getId()).isEqualTo(guide.getId());
		mvc.perform(post("/organization/"+trip.getId()).with(user("u").roles("USUARIO")).with(csrf())).andExpect(status().isForbidden());
		guide.setEnabled(false); users.save(guide);
		mvc.perform(post("/organization/"+trip.getId()).with(user("e").roles("EMPLEADO")).with(csrf()).param("guideId",guide.getId().toString())).andExpect(status().isOk()).andExpect(model().attributeExists("error"));
	}
	@Test void tripPermissionsAndValidation() throws Exception {
		mvc.perform(post("/manage/trips").with(user("guia").roles("GUIA")).with(csrf())).andExpect(status().isForbidden());
		mvc.perform(post("/manage/trips").with(user("owner").roles("DUENO")).with(csrf()).param("name","Viaje").param("destination","Canc?n").param("description","Un viaje").param("startDate","2035-01-10").param("endDate","2035-01-01").param("price","100").param("available","2")).andExpect(status().isOk()).andExpect(model().attributeHasErrors("trip"));
		mvc.perform(post("/manage/trips").with(user("owner").roles("DUENO")).with(csrf()).param("name","Viaje nuevo").param("destination","Canc?n").param("description","Un viaje").param("startDate","2035-01-01").param("endDate","2035-01-10").param("price","100").param("available","2")).andExpect(status().is3xxRedirection());
		assertThat(trips.findAll()).anyMatch(t -> t.getName().equals("Viaje nuevo"));
	}
	@Test void budgetsAndEventsAreRoleProtected() throws Exception {
		mvc.perform(post("/budgets").with(user("e").roles("EMPLEADO")).with(csrf()).param("concept","Transporte").param("amount","500")).andExpect(status().is3xxRedirection());
		Budget b=budgets.findAll().stream().filter(x -> x.getConcept().equals("Transporte")).findFirst().orElseThrow();
		mvc.perform(post("/budgets/"+b.getId()+"/decision").with(user("e").roles("EMPLEADO")).with(csrf()).param("status","APROBADO")).andExpect(status().isForbidden());
		mvc.perform(post("/budgets/"+b.getId()+"/decision").with(user("d").roles("DUENO")).with(csrf()).param("status","APROBADO")).andExpect(status().isConflict());
		mvc.perform(post("/budgets/"+b.getId()+"/submit").with(user("a").roles("ADMINISTRADOR")).with(csrf())).andExpect(status().is3xxRedirection());
		mvc.perform(post("/budgets/"+b.getId()+"/decision").with(user("d").roles("DUENO")).with(csrf()).param("status","APROBADO")).andExpect(status().is3xxRedirection());
		assertThat(budgets.findById(b.getId()).orElseThrow().getStatus()).isEqualTo(BudgetStatus.APROBADO);
		mvc.perform(post("/events").with(user("u").roles("USUARIO")).with(csrf())).andExpect(status().isForbidden());
		mvc.perform(post("/events").with(user("g").roles("GUIA")).with(csrf()).param("description","Salida al cenote").param("eventAt","2035-01-01T09:00")).andExpect(status().is3xxRedirection());
		assertThat(events.findAll()).anyMatch(e -> e.getDescription().equals("Salida al cenote"));
	}
}
