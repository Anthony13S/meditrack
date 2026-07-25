package com.example.meditrack.controller;

import com.example.meditrack.model.Appointment;
import com.example.meditrack.service.AppointmentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    // Ninguna firma bloqueante: se retorna directamente el Flux del servicio
    @GetMapping
    public Flux<Appointment> getAppointments() {
        return appointmentService.getValidAppointments();
    }

    // Ninguna firma bloqueante: se retorna directamente el Mono del servicio
    @GetMapping("/{id}")
    public Mono<Appointment> getAppointmentById(@PathVariable String id) {
        return appointmentService.findById(id);
    }
}
