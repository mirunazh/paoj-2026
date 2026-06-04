package com.pao.proiect.imobiliare.service;

import com.pao.proiect.imobiliare.model.AgentieImobiliara;
import com.pao.proiect.imobiliare.model.Proprietate;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeSet;

public class ProprietateService extends ServiceSupport {
    private final List<Proprietate> proprietati = new ArrayList<>();
    private final Map<Integer, Proprietate> proprietatiById = new LinkedHashMap<>();
    private final Map<String, List<Proprietate>> proprietatiByOras = new LinkedHashMap<>();
    private final TreeSet<Proprietate> proprietatiSortate = new TreeSet<>();
    private int nextId = 40;
    private final AgentieService agentieService = AgentieService.getInstance();

    private ProprietateService() {
    }

    private static class Holder {
        private static final ProprietateService INSTANCE = new ProprietateService();
    }

    public static ProprietateService getInstance() {
        return Holder.INSTANCE;
    }

    public int genereazaId() {
        return nextId++;
    }

    public Proprietate add(String titlu, String oras, String adresa, double pret, String tipTranzactie, double suprafata, boolean disponibila, int idAgentie) {
        auditCurrentAction();
        AgentieImobiliara agentie = agentieService.getById(idAgentie);
        if (agentie == null) {
            System.out.println("Agentia imobiliara nu exista. Proprietatea nu a fost creata.");
            return null;
        }
        if (!esteValid(titlu, oras, adresa, pret, tipTranzactie, suprafata)) {
            return null;
        }
        int id = genereazaId();
        Proprietate proprietate = new Proprietate(id, titlu.trim(), oras.trim(), adresa.trim(), pret, tipTranzactie.trim(), suprafata, disponibila, agentie);
        adaugaInColectii(proprietate);
        afiseazaConfirmareCreare("Proprietatea", id);
        return proprietate;
    }

    public void addExisting(Proprietate proprietate) {
        auditCurrentAction();
        if (proprietate == null) {
            System.out.println("Proprietatea primita este invalida.");
            return;
        }
        if (getById(proprietate.getId()) != null) {
            System.out.println("Exista deja o proprietate cu acest ID.");
            return;
        }
        adaugaInColectii(proprietate);
    }

    private void adaugaInColectii(Proprietate proprietate) {
        proprietati.add(proprietate);
        proprietatiById.put(proprietate.getId(), proprietate);
        proprietatiSortate.add(proprietate);
        proprietatiByOras.computeIfAbsent(normalizeKey(proprietate.getOras()), cheie -> new ArrayList<>()).add(proprietate);
    }

    private void reindexeaza(Proprietate proprietate, String orasVechi) {
        proprietatiSortate.remove(proprietate);
        if (orasVechi != null) {
            List<Proprietate> dinOras = proprietatiByOras.get(normalizeKey(orasVechi));
            if (dinOras != null) {
                dinOras.remove(proprietate);
                if (dinOras.isEmpty()) {
                    proprietatiByOras.remove(normalizeKey(orasVechi));
                }
            }
        }
        proprietatiSortate.add(proprietate);
        proprietatiByOras.computeIfAbsent(normalizeKey(proprietate.getOras()), cheie -> new ArrayList<>()).add(proprietate);
    }

    public Proprietate[] listAll() {
        auditCurrentAction();
        return proprietati.toArray(new Proprietate[0]);
    }

    public Proprietate[] listAllSorted() {
        auditCurrentAction();
        return proprietatiSortate.toArray(new Proprietate[0]);
    }

    public Proprietate getById(int id) {
        auditCurrentAction();
        return proprietatiById.get(id);
    }

