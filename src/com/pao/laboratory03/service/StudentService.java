package com.pao.laboratory03.service;

import com.pao.laboratory03.exception.StudentNotFoundException;
import com.pao.laboratory03.model.Student;
import com.pao.laboratory03.model.Subject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class StudentService {
    private static StudentService instance;
    private List<Student> students;

    private StudentService() {
        students = new ArrayList<>();
    }

    public static StudentService getInstance() {
        if (instance == null) {
            instance = new StudentService();
        }
        return instance;
    }

    public void addStudent(String name, int age) {
        for (Student student : students) {
            if (student.getName().equalsIgnoreCase(name)) {
                throw new RuntimeException("Exista deja un student cu numele " + name + ".");
            }
        }

        students.add(new Student(name, age));
    }

    public Student findByName(String name) {
        for (Student student : students) {
            if (student.getName().equalsIgnoreCase(name)) {
                return student;
            }
        }
        throw new StudentNotFoundException("Studentul " + name + " nu a fost gasit.");
    }

    public void addGrade(String studentName, Subject subject, double grade) {
        Student student = findByName(studentName);
        student.addGrade(subject, grade);
    }

    public void printAllStudents() {
        if (students.isEmpty()) {
            System.out.println("Nu exista studenti.");
            return;
        }

        for (Student student : students) {
            System.out.println(student);
            System.out.println("Note: " + student.getGrades());
        }
    }

    public void printTopStudents() {
        if (students.isEmpty()) {
            System.out.println("Nu exista studenti.");
            return;
        }

        List<Student> sortedStudents = new ArrayList<>(students);
        sortedStudents.sort(Comparator.comparingDouble(Student::getAverage).reversed());

        for (Student student : sortedStudents) {
            System.out.println(student);
        }
    }

    public Map<Subject, Double> getAveragePerSubject() {
        Map<Subject, Double> averages = new EnumMap<>(Subject.class);

        for (Subject subject : Subject.values()) {
            double sum = 0;
            int count = 0;

            for (Student student : students) {
                if (student.getGrades().containsKey(subject)) {
                    sum += student.getGrades().get(subject);
                    count++;
                }
            }

            if (count > 0) {
                averages.put(subject, sum / count);
            }
        }

        return averages;
    }
}