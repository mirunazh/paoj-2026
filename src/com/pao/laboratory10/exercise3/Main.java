package com.pao.laboratory10.exercise3;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class Main {

enum TipTranzactie {CREDIT, DEBIT}

static class Tranzactie {
    private int id;
    private String contSursa;
    private String data;
    private double suma;
    private TipTranzactie tip;

    public Tranzactie(int id, String contSursa, String data, double suma, TipTranzactie tip) {
        this.id = id;
        this.contSursa = contSursa;
        this.data = data;
        this.suma = suma;
        this.tip = tip;
    }

    public int getId() {
        return id;
    }
    public String getContSursa() {
        return contSursa;
    }
    public String getData() {
        return data;
    }
    public double getSuma() {
        return suma;
    }
    public TipTranzactie getTip() {
        return tip;
    }

    @Override
    public String toString() {
        return String.format("[%d] %s %s %s: %.2f RON", id, contSursa, data, tip, suma);
    }
}

public static void main(String[] args) {
    List<Tranzactie> tranzactii = Arrays.asList(
            new Tranzactie(1, "CONT_A", "2026-01-10", 500.0, TipTranzactie.CREDIT),
            new Tranzactie(2, "CONT_B", "2026-01-15", 250.0, TipTranzactie.DEBIT),
            new Tranzactie(3, "CONT_A", "2026-01-20", 700.0, TipTranzactie.CREDIT),
            new Tranzactie(4, "CONT_C", "2026-02-03", 1200.0, TipTranzactie.DEBIT),
            new Tranzactie(5, "CONT_D", "2026-02-08", 450.0, TipTranzactie.CREDIT),
            new Tranzactie(6, "CONT_B", "2026-02-18", 300.0, TipTranzactie.DEBIT),
            new Tranzactie(7, "CONT_E", "2026-03-05", 900.0, TipTranzactie.CREDIT),
            new Tranzactie(8, "CONT_A", "2026-03-11", 150.0, TipTranzactie.DEBIT),
            new Tranzactie(9, "CONT_C", "2026-03-19", 650.0, TipTranzactie.CREDIT),
            new Tranzactie(10, "CONT_D", "2026-03-25", 1000.0, TipTranzactie.DEBIT)
    );

    System.out.println("1. Tranzactii CREDIT");
    tranzactii.stream()
            .filter(t -> t.getTip() == TipTranzactie.CREDIT)
            .forEach(System.out::println);

    System.out.println("\n2. Total procesat");
    double total = tranzactii.stream()
            .mapToDouble(Tranzactie::getSuma)
            .sum();
    System.out.printf("Total procesat: %.2f RON%n", total);

    System.out.println("\n3. Total pe luna");
    Map<String, Double> totalPeLuna = tranzactii.stream()
            .collect(Collectors.groupingBy(
                    t -> t.getData().substring(0, 7),
                    TreeMap::new,
                    Collectors.summingDouble(Tranzactie::getSuma)
            ));

    totalPeLuna.forEach((luna, suma) ->
            System.out.printf("%s: %.2f RON%n", luna, suma));

    System.out.println("\n4. Top 3 tranzactii");
    tranzactii.stream()
            .sorted((t1, t2) -> Double.compare(t2.getSuma(), t1.getSuma()))
            .limit(3)
            .forEach(System.out::println);

    System.out.println("\n5. Conturi sursa unice");
    List<String> conturiUnice = tranzactii.stream()
            .map(Tranzactie::getContSursa)
            .distinct()
            .collect(Collectors.toList());
    System.out.println("Conturi sursa unice: " + conturiUnice);

    System.out.println("\n6. Suma medie");
    double medie = tranzactii.stream()
            .mapToDouble(Tranzactie::getSuma)
            .average()
            .orElse(0.0);
    System.out.printf("Suma medie: %.2f RON%n", medie);

    System.out.println("\n7. Extras de cont lunar");
    Map<String, List<Tranzactie>> extrasPeLuna = tranzactii.stream()
            .collect(Collectors.groupingBy(
                    t -> t.getData().substring(0, 7),
                    TreeMap::new,
                    Collectors.toList()
            ));

    extrasPeLuna.forEach((luna, lista) -> {
        double sumaLuna = lista.stream()
                .mapToDouble(Tranzactie::getSuma)
                .sum();

        System.out.printf("EXTRAS DE CONT - %s: %d tranzactii, total: %.2f RON%n",
                luna, lista.size(), sumaLuna);
    });
}
}