package com.pao.proiect.imobiliare.service;

import com.pao.proiect.imobiliare.model.AgentieImobiliara;
import com.pao.proiect.imobiliare.model.Apartament;

import java.util.Scanner;

public class ApartamentService extends ServiceSupport {
    private Apartament[] apartamente;
    private int nextId = 50;
    private final AgentieService agentieService = AgentieService.getInstance();
    private final ProprietateService proprietateService = ProprietateService.getInstance();

    private ApartamentService() {
        this.apartamente = new Apartament[0];
    }

    private static class Holder {
        private static final ApartamentService INSTANCE = new ApartamentService();
    }

    public static ApartamentService getInstance() {
        return Holder.INSTANCE;
    }

    private int genereazaId() {
        return nextId++;
    }

    public Apartament add(String titlu, String oras, String adresa, double pret, String tipTranzactie, double suprafata, boolean disponibila, int idAgentie, int numarCamere, int etaj, int numarBai, boolean balcon) {
        auditCurrentAction();
        AgentieImobiliara agentie = agentieService.getById(idAgentie);
        if (agentie == null) {
            System.out.println("Agentia imobiliara nu exista. Apartamentul nu a fost creat.");
            return null;
        }
        if (!esteValid(titlu, oras, adresa, pret, tipTranzactie, suprafata, numarCamere, numarBai)) {
            return null;
        }
        int id = genereazaId();
        Apartament apartament = new Apartament(id, titlu, oras, adresa, pret, tipTranzactie, suprafata, disponibila, agentie, numarCamere, etaj, numarBai, balcon);
        Apartament[] nou = new Apartament[apartamente.length + 1];
        System.arraycopy(apartamente, 0, nou, 0, apartamente.length);
        nou[nou.length - 1] = apartament;
        apartamente = nou;
        proprietateService.addExisting(apartament);
        afiseazaConfirmareCreare("Apartamentul", id);
        return apartament;
    }

    public Apartament[] listAll() {
        auditCurrentAction();
        return apartamente;
    }

    public Apartament getById(int id) {
        auditCurrentAction();
        for (Apartament apartament : apartamente) {
            if (apartament.getId() == id) {
                return apartament;
            }
        }
        return null;
    }

    public boolean updateById(int id, String titlu, String oras, String adresa, double pret, String tipTranzactie, double suprafata, boolean disponibila, int idAgentie, int numarCamere, int etaj, int numarBai, boolean balcon) {
        auditCurrentAction();
        Apartament apartament = getById(id);
        if (apartament == null) {
            System.out.println("Apartamentul nu a fost gasit.");
            return false;
        }
        AgentieImobiliara agentie = agentieService.getById(idAgentie);
        if (agentie == null) {
            System.out.println("Agentia imobiliara nu exista. Apartamentul nu a fost actualizat.");
            return false;
        }
        if (!esteValid(titlu, oras, adresa, pret, tipTranzactie, suprafata, numarCamere, numarBai)) {
            return false;
        }
        apartament.setTitlu(titlu);
        apartament.setOras(oras);
        apartament.setAdresa(adresa);
        apartament.setPret(pret);
        apartament.setTipTranzactie(tipTranzactie);
        apartament.setSuprafata(suprafata);
        apartament.setDisponibila(disponibila);
        apartament.setAgentieImobiliara(agentie);
        apartament.setNumarCamere(numarCamere);
        apartament.setEtaj(etaj);
        apartament.setNumarBai(numarBai);
        apartament.setBalcon(balcon);
        afiseazaConfirmareActualizare("Apartamentul", id);
        return true;
    }

    public boolean deleteById(int id) {
        auditCurrentAction();
        int pozitie = -1;
        for (int i = 0; i < apartamente.length; i++) {
            if (apartamente[i].getId() == id) {
                pozitie = i;
                break;
            }
        }
        if (pozitie == -1) {
            System.out.println("Apartamentul nu a fost gasit.");
            return false;
        }
        Apartament[] nou = new Apartament[apartamente.length - 1];
        for (int i = 0, j = 0; i < apartamente.length; i++) {
            if (i != pozitie) {
                nou[j++] = apartamente[i];
            }
        }
        apartamente = nou;
        proprietateService.deleteById(id);
        afiseazaConfirmareStergere("Apartamentul", id);
        return true;
    }

