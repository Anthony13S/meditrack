package com.example.meditrack.service;

import com.example.meditrack.model.Appointment;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

/**
 * Servicio reactivo que expone las citas medicas de MediTrack.
 */
@Service
public class AppointmentService {

    /**
     * Regla de negocio: una cita es valida si el costo es mayor a 0
     * y la lista de correos a notificar no esta vacia.
     */
    private static final Predicate<Appointment> IS_VALID = appointment ->
            appointment.getCostUsd() != null
                    && appointment.getCostUsd() > 0
                    && appointment.getNotifyEmails() != null
                    && !appointment.getNotifyEmails().isEmpty();

    private static final Appointment GENERIC_APPOINTMENT = new Appointment(
            "A-000", "Cita Generica", "General", 0.0, List.of());

    private final List<Appointment> appointments;

    // Constructor: carga las 5 citas en memoria (3 validas, 2 invalidas)
    public AppointmentService() {
        this.appointments = List.of(
                new Appointment("A1", "Maria Fernanda Lopez", "Cardiologia", 45.0,
                        List.of("maria.lopez@espe.edu.ec")),
                new Appointment("A2", "Carlos Andrade", "Pediatria", 0.0, // invalida: costo no > 0
                        List.of("carlos.andrade@espe.edu.ec")),
                new Appointment("A3", "Sofia Ramirez", "Dermatologia", 60.0,
                        List.of("sofia.ramirez@espe.edu.ec", "backup@espe.edu.ec")),
                new Appointment("A4", "Jorge Paredes", "Traumatologia", 35.0,
                        List.of()), // invalida: lista de emails vacia
                new Appointment("A5", "Anthony Leon", "Neurologia", 80.0,
                        List.of("asleon7@espe.edu.ec"))
        );
    }

    /**
     * Devuelve el flujo de citas validas, transformadas y con un valor por defecto
     * si el filtrado deja el flujo vacio.
     */
    public Flux<Appointment> getValidAppointments() {
        return Flux.fromIterable(appointments)
                // filter: descarta las citas que no cumplen la regla de negocio (costo <= 0 o sin correos)
                .filter(IS_VALID)
                // map: transforma cada cita valida, normalizando la especialidad a mayusculas
                .map(a -> new Appointment(a.getId(), a.getPatientName(), a.getSpecialty().toUpperCase(),
                        a.getCostUsd(), a.getNotifyEmails()))
                // defaultIfEmpty: si el filtro descarto todas las citas, se emite una cita generica
                // para que el flujo nunca quede vacio
                .defaultIfEmpty(GENERIC_APPOINTMENT);
    }

    /**
     * Busca una cita por id dentro del flujo original (incluye validas e invalidas).
     * Si no existe, el Mono termina en error en vez de bloquear con block() o un if.
     */
    public Mono<Appointment> findById(String id) {
        return Flux.fromIterable(appointments)
                .filter(a -> a.getId().equals(id))
                .next()
                // switchIfEmpty: si next() no encontro ninguna coincidencia, se propaga un error reactivo
                .switchIfEmpty(Mono.error(new NoSuchElementException("Cita no encontrada: id=" + id)));
    }
}
