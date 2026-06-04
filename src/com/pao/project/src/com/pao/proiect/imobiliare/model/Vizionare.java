package com.pao.proiect.imobiliare.model;

public class Vizionare {
    private int id;
    private String dataVizionare;
    private String oraVizionare;
    private String status;
    private Client client;
    private AgentImobiliar agentImobiliar;
    private Proprietate proprietate;

    public Vizionare(int id, String dataVizionare, String oraVizionare, String status, Client client, AgentImobiliar agentImobiliar, Proprietate proprietate) {
        this.id = id;
        this.dataVizionare = dataVizionare;
        this.oraVizionare = oraVizionare;
        this.status = status;
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

    public String getDataVizionare() {
        return dataVizionare;
    }

    public void setDataVizionare(String dataVizionare) {
        this.dataVizionare = dataVizionare;
    }

    public String getOraVizionare() {
        return oraVizionare;
    }

    public void setOraVizionare(String oraVizionare) {
        this.oraVizionare = oraVizionare;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
        return "Vizionare{" +
                "id=" + id +
                ", dataVizionare='" + dataVizionare + '\'' +
                ", oraVizionare='" + oraVizionare + '\'' +
                ", status='" + status + '\'' +
                ", client=" + (client == null ? "necunoscut" : client.getNume()) +
                ", agentImobiliar=" + (agentImobiliar == null ? "necunoscut" : agentImobiliar.getNume()) +
                ", proprietate=" + (proprietate == null ? "necunoscuta" : proprietate.getTitlu()) +
                '}';
    }
}
