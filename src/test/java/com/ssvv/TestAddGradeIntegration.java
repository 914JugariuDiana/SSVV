package com.ssvv;

import com.ssvv.domain.Nota;
import com.ssvv.domain.Student;
import com.ssvv.domain.Tema;
import com.ssvv.repository.NotaXMLRepo;
import com.ssvv.repository.StudentXMLRepo;
import com.ssvv.repository.TemaXMLRepo;
import com.ssvv.service.Service;
import com.ssvv.validation.NotaValidator;
import com.ssvv.validation.StudentValidator;
import com.ssvv.validation.TemaValidator;
import com.ssvv.validation.ValidationException;
import org.junit.jupiter.api.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestAddGradeIntegration {
    private TemaXMLRepo temaFileRepository;
    private TemaValidator temaValidator;
    private Service service;
    private StudentXMLRepo studentFileRepository;
    private StudentValidator studentValidator;
    private NotaXMLRepo notaFileRepository;
    private NotaValidator notaValidator;

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

        File xmlStudents = new File("fisiere/studentiTest.xml");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(xmlStudents))) {
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
                    "<inbox>\n" +
                    "\n" +
                    "</inbox>");
            writer.flush();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        File xmlGrades = new File("fisiere/notaTest.xml");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(xmlGrades))) {
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
        this.studentFileRepository = new StudentXMLRepo("fisiere/studentiTest.xml");
        this.studentValidator = new StudentValidator();
        this.notaFileRepository = new NotaXMLRepo("fisiere/notaTest.xml");
        this.notaValidator = new NotaValidator(this.studentFileRepository, this.temaFileRepository);
        this.service = new Service(this.studentFileRepository, this.studentValidator, this.temaFileRepository, this.temaValidator, this.notaFileRepository, this.notaValidator);

    }

    @AfterEach
    void tearDown() {
        new File("fisiere/temeTest.xml").delete();
        new File("fisiere/studentiTest.xml").delete();
        new File("fisiere/notaTest.xml").delete();
    }


    @Test
    void testAddStudentAllGood() {
        Student newStudent1 = new Student("123", "Ana", 931, "s1@email.com");
        this.service.addStudent(newStudent1);
        java.util.Iterator<Student> students = this.service.getAllStudenti().iterator();
        assertEquals(students.next(), newStudent1);
    }

    @Test
    void testAddTemaAllGood() {
        Tema newTema1 = new Tema("1", "desc", 2, 1);
        this.service.addTema(newTema1);
        java.util.Iterator<Tema> teme = this.service.getAllTeme().iterator();
        assertEquals(teme.next(), newTema1);
    }

    @Test
    void testAddNotaAllGood() {
        testAddStudentAllGood();
        testAddTemaAllGood();
        Nota newNota1 = new Nota("1", "123", "1", 5, LocalDate.of(2018, 10, 15));
        this.service.addNota(newNota1, "bad.");
        java.util.Iterator<Nota> note = this.service.getAllNote().iterator();
        assertEquals(note.next(), newNota1);
    }

}