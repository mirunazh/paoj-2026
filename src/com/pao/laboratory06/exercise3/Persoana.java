package com.pao.laboratory06.exercise3;

public abstract class Persoana {
    protected String nume;
    protected String prenume;
    protected String telefon;

    public Persoana(String nume, String prenume, String telefon) {
        if (esteGol(nume) || esteGol(prenume)) {
            throw new IllegalArgumentException("Numele si prenumele nu pot fi goale.");
        }
        this.nume = nume;
        this.prenume = prenume;
        this.telefon = telefon;
    }

    protected boolean esteGol(String text) {
        return text == null || text.isBlank();
    }

    public String getNume() {
        return nume;
    }

    public String getPrenume() {
        return prenume;
    }

    public String getTelefon() {
        return telefon;
    }
}
