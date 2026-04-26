package model;

import java.util.Objects;

public class Proprietate implements Comparable<Proprietate> {
    private int id;
    private String titlu;
    private String oras;
    private String adresa;
    private double pret;
    private String tipTranzactie;
    private double suprafata;
    private boolean disponibila;
    private AgentieImobiliara agentieImobiliara;
    private final CodProprietate codProprietate;

    public Proprietate(int id, String titlu, String oras, String adresa, double pret, String tipTranzactie, double suprafata, boolean disponibila, AgentieImobiliara agentieImobiliara) {
        this.id = id;
        this.titlu = titlu;
        this.oras = oras;
        this.adresa = adresa;
        this.pret = pret;
        this.tipTranzactie = tipTranzactie;
        this.suprafata = suprafata;
        this.disponibila = disponibila;
        this.agentieImobiliara = agentieImobiliara;
        this.codProprietate = new CodProprietate(id, oras);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitlu() {
        return titlu;
    }

    public void setTitlu(String titlu) {
        this.titlu = titlu;
    }

    public String getOras() {
        return oras;
    }

    public void setOras(String oras) {
        this.oras = oras;
    }

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public double getPret() {
        return pret;
    }

    public void setPret(double pret) {
        this.pret = pret;
    }

    public String getTipTranzactie() {
        return tipTranzactie;
    }

    public void setTipTranzactie(String tipTranzactie) {
        this.tipTranzactie = tipTranzactie;
    }

    public double getSuprafata() {
        return suprafata;
    }

    public void setSuprafata(double suprafata) {
        this.suprafata = suprafata;
    }

    public boolean isDisponibila() {
        return disponibila;
    }

    public void setDisponibila(boolean disponibila) {
        this.disponibila = disponibila;
    }

    public AgentieImobiliara getAgentieImobiliara() {
        return agentieImobiliara;
    }

    public void setAgentieImobiliara(AgentieImobiliara agentieImobiliara) {
        this.agentieImobiliara = agentieImobiliara;
    }

    public CodProprietate getCodProprietate() {
        return codProprietate;
    }

    @Override
    public int compareTo(Proprietate alta) {
        int dupaTitlu = this.titlu.compareToIgnoreCase(alta.titlu);
        if (dupaTitlu != 0) {
            return dupaTitlu;
        }
        return Integer.compare(this.id, alta.id);
    }

    @Override
    public String toString() {
        return "Proprietate{" +
                "id=" + id +
                ", cod='" + codProprietate + '\'' +
                ", titlu='" + titlu + '\'' +
                ", oras='" + oras + '\'' +
                ", adresa='" + adresa + '\'' +
                ", pret=" + pret +
                ", tipTranzactie='" + tipTranzactie + '\'' +
                ", suprafata=" + suprafata +
                ", disponibila=" + disponibila +
                ", agentieImobiliara=" + (agentieImobiliara == null ? "necunoscuta" : agentieImobiliara.getNume()) +
                '}';
    }

    @Override
    public boolean equals(Object obiect) {
        if (this == obiect) {
            return true;
        }
        if (!(obiect instanceof Proprietate proprietate)) {
            return false;
        }
        return id == proprietate.id && Objects.equals(codProprietate, proprietate.codProprietate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, codProprietate);
    }
}
