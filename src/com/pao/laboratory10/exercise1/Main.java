package com.pao.laboratory10.exercise1;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        // TODO: Implementează conform Readme.md
        //
        Scanner scanner = new Scanner(System.in);
        // Folosește LinkedList<Tranzactie> ca structură internă.
        LinkedList<Tranzactie> coada = new LinkedList<>();
        // Citește comenzi din stdin până la EOF:
        //
        while(scanner.hasNext()){
            String comanda = scanner.next();
            switch(comanda){
                case "ENQUEUE":{
                    int id = scanner.nextInt();
                    double suma = scanner.nextDouble();
                    String data = scanner.next();
                    TipTranzactie tip = TipTranzactie.valueOf(scanner.next());

                    Tranzactie tranzactie = new Tranzactie(id, suma, data, tip);
                    coada.addLast(tranzactie);
                    break;
                }
                case "DEQUEUE":{
                    if(coada.isEmpty()){
                        System.out.println("Coada goala.");
                    }
                    else{
                        Tranzactie tranzactie = coada.removeFirst();
                        System.out.println("Procesat: " + tranzactie);
                    }
                    break;
                }
                case "PUSH":{
                    int id = scanner.nextInt();
                    double suma = scanner.nextDouble();
                    String data = scanner.next();
                    TipTranzactie tip = TipTranzactie.valueOf(scanner.next());

                    Tranzactie tranzactie = new Tranzactie(id, suma, data, tip);
                    coada.addFirst(tranzactie);
                    break;
                }
                case "POP":{
                    if(coada.isEmpty()){
                        System.out.println("Coada goala.");
                    }
                    else{
                        Tranzactie tranzactie = coada.removeFirst();
                        System.out.println("Extras: " + tranzactie);
                    }
                    break;
                }
                case "REMOVE_DEBIT":{
                    int c = 0;
                    Iterator<Tranzactie> iterator = coada.iterator();
                    while(iterator.hasNext()){
                        Tranzactie tranzactie = iterator.next();
                        if(tranzactie.getTip() == TipTranzactie.DEBIT){
                            iterator.remove();
                            c++;
                        }
                    }
                    System.out.println("Eliminat " + c + " tranzactii DEBIT.");
                    break;
                }
                case "REMOVE_BELOW":{
                    double threshold = scanner.nextDouble();
                    int c = 0;
                    Iterator<Tranzactie> iterator = coada.iterator();
                    while(iterator.hasNext()){
                        Tranzactie tranzactie = iterator.next();
                        if(tranzactie.getSuma() < threshold){
                            iterator.remove();
                            c++;
                        }
                    }
                    System.out.println(String.format("Eliminat %d tranzactii sub %.2f RON.", c, threshold));
                    break;
                }
                case "PRINT":{
                    Iterator<Tranzactie> iterator = coada.iterator();
                    while(iterator.hasNext()){
                        Tranzactie tranzactie = iterator.next();
                        System.out.println(tranzactie);
                    }
                    break;
                }
                case "SIZE":{
                    /*int c = 0;
                    while(iterator.hasNext()){
                        Tranzactie tranzactie = iterator.next();
                        c++;
                    }
                    System.out.println("Dimensiune coada: " + c);*/
                    System.out.println("Dimensiune coada: " + coada.size());
                    break;
                }
            }
        //   ENQUEUE id suma data tip   → addLast  (niciun output)
        //   DEQUEUE                    → removeFirst sau "Coada goala."
        //                                format: "Procesat: [id] data tip: suma RON"
        //   PUSH id suma data tip      → addFirst  (niciun output)
        //   POP                        → removeFirst sau "Coada goala."
        //                                format: "Extras: [id] data tip: suma RON"
        //   REMOVE_DEBIT               → Iterator.remove() pe toate DEBIT
        //                                afișează "Eliminat N tranzactii DEBIT."
        //   REMOVE_BELOW threshold     → Iterator.remove() pe suma < threshold
        //                                afișează "Eliminat N tranzactii sub threshold RON."
        //   PRINT                      → afișează toate, câte una pe linie
        //   SIZE                       → "Dimensiune coada: N"
        }

        //
        // Format linie tranzacție: [id] data tip: suma RON
        //   Ex: [1] 2024-01-10 CREDIT: 500.00 RON

        //System.out.println("TODO: implementează exercițiul 1");
    }
}
