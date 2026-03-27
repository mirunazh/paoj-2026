package com.pao.laboratory05.biblioteca;

import java.util.Arrays;
import java.util.Comparator;

public class BibliotecaService {
    private Carte[] carti = new Carte[0];

    private BibliotecaService() {
    }

    private static class Holder {
        private static final BibliotecaService INSTANCE = new BibliotecaService();
    }

    public static BibliotecaService getInstance() {
        return Holder.INSTANCE;
    }

    public void addCarte(Carte carte) {
        Carte[] copie = new Carte[carti.length + 1];
        System.arraycopy(carti, 0, copie, 0, carti.length);
        copie[copie.length - 1] = carte;
        carti = copie;
        System.out.println("Carte adăugată: " + carte.getTitlu());
    }

    public void listSortedByRating() {
        Carte[] copie = carti.clone();
        Arrays.sort(copie);
        printCarti(copie);
    }

    public void listSortedBy(Comparator<Carte> comparator) {
        Carte[] copie = carti.clone();
        Arrays.sort(copie, comparator);
        printCarti(copie);
    }

    private void printCarti(Carte[] cartiDeAfisat) {
        for (int i = 0; i < cartiDeAfisat.length; i++) {
            System.out.println((i + 1) + ". " + cartiDeAfisat[i]);
        }
    }
}
