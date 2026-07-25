package com.example.meditrack.model;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

public class AppointmentTest {

    @Test
    public void getters_alCrearConValores_debenDevolverLoRecibidoEnConstructor() {
        // Arrange
        List<String> emails = List.of("paciente@espe.edu.ec");
        Appointment appointment = new Appointment("A1", "Maria Lopez", "Cardiologia", 45.0, emails);

        // Act
        String id = appointment.getId();
        String patientName = appointment.getPatientName();
        String specialty = appointment.getSpecialty();
        Double cost = appointment.getCostUsd();

        // Assert
        assertEquals("A1", id);
        assertEquals("Maria Lopez", patientName);
        assertEquals("Cardiologia", specialty);
        assertEquals(45.0, cost, 0.0001);
        assertEquals(emails, appointment.getNotifyEmails());
    }

    @Test
    public void getNotifyEmails_alModificarListaOriginalDespuesDeCrear_noDebeAlterarElEstadoInterno() {
        // Arrange
        List<String> emailsOriginales = new ArrayList<>();
        emailsOriginales.add("uno@espe.edu.ec");
        emailsOriginales.add("dos@espe.edu.ec");
        Appointment appointment = new Appointment("A2", "Carlos Andrade", "Pediatria", 30.0, emailsOriginales);

        // Act: se modifica la lista original despues de construir el objeto
        emailsOriginales.add("intruso@espe.edu.ec");
        List<String> emailsInternos = appointment.getNotifyEmails();

        // Assert: el tamano interno no debe reflejar el cambio externo
        assertEquals(2, emailsInternos.size());
    }

    @Test
    public void getNotifyEmails_alLlamarloDosVeces_debeDevolverReferenciasDistintas() {
        // Arrange
        List<String> emails = List.of("paciente@espe.edu.ec");
        Appointment appointment = new Appointment("A3", "Sofia Ramirez", "Dermatologia", 60.0, emails);

        // Act
        List<String> primeraLlamada = appointment.getNotifyEmails();
        List<String> segundaLlamada = appointment.getNotifyEmails();

        // Assert: cada getter entrega una copia nueva, no la misma referencia
        assertNotSame(primeraLlamada, segundaLlamada);
    }
}
