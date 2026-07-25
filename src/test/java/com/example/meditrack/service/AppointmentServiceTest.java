package com.example.meditrack.service;

import com.example.meditrack.model.Appointment;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.NoSuchElementException;

public class AppointmentServiceTest {

    @Test
    public void getValidAppointments_debeEmitirSoloLasTresValidas() {
        // Arrange
        AppointmentService service = new AppointmentService();

        // Act
        Flux<Appointment> flujo = service.getValidAppointments();

        // Assert
        StepVerifier.create(flujo)
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    public void getValidAppointments_cuandoTodasSonInvalidas_debeEmitirSoloLaCitaGenerica() {
        // Arrange: se arma manualmente un flujo con citas que NO cumplen la regla de negocio
        // (mismo caso que maneja el service, pero aislado para probar solo el comportamiento de defaultIfEmpty)
        Appointment citaGenerica = new Appointment("A-000", "Cita Generica", "General", 0.0, List.of());
        Flux<Appointment> flujoInvalidas = Flux.just(
                new Appointment("B1", "Paciente Uno", "Cardiologia", 0.0, List.of("correo@espe.edu.ec")), // costo no > 0
                new Appointment("B2", "Paciente Dos", "Pediatria", 20.0, List.of()) // sin correos
        );

        // Act: se aplica la misma cadena de operadores que usa el service
        Flux<Appointment> flujo = flujoInvalidas
                .filter(a -> a.getCostUsd() != null && a.getCostUsd() > 0
                        && a.getNotifyEmails() != null && !a.getNotifyEmails().isEmpty())
                .defaultIfEmpty(citaGenerica);

        // Assert: el filtro deja el flujo vacio, por lo que defaultIfEmpty debe emitir una unica cita generica
        StepVerifier.create(flujo)
                .expectNextMatches(cita -> "A-000".equals(cita.getId()))
                .verifyComplete();
    }

    @Test
    public void findById_conIdExistente_debeEmitirLaCitaCorrespondiente() {
        // Arrange
        AppointmentService service = new AppointmentService();

        // Act
        Mono<Appointment> resultado = service.findById("A1");

        // Assert
        StepVerifier.create(resultado)
                .expectNextMatches(cita -> "A1".equals(cita.getId()))
                .verifyComplete();
    }

    @Test
    public void findById_conIdInexistente_debeTerminarEnError() {
        // Arrange
        AppointmentService service = new AppointmentService();

        // Act
        Mono<Appointment> resultado = service.findById("NO-EXISTE");

        // Assert
        StepVerifier.create(resultado)
                .verifyError(NoSuchElementException.class);
    }
}