    public boolean updateById(int id, String titlu, String oras, String adresa, double pret, String tipTranzactie, double suprafata, boolean disponibila, int idAgentie) {
        auditCurrentAction();
        Proprietate proprietate = getById(id);
        if (proprietate == null) {
            System.out.println("Proprietatea nu a fost gasita.");
            return false;
        }
        AgentieImobiliara agentie = agentieService.getById(idAgentie);
        if (agentie == null) {
            System.out.println("Agentia imobiliara nu exista. Proprietatea nu a fost actualizata.");
            return false;
        }
        if (!esteValid(titlu, oras, adresa, pret, tipTranzactie, suprafata)) {
            return false;
        }
        String orasVechi = proprietate.getOras();
        proprietate.setTitlu(titlu.trim());
        proprietate.setOras(oras.trim());
        proprietate.setAdresa(adresa.trim());
        proprietate.setPret(pret);
        proprietate.setTipTranzactie(tipTranzactie.trim());
        proprietate.setSuprafata(suprafata);
        proprietate.setDisponibila(disponibila);
        proprietate.setAgentieImobiliara(agentie);
        reindexeaza(proprietate, orasVechi);
        afiseazaConfirmareActualizare("Proprietatea", id);
        return true;
    }

    public boolean deleteById(int id) {
        auditCurrentAction();
        Proprietate proprietate = proprietatiById.remove(id);
        if (proprietate == null) {
            System.out.println("Proprietatea nu a fost gasita.");
            return false;
        }
        proprietati.remove(proprietate);
        proprietatiSortate.remove(proprietate);
        List<Proprietate> dinOras = proprietatiByOras.get(normalizeKey(proprietate.getOras()));
        if (dinOras != null) {
            dinOras.remove(proprietate);
            if (dinOras.isEmpty()) {
                proprietatiByOras.remove(normalizeKey(proprietate.getOras()));
            }
        }
        afiseazaConfirmareStergere("Proprietatea", id);
        return true;
    }

    public Proprietate[] getByNume(String titlu) {
        auditCurrentAction();
        if (titlu == null || titlu.isBlank()) {
            return new Proprietate[0];
        }
        List<Proprietate> rezultat = new ArrayList<>();
        for (Proprietate proprietate : proprietati) {
            if (proprietate.getTitlu().equalsIgnoreCase(titlu.trim())) {
                rezultat.add(proprietate);
            }
        }
        return rezultat.toArray(new Proprietate[0]);
    }

    public Proprietate[] getByOras(String oras) {
        auditCurrentAction();
        if (oras == null || oras.isBlank()) {
            return new Proprietate[0];
        }
        List<Proprietate> rezultat = proprietatiByOras.get(normalizeKey(oras));
        if (rezultat == null) {
            return new Proprietate[0];
        }
        return rezultat.toArray(new Proprietate[0]);
    }

    public Proprietate[] getByBugetMaxim(double bugetMaxim) {
        auditCurrentAction();
        if (bugetMaxim < 0) {
            return new Proprietate[0];
        }
        List<Proprietate> rezultat = new ArrayList<>();
        for (Proprietate proprietate : proprietati) {
            if (proprietate.getPret() <= bugetMaxim) {
                rezultat.add(proprietate);
            }
        }
        return rezultat.toArray(new Proprietate[0]);
    }

    public Proprietate[] getByTipTranzactie(String tipTranzactie) {
        auditCurrentAction();
        if (tipTranzactie == null || tipTranzactie.isBlank()) {
            return new Proprietate[0];
        }
        List<Proprietate> rezultat = new ArrayList<>();
        for (Proprietate proprietate : proprietati) {
            if (proprietate.getTipTranzactie().equalsIgnoreCase(tipTranzactie.trim())) {
                rezultat.add(proprietate);
            }
        }
        return rezultat.toArray(new Proprietate[0]);
    }

    public Proprietate[] getByOrasBugetSiTranzactie(String oras, double bugetMaxim, String tipTranzactie) {
        auditCurrentAction();
        if (oras == null || oras.isBlank() || tipTranzactie == null || tipTranzactie.isBlank() || bugetMaxim < 0) {
            return new Proprietate[0];
        }
        List<Proprietate> rezultat = new ArrayList<>();
        for (Proprietate proprietate : getByOras(oras)) {
            if (proprietate.getPret() <= bugetMaxim && proprietate.getTipTranzactie().equalsIgnoreCase(tipTranzactie.trim())) {
                rezultat.add(proprietate);
            }
        }
        return rezultat.toArray(new Proprietate[0]);
    }

