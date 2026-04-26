package service;

import model.AgentieImobiliara;
import model.Casa;

import java.util.Scanner;

public class CasaService extends ServiceSupport {
    private Casa[] caseImobiliare;
    private int nextId = 60;
    private final AgentieService agentieService = AgentieService.getInstance();
    private final ProprietateService proprietateService = ProprietateService.getInstance();

    private CasaService() {
        this.caseImobiliare = new Casa[0];
    }

    private static class Holder {
        private static final CasaService INSTANCE = new CasaService();
    }

    public static CasaService getInstance() {
        return Holder.INSTANCE;
    }

    private int genereazaId() {
        return nextId++;
    }

    public Casa add(String titlu, String oras, String adresa, double pret, String tipTranzactie, double suprafata, boolean disponibila, int idAgentie, int numarCamere, int numarEtaje, double suprafataCurte, boolean garaj) {
        AgentieImobiliara agentie = agentieService.getById(idAgentie);
        if (agentie == null) {
            System.out.println("Agentia imobiliara nu exista. Casa nu a fost creata.");
            return null;
        }
        if (!esteValid(titlu, oras, adresa, pret, tipTranzactie, suprafata, numarCamere, numarEtaje, suprafataCurte)) {
            return null;
        }
        int id = genereazaId();
        Casa casa = new Casa(id, titlu, oras, adresa, pret, tipTranzactie, suprafata, disponibila, agentie, numarCamere, numarEtaje, suprafataCurte, garaj);
        Casa[] nou = new Casa[caseImobiliare.length + 1];
        System.arraycopy(caseImobiliare, 0, nou, 0, caseImobiliare.length);
        nou[nou.length - 1] = casa;
        caseImobiliare = nou;
        proprietateService.addExisting(casa);
        afiseazaConfirmareCreare("Casa", id);
        return casa;
    }

    public Casa[] listAll() {
        return caseImobiliare;
    }

    public Casa getById(int id) {
        for (Casa casa : caseImobiliare) {
            if (casa.getId() == id) {
                return casa;
            }
        }
        return null;
    }

    public boolean updateById(int id, String titlu, String oras, String adresa, double pret, String tipTranzactie, double suprafata, boolean disponibila, int idAgentie, int numarCamere, int numarEtaje, double suprafataCurte, boolean garaj) {
        Casa casa = getById(id);
        if (casa == null) {
            System.out.println("Casa nu a fost gasita.");
            return false;
        }
        AgentieImobiliara agentie = agentieService.getById(idAgentie);
        if (agentie == null) {
            System.out.println("Agentia imobiliara nu exista. Casa nu a fost actualizata.");
            return false;
        }
        if (!esteValid(titlu, oras, adresa, pret, tipTranzactie, suprafata, numarCamere, numarEtaje, suprafataCurte)) {
            return false;
        }
        casa.setTitlu(titlu);
        casa.setOras(oras);
        casa.setAdresa(adresa);
        casa.setPret(pret);
        casa.setTipTranzactie(tipTranzactie);
        casa.setSuprafata(suprafata);
        casa.setDisponibila(disponibila);
        casa.setAgentieImobiliara(agentie);
        casa.setNumarCamere(numarCamere);
        casa.setNumarEtaje(numarEtaje);
        casa.setSuprafataCurte(suprafataCurte);
        casa.setGaraj(garaj);
        afiseazaConfirmareActualizare("Casa", id);
        return true;
    }

    public boolean deleteById(int id) {
        int pozitie = -1;
        for (int i = 0; i < caseImobiliare.length; i++) {
            if (caseImobiliare[i].getId() == id) {
                pozitie = i;
                break;
            }
        }
        if (pozitie == -1) {
            System.out.println("Casa nu a fost gasita.");
            return false;
        }
        Casa[] nou = new Casa[caseImobiliare.length - 1];
        for (int i = 0, j = 0; i < caseImobiliare.length; i++) {
            if (i != pozitie) {
                nou[j++] = caseImobiliare[i];
            }
        }
        caseImobiliare = nou;
        proprietateService.deleteById(id);
        afiseazaConfirmareStergere("Casa", id);
        return true;
    }

