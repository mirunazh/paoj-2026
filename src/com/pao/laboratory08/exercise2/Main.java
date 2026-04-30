package com.pao.laboratory08.exercise2;

import com.pao.laboratory08.exercise1.Adresa;
import com.pao.laboratory08.exercise1.Student;

import java.io.*;
import java.util.*;

public class Main {
    private static final String FILE_PATH = "com/pao/laboratory08/tests/studenti.txt";
    private static final String OUTPUT_FILE = "rezultate.txt";

    public static void main(String[] args) throws Exception {
        List<Student> studenti = citesteStudenti();
        try (Scanner scanner = new Scanner(System.in)) {
            if (!scanner.hasNextInt()) {
                return;
            }

            int prag = scanner.nextInt();
            List<Student> filtrati = new ArrayList<>();
            for (Student student : studenti) {
                if (student.getVarsta() >= prag) {
                    filtrati.add(student);
                }
            }

            try (BufferedWriter fout = new BufferedWriter(new FileWriter(OUTPUT_FILE))) {
                for (Student student : filtrati) {
                    fout.write(student.toString());
                    fout.newLine();
                }
            }

            System.out.println("Filtru: varsta >= " + prag);
            System.out.println("Rezultate: " + filtrati.size() + " studenti");
            System.out.println();
            for (Student student : filtrati) {
                System.out.println(student);
            }
            System.out.println();
            System.out.println("Scris in: " + OUTPUT_FILE);
        }
    }

    private static List<Student> citesteStudenti() throws IOException {
        List<Student> studenti = new ArrayList<>();
        try (BufferedReader fin = new BufferedReader(new FileReader(FILE_PATH))) {
            String linie;

            while ((linie = fin.readLine()) != null) {
                if (linie.trim().isEmpty()) {
                    continue;
                }

                String[] parti = linie.split(",");
                String nume = parti[0].trim();
                int varsta = Integer.parseInt(parti[1].trim());
                String oras = parti[2].trim();
                String strada = parti[3].trim();

                studenti.add(new Student(nume, varsta, new Adresa(oras, strada)));
            }
        }
        return studenti;
    }
}
