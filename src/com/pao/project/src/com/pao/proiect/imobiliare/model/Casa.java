package com.pao.proiect.imobiliare.model;

public class Casa extends Proprietate {
    private int numarCamere;
    private int numarEtaje;
    private double suprafataCurte;
    private boolean garaj;

    public Casa(int id, String titlu, String oras, String adresa, double pret, String tipTranzactie, double suprafata, boolean disponibila, AgentieImobiliara agentieImobiliara, int numarCamere, int numarEtaje, double suprafataCurte, boolean garaj) {
        super(id, titlu, oras, adresa, pret, tipTranzactie, suprafata, disponibila, agentieImobiliara);
        this.numarCamere = numarCamere;
        this.numarEtaje = numarEtaje;
        this.suprafataCurte = suprafataCurte;
        this.garaj = garaj;
    }

    public int getNumarCamere() {
        return numarCamere;
    }

    public void setNumarCamere(int numarCamere) {
        this.numarCamere = numarCamere;
    }

    public int getNumarEtaje() {
        return numarEtaje;
    }

    public void setNumarEtaje(int numarEtaje) {
        this.numarEtaje = numarEtaje;
    }

    public double getSuprafataCurte() {
        return suprafataCurte;
    }

    public void setSuprafataCurte(double suprafataCurte) {
        this.suprafataCurte = suprafataCurte;
    }

    public boolean isGaraj() {
        return garaj;
    }

    public void setGaraj(boolean garaj) {
        this.garaj = garaj;
    }

    @Override
    public String toString() {
        return "Casa{" +
                "baza=" + super.toString() +
                ", numarCamere=" + numarCamere +
                ", numarEtaje=" + numarEtaje +
                ", suprafataCurte=" + suprafataCurte +
                ", garaj=" + garaj +
                '}';
    }
}