    public Casa[] getByNume(String titlu) {
        int count = 0;
        for (Casa casa : caseImobiliare) {
            if (casa.getTitlu().equalsIgnoreCase(titlu)) {
                count++;
            }
        }
        Casa[] rezultat = new Casa[count];
        for (int i = 0, j = 0; i < caseImobiliare.length; i++) {
            if (caseImobiliare[i].getTitlu().equalsIgnoreCase(titlu)) {
                rezultat[j++] = caseImobiliare[i];
            }
        }
        return rezultat;
    }

    public Casa[] getBySuprafataCurte(double suprafataMinima) {
        int count = 0;
        for (Casa casa : caseImobiliare) {
            if (casa.getSuprafataCurte() >= suprafataMinima) {
                count++;
            }
        }
        Casa[] rezultat = new Casa[count];
        for (int i = 0, j = 0; i < caseImobiliare.length; i++) {
            if (caseImobiliare[i].getSuprafataCurte() >= suprafataMinima) {
                rezultat[j++] = caseImobiliare[i];
            }
        }
        return rezultat;
    }

    public Casa[] getByEtaje(int numarEtaje) {
        int count = 0;
        for (Casa casa : caseImobiliare) {
            if (casa.getNumarEtaje() == numarEtaje) {
                count++;
            }
        }
        Casa[] rezultat = new Casa[count];
        for (int i = 0, j = 0; i < caseImobiliare.length; i++) {
            if (caseImobiliare[i].getNumarEtaje() == numarEtaje) {
                rezultat[j++] = caseImobiliare[i];
            }
        }
        return rezultat;
    }

    public Casa[] getByOrasCurteSiEtaje(String oras, double suprafataCurte, int numarEtaje) {
        int count = 0;
        for (Casa casa : caseImobiliare) {
            if (casa.getOras().equalsIgnoreCase(oras) && casa.getSuprafataCurte() >= suprafataCurte && casa.getNumarEtaje() == numarEtaje) {
                count++;
            }
        }
        Casa[] rezultat = new Casa[count];
        for (int i = 0, j = 0; i < caseImobiliare.length; i++) {
            if (caseImobiliare[i].getOras().equalsIgnoreCase(oras) && caseImobiliare[i].getSuprafataCurte() >= suprafataCurte && caseImobiliare[i].getNumarEtaje() == numarEtaje) {
                rezultat[j++] = caseImobiliare[i];
            }
        }
        return rezultat;
    }

    private boolean esteValid(String titlu, String oras, String adresa, double pret, String tipTranzactie, double suprafata, int numarCamere, int numarEtaje, double suprafataCurte) {
        if (titlu == null || titlu.isBlank() || oras == null || oras.isBlank() || adresa == null || adresa.isBlank() || tipTranzactie == null || tipTranzactie.isBlank()) {
            System.out.println("Date invalide pentru casa.");
            return false;
        }
        if (pret < 0 || suprafata <= 0 || numarCamere <= 0 || numarEtaje <= 0 || suprafataCurte < 0) {
            System.out.println("Valorile numerice pentru casa sunt invalide.");
            return false;
        }
        return true;
    }

