package service;

import model.AgentImobiliar;
import model.Client;
import model.Proprietate;
import model.Vizionare;

import java.util.Scanner;

public class VizionareService extends ServiceSupport {
    private Vizionare[] vizionari;
    private int nextId = 90;
    private final ClientService clientService = ClientService.getInstance();
    private final AgentImobiliarService agentImobiliarService = AgentImobiliarService.getInstance();
    private final ProprietateService proprietateService = ProprietateService.getInstance();

    private VizionareService() {
        this.vizionari = new Vizionare[0];
    }

    private static class Holder {
        private static final VizionareService INSTANCE = new VizionareService();
    }

    public static VizionareService getInstance() {
        return Holder.INSTANCE;
    }

    private int genereazaId() {
        return nextId++;
    }

    public Vizionare add(String dataVizionare, String oraVizionare, String status, int idClient, int idAgent, int idProprietate) {
        Client client = clientService.getById(idClient);
        if (client == null) {
            System.out.println("Clientul nu exista. Vizionarea nu a fost creata.");
            return null;
        }
        AgentImobiliar agent = agentImobiliarService.getById(idAgent);
        if (agent == null) {
            System.out.println("Agentul imobiliar nu exista. Vizionarea nu a fost creata.");
            return null;
        }
        Proprietate proprietate = proprietateService.getById(idProprietate);
        if (proprietate == null) {
            System.out.println("Proprietatea nu exista. Vizionarea nu a fost creata.");
            return null;
        }
        if (!esteValid(dataVizionare, oraVizionare, status)) {
            return null;
        }
        int id = genereazaId();
        Vizionare vizionare = new Vizionare(id, dataVizionare, oraVizionare, status, client, agent, proprietate);
        Vizionare[] nou = new Vizionare[vizionari.length + 1];
        System.arraycopy(vizionari, 0, nou, 0, vizionari.length);
        nou[nou.length - 1] = vizionare;
        vizionari = nou;
        afiseazaConfirmareCreare("Vizionarea", id);
        return vizionare;
    }

    public Vizionare[] listAll() {
        return vizionari;
    }

    public Vizionare getById(int id) {
        for (Vizionare vizionare : vizionari) {
            if (vizionare.getId() == id) {
                return vizionare;
            }
        }
        return null;
    }

    public boolean updateById(int id, String dataVizionare, String oraVizionare, String status, int idClient, int idAgent, int idProprietate) {
        Vizionare vizionare = getById(id);
        if (vizionare == null) {
            System.out.println("Vizionarea nu a fost gasita.");
            return false;
        }
        Client client = clientService.getById(idClient);
        if (client == null) {
            System.out.println("Clientul nu exista. Vizionarea nu a fost actualizata.");
            return false;
        }
        AgentImobiliar agent = agentImobiliarService.getById(idAgent);
        if (agent == null) {
            System.out.println("Agentul imobiliar nu exista. Vizionarea nu a fost actualizata.");
            return false;
        }
        Proprietate proprietate = proprietateService.getById(idProprietate);
        if (proprietate == null) {
            System.out.println("Proprietatea nu exista. Vizionarea nu a fost actualizata.");
            return false;
        }
        if (!esteValid(dataVizionare, oraVizionare, status)) {
            return false;
        }
        vizionare.setDataVizionare(dataVizionare);
        vizionare.setOraVizionare(oraVizionare);
        vizionare.setStatus(status);
        vizionare.setClient(client);
        vizionare.setAgentImobiliar(agent);
        vizionare.setProprietate(proprietate);
        afiseazaConfirmareActualizare("Vizionarea", id);
        return true;
    }

    public boolean deleteById(int id) {
        int pozitie = -1;
        for (int i = 0; i < vizionari.length; i++) {
            if (vizionari[i].getId() == id) {
                pozitie = i;
                break;
            }
        }
        if (pozitie == -1) {
            System.out.println("Vizionarea nu a fost gasita.");
            return false;
        }
        Vizionare[] nou = new Vizionare[vizionari.length - 1];
        for (int i = 0, j = 0; i < vizionari.length; i++) {
            if (i != pozitie) {
                nou[j++] = vizionari[i];
            }
        }
        vizionari = nou;
        afiseazaConfirmareStergere("Vizionarea", id);
        return true;
    }

