package com.pao.proiect.imobiliare.model;

public class Teren extends Proprietate {
    private boolean intravilan;
    private double deschidere;
    private boolean utilitati;

    public Teren(int id, String titlu, String oras, String adresa, double pret, String tipTranzactie, double suprafata, boolean disponibila, AgentieImobiliara agentieImobiliara, boolean intravilan, double deschidere, boolean utilitati) {
        super(id, titlu, oras, adresa, pret, tipTranzactie, suprafata, disponibila, agentieImobiliara);
        this.intravilan = intravilan;
        this.deschidere = deschidere;
        this.utilitati = utilitati;
    }

    public boolean isIntravilan() {
        return intravilan;
    }

    public void setIntravilan(boolean intravilan) {
        this.intravilan = intravilan;
    }

    public double getDeschidere() {
        return deschidere;
    }

    public void setDeschidere(double deschidere) {
        this.deschidere = deschidere;
    }

    public boolean isUtilitati() {
        return utilitati;
    }

    public void setUtilitati(boolean utilitati) {
        this.utilitati = utilitati;
    }

    @Override
    public String toString() {
        return "Teren{" +
                "baza=" + super.toString() +
                ", intravilan=" + intravilan +
                ", deschidere=" + deschidere +
                ", utilitati=" + utilitati +
                '}';
    }
}
