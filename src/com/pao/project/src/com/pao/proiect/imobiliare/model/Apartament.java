package com.pao.proiect.imobiliare.model;

public class Apartament extends Proprietate {
    private int numarCamere;
    private int etaj;
    private int numarBai;
    private boolean balcon;

    public Apartament(int id, String titlu, String oras, String adresa, double pret, String tipTranzactie, double suprafata, boolean disponibila, AgentieImobiliara agentieImobiliara, int numarCamere, int etaj, int numarBai, boolean balcon) {
        super(id, titlu, oras, adresa, pret, tipTranzactie, suprafata, disponibila, agentieImobiliara);
        this.numarCamere = numarCamere;
        this.etaj = etaj;
        this.numarBai = numarBai;
        this.balcon = balcon;
    }

    public int getNumarCamere() {
        return numarCamere;
    }

    public void setNumarCamere(int numarCamere) {
        this.numarCamere = numarCamere;
    }

    public int getEtaj() {
        return etaj;
    }

    public void setEtaj(int etaj) {
        this.etaj = etaj;
    }

    public int getNumarBai() {
        return numarBai;
    }

    public void setNumarBai(int numarBai) {
        this.numarBai = numarBai;
    }

    public boolean isBalcon() {
        return balcon;
    }

    public void setBalcon(boolean balcon) {
        this.balcon = balcon;
    }

    @Override
    public String toString() {
        return "Apartament{" +
                "baza=" + super.toString() +
                ", numarCamere=" + numarCamere +
                ", etaj=" + etaj +
                ", numarBai=" + numarBai +
                ", balcon=" + balcon +
                '}';
    }
}
