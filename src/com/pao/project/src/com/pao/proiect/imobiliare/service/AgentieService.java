package com.pao.proiect.imobiliare.service;

import com.pao.proiect.imobiliare.model.AgentieImobiliara;

import java.util.Scanner;

public class AgentieService extends ServiceSupport {
    private AgentieImobiliara[] agentii;
    private int nextId = 10;

    private AgentieService() {
        this.agentii = new AgentieImobiliara[0];
    }

    private static class Holder {
        private static final AgentieService INSTANCE = new AgentieService();
    }

    public static AgentieService getInstance() {
        return Holder.INSTANCE;
    }

    private int genereazaId() {
        return nextId++;
    }

    public AgentieImobiliara add(String nume, String adresa, String telefon, String email, int anDeschidere, String[] oraseActive) {
        auditCurrentAction();
        if (!esteValid(nume, adresa, telefon, email, anDeschidere)) {
            return null;
        }
        int id = genereazaId();
        AgentieImobiliara agentie = new AgentieImobiliara(id, nume, adresa, telefon, email, anDeschidere, oraseActive);
        AgentieImobiliara[] nou = new AgentieImobiliara[agentii.length + 1];
        System.arraycopy(agentii, 0, nou, 0, agentii.length);
        nou[nou.length - 1] = agentie;
        agentii = nou;
        afiseazaConfirmareCreare("Agentia", id);
        return agentie;
    }

    public AgentieImobiliara[] listAll() {
        auditCurrentAction();
        return agentii;
    }

    public AgentieImobiliara getById(int id) {
        auditCurrentAction();
        for (AgentieImobiliara agentie : agentii) {
            if (agentie.getId() == id) {
                return agentie;
            }
        }
        return null;
    }

    public boolean updateById(int id, String nume, String adresa, String telefon, String email, int anDeschidere, String[] oraseActive) {
        auditCurrentAction();
        AgentieImobiliara agentie = getById(id);
        if (agentie == null) {
            System.out.println("Agentia nu a fost gasita.");
            return false;
        }
        if (!esteValid(nume, adresa, telefon, email, anDeschidere)) {
            return false;
        }
        agentie.setNume(nume);
        agentie.setAdresa(adresa);
        agentie.setTelefon(telefon);
        agentie.setEmail(email);
        agentie.setAnDeschidere(anDeschidere);
        agentie.setOraseActive(oraseActive);
        afiseazaConfirmareActualizare("Agentia", id);
        return true;
    }

    public boolean deleteById(int id) {
        auditCurrentAction();
        int pozitie = -1;
        for (int i = 0; i < agentii.length; i++) {
            if (agentii[i].getId() == id) {
                pozitie = i;
                break;
            }
        }
        if (pozitie == -1) {
            System.out.println("Agentia nu a fost gasita.");
            return false;
        }
        AgentieImobiliara[] nou = new AgentieImobiliara[agentii.length - 1];
        for (int i = 0, j = 0; i < agentii.length; i++) {
            if (i != pozitie) {
                nou[j++] = agentii[i];
            }
        }
        agentii = nou;
        afiseazaConfirmareStergere("Agentia", id);
        return true;
    }

    public AgentieImobiliara[] getByNume(String nume) {
        auditCurrentAction();
        int count = 0;
        for (AgentieImobiliara agentie : agentii) {
            if (agentie.getNume().equalsIgnoreCase(nume)) {
                count++;
            }
        }
        AgentieImobiliara[] rezultat = new AgentieImobiliara[count];
        for (int i = 0, j = 0; i < agentii.length; i++) {
            if (agentii[i].getNume().equalsIgnoreCase(nume)) {
                rezultat[j++] = agentii[i];
            }
        }
        return rezultat;
    }

    public AgentieImobiliara[] getByOras(String oras) {
        auditCurrentAction();
        int count = 0;
        for (AgentieImobiliara agentie : agentii) {
            if (contineOras(agentie, oras)) {
                count++;
            }
        }
        AgentieImobiliara[] rezultat = new AgentieImobiliara[count];
        for (int i = 0, j = 0; i < agentii.length; i++) {
            if (contineOras(agentii[i], oras)) {
                rezultat[j++] = agentii[i];
            }
        }
        return rezultat;
    }

    public AgentieImobiliara[] getByNumeSiOras(String nume, String oras) {
        auditCurrentAction();
        int count = 0;
        for (AgentieImobiliara agentie : agentii) {
            if (agentie.getNume().equalsIgnoreCase(nume) && contineOras(agentie, oras)) {
                count++;
            }
        }
        AgentieImobiliara[] rezultat = new AgentieImobiliara[count];
        for (int i = 0, j = 0; i < agentii.length; i++) {
            if (agentii[i].getNume().equalsIgnoreCase(nume) && contineOras(agentii[i], oras)) {
                rezultat[j++] = agentii[i];
            }
        }
        return rezultat;
    }

