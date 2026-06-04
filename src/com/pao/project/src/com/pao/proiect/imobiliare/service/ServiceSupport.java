package com.pao.proiect.imobiliare.service;

import java.util.Scanner;

public abstract class ServiceSupport {
    protected void auditCurrentAction() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for (StackTraceElement element : stackTrace) {
            String className = element.getClassName();
            if (className.startsWith("com.pao.proiect.imobiliare.service.")
                    && !className.equals(ServiceSupport.class.getName())
                    && !className.equals(AuditService.class.getName())) {
                String actionName = mapAuditAction(className, element.getMethodName());
                if (actionName != null) {
                    AuditService.getInstance().logAction(actionName);
                }
                return;
            }
        }
    }

    private String mapAuditAction(String className, String methodName) {
        String simpleClassName = className.substring(className.lastIndexOf('.') + 1);

        if (simpleClassName.equals("AgentieService") && methodName.equals("add")) {
            return "adauga_agentie_imobiliara";
        }
        if (simpleClassName.equals("AgentImobiliarService") && methodName.equals("add")) {
            return "inregistreaza_agent_imobiliar";
        }
        if (simpleClassName.equals("ClientService") && methodName.equals("add")) {
            return "inregistreaza_client";
        }
        if ((simpleClassName.equals("ProprietateService")
                || simpleClassName.equals("ApartamentService")
                || simpleClassName.equals("CasaService")
                || simpleClassName.equals("TerenService"))
                && methodName.equals("add")) {
            return "adauga_proprietate";
        }
        if (simpleClassName.equals("AnuntService") && methodName.equals("add")) {
            return "adauga_anunt";
        }
        if (simpleClassName.equals("VizionareService") && methodName.equals("add")) {
            return "programeaza_vizionare";
        }
        if (simpleClassName.equals("VizionareService") && methodName.equals("schimbaStatus")) {
            return "schimba_status_vizionare";
        }
        if (simpleClassName.equals("ContractService") && methodName.equals("add")) {
            return "incheie_contract";
        }
        if (simpleClassName.equals("ListaFavoriteService") && methodName.equals("adaugaProprietate")) {
            return "adauga_proprietate_favorite";
        }
        if (simpleClassName.equals("ProprietateService") && methodName.equals("getByOrasBugetSiTranzactie")) {
            return "cauta_proprietati_dupa_oras_buget_tranzactie";
        }

        return null;
    }

    protected int citesteInt(Scanner sc, String mesaj) {
        while (true) {
            System.out.print(mesaj);
            String linie = sc.nextLine().trim();
            try {
                return Integer.parseInt(linie);
            } catch (NumberFormatException e) {
                System.out.println("Val invalida. Intro un nr intreg");
            }
        }
    }

    protected double citesteDouble(Scanner sc, String mesaj) {
        while (true) {
            System.out.print(mesaj);
            String linie = sc.nextLine().trim();
            try {
                return Double.parseDouble(linie);
            } catch (NumberFormatException e) {
                System.out.println("Valoare invalida. Introduceti un numar");
            }
        }
    }

    protected String citesteString(Scanner sc, String mesaj) {
        while (true) {
            System.out.print(mesaj);
            String valoare = sc.nextLine().trim();
            if (!valoare.isEmpty()) {
                return valoare;
            }
            System.out.println("Campul nu poate fi gol");
        }
    }

    protected boolean citesteBoolean(Scanner sc, String mesaj) {
        while (true) {
            System.out.print(mesaj);
            String raspuns = sc.nextLine().trim().toLowerCase();
            if (raspuns.equals("da")) {
                return true;
            }
            if (raspuns.equals("nu")) {
                return false;
            }
            System.out.println("Raspuns invalid. Introduceti da sau nu");
        }
    }

    protected String[] citesteVector(Scanner sc, String mesaj) {
        System.out.print(mesaj);
        String linie = sc.nextLine().trim();
        if (linie.isEmpty()) {
            return new String[0];
        }
        String[] valori = linie.split(",");
        for (int i = 0; i < valori.length; i++) {
            valori[i] = valori[i].trim();
        }
        return valori;
    }

    protected void afiseazaObiecte(Object[] obiecte, String mesajGol) {
        if (obiecte.length == 0) {
            System.out.println(mesajGol);
            return;
        }
        for (Object obiect : obiecte) {
            System.out.println(obiect);
        }
    }

    protected void afiseazaConfirmareCreare(String entitate, int id) {
        System.out.println("Conf: op de creare pt " + entitate + " efectuata cu succes. ID atribuit: " + id);
    }

    protected void afiseazaConfirmareActualizare(String entitate, int id) {
        System.out.println("Confirmare: operatia de actualizare pentru " + entitate + " cu ID " + id + " efectuata cu succes");
    }

    protected void afiseazaConfirmareStergere(String entitate, int id) {
        System.out.println("Confirmare: operatia de stergere pentru " + entitate + " cu ID " + id + " efectuata cu succes");
    }
}