    public Vizionare[] getByNume(String status) {
        return getByStatus(status);
    }

    public Vizionare[] getByStatus(String status) {
        int count = 0;
        for (Vizionare vizionare : vizionari) {
            if (vizionare.getStatus().equalsIgnoreCase(status)) {
                count++;
            }
        }
        Vizionare[] rezultat = new Vizionare[count];
        for (int i = 0, j = 0; i < vizionari.length; i++) {
            if (vizionari[i].getStatus().equalsIgnoreCase(status)) {
                rezultat[j++] = vizionari[i];
            }
        }
        return rezultat;
    }

    public Vizionare[] getByClient(int idClient) {
        int count = 0;
        for (Vizionare vizionare : vizionari) {
            if (vizionare.getClient().getId() == idClient) {
                count++;
            }
        }
        Vizionare[] rezultat = new Vizionare[count];
        for (int i = 0, j = 0; i < vizionari.length; i++) {
            if (vizionari[i].getClient().getId() == idClient) {
                rezultat[j++] = vizionari[i];
            }
        }
        return rezultat;
    }

    public Vizionare[] getByAgent(int idAgent) {
        int count = 0;
        for (Vizionare vizionare : vizionari) {
            if (vizionare.getAgentImobiliar().getId() == idAgent) {
                count++;
            }
        }
        Vizionare[] rezultat = new Vizionare[count];
        for (int i = 0, j = 0; i < vizionari.length; i++) {
            if (vizionari[i].getAgentImobiliar().getId() == idAgent) {
                rezultat[j++] = vizionari[i];
            }
        }
        return rezultat;
    }

    public Vizionare[] getByStatusSiAgent(String status, int idAgent) {
        int count = 0;
        for (Vizionare vizionare : vizionari) {
            if (vizionare.getStatus().equalsIgnoreCase(status) && vizionare.getAgentImobiliar().getId() == idAgent) {
                count++;
            }
        }
        Vizionare[] rezultat = new Vizionare[count];
        for (int i = 0, j = 0; i < vizionari.length; i++) {
            if (vizionari[i].getStatus().equalsIgnoreCase(status) && vizionari[i].getAgentImobiliar().getId() == idAgent) {
                rezultat[j++] = vizionari[i];
            }
        }
        return rezultat;
    }

    public boolean schimbaStatus(int id, String statusNou) {
        Vizionare vizionare = getById(id);
        if (vizionare == null) {
            System.out.println("Vizionarea nu a fost gasita.");
            return false;
        }
        if (statusNou == null || statusNou.isBlank()) {
            System.out.println("Status invalid.");
            return false;
        }
        vizionare.setStatus(statusNou);
        afiseazaConfirmareActualizare("Vizionarea", id);
        return true;
    }

    private boolean esteValid(String dataVizionare, String oraVizionare, String status) {
        if (dataVizionare == null || dataVizionare.isBlank() || oraVizionare == null || oraVizionare.isBlank() || status == null || status.isBlank()) {
            System.out.println("Date invalide pentru vizionare.");
            return false;
        }
        return true;
    }