    public Apartament[] getByNume(String titlu) {
        auditCurrentAction();
        int count = 0;
        for (Apartament apartament : apartamente) {
            if (apartament.getTitlu().equalsIgnoreCase(titlu)) {
                count++;
            }
        }
        Apartament[] rezultat = new Apartament[count];
        for (int i = 0, j = 0; i < apartamente.length; i++) {
            if (apartamente[i].getTitlu().equalsIgnoreCase(titlu)) {
                rezultat[j++] = apartamente[i];
            }
        }
        return rezultat;
    }

    public Apartament[] getByCamere(int camere) {
        auditCurrentAction();
        int count = 0;
        for (Apartament apartament : apartamente) {
            if (apartament.getNumarCamere() == camere) {
                count++;
            }
        }
        Apartament[] rezultat = new Apartament[count];
        for (int i = 0, j = 0; i < apartamente.length; i++) {
            if (apartamente[i].getNumarCamere() == camere) {
                rezultat[j++] = apartamente[i];
            }
        }
        return rezultat;
    }

    public Apartament[] getByEtaj(int etaj) {
        auditCurrentAction();
        int count = 0;
        for (Apartament apartament : apartamente) {
            if (apartament.getEtaj() == etaj) {
                count++;
            }
        }
        Apartament[] rezultat = new Apartament[count];
        for (int i = 0, j = 0; i < apartamente.length; i++) {
            if (apartamente[i].getEtaj() == etaj) {
                rezultat[j++] = apartamente[i];
            }
        }
        return rezultat;
    }

    public Apartament[] getByOrasCamereEtaj(String oras, int camere, int etaj) {
        auditCurrentAction();
        int count = 0;
        for (Apartament apartament : apartamente) {
            if (apartament.getOras().equalsIgnoreCase(oras) && apartament.getNumarCamere() == camere && apartament.getEtaj() == etaj) {
                count++;
            }
        }
        Apartament[] rezultat = new Apartament[count];
        for (int i = 0, j = 0; i < apartamente.length; i++) {
            if (apartamente[i].getOras().equalsIgnoreCase(oras) && apartamente[i].getNumarCamere() == camere && apartamente[i].getEtaj() == etaj) {
                rezultat[j++] = apartamente[i];
            }
        }
        return rezultat;
    }

    private boolean esteValid(String titlu, String oras, String adresa, double pret, String tipTranzactie, double suprafata, int numarCamere, int numarBai) {
        if (titlu == null || titlu.isBlank() || oras == null || oras.isBlank() || adresa == null || adresa.isBlank() || tipTranzactie == null || tipTranzactie.isBlank()) {
            System.out.println("Date invalide pentru apartament.");
            return false;
        }
        if (pret < 0 || suprafata <= 0 || numarCamere <= 0 || numarBai <= 0) {
            System.out.println("Valorile numerice pentru apartament sunt invalide.");
            return false;
        }
        return true;
    }

