package com.cancuntravel.app;
import java.time.LocalDate;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class TravelController {
	private final TripRepository trips; private final ReservationRepository reservations; private final BudgetRepository budgets; private final TravelEventRepository events; private final TravelService service;
	public TravelController(TripRepository trips, ReservationRepository reservations, BudgetRepository budgets, TravelEventRepository events, TravelService service){this.trips=trips;this.reservations=reservations;this.budgets=budgets;this.events=events;this.service=service;}
	@InitBinder("trip") void tripBinder(WebDataBinder b){b.setAllowedFields("name","destination","description","startDate","endDate","price","available");}
	@InitBinder("budget") void budgetBinder(WebDataBinder b){b.setAllowedFields("concept","amount");}
	@InitBinder("event") void eventBinder(WebDataBinder b){b.setAllowedFields("description","eventAt");}
	private Trip trip(Long id){return trips.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));}
	@GetMapping("/welcome") String welcome(Model m){m.addAttribute("trips",trips.findAll());return "welcome";}
	@GetMapping("/trips") String list(Model m){m.addAttribute("trips",trips.findAll()); return "trips";}
	@GetMapping("/trips/{id}") String detail(@PathVariable Long id, Model m, Authentication auth){m.addAttribute("trip",trip(id));m.addAttribute("today",LocalDate.now());m.addAttribute("role",auth == null ? "" : auth.getAuthorities().iterator().next().getAuthority());return "trip-detail";}
	@GetMapping("/manage/trips/new") String newTrip(Model m){m.addAttribute("trip",new Trip());return "trip-form";}
	@GetMapping("/manage/trips/{id}/edit") String edit(@PathVariable Long id,Model m){m.addAttribute("trip",trip(id));return "trip-form";}
	@PostMapping("/manage/trips") String create(@Valid @ModelAttribute("trip") Trip form, BindingResult errors, RedirectAttributes flash){if(errors.hasErrors())return "trip-form"; trips.save(form);flash.addFlashAttribute("message","Viaje creado correctamente.");return "redirect:/trips";}
	@PostMapping("/manage/trips/{id}") String update(@PathVariable Long id,@Valid @ModelAttribute("trip") Trip form, BindingResult errors, Model m, RedirectAttributes flash){Trip current=trip(id);if(errors.hasErrors()){m.addAttribute("editId",id);return "trip-form";} current.setName(form.getName());current.setDestination(form.getDestination());current.setDescription(form.getDescription());current.setStartDate(form.getStartDate());current.setEndDate(form.getEndDate());current.setPrice(form.getPrice());current.setAvailable(form.getAvailable());trips.save(current);flash.addFlashAttribute("message","Viaje actualizado.");return "redirect:/trips/"+id;}
	@PostMapping("/reservations/{id}") String reserve(@PathVariable Long id,Authentication auth,RedirectAttributes flash){boolean ok=service.reserve(id,auth.getName());flash.addFlashAttribute("message",ok ? "Reserva de prueba registrada. No se ha realizado ning?n pago." : "No hay cupos disponibles o la fecha ya pas?.");return "redirect:/trips/"+id;}
	@GetMapping("/reservations") String reservations(Authentication auth,Model m){m.addAttribute("reservations",reservations.findByUsernameOrderByCreatedAtDesc(auth.getName()));return "reservations";}
	@GetMapping("/budgets") String budgets(Model m,Authentication auth){m.addAttribute("budgets",budgets.findAll());m.addAttribute("role",auth.getAuthorities().iterator().next().getAuthority());return "budgets";}
	@GetMapping("/budgets/new") String newBudget(Model m){m.addAttribute("budget",new Budget());return "budget-form";}
	@PostMapping("/budgets") String createBudget(@Valid @ModelAttribute("budget") Budget budget,BindingResult errors,Authentication auth,RedirectAttributes flash){if(errors.hasErrors())return "budget-form";budget.setCreatedBy(auth.getName());budget.setStatus(BudgetStatus.PENDIENTE);budgets.save(budget);flash.addFlashAttribute("message","Presupuesto enviado para revisi?n.");return "redirect:/budgets";}
	@PostMapping("/budgets/{id}/decision") String decision(@PathVariable Long id,@RequestParam BudgetStatus status,RedirectAttributes flash){if(status == BudgetStatus.PENDIENTE)throw new ResponseStatusException(HttpStatus.BAD_REQUEST);Budget b=budgets.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));if(b.getStatus()!=BudgetStatus.PENDIENTE)throw new ResponseStatusException(HttpStatus.CONFLICT,"El presupuesto ya fue revisado");b.setStatus(status);budgets.save(b);flash.addFlashAttribute("message","Presupuesto revisado.");return "redirect:/budgets";}
	@GetMapping("/events") String events(Model m){m.addAttribute("events",events.findAll());m.addAttribute("event",new TravelEvent());return "events";}
	@PostMapping("/events") String createEvent(@Valid @ModelAttribute("event") TravelEvent event,BindingResult errors,Authentication auth,Model m,RedirectAttributes flash){if(errors.hasErrors()){m.addAttribute("events",events.findAll());return "events";}event.setGuide(auth.getName());events.save(event);flash.addFlashAttribute("message","Evento registrado.");return "redirect:/events";}
}