    public void adminMenu(Scanner sc) {
        boolean inapoi = false;
        while (!inapoi) {
            System.out.println("\n=== ADMIN VIZIONARI ===");
            System.out.println("1. Adauga");
            System.out.println("2. Afiseaza toate");
            System.out.println("3. Cauta dupa ID");
            System.out.println("4. Cauta dupa status");
            System.out.println("5. Afiseaza vizionari client");
            System.out.println("6. Afiseaza vizionari agent");
            System.out.println("7. Cauta dupa status si agent");
            System.out.println("8. Schimba status");
            System.out.println("9. Update dupa ID");
            System.out.println("10. Sterge dupa ID");
            System.out.println("0. Inapoi");

            int optiune = citesteInt(sc, "Alegeti optiunea: ");
            switch (optiune) {
                case 1 -> add(citesteString(sc, "Introduceti data vizionarii: "), citesteString(sc, "Introduceti ora vizionarii: "), citesteString(sc, "Introduceti statusul vizionarii: "), citesteInt(sc, "Introduceti ID-ul clientului: "), citesteInt(sc, "Introduceti ID-ul agentului imobiliar: "), citesteInt(sc, "Introduceti ID-ul proprietatii: "));
                case 2 -> afiseazaObiecte(listAll(), "Nu exista vizionari in sistem.");
                case 3 -> afiseazaById(sc);
                case 4 -> afiseazaObiecte(getByStatus(citesteString(sc, "Introduceti statusul vizionarii: ")), "Nu exista vizionari cu acest status.");
                case 5 -> afiseazaObiecte(getByClient(citesteInt(sc, "Introduceti ID-ul clientului: ")), "Nu exista vizionari pentru acest client.");
                case 6 -> afiseazaObiecte(getByAgent(citesteInt(sc, "Introduceti ID-ul agentului imobiliar: ")), "Nu exista vizionari pentru acest agent.");
                case 7 -> afiseazaObiecte(getByStatusSiAgent(citesteString(sc, "Introduceti statusul vizionarii: "), citesteInt(sc, "Introduceti ID-ul agentului imobiliar: ")), "Nu exista vizionari pentru criteriile introduse.");
                case 8 -> schimbaStatus(citesteInt(sc, "Introduceti ID-ul vizionarii: "), citesteString(sc, "Introduceti noul status: "));
                case 9 -> updateById(citesteInt(sc, "Introduceti ID-ul vizionarii de actualizat: "), citesteString(sc, "Introduceti data vizionarii: "), citesteString(sc, "Introduceti ora vizionarii: "), citesteString(sc, "Introduceti statusul vizionarii: "), citesteInt(sc, "Introduceti ID-ul clientului: "), citesteInt(sc, "Introduceti ID-ul agentului imobiliar: "), citesteInt(sc, "Introduceti ID-ul proprietatii: "));
                case 10 -> deleteById(citesteInt(sc, "Introduceti ID-ul vizionarii de sters: "));
                case 0 -> inapoi = true;
                default -> System.out.println("Optiune invalida.");
            }
        }
    }

    public void userMenu(Scanner sc) {
        boolean inapoi = false;
        while (!inapoi) {
            System.out.println("\n=== USER VIZIONARI ===");
            System.out.println("1. Afiseaza toate");
            System.out.println("2. Cauta dupa ID");
            System.out.println("3. Cauta dupa status");
            System.out.println("4. Afiseaza vizionari client");
            System.out.println("5. Afiseaza vizionari agent");
            System.out.println("6. Cauta dupa status si agent");
            System.out.println("0. Inapoi");

            int optiune = citesteInt(sc, "Alegeti optiunea: ");
            switch (optiune) {
                case 1 -> afiseazaObiecte(listAll(), "Nu exista vizionari in sistem.");
                case 2 -> afiseazaById(sc);
                case 3 -> afiseazaObiecte(getByStatus(citesteString(sc, "Introduceti statusul vizionarii: ")), "Nu exista vizionari cu acest status.");
                case 4 -> afiseazaObiecte(getByClient(citesteInt(sc, "Introduceti ID-ul clientului: ")), "Nu exista vizionari pentru acest client.");
                case 5 -> afiseazaObiecte(getByAgent(citesteInt(sc, "Introduceti ID-ul agentului imobiliar: ")), "Nu exista vizionari pentru acest agent.");
                case 6 -> afiseazaObiecte(getByStatusSiAgent(citesteString(sc, "Introduceti statusul vizionarii: "), citesteInt(sc, "Introduceti ID-ul agentului imobiliar: ")), "Nu exista vizionari pentru criteriile introduse.");
                case 0 -> inapoi = true;
                default -> System.out.println("Optiune invalida.");
            }
        }
    }

    private void afiseazaById(Scanner sc) {
        Vizionare vizionare = getById(citesteInt(sc, "Introduceti ID-ul vizionarii: "));
        if (vizionare == null) {
            System.out.println("Vizionarea nu a fost gasita.");
            return;
        }
        System.out.println(vizionare);
    }
}
