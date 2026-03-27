package com.pao.laboratory05.audit;

import java.time.LocalDateTime;
import java.util.Arrays;

public class AngajatService {
    private Angajat[] angajati = new Angajat[0];
    private AuditEntry[] auditLog = new AuditEntry[0];

    private AngajatService() {
    }

    private static class Holder {
        private static final AngajatService INSTANCE = new AngajatService();
    }

    public static AngajatService getInstance() {
        return Holder.INSTANCE;
    }

    public void addAngajat(Angajat a) {
        Angajat[] copie = new Angajat[angajati.length + 1];
        System.arraycopy(angajati, 0, copie, 0, angajati.length);
        copie[copie.length - 1] = a;
        angajati = copie;
        System.out.println("Angajat adăugat: " + a.getNume());
        logAction("ADD", a.getNume());
    }

    public void printAll() {
        for (Angajat angajat : angajati) {
            System.out.println(angajat);
        }
    }

    public void listBySalary() {
        Angajat[] copie = angajati.clone();
        Arrays.sort(copie);

        for (int i = 0; i < copie.length; i++) {
            System.out.println((i + 1) + ". " + copie[i]);
        }
    }

    public void findByDepartament(String numeDept) {
        logAction("FIND_BY_DEPT", numeDept);

        boolean gasit = false;

        for (Angajat angajat : angajati) {
            if (angajat.getDepartament().nume().equalsIgnoreCase(numeDept)) {
                if (!gasit) {
                    System.out.println("--- Angajați din " + numeDept + " ---");
                }
                System.out.println(angajat);
                gasit = true;
            }
        }

        if (!gasit) {
            System.out.println("Niciun angajat în departamentul: " + numeDept);
        }
    }

    public void printAuditLog() {
        for (AuditEntry auditEntry : auditLog) {
            System.out.println(auditEntry);
        }
    }

    private void logAction(String action, String target) {
        AuditEntry auditEntry = new AuditEntry(action, target, LocalDateTime.now().toString());
        AuditEntry[] copie = new AuditEntry[auditLog.length + 1];
        System.arraycopy(auditLog, 0, copie, 0, auditLog.length);
        copie[copie.length - 1] = auditEntry;
        auditLog = copie;
    }
}