    public Proprietate[] getByAgentie(int idAgentie) {
        auditCurrentAction();
        List<Proprietate> rezultat = new ArrayList<>();
        for (Proprietate proprietate : proprietati) {
            if (proprietate.getAgentieImobiliara() != null && proprietate.getAgentieImobiliara().getId() == idAgentie) {
                rezultat.add(proprietate);
            }
        }
        return rezultat.toArray(new Proprietate[0]);
    }

    public double calculeazaPretMediu() {
        auditCurrentAction();
        if (proprietati.isEmpty()) {
            return 0;
        }
        double suma = 0;
        for (Proprietate proprietate : proprietati) {
            suma += proprietate.getPret();
        }
        return suma / proprietati.size();
    }

    private String normalizeKey(String valoare) {
        return valoare == null ? "" : valoare.trim().toLowerCase();
    }

    private boolean esteValid(String titlu, String oras, String adresa, double pret, String tipTranzactie, double suprafata) {
        if (titlu == null || titlu.isBlank() || oras == null || oras.isBlank() || adresa == null || adresa.isBlank() || tipTranzactie == null || tipTranzactie.isBlank()) {
            System.out.println("Date invalide pentru proprietate.");
            return false;
        }
        if (pret < 0 || suprafata <= 0) {
            System.out.println("Pretul si suprafata trebuie sa aiba valori corecte.");
            return false;
        }
        return true;
    }

    public void adminMenu(Scanner sc) {
        boolean inapoi = false;
        while (!inapoi) {
            System.out.println("\n=== ADMIN PROPRIETATI ===");
            System.out.println("1. Adauga");
            System.out.println("2. Afiseaza toate");
            System.out.println("3. Cauta dupa ID");
            System.out.println("4. Cauta dupa nume");
            System.out.println("5. Filtrare dupa oras");
            System.out.println("6. Filtrare dupa buget maxim");
            System.out.println("7. Filtrare dupa tip tranzactie");
            System.out.println("8. Filtrare multi-criteriu");
            System.out.println("9. Afiseaza proprietatile unei agentii");
            System.out.println("10. Afiseaza pretul mediu");
            System.out.println("11. Update dupa ID");
            System.out.println("12. Sterge dupa ID");
            System.out.println("0. Inapoi");

            int optiune = citesteInt(sc, "Alegeti optiunea: ");
            switch (optiune) {
                case 1 -> add(citesteString(sc, "Introduceti titlul proprietatii: "), citesteString(sc, "Introduceti orasul: "), citesteString(sc, "Introduceti adresa proprietatii: "), citesteDouble(sc, "Introduceti pretul: "), citesteString(sc, "Introduceti tipul tranzactiei: "), citesteDouble(sc, "Introduceti suprafata: "), citesteBoolean(sc, "Proprietatea este disponibila? (da/nu): "), citesteInt(sc, "Introduceti ID-ul agentiei imobiliare: "));
                case 2 -> afiseazaObiecte(listAll(), "Nu exista proprietati in sistem.");
                case 3 -> afiseazaById(sc);
                case 4 -> afiseazaObiecte(getByNume(citesteString(sc, "Introduceti titlul proprietatii: ")), "Nu exista proprietati cu acest titlu.");
                case 5 -> afiseazaObiecte(getByOras(citesteString(sc, "Introduceti orasul: ")), "Nu exista proprietati in acest oras.");
                case 6 -> afiseazaObiecte(getByBugetMaxim(citesteDouble(sc, "Introduceti bugetul maxim: ")), "Nu exista proprietati in acest buget.");
                case 7 -> afiseazaObiecte(getByTipTranzactie(citesteString(sc, "Introduceti tipul tranzactiei: ")), "Nu exista proprietati cu acest tip de tranzactie.");
                case 8 -> afiseazaObiecte(getByOrasBugetSiTranzactie(citesteString(sc, "Introduceti orasul: "), citesteDouble(sc, "Introduceti bugetul maxim: "), citesteString(sc, "Introduceti tipul tranzactiei: ")), "Nu exista proprietati pentru criteriile introduse.");
                case 9 -> afiseazaObiecte(getByAgentie(citesteInt(sc, "Introduceti ID-ul agentiei imobiliare: ")), "Nu exista proprietati pentru aceasta agentie.");
                case 10 -> System.out.println("Pretul mediu al proprietatilor este: " + calculeazaPretMediu());
                case 11 -> updateById(citesteInt(sc, "Introduceti ID-ul proprietatii de actualizat: "), citesteString(sc, "Introduceti titlul proprietatii: "), citesteString(sc, "Introduceti orasul: "), citesteString(sc, "Introduceti adresa proprietatii: "), citesteDouble(sc, "Introduceti pretul: "), citesteString(sc, "Introduceti tipul tranzactiei: "), citesteDouble(sc, "Introduceti suprafata: "), citesteBoolean(sc, "Proprietatea este disponibila? (da/nu): "), citesteInt(sc, "Introduceti ID-ul agentiei imobiliare: "));
                case 12 -> deleteById(citesteInt(sc, "Introduceti ID-ul proprietatii de sters: "));
                case 0 -> inapoi = true;
                default -> System.out.println("Optiune invalida.");
            }
        }
    }

