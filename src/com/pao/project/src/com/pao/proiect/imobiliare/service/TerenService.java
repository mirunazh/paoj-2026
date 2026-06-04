package com.pao.proiect.imobiliare.service;

import com.pao.proiect.imobiliare.model.AgentieImobiliara;
import com.pao.proiect.imobiliare.model.Teren;

import java.util.Scanner;

public class TerenService extends ServiceSupport {
    private Teren[] terenuri;
    private int nextId = 70;
    private final AgentieService agentieService = AgentieService.getInstance();
    private final ProprietateService proprietateService = ProprietateService.getInstance();

    private TerenService() {
        this.terenuri = new Teren[0];
    }

    private static class Holder {
        private static final TerenService INSTANCE = new TerenService();
    }

    public static TerenService getInstance() {
        return Holder.INSTANCE;
    }

    private int genereazaId() {
        return nextId++;
    }

    public Teren add(String titlu, String oras, String adresa, double pret, String tipTranzactie, double suprafata, boolean disponibila, int idAgentie, boolean intravilan, double deschidere, boolean utilitati) {
        auditCurrentAction();
        AgentieImobiliara agentie = agentieService.getById(idAgentie);
        if (agentie == null) {
            System.out.println("Agentia imobiliara nu exista. Terenul nu a fost creat.");
            return null;
        }
        if (!esteValid(titlu, oras, adresa, pret, tipTranzactie, suprafata, deschidere)) {
            return null;
        }
        int id = genereazaId();
        Teren teren = new Teren(id, titlu, oras, adresa, pret, tipTranzactie, suprafata, disponibila, agentie, intravilan, deschidere, utilitati);
        Teren[] nou = new Teren[terenuri.length + 1];
        System.arraycopy(terenuri, 0, nou, 0, terenuri.length);
        nou[nou.length - 1] = teren;
        terenuri = nou;
        proprietateService.addExisting(teren);
        afiseazaConfirmareCreare("Terenul", id);
        return teren;
    }

    public Teren[] listAll() {
        auditCurrentAction();
        return terenuri;
    }

    public Teren getById(int id) {
        auditCurrentAction();
        for (Teren teren : terenuri) {
            if (teren.getId() == id) {
                return teren;
            }
        }
        return null;
    }

    public boolean updateById(int id, String titlu, String oras, String adresa, double pret, String tipTranzactie, double suprafata, boolean disponibila, int idAgentie, boolean intravilan, double deschidere, boolean utilitati) {
        auditCurrentAction();
        Teren teren = getById(id);
        if (teren == null) {
            System.out.println("Terenul nu a fost gasit.");
            return false;
        }
        AgentieImobiliara agentie = agentieService.getById(idAgentie);
        if (agentie == null) {
            System.out.println("Agentia imobiliara nu exista. Terenul nu a fost actualizat.");
            return false;
        }
        if (!esteValid(titlu, oras, adresa, pret, tipTranzactie, suprafata, deschidere)) {
            return false;
        }
        teren.setTitlu(titlu);
        teren.setOras(oras);
        teren.setAdresa(adresa);
        teren.setPret(pret);
        teren.setTipTranzactie(tipTranzactie);
        teren.setSuprafata(suprafata);
        teren.setDisponibila(disponibila);
        teren.setAgentieImobiliara(agentie);
        teren.setIntravilan(intravilan);
        teren.setDeschidere(deschidere);
        teren.setUtilitati(utilitati);
        afiseazaConfirmareActualizare("Terenul", id);
        return true;
    }

    public boolean deleteById(int id) {
        auditCurrentAction();
        int pozitie = -1;
        for (int i = 0; i < terenuri.length; i++) {
            if (terenuri[i].getId() == id) {
                pozitie = i;
                break;
            }
        }
        if (pozitie == -1) {
            System.out.println("Terenul nu a fost gasit.");
            return false;
        }
        Teren[] nou = new Teren[terenuri.length - 1];
        for (int i = 0, j = 0; i < terenuri.length; i++) {
            if (i != pozitie) {
                nou[j++] = terenuri[i];
            }
        }
        terenuri = nou;
        proprietateService.deleteById(id);
        afiseazaConfirmareStergere("Terenul", id);
        return true;
    }

