package com.pao.proiect.imobiliare.service;

import com.pao.proiect.imobiliare.model.AgentImobiliar;
import com.pao.proiect.imobiliare.model.AgentieImobiliara;

import java.util.Scanner;

public class AgentImobiliarService extends ServiceSupport {
    private AgentImobiliar[] agenti;
    private int nextId = 20;
    private final AgentieService agentieService = AgentieService.getInstance();

    private AgentImobiliarService() {
        this.agenti = new AgentImobiliar[0];
    }

    private static class Holder {
        private static final AgentImobiliarService INSTANCE = new AgentImobiliarService();
    }

    public static AgentImobiliarService getInstance() {
        return Holder.INSTANCE;
    }

    private int genereazaId() {
        return nextId++;
    }

    public AgentImobiliar add(String nume, String telefon, String email, String specializare, int aniExperienta, int idAgentie) {
        auditCurrentAction();
        AgentieImobiliara agentie = agentieService.getById(idAgentie);
        if (agentie == null) {
            System.out.println("Agentia imobiliara nu exista. Agentul nu a fost creat.");
            return null;
        }
        if (!esteValid(nume, telefon, email, specializare, aniExperienta)) {
            return null;
        }
        int id = genereazaId();
        AgentImobiliar agent = new AgentImobiliar(id, nume, telefon, email, specializare, aniExperienta, agentie);
        AgentImobiliar[] nou = new AgentImobiliar[agenti.length + 1];
        System.arraycopy(agenti, 0, nou, 0, agenti.length);
        nou[nou.length - 1] = agent;
        agenti = nou;
        afiseazaConfirmareCreare("Agentul imobiliar", id);
        return agent;
    }

    public AgentImobiliar[] listAll() {
        auditCurrentAction();
        return agenti;
    }

    public AgentImobiliar getById(int id) {
        auditCurrentAction();
        for (AgentImobiliar agent : agenti) {
            if (agent.getId() == id) {
                return agent;
            }
        }
        return null;
    }

    public boolean updateById(int id, String nume, String telefon, String email, String specializare, int aniExperienta, int idAgentie) {
        auditCurrentAction();
        AgentImobiliar agent = getById(id);
        if (agent == null) {
            System.out.println("Agentul nu a fost gasit.");
            return false;
        }
        AgentieImobiliara agentie = agentieService.getById(idAgentie);
        if (agentie == null) {
            System.out.println("Agentia imobiliara nu exista. Agentul nu a fost actualizat.");
            return false;
        }
        if (!esteValid(nume, telefon, email, specializare, aniExperienta)) {
            return false;
        }
        agent.setNume(nume);
        agent.setTelefon(telefon);
        agent.setEmail(email);
        agent.setSpecializare(specializare);
        agent.setAniExperienta(aniExperienta);
        agent.setAgentieImobiliara(agentie);
        afiseazaConfirmareActualizare("Agentul imobiliar", id);
        return true;
    }

    public boolean deleteById(int id) {
        auditCurrentAction();
        int pozitie = -1;
        for (int i = 0; i < agenti.length; i++) {
            if (agenti[i].getId() == id) {
                pozitie = i;
                break;
            }
        }
        if (pozitie == -1) {
            System.out.println("Agentul nu a fost gasit.");
            return false;
        }
        AgentImobiliar[] nou = new AgentImobiliar[agenti.length - 1];
        for (int i = 0, j = 0; i < agenti.length; i++) {
            if (i != pozitie) {
                nou[j++] = agenti[i];
            }
        }
        agenti = nou;
        afiseazaConfirmareStergere("Agentul imobiliar", id);
        return true;
    }

    public AgentImobiliar[] getByNume(String nume) {
        auditCurrentAction();
        int count = 0;
        for (AgentImobiliar agent : agenti) {
            if (agent.getNume().equalsIgnoreCase(nume)) {
                count++;
            }
        }
        AgentImobiliar[] rezultat = new AgentImobiliar[count];
        for (int i = 0, j = 0; i < agenti.length; i++) {
            if (agenti[i].getNume().equalsIgnoreCase(nume)) {
                rezultat[j++] = agenti[i];
            }
        }
        return rezultat;
    }

    public AgentImobiliar[] getBySpecializare(String specializare) {
        auditCurrentAction();
        int count = 0;
        for (AgentImobiliar agent : agenti) {
            if (agent.getSpecializare().equalsIgnoreCase(specializare)) {
                count++;
            }
        }
        AgentImobiliar[] rezultat = new AgentImobiliar[count];
        for (int i = 0, j = 0; i < agenti.length; i++) {
            if (agenti[i].getSpecializare().equalsIgnoreCase(specializare)) {
                rezultat[j++] = agenti[i];
            }
        }
        return rezultat;
    }

    public AgentImobiliar[] getByNumeSiSpecializare(String nume, String specializare) {
        auditCurrentAction();
        int count = 0;
        for (AgentImobiliar agent : agenti) {
            if (agent.getNume().equalsIgnoreCase(nume) && agent.getSpecializare().equalsIgnoreCase(specializare)) {
                count++;
            }
        }
        AgentImobiliar[] rezultat = new AgentImobiliar[count];
        for (int i = 0, j = 0; i < agenti.length; i++) {
            if (agenti[i].getNume().equalsIgnoreCase(nume) && agenti[i].getSpecializare().equalsIgnoreCase(specializare)) {
                rezultat[j++] = agenti[i];
            }
        }
        return rezultat;
    }

