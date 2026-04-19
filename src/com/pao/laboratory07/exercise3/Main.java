package com.pao.laboratory07.exercise3;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        // Vezi Readme.md pentru cerințe
        Scanner sc = new Scanner(System.in);
        int n = Integer.parseInt(sc.nextLine().trim());
        List<Comanda> comenzi = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            String line = sc.nextLine().trim();
            String[] tokens = line.split(" ");
            comenzi.add(parseComanda(tokens));
        }

        for (Comanda comanda : comenzi) {
            System.out.println(comanda.descriereCompleta());
        }

        while (sc.hasNextLine()) {
            String line = sc.nextLine().trim();
            if (line.isEmpty()) {
                continue;
            }

            if (line.equals("QUIT")) {
                return;
            }

            if (line.equals("STATS")) {
                afiseazaStats(comenzi);
            } else if (line.startsWith("FILTER ")) {
                double prag = Double.parseDouble(line.split(" ")[1]);
                afiseazaFilter(comenzi, prag);
            } else if (line.equals("SORT")) {
                afiseazaSort(comenzi);
            } else if (line.equals("SPECIAL")) {
                afiseazaSpecial(comenzi);
            } else {
                throw new InvalidCommandException("Comanda invalida: " + line);
            }
        }
    }

    private static Comanda parseComanda(String[] tokens) {
        if (tokens[0].equals("STANDARD")) {
            return new ComandaStandard(tokens[1], Double.parseDouble(tokens[2]), tokens[3]);
        }
        if (tokens[0].equals("DISCOUNTED")) {
            return new ComandaRedusa(tokens[1], Double.parseDouble(tokens[2]), Integer.parseInt(tokens[3]), tokens[4]);
        }
        if (tokens[0].equals("GIFT")) {
            return new ComandaGratuita(tokens[1], tokens[2]);
        }
        throw new InvalidCommandException("Tip de comanda invalid: " + tokens[0]);
    }

    private static void afiseazaStats(List<Comanda> comenzi) {
        System.out.println();
        System.out.println("--- STATS ---");

        afiseazaMedieDacaExista(comenzi, "STANDARD");
        afiseazaMedieDacaExista(comenzi, "DISCOUNTED");
        afiseazaMedieDacaExista(comenzi, "GIFT");
    }

    private static void afiseazaMedieDacaExista(List<Comanda> comenzi, String tip) {
        List<Comanda> comenziTip = comenzi.stream()
                .filter(comanda -> comanda.tip().equals(tip))
                .toList();

        if (!comenziTip.isEmpty()) {
            double medie = comenziTip.stream()
                    .collect(Collectors.averagingDouble(Comanda::pretFinal));
            System.out.printf("%s: medie = %.2f lei%n", tip, medie);
        }
    }

    private static void afiseazaFilter(List<Comanda> comenzi, double prag) {
        System.out.println();
        System.out.printf("--- FILTER (>= %.2f) ---%n", prag);

        List<Comanda> filtrate = comenzi.stream()
                .filter(comanda -> comanda.pretFinal() >= prag)
                .toList();

        for (Comanda comanda : filtrate) {
            System.out.println(comanda.descriereScurta());
        }
    }

    private static void afiseazaSort(List<Comanda> comenzi) {
        System.out.println();
        System.out.println("--- SORT (by client, then by pret) ---");

        List<Comanda> sortate = comenzi.stream()
                .sorted(Comparator.comparing(Comanda::getClient)
                        .thenComparing(Comanda::pretFinal))
                .toList();

        for (Comanda comanda : sortate) {
            System.out.println(comanda.descriereScurta());
        }
    }

    private static void afiseazaSpecial(List<Comanda> comenzi) {
        System.out.println();
        System.out.println("--- SPECIAL (discount > 15%) ---");

        List<ComandaRedusa> speciale = comenzi.stream()
                .filter(comanda -> comanda instanceof ComandaRedusa)
                .map(comanda -> (ComandaRedusa) comanda)
                .filter(comanda -> comanda.getDiscountProcent() > 15)
                .toList();

        for (ComandaRedusa comanda : speciale) {
            System.out.println(comanda.descriereScurta());
        }
    }
}