    public Teren[] getByNume(String titlu) {
        auditCurrentAction();
        int count = 0;
        for (Teren teren : terenuri) {
            if (teren.getTitlu().equalsIgnoreCase(titlu)) {
                count++;
            }
        }
        Teren[] rezultat = new Teren[count];
        for (int i = 0, j = 0; i < terenuri.length; i++) {
            if (terenuri[i].getTitlu().equalsIgnoreCase(titlu)) {
                rezultat[j++] = terenuri[i];
            }
        }
        return rezultat;
    }

    public Teren[] getByIntravilan(boolean intravilan) {
        auditCurrentAction();
        int count = 0;
        for (Teren teren : terenuri) {
            if (teren.isIntravilan() == intravilan) {
                count++;
            }
        }
        Teren[] rezultat = new Teren[count];
        for (int i = 0, j = 0; i < terenuri.length; i++) {
            if (terenuri[i].isIntravilan() == intravilan) {
                rezultat[j++] = terenuri[i];
            }
        }
        return rezultat;
    }

    public Teren[] getBySuprafata(double suprafataMinima) {
        auditCurrentAction();
        int count = 0;
        for (Teren teren : terenuri) {
            if (teren.getSuprafata() >= suprafataMinima) {
                count++;
            }
        }
        Teren[] rezultat = new Teren[count];
        for (int i = 0, j = 0; i < terenuri.length; i++) {
            if (terenuri[i].getSuprafata() >= suprafataMinima) {
                rezultat[j++] = terenuri[i];
            }
        }
        return rezultat;
    }

    public Teren[] getByOrasTipSiSuprafata(String oras, boolean intravilan, double suprafataMinima) {
        auditCurrentAction();
        int count = 0;
        for (Teren teren : terenuri) {
            if (teren.getOras().equalsIgnoreCase(oras) && teren.isIntravilan() == intravilan && teren.getSuprafata() >= suprafataMinima) {
                count++;
            }
        }
        Teren[] rezultat = new Teren[count];
        for (int i = 0, j = 0; i < terenuri.length; i++) {
            if (terenuri[i].getOras().equalsIgnoreCase(oras) && terenuri[i].isIntravilan() == intravilan && terenuri[i].getSuprafata() >= suprafataMinima) {
                rezultat[j++] = terenuri[i];
            }
        }
        return rezultat;
    }

    private boolean esteValid(String titlu, String oras, String adresa, double pret, String tipTranzactie, double suprafata, double deschidere) {
        if (titlu == null || titlu.isBlank() || oras == null || oras.isBlank() || adresa == null || adresa.isBlank() || tipTranzactie == null || tipTranzactie.isBlank()) {
            System.out.println("Date invalide pentru teren.");
            return false;
        }
        if (pret < 0 || suprafata <= 0 || deschidere < 0) {
            System.out.println("Valorile numerice pentru teren sunt invalide.");
            return false;
        }
        return true;
    }

