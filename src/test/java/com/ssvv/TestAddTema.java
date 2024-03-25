package com.ssvv;

import com.ssvv.domain.Tema;
import com.ssvv.repository.TemaXMLRepo;
import com.ssvv.service.Service;
import com.ssvv.validation.TemaValidator;
import com.ssvv.validation.ValidationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestAddTema {
    private TemaXMLRepo temaFileRepository;
    private TemaValidator temaValidator;
    private Service service;

    @BeforeAll
    static void createXML() {
        File xml = new File("fisiere/temeTest.xml");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(xml))) {
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
                    "<inbox>\n" +
                    "\n" +
                    "</inbox>");
            writer.flush();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @BeforeEach
    void setUp() {
        this.temaFileRepository = new TemaXMLRepo("fisiere/temeTest.xml");
        this.temaValidator = new TemaValidator();
        this.service = new Service(null, null, this.temaFileRepository, this.temaValidator, null, null);
    }

    @AfterEach
    void tearDown() {
        new File("fisiere/temeTest.xml").delete();
    }

    @Test
    void testAddTemaEmptyId() {
        Tema newTema1 = new Tema("1", "desc", 10, 9);
        Tema newTema2 = new Tema("", "desc", 10, 9);
        this.service.addTema(newTema1);
        java.util.Iterator<Tema> students = this.service.getAllTeme().iterator();
        assertEquals(students.next(), newTema1);
        assertThrows(ValidationException.class, () -> this.service.addTema(newTema2));
    }

    @Test
    void testAddTemaNullId() {
        Tema newTema1 = new Tema("1", "desc", 10, 9);
        Tema newTema2 = new Tema(new String(), "desc", 10, 9);
        this.service.addTema(newTema1);
        java.util.Iterator<Tema> students = this.service.getAllTeme().iterator();
        assertEquals(students.next(), newTema1);
        assertThrows(ValidationException.class, () -> this.service.addTema(newTema2));
    }

    @Test
    void testAddTemaNullDesc() {
        Tema newTema1 = new Tema("1", "desc", 10, 9);
        Tema newTema2 = new Tema("2", "", 10, 9);
        this.service.addTema(newTema1);
        java.util.Iterator<Tema> students = this.service.getAllTeme().iterator();
        assertEquals(students.next(), newTema1);
        assertThrows(ValidationException.class, () -> this.service.addTema(newTema2));
    }

    @Test
    void testAddTemaDeadlineOverLimit() {
        Tema newTema1 = new Tema("1", "desc", 10, 9);
        Tema newTema2 = new Tema("2", "desc", 15, 9);
        this.service.addTema(newTema1);
        java.util.Iterator<Tema> students = this.service.getAllTeme().iterator();
        assertEquals(students.next(), newTema1);
        assertThrows(ValidationException.class, () -> this.service.addTema(newTema2));
    }

    @Test
    void testAddTemaDeadlineUnderLimit() {
        Tema newTema1 = new Tema("1", "desc", 10, 9);
        Tema newTema2 = new Tema("2", "desc", 0, 9);
        this.service.addTema(newTema1);
        java.util.Iterator<Tema> students = this.service.getAllTeme().iterator();
        assertEquals(students.next(), newTema1);
        assertThrows(ValidationException.class, () -> this.service.addTema(newTema2));
    }

    @Test
    void testAddTemaPrimireOverLimit() {
        Tema newTema1 = new Tema("1", "desc", 10, 9);
        Tema newTema2 = new Tema("2", "desc", 10, 15);
        this.service.addTema(newTema1);
        java.util.Iterator<Tema> students = this.service.getAllTeme().iterator();
        assertEquals(students.next(), newTema1);
        assertThrows(ValidationException.class, () -> this.service.addTema(newTema2));
    }

    @Test
    void testAddTemaPrimireUnderLimit() {
        Tema newTema1 = new Tema("1", "desc", 10, 9);
        Tema newTema2 = new Tema("2", "desc", 10, 0);
        this.service.addTema(newTema1);
        java.util.Iterator<Tema> students = this.service.getAllTeme().iterator();
        assertEquals(students.next(), newTema1);
        assertThrows(ValidationException.class, () -> this.service.addTema(newTema2));
    }

    @Test
    void testAddTemaAllGood() {
        Tema newTema1 = new Tema("1", "desc", 10, 9);
        Tema newTema2 = new Tema("2", "desc", 11, 8);
        this.service.addTema(newTema1);
        java.util.Iterator<Tema> students = this.service.getAllTeme().iterator();
        assertEquals(students.next(), newTema1);
        this.service.deleteTema("1");
        this.service.addTema(newTema2);
        students = this.service.getAllTeme().iterator();
        assertEquals(students.next(), newTema2);
    }

}