    public void adminMenu(Scanner sc) {
        boolean inapoi = false;
        while (!inapoi) {
            System.out.println("\n=== ADMIN APARTAMENTE ===");
            System.out.println("1. Adauga");
            System.out.println("2. Afiseaza toate");
            System.out.println("3. Cauta dupa ID");
            System.out.println("4. Cauta dupa nume");
            System.out.println("5. Filtrare dupa camere");
            System.out.println("6. Filtrare dupa etaj");
            System.out.println("7. Filtrare multi-criteriu");
            System.out.println("8. Update dupa ID");
            System.out.println("9. Sterge dupa ID");
            System.out.println("0. Inapoi");

            int optiune = citesteInt(sc, "Alegeti optiunea: ");
            switch (optiune) {
                case 1 -> add(citesteString(sc, "Introduceti titlul apartamentului: "), citesteString(sc, "Introduceti orasul: "), citesteString(sc, "Introduceti adresa apartamentului: "), citesteDouble(sc, "Introduceti pretul: "), citesteString(sc, "Introduceti tipul tranzactiei: "), citesteDouble(sc, "Introduceti suprafata: "), citesteBoolean(sc, "Apartamentul este disponibil? (da/nu): "), citesteInt(sc, "Introduceti ID-ul agentiei imobiliare: "), citesteInt(sc, "Introduceti numarul de camere: "), citesteInt(sc, "Introduceti etajul: "), citesteInt(sc, "Introduceti numarul de bai: "), citesteBoolean(sc, "Apartamentul are balcon? (da/nu): "));
                case 2 -> afiseazaObiecte(listAll(), "Nu exista apartamente in sistem.");
                case 3 -> afiseazaById(sc);
                case 4 -> afiseazaObiecte(getByNume(citesteString(sc, "Introduceti titlul apartamentului: ")), "Nu exista apartamente cu acest titlu.");
                case 5 -> afiseazaObiecte(getByCamere(citesteInt(sc, "Introduceti numarul de camere: ")), "Nu exista apartamente cu acest numar de camere.");
                case 6 -> afiseazaObiecte(getByEtaj(citesteInt(sc, "Introduceti etajul: ")), "Nu exista apartamente la acest etaj.");
                case 7 -> afiseazaObiecte(getByOrasCamereEtaj(citesteString(sc, "Introduceti orasul: "), citesteInt(sc, "Introduceti numarul de camere: "), citesteInt(sc, "Introduceti etajul: ")), "Nu exista apartamente pentru criteriile introduse.");
                case 8 -> updateById(citesteInt(sc, "Introduceti ID-ul apartamentului de actualizat: "), citesteString(sc, "Introduceti titlul apartamentului: "), citesteString(sc, "Introduceti orasul: "), citesteString(sc, "Introduceti adresa apartamentului: "), citesteDouble(sc, "Introduceti pretul: "), citesteString(sc, "Introduceti tipul tranzactiei: "), citesteDouble(sc, "Introduceti suprafata: "), citesteBoolean(sc, "Apartamentul este disponibil? (da/nu): "), citesteInt(sc, "Introduceti ID-ul agentiei imobiliare: "), citesteInt(sc, "Introduceti numarul de camere: "), citesteInt(sc, "Introduceti etajul: "), citesteInt(sc, "Introduceti numarul de bai: "), citesteBoolean(sc, "Apartamentul are balcon? (da/nu): "));
                case 9 -> deleteById(citesteInt(sc, "Introduceti ID-ul apartamentului de sters: "));
                case 0 -> inapoi = true;
                default -> System.out.println("Optiune invalida.");
            }
        }
    }

    public void userMenu(Scanner sc) {
        boolean inapoi = false;
        while (!inapoi) {
            System.out.println("\n=== USER APARTAMENTE ===");
            System.out.println("1. Afiseaza toate");
            System.out.println("2. Cauta dupa ID");
            System.out.println("3. Cauta dupa nume");
            System.out.println("4. Filtrare dupa camere");
            System.out.println("5. Filtrare dupa etaj");
            System.out.println("6. Filtrare multi-criteriu");
            System.out.println("0. Inapoi");

            int optiune = citesteInt(sc, "Alegeti optiunea: ");
            switch (optiune) {
                case 1 -> afiseazaObiecte(listAll(), "Nu exista apartamente in sistem.");
                case 2 -> afiseazaById(sc);
                case 3 -> afiseazaObiecte(getByNume(citesteString(sc, "Introduceti titlul apartamentului: ")), "Nu exista apartamente cu acest titlu.");
                case 4 -> afiseazaObiecte(getByCamere(citesteInt(sc, "Introduceti numarul de camere: ")), "Nu exista apartamente cu acest numar de camere.");
                case 5 -> afiseazaObiecte(getByEtaj(citesteInt(sc, "Introduceti etajul: ")), "Nu exista apartamente la acest etaj.");
                case 6 -> afiseazaObiecte(getByOrasCamereEtaj(citesteString(sc, "Introduceti orasul: "), citesteInt(sc, "Introduceti numarul de camere: "), citesteInt(sc, "Introduceti etajul: ")), "Nu exista apartamente pentru criteriile introduse.");
                case 0 -> inapoi = true;
                default -> System.out.println("Optiune invalida.");
            }
        }
    }

    private void afiseazaById(Scanner sc) {
        Apartament apartament = getById(citesteInt(sc, "Introduceti ID-ul apartamentului: "));
        if (apartament == null) {
            System.out.println("Apartamentul nu a fost gasit.");
            return;
        }
        System.out.println(apartament);
    }
}
