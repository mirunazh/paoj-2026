package model;

import java.util.Objects;

public abstract class Persoana {
    protected int id;
    protected String nume;
    protected String telefon;
    protected String email;

    protected Persoana(int id, String nume, String telefon, String email) {
        this.id = id;
        this.nume = Objects.requireNonNull(nume, "nume");
        this.telefon = Objects.requireNonNull(telefon, "telefon");
        this.email = Objects.requireNonNull(email, "email");
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = Objects.requireNonNull(nume, "nume");
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = Objects.requireNonNull(telefon, "telefon");
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = Objects.requireNonNull(email, "email");
    }

    public abstract String getRol();
}