    public void adminMenu(Scanner sc) {
        boolean inapoi = false;
        while (!inapoi) {
            System.out.println("\n=== ADMIN CASE ===");
            System.out.println("1. Adauga");
            System.out.println("2. Afiseaza toate");
            System.out.println("3. Cauta dupa ID");
            System.out.println("4. Cauta dupa nume");
            System.out.println("5. Filtrare dupa suprafata curte");
            System.out.println("6. Filtrare dupa etaje");
            System.out.println("7. Filtrare multi-criteriu");
            System.out.println("8. Update dupa ID");
            System.out.println("9. Sterge dupa ID");
            System.out.println("0. Inapoi");

            int optiune = citesteInt(sc, "Alegeti optiunea: ");
            switch (optiune) {
                case 1 -> add(citesteString(sc, "Introduceti titlul casei: "), citesteString(sc, "Introduceti orasul: "), citesteString(sc, "Introduceti adresa casei: "), citesteDouble(sc, "Introduceti pretul: "), citesteString(sc, "Introduceti tipul tranzactiei: "), citesteDouble(sc, "Introduceti suprafata: "), citesteBoolean(sc, "Casa este disponibila? (da/nu): "), citesteInt(sc, "Introduceti ID-ul agentiei imobiliare: "), citesteInt(sc, "Introduceti numarul de camere: "), citesteInt(sc, "Introduceti numarul de etaje: "), citesteDouble(sc, "Introduceti suprafata curtii: "), citesteBoolean(sc, "Casa are garaj? (da/nu): "));
                case 2 -> afiseazaObiecte(listAll(), "Nu exista case in sistem.");
                case 3 -> afiseazaById(sc);
                case 4 -> afiseazaObiecte(getByNume(citesteString(sc, "Introduceti titlul casei: ")), "Nu exista case cu acest titlu.");
                case 5 -> afiseazaObiecte(getBySuprafataCurte(citesteDouble(sc, "Introduceti suprafata minima a curtii: ")), "Nu exista case pentru aceasta suprafata de curte.");
                case 6 -> afiseazaObiecte(getByEtaje(citesteInt(sc, "Introduceti numarul de etaje: ")), "Nu exista case cu acest numar de etaje.");
                case 7 -> afiseazaObiecte(getByOrasCurteSiEtaje(citesteString(sc, "Introduceti orasul: "), citesteDouble(sc, "Introduceti suprafata minima a curtii: "), citesteInt(sc, "Introduceti numarul de etaje: ")), "Nu exista case pentru criteriile introduse.");
                case 8 -> updateById(citesteInt(sc, "Introduceti ID-ul casei de actualizat: "), citesteString(sc, "Introduceti titlul casei: "), citesteString(sc, "Introduceti orasul: "), citesteString(sc, "Introduceti adresa casei: "), citesteDouble(sc, "Introduceti pretul: "), citesteString(sc, "Introduceti tipul tranzactiei: "), citesteDouble(sc, "Introduceti suprafata: "), citesteBoolean(sc, "Casa este disponibila? (da/nu): "), citesteInt(sc, "Introduceti ID-ul agentiei imobiliare: "), citesteInt(sc, "Introduceti numarul de camere: "), citesteInt(sc, "Introduceti numarul de etaje: "), citesteDouble(sc, "Introduceti suprafata curtii: "), citesteBoolean(sc, "Casa are garaj? (da/nu): "));
                case 9 -> deleteById(citesteInt(sc, "Introduceti ID-ul casei de sters: "));
                case 0 -> inapoi = true;
                default -> System.out.println("Optiune invalida.");
            }
        }
    }

    public void userMenu(Scanner sc) {
        boolean inapoi = false;
        while (!inapoi) {
            System.out.println("\n=== USER CASE ===");
            System.out.println("1. Afiseaza toate");
            System.out.println("2. Cauta dupa ID");
            System.out.println("3. Cauta dupa nume");
            System.out.println("4. Filtrare dupa suprafata curte");
            System.out.println("5. Filtrare dupa etaje");
            System.out.println("6. Filtrare multi-criteriu");
            System.out.println("0. Inapoi");

            int optiune = citesteInt(sc, "Alegeti optiunea: ");
            switch (optiune) {
                case 1 -> afiseazaObiecte(listAll(), "Nu exista case in sistem.");
                case 2 -> afiseazaById(sc);
                case 3 -> afiseazaObiecte(getByNume(citesteString(sc, "Introduceti titlul casei: ")), "Nu exista case cu acest titlu.");
                case 4 -> afiseazaObiecte(getBySuprafataCurte(citesteDouble(sc, "Introduceti suprafata minima a curtii: ")), "Nu exista case pentru aceasta suprafata de curte.");
                case 5 -> afiseazaObiecte(getByEtaje(citesteInt(sc, "Introduceti numarul de etaje: ")), "Nu exista case cu acest numar de etaje.");
                case 6 -> afiseazaObiecte(getByOrasCurteSiEtaje(citesteString(sc, "Introduceti orasul: "), citesteDouble(sc, "Introduceti suprafata minima a curtii: "), citesteInt(sc, "Introduceti numarul de etaje: ")), "Nu exista case pentru criteriile introduse.");
                case 0 -> inapoi = true;
                default -> System.out.println("Optiune invalida.");
            }
        }
    }

    private void afiseazaById(Scanner sc) {
        Casa casa = getById(citesteInt(sc, "Introduceti ID-ul casei: "));
        if (casa == null) {
            System.out.println("Casa nu a fost gasita.");
            return;
        }
        System.out.println(casa);
    }
}
