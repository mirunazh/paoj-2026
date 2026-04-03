package com.pao.laboratory06.exercise3;

public class Inginer extends Angajat implements PlataOnline, Comparable<Inginer> {
    private double sold;
    private boolean autentificat;

    public Inginer(String nume, String prenume, String telefon, double salariu, double sold) {
        super(nume, prenume, telefon, salariu);
        if (sold < 0) {
            throw new IllegalArgumentException("Soldul nu poate fi negativ.");
        }
        this.sold = sold;
    }

    @Override
    public void autentificare(String user, String parola) {
        if (user == null || user.isBlank() || parola == null || parola.isBlank()) {
            throw new IllegalArgumentException("User si parola trebuie completate.");
        }
        autentificat = true;
    }

    @Override
    public double consultareSold() {
        return sold;
    }

    @Override
    public boolean efectuarePlata(double suma) {
        if (suma <= 0) {
            throw new IllegalArgumentException("Suma trebuie sa fie pozitiva.");
        }
        if (!autentificat || suma > sold) {
            return false;
        }
        sold -= suma;
        return true;
    }

    @Override
    public int compareTo(Inginer other) {
        return nume.compareTo(other.nume);
    }

    @Override
    public String toString() {
        return "Inginer{nume='" + nume + "', prenume='" + prenume + "', salariu=" + salariu + "}";
    }
}
