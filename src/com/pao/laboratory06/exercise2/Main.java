package com.pao.laboratory06.exercise2;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = Integer.parseInt(in.nextLine().trim());
        List<Colaborator> colaboratori = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            Scanner lineScanner = new Scanner(in.nextLine());
            String tip = lineScanner.next();
            Colaborator colaborator = switch (tip) {
                case "CIM" -> new CIMColaborator();
                case "PFA" -> new PFAColaborator();
                case "SRL" -> new SRLColaborator();
                default -> throw new IllegalArgumentException("Tip necunoscut: " + tip);
            };
            colaborator.citeste(lineScanner);
            colaboratori.add(colaborator);
        }

        for (TipColaborator tip : TipColaborator.values()) {
            colaboratori.stream()
                    .filter(colaborator -> colaborator.getTip() == tip)
                    .sorted((left, right) ->
                            Double.compare(right.calculeazaVenitNetAnual(), left.calculeazaVenitNetAnual()))
                    .forEach(Colaborator::afiseaza);
        }

        System.out.println();
        Colaborator maxim = colaboratori.stream()
                .max((left, right) -> Double.compare(left.calculeazaVenitNetAnual(), right.calculeazaVenitNetAnual()))
                .orElse(null);
        System.out.print("Colaborator cu venit net maxim: ");
        if (maxim != null) {
            System.out.println(maxim.formatAfisare());
        } else {
            System.out.println();
        }

        System.out.println();
        System.out.println("Colaboratori persoane juridice:");
        colaboratori.stream()
                .filter(colaborator -> colaborator instanceof PersoanaJuridica)
                .sorted((left, right) ->
                        Double.compare(right.calculeazaVenitNetAnual(), left.calculeazaVenitNetAnual()))
                .forEach(Colaborator::afiseaza);

        Map<TipColaborator, Double> sume = new EnumMap<>(TipColaborator.class);
        Map<TipColaborator, Integer> numere = new EnumMap<>(TipColaborator.class);
        for (Colaborator colaborator : colaboratori) {
            TipColaborator tip = colaborator.getTip();
            sume.put(tip, sume.getOrDefault(tip, 0.0) + colaborator.calculeazaVenitNetAnual());
            numere.put(tip, numere.getOrDefault(tip, 0) + 1);
        }

        System.out.println();
        System.out.println("Sume și număr colaboratori pe tip:");
        for (TipColaborator tip : TipColaborator.values()) {
            Double suma = sume.get(tip);
            Integer numar = numere.get(tip);
            if (suma == null) {
                System.out.printf("%s: suma = nu lei, număr = null%n", tip);
            } else {
                System.out.printf("%s: suma = %.2f lei, număr = %d%n", tip, suma, numar);
            }
        }
    }
}
