package com.example.meditrack.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Modelo inmutable que representa una cita medica de MediTrack.
 *
 * Requisitos cumplidos:
 * - Clase final, todos los atributos son 'final'.
 * - No existen setters.
 * - La lista de notifyEmails se copia defensivamente al entrar (constructor)
 *   y al salir (getter), evitando que una referencia externa mute el estado interno.
 */
public final class Appointment {

    private final String id;
    private final String patientName;
    private final String specialty;
    private final Double costUsd;
    private final List<String> notifyEmails;

    public Appointment(String id, String patientName, String specialty, Double costUsd, List<String> notifyEmails) {
        this.id = id;
        this.patientName = patientName;
        this.specialty = specialty;
        this.costUsd = costUsd;
        // Copia defensiva de entrada: si viene null se guarda una lista vacia inmutable,
        // caso contrario se copia el contenido para que la lista original no pueda alterar el estado interno
        this.notifyEmails = (notifyEmails == null)
                ? Collections.emptyList()
                : Collections.unmodifiableList(new ArrayList<>(notifyEmails));
    }

    public String getId() {
        return id;
    }

    public String getPatientName() {
        return patientName;
    }

    public String getSpecialty() {
        return specialty;
    }

    public Double getCostUsd() {
        return costUsd;
    }

    public List<String> getNotifyEmails() {
        // Copia defensiva de salida: se retorna una nueva lista para que
        // nadie pueda modificar la interna a traves de la referencia devuelta
        return new ArrayList<>(notifyEmails);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Appointment)) return false;
        Appointment that = (Appointment) o;
        return Objects.equals(id, that.id)
                && Objects.equals(patientName, that.patientName)
                && Objects.equals(specialty, that.specialty)
                && Objects.equals(costUsd, that.costUsd)
                && Objects.equals(notifyEmails, that.notifyEmails);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, patientName, specialty, costUsd, notifyEmails);
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "id='" + id + '\'' +
                ", patientName='" + patientName + '\'' +
                ", specialty='" + specialty + '\'' +
                ", costUsd=" + costUsd +
                ", notifyEmails=" + notifyEmails +
                '}';
    }
}
