package service;

import exception.EntitateNegasitaException;
import model.Client;
import model.ListaFavorite;
import model.Proprietate;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class ListaFavoriteService extends ServiceSupport {
    private final List<ListaFavorite> listeFavorite = new ArrayList<>();
    private final Map<Integer, ListaFavorite> listeById = new LinkedHashMap<>();
    private final Map<Integer, Set<Proprietate>> proprietatiByListaId = new LinkedHashMap<>();
    private int nextId = 110;
    private final ClientService clientService = ClientService.getInstance();
    private final ProprietateService proprietateService = ProprietateService.getInstance();

    private ListaFavoriteService() {
    }

    private static class Holder {
        private static final ListaFavoriteService INSTANCE = new ListaFavoriteService();
    }

    public static ListaFavoriteService getInstance() {
        return Holder.INSTANCE;
    }

    private int genereazaId() {
        return nextId++;
    }

    public ListaFavorite add(int idClient) {
        Client client = clientService.getById(idClient);
        if (client == null) {
            System.out.println("Clientul nu exista. Lista de favorite nu a fost creata.");
            return null;
        }
        if (getByClient(idClient).length > 0) {
            System.out.println("Clientul are deja o lista de favorite.");
            return null;
        }
        int id = genereazaId();
        ListaFavorite lista = new ListaFavorite(id, client, new Proprietate[0]);
        listeFavorite.add(lista);
        listeById.put(id, lista);
        proprietatiByListaId.put(id, new LinkedHashSet<>());
        afiseazaConfirmareCreare("Lista de favorite", id);
        return lista;
    }

    public ListaFavorite[] listAll() {
        sincronizeazaListe();
        return listeFavorite.toArray(new ListaFavorite[0]);
    }

    public ListaFavorite getById(int id) {
        sincronizeazaLista(id);
        return listeById.get(id);
    }

    public boolean updateById(int id, int idClient) {
        ListaFavorite lista = getById(id);
        if (lista == null) {
            System.out.println("Lista de favorite nu a fost gasita.");
            return false;
        }
        Client client = clientService.getById(idClient);
        if (client == null) {
            System.out.println("Clientul nu exista. Lista de favorite nu a fost actualizata.");
            return false;
        }
        lista.setClient(client);
        afiseazaConfirmareActualizare("Lista de favorite", id);
        return true;
    }

    public boolean deleteById(int id) {
        ListaFavorite lista = listeById.remove(id);
        if (lista == null) {
            System.out.println("Lista de favorite nu a fost gasita.");
            return false;
        }
        listeFavorite.remove(lista);
        proprietatiByListaId.remove(id);
        afiseazaConfirmareStergere("Lista de favorite", id);
        return true;
    }

    public ListaFavorite[] getByNume(String numeClient) {
        if (numeClient == null || numeClient.isBlank()) {
            return new ListaFavorite[0];
        }
        List<ListaFavorite> rezultat = new ArrayList<>();
        for (ListaFavorite lista : listeFavorite) {
            if (lista.getClient() != null && lista.getClient().getNume().equalsIgnoreCase(numeClient.trim())) {
                sincronizeazaLista(lista.getId());
                rezultat.add(lista);
            }
        }
        return rezultat.toArray(new ListaFavorite[0]);
    }

    public ListaFavorite[] getByClient(int idClient) {
        List<ListaFavorite> rezultat = new ArrayList<>();
        for (ListaFavorite lista : listeFavorite) {
            if (lista.getClient() != null && lista.getClient().getId() == idClient) {
                sincronizeazaLista(lista.getId());
                rezultat.add(lista);
            }
        }
        return rezultat.toArray(new ListaFavorite[0]);
    }

    public ListaFavorite[] getByClientSiProprietate(int idClient, int idProprietate) {
        List<ListaFavorite> rezultat = new ArrayList<>();
        for (ListaFavorite lista : listeFavorite) {
            if (lista.getClient() != null && lista.getClient().getId() == idClient && contineProprietate(lista, idProprietate)) {
                sincronizeazaLista(lista.getId());
                rezultat.add(lista);
            }
        }
        return rezultat.toArray(new ListaFavorite[0]);
    }

    public boolean adaugaProprietate(int idLista, int idProprietate) {
        try {
            ListaFavorite lista = cereListaExistenta(idLista);
            Proprietate proprietate = cereProprietateExistenta(idProprietate);
            Set<Proprietate> favorite = proprietatiByListaId.computeIfAbsent(idLista, cheie -> new LinkedHashSet<>());
            if (!favorite.add(proprietate)) {
                System.out.println("Proprietatea exista deja in lista.");
                return false;
            }
            sincronizeazaLista(idLista);
            afiseazaConfirmareCreare("Proprietatea in lista de favorite", idProprietate);
            return true;
        } catch (EntitateNegasitaException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean eliminaProprietate(int idLista, int idProprietate) {
        try {
            cereListaExistenta(idLista);
            Set<Proprietate> favorite = proprietatiByListaId.get(idLista);
            if (favorite == null || favorite.removeIf(proprietate -> proprietate.getId() == idProprietate) == false) {
                System.out.println("Proprietatea nu exista in lista.");
                return false;
            }
            sincronizeazaLista(idLista);
            afiseazaConfirmareStergere("Proprietatea din lista de favorite", idProprietate);
            return true;
        } catch (EntitateNegasitaException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean contineProprietate(ListaFavorite lista, int idProprietate) {
        if (lista == null) {
            return false;
        }
        Set<Proprietate> favorite = proprietatiByListaId.get(lista.getId());
        if (favorite == null) {
            return false;
        }
        for (Proprietate proprietate : favorite) {
            if (proprietate.getId() == idProprietate) {
                return true;
            }
        }
        return false;
    }

    public boolean verificaExistenta(int idLista, int idProprietate) {
        ListaFavorite lista = getById(idLista);
        if (lista == null) {
            System.out.println("Lista de favorite nu a fost gasita.");
            return false;
        }
        boolean exista = contineProprietate(lista, idProprietate);
        System.out.println(exista ? "Proprietatea exista in lista." : "Proprietatea nu exista in lista.");
        return exista;
    }

    private ListaFavorite cereListaExistenta(int idLista) throws EntitateNegasitaException {
        ListaFavorite lista = listeById.get(idLista);
        if (lista == null) {
            throw new EntitateNegasitaException("Lista de favorite nu a fost gasita.");
        }
        return lista;
    }

    private Proprietate cereProprietateExistenta(int idProprietate) throws EntitateNegasitaException {
        Proprietate proprietate = proprietateService.getById(idProprietate);
        if (proprietate == null) {
            throw new EntitateNegasitaException("Proprietatea nu exista. Nu a fost adaugata.");
        }
        return proprietate;
    }

    private void sincronizeazaListe() {
        for (ListaFavorite lista : listeFavorite) {
            sincronizeazaLista(lista.getId());
        }
    }

    private void sincronizeazaLista(int idLista) {
        ListaFavorite lista = listeById.get(idLista);
        if (lista == null) {
            return;
        }
        Set<Proprietate> favorite = proprietatiByListaId.get(idLista);
        if (favorite == null) {
            lista.setProprietati(new Proprietate[0]);
            return;
        }
        lista.setProprietati(favorite.toArray(new Proprietate[0]));
    }

    public void adminMenu(Scanner sc) {
        boolean inapoi = false;
        while (!inapoi) {
            System.out.println("\n=== ADMIN LISTE FAVORITE ===");
            System.out.println("1. Adauga lista");
            System.out.println("2. Afiseaza toate");
            System.out.println("3. Cauta dupa ID");
            System.out.println("4. Cauta dupa nume client");
            System.out.println("5. Cauta dupa client");
            System.out.println("6. Cauta dupa client si proprietate");
            System.out.println("7. Adauga proprietate in lista");
            System.out.println("8. Elimina proprietate din lista");
            System.out.println("9. Verifica existenta proprietate");
            System.out.println("10. Update dupa ID");
            System.out.println("11. Sterge dupa ID");
            System.out.println("0. Inapoi");

            int optiune = citesteInt(sc, "Alegeti optiunea: ");
            switch (optiune) {
                case 1 -> add(citesteInt(sc, "Introduceti ID-ul clientului: "));
                case 2 -> afiseazaObiecte(listAll(), "Nu exista liste de favorite in sistem.");
                case 3 -> afiseazaById(sc);
                case 4 -> afiseazaObiecte(getByNume(citesteString(sc, "Introduceti numele clientului: ")), "Nu exista liste pentru acest client.");
                case 5 -> afiseazaObiecte(getByClient(citesteInt(sc, "Introduceti ID-ul clientului: ")), "Nu exista liste pentru acest client.");
                case 6 -> afiseazaObiecte(getByClientSiProprietate(citesteInt(sc, "Introduceti ID-ul clientului: "), citesteInt(sc, "Introduceti ID-ul proprietatii: ")), "Nu exista liste pentru criteriile introduse.");
                case 7 -> adaugaProprietate(citesteInt(sc, "Introduceti ID-ul listei de favorite: "), citesteInt(sc, "Introduceti ID-ul proprietatii: "));
                case 8 -> eliminaProprietate(citesteInt(sc, "Introduceti ID-ul listei de favorite: "), citesteInt(sc, "Introduceti ID-ul proprietatii: "));
                case 9 -> verificaExistenta(citesteInt(sc, "Introduceti ID-ul listei de favorite: "), citesteInt(sc, "Introduceti ID-ul proprietatii: "));
                case 10 -> updateById(citesteInt(sc, "Introduceti ID-ul listei de favorite de actualizat: "), citesteInt(sc, "Introduceti ID-ul clientului: "));
                case 11 -> deleteById(citesteInt(sc, "Introduceti ID-ul listei de favorite de sters: "));
                case 0 -> inapoi = true;
                default -> System.out.println("Optiune invalida.");
            }
        }
    }

    public void userMenu(Scanner sc) {
        boolean inapoi = false;
        while (!inapoi) {
            System.out.println("\n=== USER LISTE FAVORITE ===");
            System.out.println("1. Afiseaza toate");
            System.out.println("2. Cauta dupa ID");
            System.out.println("3. Cauta dupa nume client");
            System.out.println("4. Cauta dupa client");
            System.out.println("5. Cauta dupa client si proprietate");
            System.out.println("6. Verifica existenta proprietate");
            System.out.println("0. Inapoi");

            int optiune = citesteInt(sc, "Alegeti optiunea: ");
            switch (optiune) {
                case 1 -> afiseazaObiecte(listAll(), "Nu exista liste de favorite in sistem.");
                case 2 -> afiseazaById(sc);
                case 3 -> afiseazaObiecte(getByNume(citesteString(sc, "Introduceti numele clientului: ")), "Nu exista liste pentru acest client.");
                case 4 -> afiseazaObiecte(getByClient(citesteInt(sc, "Introduceti ID-ul clientului: ")), "Nu exista liste pentru acest client.");
                case 5 -> afiseazaObiecte(getByClientSiProprietate(citesteInt(sc, "Introduceti ID-ul clientului: "), citesteInt(sc, "Introduceti ID-ul proprietatii: ")), "Nu exista liste pentru criteriile introduse.");
                case 6 -> verificaExistenta(citesteInt(sc, "Introduceti ID-ul listei de favorite: "), citesteInt(sc, "Introduceti ID-ul proprietatii: "));
                case 0 -> inapoi = true;
                default -> System.out.println("Optiune invalida.");
            }
        }
    }

    private void afiseazaById(Scanner sc) {
        ListaFavorite lista = getById(citesteInt(sc, "Introduceti ID-ul listei de favorite: "));
        if (lista == null) {
            System.out.println("Lista de favorite nu a fost gasita.");
            return;
        }
        System.out.println(lista);
    }
}
