package service;

import model.Client;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ClientService extends ServiceSupport {
    private final List<Client> clienti = new ArrayList<>();
    private final Map<Integer, Client> clientiById = new LinkedHashMap<>();
    private int nextId = 30;

    private ClientService() {
    }

    private static class Holder {
        private static final ClientService INSTANCE = new ClientService();
    }

    public static ClientService getInstance() {
        return Holder.INSTANCE;
    }

    private int genereazaId() {
        return nextId++;
    }

    public Client add(String nume, String telefon, String email, double buget, String tipClient) {
        if (!esteValid(nume, telefon, email, buget, tipClient)) {
            return null;
        }
        int id = genereazaId();
        Client client = new Client(id, nume.trim(), telefon.trim(), email.trim(), buget, tipClient.trim());
        clienti.add(client);
        clientiById.put(id, client);
        afiseazaConfirmareCreare("Clientul", id);
        return client;
    }

    public Client[] listAll() {
        return clienti.toArray(new Client[0]);
    }

    public Client getById(int id) {
        return clientiById.get(id);
    }

    public boolean updateById(int id, String nume, String telefon, String email, double buget, String tipClient) {
        Client client = getById(id);
        if (client == null) {
            System.out.println("Clientul nu a fost gasit.");
            return false;
        }
        if (!esteValid(nume, telefon, email, buget, tipClient)) {
            return false;
        }
        client.setNume(nume.trim());
        client.setTelefon(telefon.trim());
        client.setEmail(email.trim());
        client.setBuget(buget);
        client.setTipClient(tipClient.trim());
        afiseazaConfirmareActualizare("Clientul", id);
        return true;
    }

    public boolean deleteById(int id) {
        Client client = clientiById.remove(id);
        if (client == null) {
            System.out.println("Clientul nu a fost gasit.");
            return false;
        }
        clienti.remove(client);
        afiseazaConfirmareStergere("Clientul", id);
        return true;
    }

    public Client[] getByNume(String nume) {
        if (nume == null || nume.isBlank()) {
            return new Client[0];
        }
        List<Client> rezultat = new ArrayList<>();
        for (Client client : clienti) {
            if (client.getNume().equalsIgnoreCase(nume.trim())) {
                rezultat.add(client);
            }
        }
        return rezultat.toArray(new Client[0]);
    }

    public Client[] getByEmail(String email) {
        if (email == null || email.isBlank()) {
            return new Client[0];
        }
        List<Client> rezultat = new ArrayList<>();
        for (Client client : clienti) {
            if (client.getEmail().equalsIgnoreCase(email.trim())) {
                rezultat.add(client);
            }
        }
        return rezultat.toArray(new Client[0]);
    }

    public Client[] getByNumeSiBugetMaxim(String nume, double bugetMaxim) {
        if (nume == null || nume.isBlank() || bugetMaxim < 0) {
            return new Client[0];
        }
        List<Client> rezultat = new ArrayList<>();
        for (Client client : clienti) {
            if (client.getNume().equalsIgnoreCase(nume.trim()) && client.getBuget() <= bugetMaxim) {
                rezultat.add(client);
            }
        }
        return rezultat.toArray(new Client[0]);
    }

    private boolean esteValid(String nume, String telefon, String email, double buget, String tipClient) {
        if (nume == null || nume.isBlank() || telefon == null || telefon.isBlank() || email == null || email.isBlank() || tipClient == null || tipClient.isBlank()) {
            System.out.println("Date invalide pentru client.");
            return false;
        }
        if (buget < 0) {
            System.out.println("Bugetul trebuie sa fie pozitiv.");
            return false;
        }
        return true;
    }

    public void adminMenu(Scanner sc) {
        boolean inapoi = false;
        while (!inapoi) {
            System.out.println("\n=== ADMIN CLIENTI ===");
            System.out.println("1. Adauga");
            System.out.println("2. Afiseaza toate");
            System.out.println("3. Cauta dupa ID");
            System.out.println("4. Cauta dupa nume");
            System.out.println("5. Cauta dupa email");
            System.out.println("6. Cauta dupa nume si buget maxim");
            System.out.println("7. Update dupa ID");
            System.out.println("8. Sterge dupa ID");
            System.out.println("0. Inapoi");

            int optiune = citesteInt(sc, "Alegeti optiunea: ");
            switch (optiune) {
                case 1 -> add(citesteString(sc, "Introduceti numele clientului: "), citesteString(sc, "Introduceti telefonul clientului: "), citesteString(sc, "Introduceti emailul clientului: "), citesteDouble(sc, "Introduceti bugetul clientului: "), citesteString(sc, "Introduceti tipul clientului: "));
                case 2 -> afiseazaObiecte(listAll(), "Nu exista clienti in sistem.");
                case 3 -> afiseazaById(sc);
                case 4 -> afiseazaObiecte(getByNume(citesteString(sc, "Introduceti numele clientului: ")), "Nu exista clienti cu acest nume.");
                case 5 -> afiseazaObiecte(getByEmail(citesteString(sc, "Introduceti emailul clientului: ")), "Nu exista clienti cu acest email.");
                case 6 -> afiseazaObiecte(getByNumeSiBugetMaxim(citesteString(sc, "Introduceti numele clientului: "), citesteDouble(sc, "Introduceti bugetul maxim: ")), "Nu exista clienti pentru criteriile introduse.");
                case 7 -> updateById(citesteInt(sc, "Introduceti ID-ul clientului de actualizat: "), citesteString(sc, "Introduceti numele clientului: "), citesteString(sc, "Introduceti telefonul clientului: "), citesteString(sc, "Introduceti emailul clientului: "), citesteDouble(sc, "Introduceti bugetul clientului: "), citesteString(sc, "Introduceti tipul clientului: "));
                case 8 -> deleteById(citesteInt(sc, "Introduceti ID-ul clientului de sters: "));
                case 0 -> inapoi = true;
                default -> System.out.println("Optiune invalida.");
            }
        }
    }

    public void userMenu(Scanner sc) {
        boolean inapoi = false;
        while (!inapoi) {
            System.out.println("\n=== USER CLIENTI ===");
            System.out.println("1. Afiseaza toate");
            System.out.println("2. Cauta dupa ID");
            System.out.println("3. Cauta dupa nume");
            System.out.println("4. Cauta dupa email");
            System.out.println("5. Cauta dupa nume si buget maxim");
            System.out.println("0. Inapoi");

            int optiune = citesteInt(sc, "Alegeti optiunea: ");
            switch (optiune) {
                case 1 -> afiseazaObiecte(listAll(), "Nu exista clienti in sistem.");
                case 2 -> afiseazaById(sc);
                case 3 -> afiseazaObiecte(getByNume(citesteString(sc, "Introduceti numele clientului: ")), "Nu exista clienti cu acest nume.");
                case 4 -> afiseazaObiecte(getByEmail(citesteString(sc, "Introduceti emailul clientului: ")), "Nu exista clienti cu acest email.");
                case 5 -> afiseazaObiecte(getByNumeSiBugetMaxim(citesteString(sc, "Introduceti numele clientului: "), citesteDouble(sc, "Introduceti bugetul maxim: ")), "Nu exista clienti pentru criteriile introduse.");
                case 0 -> inapoi = true;
                default -> System.out.println("Optiune invalida.");
            }
        }
    }

    private void afiseazaById(Scanner sc) {
        Client client = getById(citesteInt(sc, "Introduceti ID-ul clientului: "));
        if (client == null) {
            System.out.println("Clientul nu a fost gasit.");
            return;
        }
        System.out.println(client);
    }
}
