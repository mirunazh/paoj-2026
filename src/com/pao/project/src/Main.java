import service.AgentImobiliarService;
import service.AgentieService;
import service.AnuntService;
import service.ApartamentService;
import service.CasaService;
import service.ClientService;
import service.ContractService;
import service.ListaFavoriteService;
import service.ProprietateService;
import service.TerenService;
import service.VizionareService;

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