    public AgentImobiliar[] getByAgentie(int idAgentie) {
        auditCurrentAction();
        int count = 0;
        for (AgentImobiliar agent : agenti) {
            if (agent.getAgentieImobiliara().getId() == idAgentie) {
                count++;
            }
        }
        AgentImobiliar[] rezultat = new AgentImobiliar[count];
        for (int i = 0, j = 0; i < agenti.length; i++) {
            if (agenti[i].getAgentieImobiliara().getId() == idAgentie) {
                rezultat[j++] = agenti[i];
            }
        }
        return rezultat;
    }

    private boolean esteValid(String nume, String telefon, String email, String specializare, int aniExperienta) {
        if (nume == null || nume.isBlank() || telefon == null || telefon.isBlank() || email == null || email.isBlank() || specializare == null || specializare.isBlank()) {
            System.out.println("Date invalide pentru agent.");
            return false;
        }
        if (aniExperienta < 0) {
            System.out.println("Anii de experienta trebuie sa fie pozitivi.");
            return false;
        }
        return true;
    }

    public void adminMenu(Scanner sc) {
        boolean inapoi = false;
        while (!inapoi) {
            System.out.println("\n=== ADMIN AGENTI IMOBILIARI ===");
            System.out.println("1. Adauga");
            System.out.println("2. Afiseaza toate");
            System.out.println("3. Cauta dupa ID");
            System.out.println("4. Cauta dupa nume");
            System.out.println("5. Cauta dupa specializare");
            System.out.println("6. Cauta dupa nume si specializare");
            System.out.println("7. Afiseaza agentii unei agentii");
            System.out.println("8. Update dupa ID");
            System.out.println("9. Sterge dupa ID");
            System.out.println("0. Inapoi");

            int optiune = citesteInt(sc, "Alegeti optiunea: ");
            switch (optiune) {
                case 1 -> add(citesteString(sc, "Introduceti numele agentului: "), citesteString(sc, "Introduceti telefonul agentului: "), citesteString(sc, "Introduceti emailul agentului: "), citesteString(sc, "Introduceti specializarea agentului: "), citesteInt(sc, "Introduceti anii de experienta: "), citesteInt(sc, "Introduceti ID-ul agentiei imobiliare: "));
                case 2 -> afiseazaObiecte(listAll(), "Nu exista agenti in sistem.");
                case 3 -> afiseazaById(sc);
                case 4 -> afiseazaObiecte(getByNume(citesteString(sc, "Introduceti numele agentului: ")), "Nu exista agenti cu acest nume.");
                case 5 -> afiseazaObiecte(getBySpecializare(citesteString(sc, "Introduceti specializarea: ")), "Nu exista agenti cu aceasta specializare.");
                case 6 -> afiseazaObiecte(getByNumeSiSpecializare(citesteString(sc, "Introduceti numele agentului: "), citesteString(sc, "Introduceti specializarea: ")), "Nu exista agenti pentru criteriile introduse.");
                case 7 -> afiseazaObiecte(getByAgentie(citesteInt(sc, "Introduceti ID-ul agentiei imobiliare: ")), "Nu exista agenti pentru aceasta agentie.");
                case 8 -> updateById(citesteInt(sc, "Introduceti ID-ul agentului de actualizat: "), citesteString(sc, "Introduceti numele agentului: "), citesteString(sc, "Introduceti telefonul agentului: "), citesteString(sc, "Introduceti emailul agentului: "), citesteString(sc, "Introduceti specializarea agentului: "), citesteInt(sc, "Introduceti anii de experienta: "), citesteInt(sc, "Introduceti ID-ul agentiei imobiliare: "));
                case 9 -> deleteById(citesteInt(sc, "Introduceti ID-ul agentului de sters: "));
                case 0 -> inapoi = true;
                default -> System.out.println("Optiune invalida.");
            }
        }
    }

    public void userMenu(Scanner sc) {
        boolean inapoi = false;
        while (!inapoi) {
            System.out.println("\n=== USER AGENTI IMOBILIARI ===");
            System.out.println("1. Afiseaza toate");
            System.out.println("2. Cauta dupa ID");
            System.out.println("3. Cauta dupa nume");
            System.out.println("4. Cauta dupa specializare");
            System.out.println("5. Cauta dupa nume si specializare");
            System.out.println("6. Afiseaza agentii unei agentii");
            System.out.println("0. Inapoi");

            int optiune = citesteInt(sc, "Alegeti optiunea: ");
            switch (optiune) {
                case 1 -> afiseazaObiecte(listAll(), "Nu exista agenti in sistem.");
                case 2 -> afiseazaById(sc);
                case 3 -> afiseazaObiecte(getByNume(citesteString(sc, "Introduceti numele agentului: ")), "Nu exista agenti cu acest nume.");
                case 4 -> afiseazaObiecte(getBySpecializare(citesteString(sc, "Introduceti specializarea: ")), "Nu exista agenti cu aceasta specializare.");
                case 5 -> afiseazaObiecte(getByNumeSiSpecializare(citesteString(sc, "Introduceti numele agentului: "), citesteString(sc, "Introduceti specializarea: ")), "Nu exista agenti pentru criteriile introduse.");
                case 6 -> afiseazaObiecte(getByAgentie(citesteInt(sc, "Introduceti ID-ul agentiei imobiliare: ")), "Nu exista agenti pentru aceasta agentie.");
                case 0 -> inapoi = true;
                default -> System.out.println("Optiune invalida.");
            }
        }
    }

    private void afiseazaById(Scanner sc) {
        AgentImobiliar agent = getById(citesteInt(sc, "Introduceti ID-ul agentului: "));
        if (agent == null) {
            System.out.println("Agentul nu a fost gasit.");
            return;
        }
        System.out.println(agent);
    }
}
