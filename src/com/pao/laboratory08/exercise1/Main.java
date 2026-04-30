package com.pao.laboratory08.exercise1;

import java.io.*;
import java.util.*;

public class Main {
    private static final String FILE_PATH = "com/pao/laboratory08/tests/studenti.txt";

    public static void main(String[] args) throws Exception {
        List<Student> studenti = citesteStudenti();
        Scanner scanner = new Scanner(System.in);

        if (!scanner.hasNextLine()) {
            return;
        }

        String comanda = scanner.nextLine().trim();
        if (comanda.equals("PRINT")) {
            for (Student student : studenti) {
                System.out.println(student);
            }
            return;
        }

        String[] parti = comanda.split(" ", 2);
        if (parti.length < 2) {
            return;
        }

        Student studentGasit = cautaStudent(studenti, parti[1].trim());
        if (studentGasit == null) {
            return;
        }

        Student clona;
        if (parti[0].equals("SHALLOW")) {
            clona = studentGasit.shallowClone();
        } else if (parti[0].equals("DEEP")) {
            clona = studentGasit.deepClone();
        } else {
            return;
        }

        clona.getAdresa().setOras("MODIFICAT");
        System.out.println("Original: " + studentGasit);
        System.out.println("Clona: " + clona);
    }

    private static List<Student> citesteStudenti() throws IOException {
        List<Student> studenti = new ArrayList<>();
        BufferedReader fin = new BufferedReader(new FileReader(FILE_PATH));
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

        fin.close();
        return studenti;
    }

    private static Student cautaStudent(List<Student> studenti, String nume) {
        for (Student student : studenti) {
            if (student.getNume().equals(nume)) {
                return student;
            }
        }
        return null;
    }
}
