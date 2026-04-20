package com.stararts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@SpringBootApplication
public class RentalApp {
    public static void main(String[] args) {
        SpringApplication.run(RentalApp.class, args);
    }
}

@Entity
@Table(name = "rentals")
class Rental {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    public String customerName;
    public String customerPhone;
    public String itemName;
    public Double deposit;
    public String referredBy; 
    public String status = "Active";
    public String startTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd MMM, hh:mm a"));
}

interface RentalRepository extends JpaRepository<Rental, Long> {}

@Controller
class RentalController {
    private final RentalRepository repository;
    public RentalController(RentalRepository repository) { this.repository = repository; }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("rentals", repository.findAll());
        model.addAttribute("newRental", new Rental());
        return "dashboard";
    }

    @PostMapping("/rent")
    public String rent(@ModelAttribute Rental rental) {
        repository.save(rental);
        return "redirect:/";
    }

    @PostMapping("/return/{id}")
    public String returnItem(@PathVariable Long id) {
        Rental r = repository.findById(id).orElseThrow();
        r.status = "Closed";
        repository.save(r);
        return "redirect:/";
    }
}
