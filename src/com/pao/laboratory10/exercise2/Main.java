package com.pao.laboratory10.exercise2;

import com.pao.laboratory10.exercise1.Tranzactie;
import com.pao.laboratory10.exercise1.TipTranzactie;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        // TODO: Implementează conform Readme.md
        //
        // 1. Citește N din stdin, apoi cele N tranzacții (id suma data tip) — pot exista duplicate de id
        //    Stochează-le toate într-un ArrayList<Tranzactie> (cu duplicate, ordine inserare)
        //
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        ArrayList<Tranzactie> lista = new ArrayList<>();
        for(int i = 0; i < n; i++){
            int id = scanner.nextInt();
            double suma = scanner.nextDouble();
            String data = scanner.next();
            TipTranzactie tip = TipTranzactie.valueOf(scanner.next());
            
            Tranzactie tranzactie = new Tranzactie(id, suma, data, tip);
            lista.add(tranzactie);
        }
        // 2. Procesează comenzile din stdin până la EOF:
        //
        while(scanner.hasNext()){
            String comanda = scanner.next();
            switch(comanda){
                case "UNIQUE_IDS":{
                    LinkedHashSet<Integer> uniqIds = new LinkedHashSet<>();
                    for(Tranzactie t : lista){
                        uniqIds.add(t.getid());
                    }
                    System.out.println("IDs unice ("+ uniqIds.size() + "): " + uniqIds);
                    break;
                }
                case "MONTHLY_REPORT":{
                    TreeMap<String, double[]> raportLunar = new TreeMap<>();
                    for(Tranzactie t : lista){
                        String luna = t.getData().substring(0, 7);
                        if(!raportLunar.containsKey(luna)){
                            raportLunar.put(luna, new double[]{0.0, 0.0});
                        }
                        double[] sume = raportLunar.get(luna);
                        if(t.getTip() == TipTranzactie.CREDIT){
                            sume[0] += t.getSuma();
                        }
                        else{
                            sume[1] += t.getSuma();
                        }
                    }
                    for(Map.Entry<String, double[]> entry : raportLunar.entrySet()){
                        String luna = entry.getKey();
                        double[] sume = entry.getValue();
                        System.out.println(String.format("%s: CREDIT %.2f RON, DEBIT %.2f RON", luna, sume[0], sume[1]));
                    }
                    break;
                }
                case "TOP":{
                    int topN = scanner.nextInt();
                    ArrayList<Tranzactie> copie = new ArrayList<>(lista);
                    Collections.sort(copie, Comparator.comparingDouble(Tranzactie::getSuma).reversed());
                    System.out.println("Top " + topN + ":");
                    for(int i = 0; i < topN && i < copie.size(); i++){
                        System.out.println(copie.get(i));
                    }
                    break;
                }
                case "SORT_ASC":{
                    Collections.sort(lista, Comparator.comparingDouble(Tranzactie::getSuma));
                    for(Tranzactie t : lista){
                        System.out.println(t);
                    }
                    break;
                }
                case "SORT_DESC":{
                    Collections.sort(lista, Comparator.comparingDouble(Tranzactie::getSuma).reversed());
                    for(Tranzactie t : lista){
                        System.out.println(t);
                    }
                    break;
                }
                case "REVERSE":{
                    Collections.reverse(lista);
                    for(Tranzactie t : lista){
                        System.out.println(t);
                    }
                    break;
                }
                case "MIN_MAX":{
                    Tranzactie minim = Collections.min(lista, Comparator.comparingDouble(Tranzactie::getSuma));
                    Tranzactie maxim = Collections.max(lista, Comparator.comparingDouble(Tranzactie::getSuma));
                    System.out.println("MIN: " + minim);
                    System.out.println("MAX: " + maxim);
                    break;
                }
                case "CME_DEMO":{
                    try{
                        for(Tranzactie t : lista){
                            lista.remove(t);
                        }
                    }catch(ConcurrentModificationException e){
                        System.out.println("ConcurrentModificationException prins: modificare in iteratie detectata.");
                    }
                    break;
                }
            }
        }
        //   UNIQUE_IDS      → LinkedHashSet<Integer> cu id-urile în ordinea primei apariții
        //                     afișează: "IDs unice (N): [1, 2, 3, ...]"
        //
        //   MONTHLY_REPORT  → TreeMap<String, ...> grupat pe yyyy-MM (substring 0-7 din data)
        //                     pentru fiecare lună, sumele CREDIT și DEBIT
        //                     format: "yyyy-MM: CREDIT X.XX RON, DEBIT Y.YY RON"
        //
        //   TOP n           → primele n tranzacții după suma descrescătoare (nu modifică lista)
        //                     afișează "Top n:" urmat de n linii
        //
        //   SORT_ASC        → Collections.sort cu suma crescătoare; afișează lista sortată
        //   SORT_DESC       → Collections.sort cu suma descrescătoare; afișează lista sortată
        //   REVERSE         → Collections.reverse; afișează lista
        //   MIN_MAX         → Collections.min/max după suma
        //                     "MIN: [id] data tip: suma RON"
        //                     "MAX: [id] data tip: suma RON"
        //
        //   CME_DEMO        → încearcă for(t : lista) lista.remove(t) în try-catch
        //                     afișează "ConcurrentModificationException prins: modificare in iteratie detectata."
        //
        // Format linie tranzacție: [id] data tip: suma RON
        //   Ex: [1] 2024-01-15 CREDIT: 1500.00 RON

        //System.out.println("TODO: implementează exercițiul 2");
    }
}
