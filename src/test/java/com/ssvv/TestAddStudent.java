package com.ssvv;

import com.ssvv.domain.Student;
import com.ssvv.repository.StudentXMLRepo;
import com.ssvv.service.Service;
import com.ssvv.validation.StudentValidator;
import com.ssvv.validation.ValidationException;
import org.junit.jupiter.api.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestAddStudent {
    private StudentXMLRepo studentFileRepository;
    private StudentValidator studentValidator;
    private Service service;

    @BeforeAll
    static void createXML() {
        File xml = new File("fisiere/studentiTest.xml");
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
        this.studentFileRepository = new StudentXMLRepo("fisiere/studentiTest.xml");
        this.studentValidator = new StudentValidator();
        this.service = new Service(this.studentFileRepository, this.studentValidator, null, null, null, null);
    }

    @AfterEach
    void tearDown() {
        new File("fisiere/studentiTest.xml").delete();
    }

    @Test
    void testAddStudentSpecialCharInName() {
        Student newStudent1 = new Student("123", "Ana", 931, "s1@email.com");
        Student newStudent2 = new Student("124", "An/a", 931, "s1@email.com");
        this.service.addStudent(newStudent1);
        java.util.Iterator<Student> students = this.service.getAllStudenti().iterator();
        assertEquals(students.next(), newStudent1);
        assertThrows(ValidationException.class, () -> this.service.addStudent(newStudent2));
    }

    @Test
    void testAddStudentOnGroup() {
        Student newStudent1 = new Student("123", "a", 931, "s1@email.com");
        this.service.addStudent(newStudent1);
        java.util.Iterator<Student> students = this.service.getAllStudenti().iterator();
        assertEquals(students.next(), newStudent1);
    }

    @Test
    void testAddStudentOnName() {
        Student newStudent1 = new Student("111", "Ana", 100, "s1@email.com");
        Student newStudent2 = new Student("1111211", "", 100, "s1@email.com");
        Student newStudent3 = new Student("1111212", null, 100, "s1@email.com");
        this.service.addStudent(newStudent1);
        java.util.Iterator<Student> students = this.service.getAllStudenti().iterator();
        assertEquals(students.next(), newStudent1);
        assertThrows(ValidationException.class, () -> this.service.addStudent(newStudent2));
        assertThrows(ValidationException.class, () -> this.service.addStudent(newStudent3));
    }

    @Test
    void testAddStudentOnEmail() {
        Student newStudent1 = new Student("111", "Ana", 100, "s1@email.com");
        Student newStudent2 = new Student("1111211", "", 100, "");
        Student newStudent3 = new Student("1111212", null, 100, null);
        this.service.addStudent(newStudent1);
        java.util.Iterator<Student> students = this.service.getAllStudenti().iterator();
        assertEquals(students.next(), newStudent1);
        assertThrows(ValidationException.class, () -> this.service.addStudent(newStudent2));
        assertThrows(ValidationException.class, () -> this.service.addStudent(newStudent3));
    }

    @Test
    void testAddStudentHasAtSymbol() {
        Student newStudent1 = new Student("111", "Ana", 100, "s1@email.com");
        Student newStudent2 = new Student("1111211", "Ana Maria", 100, "email");
        this.service.addStudent(newStudent1);
        java.util.Iterator<Student> students = this.service.getAllStudenti().iterator();
        assertEquals(students.next(), newStudent1);
        assertThrows(ValidationException.class, () -> this.service.addStudent(newStudent2));
    }

    @Test
    void testAddStudentDuplicateId() {
        Student newStudent1 = new Student("1111", "Ana Maria", 100, "s1@email.com");
        Student newStudent2 = new Student("1112", "Maria", 100, "s1@email.com");
        Student newStudent3 = new Student("1111", "Anamaria", 100, "s1@email.com");
        this.service.addStudent(newStudent1);
        this.service.addStudent(newStudent2);
        java.util.Iterator<Student> students = this.service.getAllStudenti().iterator();
        assertEquals(students.next(), newStudent2);
        assertEquals(students.next(), newStudent1);
        assertThrows(ValidationException.class, () -> this.service.addStudent(newStudent3));
    }

    @Test
    void testAddStudentDoubleId() {
        Student newStudent1 = new Student("1111", "Ana", 100, "s1@email.com");
        Student newStudent2 = new Student("1112", "Maria", 100, "s1@email.com");
        Student newStudent3 = new Student("1113.1", "Anamaria", 100, "s1@email.com");
        this.service.addStudent(newStudent1);
        this.service.addStudent(newStudent2);
        java.util.Iterator<Student> students = this.service.getAllStudenti().iterator();
        assertEquals(students.next(), newStudent2);
        assertEquals(students.next(), newStudent1);
        assertThrows(ValidationException.class, () -> this.service.addStudent(newStudent3));
    }

    @Test
    void testAddStudentIdBiggerThanUpperBoundary() {
        Student newStudent1 = new Student("10001", "Ana", 100, "s1@email.com");
        assertThrows(ValidationException.class, () -> this.service.addStudent(newStudent1));
    }

    @Test
    void testAddStudentIdEqualToUpperBoundary() {
        Student newStudent1 = new Student("10000", "Ana", 100, "s1@email.com");
        this.service.addStudent(newStudent1);
        java.util.Iterator<Student> students = this.service.getAllStudenti().iterator();
        assertEquals(students.next(), newStudent1);
    }

    @Test
    void testAddStudentIdLowerThanUpperBoundary() {
        Student newStudent1 = new Student("9999", "Ana", 100, "s1@email.com");
        this.service.addStudent(newStudent1);
        java.util.Iterator<Student> students = this.service.getAllStudenti().iterator();
        assertEquals(students.next(), newStudent1);
    }

    @Test
    void testAddStudentGroupBiggerThanUpperBoundary() {
        Student newStudent1 = new Student("101", "Ana", 10001, "s1@email.com");
        assertThrows(ValidationException.class, () -> this.service.addStudent(newStudent1));
    }

    @Test
    void testAddStudentGroupEqualToUpperBoundary() {
        Student newStudent1 = new Student("100", "Ana", 10000, "s1@email.com");
        this.service.addStudent(newStudent1);
        java.util.Iterator<Student> students = this.service.getAllStudenti().iterator();
        assertEquals(students.next(), newStudent1);
    }

    @Test
    void testAddStudentGroupLowerThanUpperBoundary() {
        Student newStudent1 = new Student("123", "Ana", 9999, "s1@email.com");
        this.service.addStudent(newStudent1);
        java.util.Iterator<Student> students = this.service.getAllStudenti().iterator();
        assertEquals(students.next(), newStudent1);
    }


    @Test
    void testAddStudentIdLowerThanLowerBoundary() {
        Student newStudent1 = new Student("-1", "Ana", 100, "s1@email.com");
        assertThrows(ValidationException.class, () -> this.service.addStudent(newStudent1));
    }

    @Test
    void testAddStudentIdEqualToLowerBoundary() {
        Student newStudent1 = new Student("0", "Ana", 100, "s1@email.com");
        assertThrows(ValidationException.class, () -> this.service.addStudent(newStudent1));
    }

    @Test
    void testAddStudentIdBiggerThanLowerBoundary() {
        Student newStudent1 = new Student("1", "Ana", 100, "s1@email.com");
        this.service.addStudent(newStudent1);
        java.util.Iterator<Student> students = this.service.getAllStudenti().iterator();
        assertEquals(students.next(), newStudent1);
    }

    @Test
    void testAddStudentGroupLowerThanLowerBoundary() {
        Student newStudent1 = new Student("123", "Ana", -1, "s1@email.com");
        assertThrows(ValidationException.class, () -> this.service.addStudent(newStudent1));
    }

    @Test
    void testAddStudentGroupEqualToLowerBoundary() {
        Student newStudent1 = new Student("132", "Ana", 0, "s1@email.com");
        assertThrows(ValidationException.class, () -> this.service.addStudent(newStudent1));
    }

    @Test
    void testAddStudentGroupBiggerThanLowerBoundary() {
        Student newStudent1 = new Student("123", "Ana", 1, "s1@email.com");
        this.service.addStudent(newStudent1);
        java.util.Iterator<Student> students = this.service.getAllStudenti().iterator();
        assertEquals(students.next(), newStudent1);
    }
}