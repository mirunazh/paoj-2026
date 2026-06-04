package com.pao.proiect.imobiliare;

import com.pao.proiect.imobiliare.model.AgentImobiliar;
import com.pao.proiect.imobiliare.model.AgentieImobiliara;
import com.pao.proiect.imobiliare.model.Anunt;
import com.pao.proiect.imobiliare.model.Client;
import com.pao.proiect.imobiliare.model.Contract;
import com.pao.proiect.imobiliare.model.ListaFavorite;
import com.pao.proiect.imobiliare.model.Proprietate;
import com.pao.proiect.imobiliare.model.Vizionare;
import com.pao.proiect.imobiliare.repository.AgentImobiliarRepository;
import com.pao.proiect.imobiliare.repository.AgentieRepository;
import com.pao.proiect.imobiliare.repository.ClientRepository;
import com.pao.proiect.imobiliare.repository.ContractRepository;
import com.pao.proiect.imobiliare.repository.ProprietateRepository;
import com.pao.proiect.imobiliare.repository.RaportRepository;
import com.pao.proiect.imobiliare.service.AgentImobiliarService;
import com.pao.proiect.imobiliare.service.AgentieService;
import com.pao.proiect.imobiliare.service.AnuntService;
import com.pao.proiect.imobiliare.service.ApartamentService;
import com.pao.proiect.imobiliare.service.CasaService;
import com.pao.proiect.imobiliare.service.ClientService;
import com.pao.proiect.imobiliare.service.ContractService;
import com.pao.proiect.imobiliare.service.ListaFavoriteService;
import com.pao.proiect.imobiliare.service.ProprietateService;
import com.pao.proiect.imobiliare.service.TerenService;
import com.pao.proiect.imobiliare.service.VizionareService;
import com.pao.proiect.imobiliare.util.DatabaseConnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final AgentieService AGENTIE_SERVICE = AgentieService.getInstance();
    private static final AgentImobiliarService AGENT_IMOBILIAR_SERVICE = AgentImobiliarService.getInstance();
    private static final ClientService CLIENT_SERVICE = ClientService.getInstance();
    private static final ProprietateService PROPRIETATE_SERVICE = ProprietateService.getInstance();
    private static final ApartamentService APARTAMENT_SERVICE = ApartamentService.getInstance();
    private static final CasaService CASA_SERVICE = CasaService.getInstance();
    private static final TerenService TEREN_SERVICE = TerenService.getInstance();
    private static final AnuntService ANUNT_SERVICE = AnuntService.getInstance();
    private static final VizionareService VIZIONARE_SERVICE = VizionareService.getInstance();
    private static final ContractService CONTRACT_SERVICE = ContractService.getInstance();
    private static final ListaFavoriteService LISTA_FAVORITE_SERVICE = ListaFavoriteService.getInstance();
    private static boolean exempleInitializate;

    public static void main(String[] args) {
        incarcaExempleHardcodate();
        demonstreazaEtapaIntai();
        demonstreazaEtapaDoi();
        boolean ruleaza = true;
        while (ruleaza) {
            System.out.println("\n===== REAL ESTATE MARKETPLACE =====");
            System.out.println("1. ADMIN");
            System.out.println("2. USER");
            System.out.println("0. Iesire");

            int tipUtilizator = citesteInt("Alegeti tipul de utilizator: ");
            switch (tipUtilizator) {
                case 1 -> meniuAdmin();
                case 2 -> meniuUser();
                case 0 -> {
                    ruleaza = false;
                    System.out.println("Aplicatia s-a inchis");
                }
                default -> System.out.println("Optiune invalida");
            }
        }
        SCANNER.close();
    }

    private static void incarcaExempleHardcodate() {
        if (exempleInitializate) {
            return;
        }

        AGENTIE_SERVICE.add(
                "UrbanNest",
                "Str. Aviatorilor 12, Bucuresti",
                "021-555-1010",
                "contact@urbannest.ro",
                2014,
                new String[]{"Bucuresti", "Cluj-Napoca", "Constanta"}
        );
        AGENTIE_SERVICE.add(
                "Transilvania Estates",
                "Bd. Eroilor 45, Cluj-Napoca",
                "0264-777-888",
                "office@transilvaniaestates.ro",
                2010,
                new String[]{"Cluj-Napoca", "Oradea", "Sibiu"}
        );

        AGENT_IMOBILIAR_SERVICE.add("Andrei Popescu", "0722-100-100", "andrei.popescu@urbannest.ro", "apartamente", 6, 10);
        AGENT_IMOBILIAR_SERVICE.add("Bianca Ionescu", "0733-200-200", "bianca.ionescu@urbannest.ro", "case", 8, 10);
        AGENT_IMOBILIAR_SERVICE.add("Radu Muresan", "0744-300-300", "radu.muresan@transilvaniaestates.ro", "terenuri", 5, 11);

        CLIENT_SERVICE.add("Mihai Georgescu", "0755-111-111", "mihai.georgescu@gmail.com", 185000, "cumparator");
        CLIENT_SERVICE.add("Elena Stan", "0766-222-222", "elena.stan@gmail.com", 320000, "investitor");
        CLIENT_SERVICE.add("Victor Dumitru", "0777-333-333", "victor.dumitru@gmail.com", 95000, "chirias");

        PROPRIETATE_SERVICE.add("Studio Central", "Bucuresti", "Str. Doamnei 18", 89000, "vanzare", 38, true, 10);
        APARTAMENT_SERVICE.add("Apartament Panorama", "Cluj-Napoca", "Str. Observatorului 27", 165000, "vanzare", 74, true, 11, 3, 6, 2, true);
        CASA_SERVICE.add("Casa Verde", "Bucuresti", "Str. Lalelelor 9", 285000, "vanzare", 160, true, 10, 5, 2, 220, true);
        TEREN_SERVICE.add("Teren pentru dezvoltare", "Oradea", "Sos. Borsului 101", 120000, "vanzare", 950, true, 11, true, 24, true);

        ANUNT_SERVICE.add("Studio ideal pentru investitie", "Garsoniera renovata complet, la 5 minute de metrou.", "2026-04-10", true, 40, 20);
        ANUNT_SERVICE.add("Apartament cu vedere panoramica", "Apartament luminos, terasa mare si finisaje premium.", "2026-04-14", true, 50, 20);
        ANUNT_SERVICE.add("Casa de familie cu curte mare", "Locuinta complet mobilata, potrivita pentru familie.", "2026-04-18", true, 60, 21);
        ANUNT_SERVICE.add("Teren intravilan cu utilitati", "Deschidere generoasa, ideal pentru proiect rezidential.", "2026-04-20", false, 70, 22);

        VIZIONARE_SERVICE.add("2026-04-28", "18:00", "programata", 30, 20, 50);
        VIZIONARE_SERVICE.add("2026-04-29", "17:30", "confirmata", 31, 21, 60);
        VIZIONARE_SERVICE.add("2026-05-02", "12:00", "finalizata", 32, 22, 70);

        CONTRACT_SERVICE.add("CTR-2026-001", "vanzare-cumparare", "2026-04-22", 89000, 30, 20, 40);
        CONTRACT_SERVICE.add("CTR-2026-002", "promisiune bilaterala", "2026-04-24", 15000, 31, 21, 60);

        LISTA_FAVORITE_SERVICE.add(30);
        LISTA_FAVORITE_SERVICE.add(31);
        LISTA_FAVORITE_SERVICE.adaugaProprietate(110, 50);
        LISTA_FAVORITE_SERVICE.adaugaProprietate(110, 60);
        LISTA_FAVORITE_SERVICE.adaugaProprietate(111, 40);
        LISTA_FAVORITE_SERVICE.adaugaProprietate(111, 70);

        exempleInitializate = true;
        System.out.println("Exemplele hardcodate au fost incarcate");
    }

    private static void demonstreazaEtapaIntai() {
        System.out.println("\n===== DEMO ETAPA I =====");
        System.out.println("1. Adaugarea unei agentii imobiliare noi.");
        AgentieImobiliara agentieDemo = AGENTIE_SERVICE.add(
                "Demo Agency",
                "Str. Demo 1",
                "021-999-9999",
                "agency.demo@example.com",
                2016,
                new String[]{"Iasi", "Brasov"}
        );
        afiseazaRezultatDemo("Agentia nou creata", agentieDemo);

        System.out.println("\n2. Inregistrarea unui agent imobiliar nou.");
        AgentImobiliar agentDemo = AGENT_IMOBILIAR_SERVICE.add(
                "Demo Agent",
                "0711-111-111",
                "agent.readme@example.com",
                "premium",
                4,
                agentieDemo.getId()
        );
        afiseazaRezultatDemo("Agentul nou creat", agentDemo);

        System.out.println("\n3. Inregistrarea unui client nou.");
        Client clientDemo = CLIENT_SERVICE.add(
                "Client Readme",
                "0722-222-222",
                "client.readme@example.com",
                210000,
                "cumparator"
        );
        afiseazaRezultatDemo("Clientul nou creat", clientDemo);

        System.out.println("\n4. Adaugarea unei proprietati noi in sistem.");
        Proprietate proprietateDemo = PROPRIETATE_SERVICE.add(
                "Proprietate Readme",
                "Iasi",
                "Str. Libertatii 4",
                145000,
                "vanzare",
                82,
                true,
                agentieDemo.getId()
        );
        afiseazaRezultatDemo("Proprietatea nou creata", proprietateDemo);

        System.out.println("\n5. Adaugarea unui anunt pentru o proprietate.");
        Anunt anuntDemo = ANUNT_SERVICE.add(
                "Anunt Readme",
                "Anunt demonstrativ pentru README.",
                "2026-06-02",
                true,
                proprietateDemo.getId(),
                agentDemo.getId()
        );
        afiseazaRezultatDemo("Anuntul nou creat", anuntDemo);

        System.out.println("\n6. Programarea unei vizionari pentru un client.");
        Vizionare vizionareDemo = VIZIONARE_SERVICE.add(
                "2026-06-10",
                "16:00",
                "programata",
                clientDemo.getId(),
                agentDemo.getId(),
                proprietateDemo.getId()
        );
        afiseazaRezultatDemo("Vizionarea programata", vizionareDemo);

        System.out.println("\n7. Schimbarea statusului unei vizionari.");
        VIZIONARE_SERVICE.schimbaStatus(vizionareDemo.getId(), "confirmata");
        afiseazaRezultatDemo("Vizionarea dupa actualizarea statusului", VIZIONARE_SERVICE.getById(vizionareDemo.getId()));

        System.out.println("\n8. Incheierea unui contract pentru o proprietate disponibila.");
        Contract contractDemo = CONTRACT_SERVICE.add(
                "CTR-2026-003",
                "rezervare",
                "2026-06-02",
                5000,
                clientDemo.getId(),
                agentDemo.getId(),
                proprietateDemo.getId()
        );
        afiseazaRezultatDemo("Contractul creat", contractDemo);
        afiseazaRezultatDemo("Proprietatea dupa contract", PROPRIETATE_SERVICE.getById(proprietateDemo.getId()));

        System.out.println("\n9. Adaugarea unei proprietati in lista de favorite a unui client.");
        ListaFavorite listaDemo = LISTA_FAVORITE_SERVICE.add(clientDemo.getId());
        LISTA_FAVORITE_SERVICE.adaugaProprietate(listaDemo.getId(), proprietateDemo.getId());
        afiseazaRezultatDemo("Lista de favorite actualizata", LISTA_FAVORITE_SERVICE.getById(listaDemo.getId()));

        System.out.println("\n10. Cautarea proprietatilor dupa oras, buget si tip de tranzactie.");
        Proprietate[] rezultate = PROPRIETATE_SERVICE.getByOrasBugetSiTranzactie("Iasi", 200000, "vanzare");
        System.out.println("Cautare proprietati dupa oras, buget si tranzactie:");
        for (Proprietate rezultat : rezultate) {
            System.out.println("  " + rezultat);
        }
    }

    private static void demonstreazaEtapaDoi() {
        System.out.println("\n===== DEMO ETAPA II =====");
        try {
            System.out.println("1. Initializare schema SQL si reset controlat al tabelelor.");
            initializeDatabaseSchema();
            AgentieRepository agentieRepository = new AgentieRepository();
            ClientRepository clientRepository = new ClientRepository();
            AgentImobiliarRepository agentRepository = new AgentImobiliarRepository();
            ProprietateRepository proprietateRepository = new ProprietateRepository();
            ContractRepository contractRepository = new ContractRepository();
            RaportRepository raportRepository = new RaportRepository();

            int agentieId = 9001;
            int clientId = 9002;
            int agentId = 9003;
            int proprietateId = 9004;
            int contractId = 9005;
            int vizionareId1 = 9006;
            int vizionareId2 = 9007;

            contractRepository.delete(contractId);
            stergeVizionareDemo(vizionareId2);
            stergeVizionareDemo(vizionareId1);
            proprietateRepository.delete(proprietateId);
            agentRepository.delete(agentId);
            clientRepository.delete(clientId);
            agentieRepository.delete(agentieId);

            AgentieImobiliara agentie = new AgentieImobiliara(
                    agentieId,
                    "Demo Estates",
                    "Str. Exemplu 1, Bucuresti",
                    "021-000-0000",
                    "demo.estates@example.com",
                    2018,
                    new String[]{"Bucuresti", "Brasov"}
            );
            Client client = new Client(clientId, "Client Demo", "0700-000-001", "client.demo@example.com", 250000, "cumparator");
            AgentImobiliar agent = new AgentImobiliar(agentId, "Agent Demo", "0700-000-002", "agent.demo@example.com", "rezidential", 7, agentie);
            Proprietate proprietate = new Proprietate(proprietateId, "Proprietate Demo", "Bucuresti", "Str. Test 10", 199000, "vanzare", 88, true, agentie);
            Contract contract = new Contract(contractId, "CTR-DEMO-9005", "vanzare-cumparare", "2026-06-02", 199000, client, agent, proprietate);

            System.out.println("2. Persistenta JDBC prin repository-uri concrete.");
            agentieRepository.save(agentie);
            clientRepository.save(client);
            agentRepository.save(agent);
            proprietateRepository.save(proprietate);

            afiseazaRezultatRepository("Agentie salvata si citita din DB", agentieRepository.findById(agentieId).orElse(null));
            afiseazaRezultatRepository("Client salvat si citit din DB", clientRepository.findById(clientId).orElse(null));
            afiseazaRezultatRepository("Agent salvat si citit din DB", agentRepository.findById(agentId).orElse(null));
            afiseazaRezultatRepository("Proprietate salvata si citita din DB", proprietateRepository.findById(proprietateId).orElse(null));
            System.out.println("   findAll():");
            System.out.println("   - agentii in DB dupa insert: " + agentieRepository.findAll().size());
            System.out.println("   - clienti in DB dupa insert: " + clientRepository.findAll().size());
            System.out.println("   - agenti in DB dupa insert: " + agentRepository.findAll().size());
            System.out.println("   - proprietati in DB dupa insert: " + proprietateRepository.findAll().size());

            System.out.println("\n2.a Actualizare si stergere prin repository-uri.");
            client.setBuget(265000);
            client.setTipClient("investitor");
            clientRepository.update(client);
            afiseazaRezultatRepository("Client actualizat prin update()", clientRepository.findById(clientId).orElse(null));

            AgentieImobiliara agentieTemporara = new AgentieImobiliara(
                    9010,
                    "Temporary Agency",
                    "Str. Temporara 5, Ploiesti",
                    "021-123-1234",
                    "temporary.agency@example.com",
                    2020,
                    new String[]{"Ploiesti"}
            );
            agentieRepository.save(agentieTemporara);
            System.out.println("   Agentie temporara salvata pentru demo delete(): " + agentieRepository.findById(9010).orElse(null));
            agentieRepository.delete(9010);
            System.out.println("   Agentia temporara mai exista dupa delete()? " + agentieRepository.findById(9010).isPresent());

            System.out.println("\n2.b Date suplimentare pentru rapoartele JOIN.");
            insereazaVizionareDemo(vizionareId1, "2026-06-03", "10:30", "programata", clientId, agentId, proprietateId);
            insereazaVizionareDemo(vizionareId2, "2026-06-04", "12:00", "confirmata", clientId, agentId, proprietateId);
            System.out.println("   Au fost inserate doua vizionari active pentru a evidentia rapoartele agregate si detaliate.");

            System.out.println("\n3. Tranzactie JDBC explicita in ContractRepository.saveWithTransaction(...).");
            System.out.println("   Inainte de tranzactie, proprietatea este disponibila = " + proprietate.isDisponibila());
            contractRepository.saveWithTransaction(contract);
            Proprietate proprietateActualizata = proprietateRepository.findById(proprietateId).orElse(null);
            afiseazaRezultatRepository("Contract salvat in aceeasi tranzactie", contractRepository.findById(contractId).orElse(null));
            if (proprietateActualizata != null) {
                System.out.println("   Dupa commit, proprietatea este disponibila = " + proprietateActualizata.isDisponibila());
            }
            System.out.println("   Efect demonstrat: inserarea contractului si actualizarea disponibilitatii au loc impreuna.");

            System.out.println("\n4. Interogari JOIN pentru rapoarte complexe.");
            afiseazaPrimeleRezultate(
                    "Clienti cu vizionari active",
                    "   JOIN intre clienti si vizionari pentru a numara programarile active.",
                    raportRepository.findClientiCuNumarulDeVizionariActive()
            );
            afiseazaPrimeleRezultate(
                    "Top proprietati vizionate",
                    "   JOIN intre proprietati, agentii si vizionari pentru a masura interesul din platforma.",
                    raportRepository.findTop5ProprietatiCeleMaiVizionateCuAgentiaLor()
            );
            afiseazaPrimeleRezultate(
                    "Vizionari active cu detalii",
                    "   JOIN intre vizionari, clienti, agenti si proprietati pentru context complet.",
                    raportRepository.findVizionariActiveCuDetalii()
            );
            afiseazaPrimeleRezultate(
                    "Top agenti dupa contracte",
                    "   JOIN intre agenti, agentii si contracte pentru volum si valoare totala.",
                    raportRepository.findTop5AgentiDupaNumarulDeContracte()
            );
            afiseazaPrimeleRezultate(
                    "Contractele clientului demo",
                    "   JOIN filtrat dupa client pentru a lega contractul de agent si proprietate.",
                    raportRepository.findContracteleUnuiClientCuDetalii(clientId)
            );
        } catch (RuntimeException e) {
            System.out.println("Demo JDBC indisponibil: " + e.getMessage());
            System.out.println("Asigura-te ca ai rulat schema.sql si ca baza de date este pornita.");
        }
        afiseazaAudit();
    }

    private static void initializeDatabaseSchema() {
        try (InputStream inputStream = openSchemaStream()) {
            StringBuilder builder = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line).append('\n');
                }
            }

            Connection connection = DatabaseConnection.getInstance().getConnection();
            try (Statement statement = connection.createStatement()) {
                for (String sqlStatement : splitSqlStatements(builder.toString())) {
                    if (!sqlStatement.isBlank()) {
                        statement.execute(sqlStatement);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Eroare la citirea schemei SQL.", e);
        } catch (Exception e) {
            throw new RuntimeException("Eroare la initializarea schemei bazei de date.", e);
        }
    }

    private static InputStream openSchemaStream() throws IOException {
        InputStream classpathStream = Main.class.getClassLoader().getResourceAsStream("schema.sql");
        if (classpathStream != null) {
            return classpathStream;
        }

        for (Path candidate : getSchemaCandidates()) {
            if (Files.exists(candidate)) {
                return Files.newInputStream(candidate);
            }
        }

        throw new IOException("Fisierul schema.sql nu a fost gasit nici in classpath, nici in proiect.");
    }

    private static List<Path> getSchemaCandidates() {
        return List.of(
                Path.of("schema.sql"),
                Path.of("resources", "schema.sql"),
                Path.of("src", "resources", "schema.sql"),
                Path.of("src", "com", "pao", "project", "schema.sql"),
                Path.of("src", "com", "pao", "project", "resources", "schema.sql"),
                Path.of("src", "com", "pao", "project", "src", "schema.sql"),
                Path.of("src", "com", "pao", "project", "src", "resources", "schema.sql")
        );
    }

    private static List<String> splitSqlStatements(String sqlScript) {
        List<String> statements = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        for (String line : sqlScript.split("\n")) {
            String trimmed = line.trim();
            if (trimmed.isEmpty() || trimmed.startsWith("--")) {
                continue;
            }
            current.append(line).append('\n');
            if (trimmed.endsWith(";")) {
                statements.add(current.toString().trim().replaceAll(";$", ""));
                current.setLength(0);
            }
        }
        if (current.length() > 0) {
            statements.add(current.toString().trim());
        }
        return statements;
    }

    private static void afiseazaPrimeleRezultate(String titlu, String descriere, List<String> rezultate) {
        System.out.println(titlu + ":");
        System.out.println(descriere);
        if (rezultate.isEmpty()) {
            System.out.println("  fara rezultate");
            return;
        }
        int limita = Math.min(2, rezultate.size());
        for (int i = 0; i < limita; i++) {
            System.out.println("  " + rezultate.get(i));
        }
    }

    private static void afiseazaRezultatRepository(String eticheta, Object rezultat) {
        System.out.println(eticheta + ":");
        System.out.println("  " + rezultat);
    }

    private static void afiseazaRezultatDemo(String eticheta, Object rezultat) {
        System.out.println(eticheta + ":");
        System.out.println("  " + rezultat);
    }

    private static void insereazaVizionareDemo(int id, String data, String ora, String status, int clientId, int agentId, int proprietateId) {
        String sql = "INSERT INTO vizionari (id, data_vizionare, ora_vizionare, status, client_id, agent_id, proprietate_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.setString(2, data);
            statement.setString(3, ora);
            statement.setString(4, status);
            statement.setInt(5, clientId);
            statement.setInt(6, agentId);
            statement.setInt(7, proprietateId);
            statement.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("Eroare la inserarea vizionarii demo in baza de date.", e);
        }
    }

    private static void stergeVizionareDemo(int id) {
        String sql = "DELETE FROM vizionari WHERE id = ?";
        try (PreparedStatement statement = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("Eroare la stergerea vizionarii demo din baza de date.", e);
        }
    }

    private static void afiseazaAudit() {
        try {
            Path auditPath = findAuditPath();
            if (!Files.exists(auditPath)) {
                System.out.println("\nAudit: fisierul audit.csv nu exista inca.");
                return;
            }
            System.out.println("\nAudit preview:");
            List<String> lines = Files.readAllLines(auditPath);
            int limita = Math.min(5, lines.size());
            for (int i = 0; i < limita; i++) {
                System.out.println("  " + lines.get(i));
            }
        } catch (Exception e) {
            System.out.println("Nu am putut afisa auditul: " + e.getMessage());
        }
    }

    private static Path findAuditPath() {
        List<Path> candidates = List.of(
                Path.of("audit.csv"),
                Path.of("src", "com", "pao", "project", "audit.csv"),
                Path.of("src", "com", "pao", "project", "src", "audit.csv")
        );
        for (Path candidate : candidates) {
            if (Files.exists(candidate)) {
                return candidate;
            }
        }
        return candidates.get(0);
    }

    private static void meniuAdmin() {
        boolean inapoi = false;
        while (!inapoi) {
            afiseazaMeniuEntitati("ADMIN");
            int optiune = citesteInt("Alegeti entitatea: ");
            switch (optiune) {
                case 1 -> AGENTIE_SERVICE.adminMenu(SCANNER);
                case 2 -> AGENT_IMOBILIAR_SERVICE.adminMenu(SCANNER);
                case 3 -> CLIENT_SERVICE.adminMenu(SCANNER);
                case 4 -> PROPRIETATE_SERVICE.adminMenu(SCANNER);
                case 5 -> APARTAMENT_SERVICE.adminMenu(SCANNER);
                case 6 -> CASA_SERVICE.adminMenu(SCANNER);
                case 7 -> TEREN_SERVICE.adminMenu(SCANNER);
                case 8 -> ANUNT_SERVICE.adminMenu(SCANNER);
                case 9 -> VIZIONARE_SERVICE.adminMenu(SCANNER);
                case 10 -> CONTRACT_SERVICE.adminMenu(SCANNER);
                case 11 -> LISTA_FAVORITE_SERVICE.adminMenu(SCANNER);
                case 0 -> inapoi = true;
                default -> System.out.println("Optiune invalida");
            }
        }
    }

    private static void meniuUser() {
        boolean inapoi = false;
        while (!inapoi) {
            afiseazaMeniuEntitati("USER");
            int optiune = citesteInt("Alegeti entitatea: ");
            switch (optiune) {
                case 1 -> AGENTIE_SERVICE.userMenu(SCANNER);
                case 2 -> AGENT_IMOBILIAR_SERVICE.userMenu(SCANNER);
                case 3 -> CLIENT_SERVICE.userMenu(SCANNER);
                case 4 -> PROPRIETATE_SERVICE.userMenu(SCANNER);
                case 5 -> APARTAMENT_SERVICE.userMenu(SCANNER);
                case 6 -> CASA_SERVICE.userMenu(SCANNER);
                case 7 -> TEREN_SERVICE.userMenu(SCANNER);
                case 8 -> ANUNT_SERVICE.userMenu(SCANNER);
                case 9 -> VIZIONARE_SERVICE.userMenu(SCANNER);
                case 10 -> CONTRACT_SERVICE.userMenu(SCANNER);
                case 11 -> LISTA_FAVORITE_SERVICE.userMenu(SCANNER);
                case 0 -> inapoi = true;
                default -> System.out.println("Optiune invalida");
            }
        }
    }

    private static void afiseazaMeniuEntitati(String tip) {
        System.out.println("\n===== " + tip + " ALEGE ENTITATE =====");
        System.out.println("1. Agentii");
        System.out.println("2. Agenti imobiliari");
        System.out.println("3. Clienti");
        System.out.println("4. Proprietati");
        System.out.println("5. Apartamente");
        System.out.println("6. Case");
        System.out.println("7. Terenuri");
        System.out.println("8. Anunturi");
        System.out.println("9. Vizionari");
        System.out.println("10. Contracte");
        System.out.println("11. Liste favorite");
        System.out.println("0. Inapoi");
    }

    private static int citesteInt(String mesaj) {
        while (true) {
            System.out.print(mesaj);
            String linie = SCANNER.nextLine().trim();
            try {
                return Integer.parseInt(linie);
            } catch (NumberFormatException e) {
                System.out.println("Val invalida. Intro un nr intreg");
            }
        }
    }
}
