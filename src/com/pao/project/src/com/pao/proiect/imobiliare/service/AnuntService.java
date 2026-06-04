package com.pao.proiect.imobiliare.service;

import com.pao.proiect.imobiliare.model.AgentImobiliar;
import com.pao.proiect.imobiliare.model.Anunt;
import com.pao.proiect.imobiliare.model.Proprietate;

import java.util.Scanner;

public class AnuntService extends ServiceSupport {
    private Anunt[] anunturi;
    private int nextId = 80;
    private final ProprietateService proprietateService = ProprietateService.getInstance();
    private final AgentImobiliarService agentImobiliarService = AgentImobiliarService.getInstance();

    private AnuntService() {
        this.anunturi = new Anunt[0];
    }

    private static class Holder {
        private static final AnuntService INSTANCE = new AnuntService();
    }

    public static AnuntService getInstance() {
        return Holder.INSTANCE;
    }

    private int genereazaId() {
        return nextId++;
    }

    public Anunt add(String titlu, String descriere, String dataPublicare, boolean activ, int idProprietate, int idAgent) {
        auditCurrentAction();
        Proprietate proprietate = proprietateService.getById(idProprietate);
        if (proprietate == null) {
            System.out.println("Proprietatea nu exista. Anuntul nu a fost creat.");
            return null;
        }
        AgentImobiliar agent = agentImobiliarService.getById(idAgent);
        if (agent == null) {
            System.out.println("Agentul imobiliar nu exista. Anuntul nu a fost creat.");
            return null;
        }
        if (!esteValid(titlu, descriere, dataPublicare)) {
            return null;
        }
        int id = genereazaId();
        Anunt anunt = new Anunt(id, titlu, descriere, dataPublicare, activ, proprietate, agent);
        Anunt[] nou = new Anunt[anunturi.length + 1];
        System.arraycopy(anunturi, 0, nou, 0, anunturi.length);
        nou[nou.length - 1] = anunt;
        anunturi = nou;
        afiseazaConfirmareCreare("Anuntul", id);
        return anunt;
    }

    public Anunt[] listAll() {
        auditCurrentAction();
        return anunturi;
    }

    public Anunt getById(int id) {
        auditCurrentAction();
        for (Anunt anunt : anunturi) {
            if (anunt.getId() == id) {
                return anunt;
            }
        }
        return null;
    }

    public boolean updateById(int id, String titlu, String descriere, String dataPublicare, boolean activ, int idProprietate, int idAgent) {
        auditCurrentAction();
        Anunt anunt = getById(id);
        if (anunt == null) {
            System.out.println("Anuntul nu a fost gasit.");
            return false;
        }
        Proprietate proprietate = proprietateService.getById(idProprietate);
        if (proprietate == null) {
            System.out.println("Proprietatea nu exista. Anuntul nu a fost actualizat.");
            return false;
        }
        AgentImobiliar agent = agentImobiliarService.getById(idAgent);
        if (agent == null) {
            System.out.println("Agentul imobiliar nu exista. Anuntul nu a fost actualizat.");
            return false;
        }
        if (!esteValid(titlu, descriere, dataPublicare)) {
            return false;
        }
        anunt.setTitlu(titlu);
        anunt.setDescriere(descriere);
        anunt.setDataPublicare(dataPublicare);
        anunt.setActiv(activ);
        anunt.setProprietate(proprietate);
        anunt.setAgentImobiliar(agent);
        afiseazaConfirmareActualizare("Anuntul", id);
        return true;
    }

    public boolean deleteById(int id) {
        auditCurrentAction();
        int pozitie = -1;
        for (int i = 0; i < anunturi.length; i++) {
            if (anunturi[i].getId() == id) {
                pozitie = i;
                break;
            }
        }
        if (pozitie == -1) {
            System.out.println("Anuntul nu a fost gasit.");
            return false;
        }
        Anunt[] nou = new Anunt[anunturi.length - 1];
        for (int i = 0, j = 0; i < anunturi.length; i++) {
            if (i != pozitie) {
                nou[j++] = anunturi[i];
            }
        }
        anunturi = nou;
        afiseazaConfirmareStergere("Anuntul", id);
        return true;
    }

    public Anunt[] getByNume(String titlu) {
        auditCurrentAction();
        int count = 0;
        for (Anunt anunt : anunturi) {
            if (anunt.getTitlu().equalsIgnoreCase(titlu)) {
                count++;
            }
        }
        Anunt[] rezultat = new Anunt[count];
        for (int i = 0, j = 0; i < anunturi.length; i++) {
            if (anunturi[i].getTitlu().equalsIgnoreCase(titlu)) {
                rezultat[j++] = anunturi[i];
            }
        }
        return rezultat;
    }

    public Anunt[] getActive() {
        auditCurrentAction();
        int count = 0;
        for (Anunt anunt : anunturi) {
            if (anunt.isActiv()) {
                count++;
            }
        }
        Anunt[] rezultat = new Anunt[count];
        for (int i = 0, j = 0; i < anunturi.length; i++) {
            if (anunturi[i].isActiv()) {
                rezultat[j++] = anunturi[i];
            }
        }
        return rezultat;
    }

    public Anunt[] getByAgentSiStatus(int idAgent, boolean activ) {
        auditCurrentAction();
        int count = 0;
        for (Anunt anunt : anunturi) {
            if (anunt.getAgentImobiliar().getId() == idAgent && anunt.isActiv() == activ) {
                count++;
            }
        }
        Anunt[] rezultat = new Anunt[count];
        for (int i = 0, j = 0; i < anunturi.length; i++) {
            if (anunturi[i].getAgentImobiliar().getId() == idAgent && anunturi[i].isActiv() == activ) {
                rezultat[j++] = anunturi[i];
            }
        }
        return rezultat;
    }

