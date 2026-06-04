package com.pao.proiect.imobiliare.model;

import java.util.Arrays;
import java.util.Objects;

public class AgentieImobiliara {
    private int id;
    private String nume;
    private String adresa;
    private String telefon;
    private String email;
    private int anDeschidere;
    private String[] oraseActive;

    public AgentieImobiliara(int id, String nume, String adresa, String telefon, String email, int anDeschidere, String[] oraseActive) {
        this.id = id;
        this.nume = nume;
        this.adresa = adresa;
        this.telefon = telefon;
        this.email = email;
        this.anDeschidere = anDeschidere;
        this.oraseActive = oraseActive;
    }

    public int getId() {
        return id;
    }

    public String getNume() {
        return nume;
    }

    public String getAdresa() {
        return adresa;
    }

    public String getTelefon() {
        return telefon;
    }

    public String getEmail() {
        return email;
    }

    public int getAnDeschidere() {
        return anDeschidere;
    }

    public String[] getOraseActive() {
        return oraseActive;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAnDeschidere(int anDeschidere) {
        this.anDeschidere = anDeschidere;
    }

    public void setOraseActive(String[] oraseActive) {
        this.oraseActive = oraseActive;
    }

    @Override
    public String toString() {
        return "AgentieImobiliara{" +
                "id=" + id +
                ", nume='" + nume + '\'' +
                ", adresa='" + adresa + '\'' +
                ", telefon='" + telefon + '\'' +
                ", email='" + email + '\'' +
                ", anDeschidere=" + anDeschidere +
                ", oraseActive=" + Arrays.toString(oraseActive) +
                '}';
    }

    @Override
    public boolean equals(Object obiect) {
        if (this == obiect) {
            return true;
        }
        if (!(obiect instanceof AgentieImobiliara agentie)) {
            return false;
        }
        return id == agentie.id && Objects.equals(email, agentie.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }
}
