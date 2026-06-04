package com.pao.proiect.imobiliare.model;

public class Contract {
    private int id;
    private String numarContract;
    private String tipContract;
    private String dataSemnare;
    private double valoare;
    private Client client;
    private AgentImobiliar agentImobiliar;
    private Proprietate proprietate;

    public Contract(int id, String numarContract, String tipContract, String dataSemnare, double valoare, Client client, AgentImobiliar agentImobiliar, Proprietate proprietate) {
        this.id = id;
        this.numarContract = numarContract;
        this.tipContract = tipContract;
        this.dataSemnare = dataSemnare;
        this.valoare = valoare;
        this.client = client;
        this.agentImobiliar = agentImobiliar;
        this.proprietate = proprietate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumarContract() {
        return numarContract;
    }

    public void setNumarContract(String numarContract) {
        this.numarContract = numarContract;
    }

    public String getTipContract() {
        return tipContract;
    }

    public void setTipContract(String tipContract) {
        this.tipContract = tipContract;
    }

    public String getDataSemnare() {
        return dataSemnare;
    }

    public void setDataSemnare(String dataSemnare) {
        this.dataSemnare = dataSemnare;
    }

    public double getValoare() {
        return valoare;
    }

    public void setValoare(double valoare) {
        this.valoare = valoare;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public AgentImobiliar getAgentImobiliar() {
        return agentImobiliar;
    }

    public void setAgentImobiliar(AgentImobiliar agentImobiliar) {
        this.agentImobiliar = agentImobiliar;
    }

    public Proprietate getProprietate() {
        return proprietate;
    }

    public void setProprietate(Proprietate proprietate) {
        this.proprietate = proprietate;
    }

    @Override
    public String toString() {
        return "Contract{" +
                "id=" + id +
                ", numarContract='" + numarContract + '\'' +
                ", tipContract='" + tipContract + '\'' +
                ", dataSemnare='" + dataSemnare + '\'' +
                ", valoare=" + valoare +
                ", client=" + (client == null ? "necunoscut" : client.getNume()) +
                ", agentImobiliar=" + (agentImobiliar == null ? "necunoscut" : agentImobiliar.getNume()) +
                ", proprietate=" + (proprietate == null ? "necunoscuta" : proprietate.getTitlu()) +
                '}';
    }
}