    private boolean contineOras(AgentieImobiliara agentie, String oras) {
        for (String orasActiv : agentie.getOraseActive()) {
            if (orasActiv.equalsIgnoreCase(oras)) {
                return true;
            }
        }
        return false;
    }

    private boolean esteValid(String nume, String adresa, String telefon, String email, int anDeschidere) {
        if (nume == null || nume.isBlank() || adresa == null || adresa.isBlank() || telefon == null || telefon.isBlank() || email == null || email.isBlank()) {
            System.out.println("Date invalide pentru agentie.");
            return false;
        }
        if (anDeschidere < 1900) {
            System.out.println("Anul deschiderii este invalid.");
            return false;
        }
        return true;
    }

    public void adminMenu(Scanner sc) {
        boolean inapoi = false;
        while (!inapoi) {
            System.out.println("\n=== ADMIN AGENTII ===");
            System.out.println("1. Adauga");
            System.out.println("2. Afiseaza toate");
            System.out.println("3. Cauta dupa ID");
            System.out.println("4. Cauta dupa nume");
            System.out.println("5. Cauta dupa oras");
            System.out.println("6. Cauta dupa nume si oras");
            System.out.println("7. Update dupa ID");
            System.out.println("8. Sterge dupa ID");
            System.out.println("0. Inapoi");

            int optiune = citesteInt(sc, "Alegeti optiunea: ");
            switch (optiune) {
                case 1 -> add(citesteString(sc, "Introduceti numele agentiei: "), citesteString(sc, "Introduceti adresa sediului: "), citesteString(sc, "Introduceti telefonul agentiei: "), citesteString(sc, "Introduceti emailul agentiei: "), citesteInt(sc, "Introduceti anul deschiderii: "), citesteVector(sc, "Introduceti orasele active separate prin virgula: "));
                case 2 -> afiseazaObiecte(listAll(), "Nu exista agentii in sistem.");
                case 3 -> afiseazaById(sc);
                case 4 -> afiseazaObiecte(getByNume(citesteString(sc, "Introduceti numele agentiei: ")), "Nu exista agentii cu acest nume.");
                case 5 -> afiseazaObiecte(getByOras(citesteString(sc, "Introduceti orasul: ")), "Nu exista agentii in acest oras.");
                case 6 -> afiseazaObiecte(getByNumeSiOras(citesteString(sc, "Introduceti numele agentiei: "), citesteString(sc, "Introduceti orasul: ")), "Nu exista agentii pentru criteriile introduse.");
                case 7 -> updateById(citesteInt(sc, "Introduceti ID-ul agentiei de actualizat: "), citesteString(sc, "Introduceti numele agentiei: "), citesteString(sc, "Introduceti adresa sediului: "), citesteString(sc, "Introduceti telefonul agentiei: "), citesteString(sc, "Introduceti emailul agentiei: "), citesteInt(sc, "Introduceti anul deschiderii: "), citesteVector(sc, "Introduceti orasele active separate prin virgula: "));
                case 8 -> deleteById(citesteInt(sc, "Introduceti ID-ul agentiei de sters: "));
                case 0 -> inapoi = true;
                default -> System.out.println("Optiune invalida.");
            }
        }
    }

    public void userMenu(Scanner sc) {
        boolean inapoi = false;
        while (!inapoi) {
            System.out.println("\n=== USER AGENTII ===");
            System.out.println("1. Afiseaza toate");
            System.out.println("2. Cauta dupa ID");
            System.out.println("3. Cauta dupa nume");
            System.out.println("4. Cauta dupa oras");
            System.out.println("5. Cauta dupa nume si oras");
            System.out.println("0. Inapoi");

            int optiune = citesteInt(sc, "Alegeti optiunea: ");
            switch (optiune) {
                case 1 -> afiseazaObiecte(listAll(), "Nu exista agentii in sistem.");
                case 2 -> afiseazaById(sc);
                case 3 -> afiseazaObiecte(getByNume(citesteString(sc, "Introduceti numele agentiei: ")), "Nu exista agentii cu acest nume.");
                case 4 -> afiseazaObiecte(getByOras(citesteString(sc, "Introduceti orasul: ")), "Nu exista agentii in acest oras.");
                case 5 -> afiseazaObiecte(getByNumeSiOras(citesteString(sc, "Introduceti numele agentiei: "), citesteString(sc, "Introduceti orasul: ")), "Nu exista agentii pentru criteriile introduse.");
                case 0 -> inapoi = true;
                default -> System.out.println("Optiune invalida.");
            }
        }
    }

    private void afiseazaById(Scanner sc) {
        AgentieImobiliara agentie = getById(citesteInt(sc, "Introduceti ID-ul agentiei: "));
        if (agentie == null) {
            System.out.println("Agentia nu a fost gasita.");
            return;
        }
        System.out.println(agentie);
    }
}