    public void userMenu(Scanner sc) {
        boolean inapoi = false;
        while (!inapoi) {
            System.out.println("\n=== USER PROPRIETATI ===");
            System.out.println("1. Afiseaza toate");
            System.out.println("2. Cauta dupa ID");
            System.out.println("3. Cauta dupa nume");
            System.out.println("4. Filtrare dupa oras");
            System.out.println("5. Filtrare dupa buget maxim");
            System.out.println("6. Filtrare dupa tip tranzactie");
            System.out.println("7. Filtrare multi-criteriu");
            System.out.println("8. Afiseaza proprietatile unei agentii");
            System.out.println("9. Afiseaza pretul mediu");
            System.out.println("0. Inapoi");

            int optiune = citesteInt(sc, "Alegeti optiunea: ");
            switch (optiune) {
                case 1 -> afiseazaObiecte(listAll(), "Nu exista proprietati in sistem.");
                case 2 -> afiseazaById(sc);
                case 3 -> afiseazaObiecte(getByNume(citesteString(sc, "Introduceti titlul proprietatii: ")), "Nu exista proprietati cu acest titlu.");
                case 4 -> afiseazaObiecte(getByOras(citesteString(sc, "Introduceti orasul: ")), "Nu exista proprietati in acest oras.");
                case 5 -> afiseazaObiecte(getByBugetMaxim(citesteDouble(sc, "Introduceti bugetul maxim: ")), "Nu exista proprietati in acest buget.");
                case 6 -> afiseazaObiecte(getByTipTranzactie(citesteString(sc, "Introduceti tipul tranzactiei: ")), "Nu exista proprietati cu acest tip de tranzactie.");
                case 7 -> afiseazaObiecte(getByOrasBugetSiTranzactie(citesteString(sc, "Introduceti orasul: "), citesteDouble(sc, "Introduceti bugetul maxim: "), citesteString(sc, "Introduceti tipul tranzactiei: ")), "Nu exista proprietati pentru criteriile introduse.");
                case 8 -> afiseazaObiecte(getByAgentie(citesteInt(sc, "Introduceti ID-ul agentiei imobiliare: ")), "Nu exista proprietati pentru aceasta agentie.");
                case 9 -> System.out.println("Pretul mediu al proprietatilor este: " + calculeazaPretMediu());
                case 0 -> inapoi = true;
                default -> System.out.println("Optiune invalida.");
            }
        }
    }

    private void afiseazaById(Scanner sc) {
        Proprietate proprietate = getById(citesteInt(sc, "Introduceti ID-ul proprietatii: "));
        if (proprietate == null) {
            System.out.println("Proprietatea nu a fost gasita.");
            return;
        }
        System.out.println(proprietate);
    }
}