    public void adminMenu(Scanner sc) {
        boolean inapoi = false;
        while (!inapoi) {
            System.out.println("\n=== ADMIN TERENURI ===");
            System.out.println("1. Adauga");
            System.out.println("2. Afiseaza toate");
            System.out.println("3. Cauta dupa ID");
            System.out.println("4. Cauta dupa nume");
            System.out.println("5. Filtrare intravilan/extravilan");
            System.out.println("6. Filtrare dupa suprafata");
            System.out.println("7. Filtrare multi-criteriu");
            System.out.println("8. Update dupa ID");
            System.out.println("9. Sterge dupa ID");
            System.out.println("0. Inapoi");

            int optiune = citesteInt(sc, "Alegeti optiunea: ");
            switch (optiune) {
                case 1 -> add(citesteString(sc, "Introduceti titlul terenului: "), citesteString(sc, "Introduceti orasul: "), citesteString(sc, "Introduceti adresa terenului: "), citesteDouble(sc, "Introduceti pretul: "), citesteString(sc, "Introduceti tipul tranzactiei: "), citesteDouble(sc, "Introduceti suprafata: "), citesteBoolean(sc, "Terenul este disponibil? (da/nu): "), citesteInt(sc, "Introduceti ID-ul agentiei imobiliare: "), citesteBoolean(sc, "Terenul este intravilan? (da/nu): "), citesteDouble(sc, "Introduceti deschiderea terenului: "), citesteBoolean(sc, "Terenul are utilitati? (da/nu): "));
                case 2 -> afiseazaObiecte(listAll(), "Nu exista terenuri in sistem.");
                case 3 -> afiseazaById(sc);
                case 4 -> afiseazaObiecte(getByNume(citesteString(sc, "Introduceti titlul terenului: ")), "Nu exista terenuri cu acest titlu.");
                case 5 -> afiseazaObiecte(getByIntravilan(citesteBoolean(sc, "Introduceti da pentru intravilan sau nu pentru extravilan: ")), "Nu exista terenuri pentru acest criteriu.");
                case 6 -> afiseazaObiecte(getBySuprafata(citesteDouble(sc, "Introduceti suprafata minima: ")), "Nu exista terenuri pentru aceasta suprafata.");
                case 7 -> afiseazaObiecte(getByOrasTipSiSuprafata(citesteString(sc, "Introduceti orasul: "), citesteBoolean(sc, "Terenul trebuie sa fie intravilan? (da/nu): "), citesteDouble(sc, "Introduceti suprafata minima: ")), "Nu exista terenuri pentru criteriile introduse.");
                case 8 -> updateById(citesteInt(sc, "Introduceti ID-ul terenului de actualizat: "), citesteString(sc, "Introduceti titlul terenului: "), citesteString(sc, "Introduceti orasul: "), citesteString(sc, "Introduceti adresa terenului: "), citesteDouble(sc, "Introduceti pretul: "), citesteString(sc, "Introduceti tipul tranzactiei: "), citesteDouble(sc, "Introduceti suprafata: "), citesteBoolean(sc, "Terenul este disponibil? (da/nu): "), citesteInt(sc, "Introduceti ID-ul agentiei imobiliare: "), citesteBoolean(sc, "Terenul este intravilan? (da/nu): "), citesteDouble(sc, "Introduceti deschiderea terenului: "), citesteBoolean(sc, "Terenul are utilitati? (da/nu): "));
                case 9 -> deleteById(citesteInt(sc, "Introduceti ID-ul terenului de sters: "));
                case 0 -> inapoi = true;
                default -> System.out.println("Optiune invalida.");
            }
        }
    }

    public void userMenu(Scanner sc) {
        boolean inapoi = false;
        while (!inapoi) {
            System.out.println("\n=== USER TERENURI ===");
            System.out.println("1. Afiseaza toate");
            System.out.println("2. Cauta dupa ID");
            System.out.println("3. Cauta dupa nume");
            System.out.println("4. Filtrare intravilan/extravilan");
            System.out.println("5. Filtrare dupa suprafata");
            System.out.println("6. Filtrare multi-criteriu");
            System.out.println("0. Inapoi");

            int optiune = citesteInt(sc, "Alegeti optiunea: ");
            switch (optiune) {
                case 1 -> afiseazaObiecte(listAll(), "Nu exista terenuri in sistem.");
                case 2 -> afiseazaById(sc);
                case 3 -> afiseazaObiecte(getByNume(citesteString(sc, "Introduceti titlul terenului: ")), "Nu exista terenuri cu acest titlu.");
                case 4 -> afiseazaObiecte(getByIntravilan(citesteBoolean(sc, "Introduceti da pentru intravilan sau nu pentru extravilan: ")), "Nu exista terenuri pentru acest criteriu.");
                case 5 -> afiseazaObiecte(getBySuprafata(citesteDouble(sc, "Introduceti suprafata minima: ")), "Nu exista terenuri pentru aceasta suprafata.");
                case 6 -> afiseazaObiecte(getByOrasTipSiSuprafata(citesteString(sc, "Introduceti orasul: "), citesteBoolean(sc, "Terenul trebuie sa fie intravilan? (da/nu): "), citesteDouble(sc, "Introduceti suprafata minima: ")), "Nu exista terenuri pentru criteriile introduse.");
                case 0 -> inapoi = true;
                default -> System.out.println("Optiune invalida.");
            }
        }
    }

    private void afiseazaById(Scanner sc) {
        Teren teren = getById(citesteInt(sc, "Introduceti ID-ul terenului: "));
        if (teren == null) {
            System.out.println("Terenul nu a fost gasit.");
            return;
        }
        System.out.println(teren);
    }
}