    public boolean seteazaStatus(int id, boolean activ) {
        auditCurrentAction();
        Anunt anunt = getById(id);
        if (anunt == null) {
            System.out.println("Anuntul nu a fost gasit.");
            return false;
        }
        anunt.setActiv(activ);
        afiseazaConfirmareActualizare("Anuntul", id);
        return true;
    }

    private boolean esteValid(String titlu, String descriere, String dataPublicare) {
        if (titlu == null || titlu.isBlank() || descriere == null || descriere.isBlank() || dataPublicare == null || dataPublicare.isBlank()) {
            System.out.println("Date invalide pentru anunt.");
            return false;
        }
        return true;
    }

    public void adminMenu(Scanner sc) {
        boolean inapoi = false;
        while (!inapoi) {
            System.out.println("\n=== ADMIN ANUNTURI ===");
            System.out.println("1. Adauga");
            System.out.println("2. Afiseaza toate");
            System.out.println("3. Cauta dupa ID");
            System.out.println("4. Cauta dupa nume");
            System.out.println("5. Afiseaza anunturi active");
            System.out.println("6. Cauta dupa agent si status");
            System.out.println("7. Activeaza anunt");
            System.out.println("8. Dezactiveaza anunt");
            System.out.println("9. Update dupa ID");
            System.out.println("10. Sterge dupa ID");
            System.out.println("0. Inapoi");

            int optiune = citesteInt(sc, "Alegeti optiunea: ");
            switch (optiune) {
                case 1 -> add(citesteString(sc, "Introduceti titlul anuntului: "), citesteString(sc, "Introduceti descrierea anuntului: "), citesteString(sc, "Introduceti data publicarii: "), citesteBoolean(sc, "Anuntul este activ? (da/nu): "), citesteInt(sc, "Introduceti ID-ul proprietatii: "), citesteInt(sc, "Introduceti ID-ul agentului imobiliar: "));
                case 2 -> afiseazaObiecte(listAll(), "Nu exista anunturi in sistem.");
                case 3 -> afiseazaById(sc);
                case 4 -> afiseazaObiecte(getByNume(citesteString(sc, "Introduceti titlul anuntului: ")), "Nu exista anunturi cu acest titlu.");
                case 5 -> afiseazaObiecte(getActive(), "Nu exista anunturi active.");
                case 6 -> afiseazaObiecte(getByAgentSiStatus(citesteInt(sc, "Introduceti ID-ul agentului imobiliar: "), citesteBoolean(sc, "Anuntul trebuie sa fie activ? (da/nu): ")), "Nu exista anunturi pentru criteriile introduse.");
                case 7 -> seteazaStatus(citesteInt(sc, "Introduceti ID-ul anuntului: "), true);
                case 8 -> seteazaStatus(citesteInt(sc, "Introduceti ID-ul anuntului: "), false);
                case 9 -> updateById(citesteInt(sc, "Introduceti ID-ul anuntului de actualizat: "), citesteString(sc, "Introduceti titlul anuntului: "), citesteString(sc, "Introduceti descrierea anuntului: "), citesteString(sc, "Introduceti data publicarii: "), citesteBoolean(sc, "Anuntul este activ? (da/nu): "), citesteInt(sc, "Introduceti ID-ul proprietatii: "), citesteInt(sc, "Introduceti ID-ul agentului imobiliar: "));
                case 10 -> deleteById(citesteInt(sc, "Introduceti ID-ul anuntului de sters: "));
                case 0 -> inapoi = true;
                default -> System.out.println("Optiune invalida.");
            }
        }
    }

    public void userMenu(Scanner sc) {
        boolean inapoi = false;
        while (!inapoi) {
            System.out.println("\n=== USER ANUNTURI ===");
            System.out.println("1. Afiseaza toate");
            System.out.println("2. Cauta dupa ID");
            System.out.println("3. Cauta dupa nume");
            System.out.println("4. Afiseaza anunturi active");
            System.out.println("5. Cauta dupa agent si status");
            System.out.println("0. Inapoi");

            int optiune = citesteInt(sc, "Alegeti optiunea: ");
            switch (optiune) {
                case 1 -> afiseazaObiecte(listAll(), "Nu exista anunturi in sistem.");
                case 2 -> afiseazaById(sc);
                case 3 -> afiseazaObiecte(getByNume(citesteString(sc, "Introduceti titlul anuntului: ")), "Nu exista anunturi cu acest titlu.");
                case 4 -> afiseazaObiecte(getActive(), "Nu exista anunturi active.");
                case 5 -> afiseazaObiecte(getByAgentSiStatus(citesteInt(sc, "Introduceti ID-ul agentului imobiliar: "), citesteBoolean(sc, "Anuntul trebuie sa fie activ? (da/nu): ")), "Nu exista anunturi pentru criteriile introduse.");
                case 0 -> inapoi = true;
                default -> System.out.println("Optiune invalida.");
            }
        }
    }

    private void afiseazaById(Scanner sc) {
        Anunt anunt = getById(citesteInt(sc, "Introduceti ID-ul anuntului: "));
        if (anunt == null) {
            System.out.println("Anuntul nu a fost gasit.");
            return;
        }
        System.out.println(anunt);
    }
}
