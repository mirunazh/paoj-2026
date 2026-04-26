package service;

import exception.EntitateNegasitaException;
import exception.ProprietateIndisponibilaException;
import model.AgentImobiliar;
import model.Client;
import model.Contract;
import model.Proprietate;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ContractService extends ServiceSupport {
    private final List<Contract> contracte = new ArrayList<>();
    private final Map<Integer, Contract> contracteById = new LinkedHashMap<>();
    private int nextId = 100;
    private final ClientService clientService = ClientService.getInstance();
    private final AgentImobiliarService agentImobiliarService = AgentImobiliarService.getInstance();
    private final ProprietateService proprietateService = ProprietateService.getInstance();

    private ContractService() {
    }

    private static class Holder {
        private static final ContractService INSTANCE = new ContractService();
    }

    public static ContractService getInstance() {
        return Holder.INSTANCE;
    }

    private int genereazaId() {
        return nextId++;
    }

    public Contract add(String numarContract, String tipContract, String dataSemnare, double valoare, int idClient, int idAgent, int idProprietate) {
        if (!esteValid(numarContract, tipContract, dataSemnare, valoare)) {
            return null;
        }
        try {
            Client client = cereClientExistent(idClient);
            AgentImobiliar agent = cereAgentExistent(idAgent);
            Proprietate proprietate = cereProprietateDisponibila(idProprietate);
            int id = genereazaId();
            Contract contract = new Contract(id, numarContract.trim(), tipContract.trim(), dataSemnare.trim(), valoare, client, agent, proprietate);
            contracte.add(contract);
            contracteById.put(id, contract);
            proprietate.setDisponibila(false);
            afiseazaConfirmareCreare("Contractul", id);
            return contract;
        } catch (EntitateNegasitaException | ProprietateIndisponibilaException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public Contract[] listAll() {
        return contracte.toArray(new Contract[0]);
    }

    public Contract getById(int id) {
        return contracteById.get(id);
    }

    public boolean updateById(int id, String numarContract, String tipContract, String dataSemnare, double valoare, int idClient, int idAgent, int idProprietate) {
        Contract contract = getById(id);
        if (contract == null) {
            System.out.println("Contractul nu a fost gasit.");
            return false;
        }
        if (!esteValid(numarContract, tipContract, dataSemnare, valoare)) {
            return false;
        }
        try {
            Client client = cereClientExistent(idClient);
            AgentImobiliar agent = cereAgentExistent(idAgent);
            Proprietate proprietate = cereProprietatePentruActualizare(idProprietate, contract);
            Proprietate proprietateVeche = contract.getProprietate();
            if (proprietateVeche != null && proprietateVeche.getId() != proprietate.getId()) {
                proprietateVeche.setDisponibila(true);
            }
            contract.setNumarContract(numarContract.trim());
            contract.setTipContract(tipContract.trim());
            contract.setDataSemnare(dataSemnare.trim());
            contract.setValoare(valoare);
            contract.setClient(client);
            contract.setAgentImobiliar(agent);
            contract.setProprietate(proprietate);
            proprietate.setDisponibila(false);
            afiseazaConfirmareActualizare("Contractul", id);
            return true;
        } catch (EntitateNegasitaException | ProprietateIndisponibilaException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean deleteById(int id) {
        Contract contract = contracteById.remove(id);
        if (contract == null) {
            System.out.println("Contractul nu a fost gasit.");
            return false;
        }
        contracte.remove(contract);
        if (contract.getProprietate() != null) {
            contract.getProprietate().setDisponibila(true);
        }
        afiseazaConfirmareStergere("Contractul", id);
        return true;
    }

    public Contract[] getByNume(String tipContract) {
        if (tipContract == null || tipContract.isBlank()) {
            return new Contract[0];
        }
        List<Contract> rezultat = new ArrayList<>();
        for (Contract contract : contracte) {
            if (contract.getTipContract().equalsIgnoreCase(tipContract.trim())) {
                rezultat.add(contract);
            }
        }
        return rezultat.toArray(new Contract[0]);
    }

    public Contract[] getByClient(int idClient) {
        List<Contract> rezultat = new ArrayList<>();
        for (Contract contract : contracte) {
            if (contract.getClient() != null && contract.getClient().getId() == idClient) {
                rezultat.add(contract);
            }
        }
        return rezultat.toArray(new Contract[0]);
    }

    public Contract[] getByAgent(int idAgent) {
        List<Contract> rezultat = new ArrayList<>();
        for (Contract contract : contracte) {
            if (contract.getAgentImobiliar() != null && contract.getAgentImobiliar().getId() == idAgent) {
                rezultat.add(contract);
            }
        }
        return rezultat.toArray(new Contract[0]);
    }

    public Contract[] getByTipSiAgent(String tipContract, int idAgent) {
        if (tipContract == null || tipContract.isBlank()) {
            return new Contract[0];
        }
        List<Contract> rezultat = new ArrayList<>();
        for (Contract contract : contracte) {
            if (contract.getAgentImobiliar() != null
                    && contract.getAgentImobiliar().getId() == idAgent
                    && contract.getTipContract().equalsIgnoreCase(tipContract.trim())) {
                rezultat.add(contract);
            }
        }
        return rezultat.toArray(new Contract[0]);
    }

    public double calculeazaSumaTotala() {
        double suma = 0;
        for (Contract contract : contracte) {
            suma += contract.getValoare();
        }
        return suma;
    }

    private Client cereClientExistent(int idClient) throws EntitateNegasitaException {
        Client client = clientService.getById(idClient);
        if (client == null) {
            throw new EntitateNegasitaException("Clientul nu exista. Contractul nu a fost procesat.");
        }
        return client;
    }

    private AgentImobiliar cereAgentExistent(int idAgent) throws EntitateNegasitaException {
        AgentImobiliar agent = agentImobiliarService.getById(idAgent);
        if (agent == null) {
            throw new EntitateNegasitaException("Agentul imobiliar nu exista. Contractul nu a fost procesat.");
        }
        return agent;
    }

    private Proprietate cereProprietateDisponibila(int idProprietate) throws EntitateNegasitaException, ProprietateIndisponibilaException {
        Proprietate proprietate = proprietateService.getById(idProprietate);
        if (proprietate == null) {
            throw new EntitateNegasitaException("Proprietatea nu exista. Contractul nu a fost procesat.");
        }
        if (!proprietate.isDisponibila()) {
            throw new ProprietateIndisponibilaException("Proprietatea nu este disponibila pentru contract.");
        }
        return proprietate;
    }

    private Proprietate cereProprietatePentruActualizare(int idProprietate, Contract contract) throws EntitateNegasitaException, ProprietateIndisponibilaException {
        Proprietate proprietate = proprietateService.getById(idProprietate);
        if (proprietate == null) {
            throw new EntitateNegasitaException("Proprietatea nu exista. Contractul nu a fost actualizat.");
        }
        if (!proprietate.isDisponibila() && (contract.getProprietate() == null || contract.getProprietate().getId() != idProprietate)) {
            throw new ProprietateIndisponibilaException("Proprietatea nu este disponibila pentru actualizare.");
        }
        return proprietate;
    }

    private boolean esteValid(String numarContract, String tipContract, String dataSemnare, double valoare) {
        if (numarContract == null || numarContract.isBlank() || tipContract == null || tipContract.isBlank() || dataSemnare == null || dataSemnare.isBlank()) {
            System.out.println("Date invalide pentru contract.");
            return false;
        }
        if (valoare < 0) {
            System.out.println("Valoarea contractului trebuie sa fie pozitiva.");
            return false;
        }
        return true;
    }

    public void adminMenu(Scanner sc) {
        boolean inapoi = false;
        while (!inapoi) {
            System.out.println("\n=== ADMIN CONTRACTE ===");
            System.out.println("1. Adauga");
            System.out.println("2. Afiseaza toate");
            System.out.println("3. Cauta dupa ID");
            System.out.println("4. Cauta dupa tip");
            System.out.println("5. Afiseaza contracte client");
            System.out.println("6. Afiseaza contracte agent");
            System.out.println("7. Cauta dupa tip si agent");
            System.out.println("8. Afiseaza suma totala contracte");
            System.out.println("9. Update dupa ID");
            System.out.println("10. Sterge dupa ID");
            System.out.println("0. Inapoi");

            int optiune = citesteInt(sc, "Alegeti optiunea: ");
            switch (optiune) {
                case 1 -> add(citesteString(sc, "Introduceti numarul contractului: "), citesteString(sc, "Introduceti tipul contractului: "), citesteString(sc, "Introduceti data semnarii: "), citesteDouble(sc, "Introduceti valoarea contractului: "), citesteInt(sc, "Introduceti ID-ul clientului: "), citesteInt(sc, "Introduceti ID-ul agentului imobiliar: "), citesteInt(sc, "Introduceti ID-ul proprietatii: "));
                case 2 -> afiseazaObiecte(listAll(), "Nu exista contracte in sistem.");
                case 3 -> afiseazaById(sc);
                case 4 -> afiseazaObiecte(getByNume(citesteString(sc, "Introduceti tipul contractului: ")), "Nu exista contracte cu acest tip.");
                case 5 -> afiseazaObiecte(getByClient(citesteInt(sc, "Introduceti ID-ul clientului: ")), "Nu exista contracte pentru acest client.");
                case 6 -> afiseazaObiecte(getByAgent(citesteInt(sc, "Introduceti ID-ul agentului imobiliar: ")), "Nu exista contracte pentru acest agent.");
                case 7 -> afiseazaObiecte(getByTipSiAgent(citesteString(sc, "Introduceti tipul contractului: "), citesteInt(sc, "Introduceti ID-ul agentului imobiliar: ")), "Nu exista contracte pentru criteriile introduse.");
                case 8 -> System.out.println("Suma totala a contractelor este: " + calculeazaSumaTotala());
                case 9 -> updateById(citesteInt(sc, "Introduceti ID-ul contractului de actualizat: "), citesteString(sc, "Introduceti numarul contractului: "), citesteString(sc, "Introduceti tipul contractului: "), citesteString(sc, "Introduceti data semnarii: "), citesteDouble(sc, "Introduceti valoarea contractului: "), citesteInt(sc, "Introduceti ID-ul clientului: "), citesteInt(sc, "Introduceti ID-ul agentului imobiliar: "), citesteInt(sc, "Introduceti ID-ul proprietatii: "));
                case 10 -> deleteById(citesteInt(sc, "Introduceti ID-ul contractului de sters: "));
                case 0 -> inapoi = true;
                default -> System.out.println("Optiune invalida.");
            }
        }
    }

    public void userMenu(Scanner sc) {
        boolean inapoi = false;
        while (!inapoi) {
            System.out.println("\n=== USER CONTRACTE ===");
            System.out.println("1. Afiseaza toate");
            System.out.println("2. Cauta dupa ID");
            System.out.println("3. Cauta dupa tip");
            System.out.println("4. Afiseaza contracte client");
            System.out.println("5. Afiseaza contracte agent");
            System.out.println("6. Cauta dupa tip si agent");
            System.out.println("7. Afiseaza suma totala contracte");
            System.out.println("0. Inapoi");

            int optiune = citesteInt(sc, "Alegeti optiunea: ");
            switch (optiune) {
                case 1 -> afiseazaObiecte(listAll(), "Nu exista contracte in sistem.");
                case 2 -> afiseazaById(sc);
                case 3 -> afiseazaObiecte(getByNume(citesteString(sc, "Introduceti tipul contractului: ")), "Nu exista contracte cu acest tip.");
                case 4 -> afiseazaObiecte(getByClient(citesteInt(sc, "Introduceti ID-ul clientului: ")), "Nu exista contracte pentru acest client.");
                case 5 -> afiseazaObiecte(getByAgent(citesteInt(sc, "Introduceti ID-ul agentului imobiliar: ")), "Nu exista contracte pentru acest agent.");
                case 6 -> afiseazaObiecte(getByTipSiAgent(citesteString(sc, "Introduceti tipul contractului: "), citesteInt(sc, "Introduceti ID-ul agentului imobiliar: ")), "Nu exista contracte pentru criteriile introduse.");
                case 7 -> System.out.println("Suma totala a contractelor este: " + calculeazaSumaTotala());
                case 0 -> inapoi = true;
                default -> System.out.println("Optiune invalida.");
            }
        }
    }

    private void afiseazaById(Scanner sc) {
        Contract contract = getById(citesteInt(sc, "Introduceti ID-ul contractului: "));
        if (contract == null) {
            System.out.println("Contractul nu a fost gasit.");
            return;
        }
        System.out.println(contract);
    }
}
