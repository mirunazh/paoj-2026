package model;

public class Anunt {
    private int id;
    private String titlu;
    private String descriere;
    private String dataPublicare;
    private boolean activ;
    private Proprietate proprietate;
    private AgentImobiliar agentImobiliar;

    public Anunt(int id, String titlu, String descriere, String dataPublicare, boolean activ, Proprietate proprietate, AgentImobiliar agentImobiliar) {
        this.id = id;
        this.titlu = titlu;
        this.descriere = descriere;
        this.dataPublicare = dataPublicare;
        this.activ = activ;
        this.proprietate = proprietate;
        this.agentImobiliar = agentImobiliar;
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

    public String getDescriere() {
        return descriere;
    }

    public void setDescriere(String descriere) {
        this.descriere = descriere;
    }

    public String getDataPublicare() {
        return dataPublicare;
    }

    public void setDataPublicare(String dataPublicare) {
        this.dataPublicare = dataPublicare;
    }

    public boolean isActiv() {
        return activ;
    }

    public void setActiv(boolean activ) {
        this.activ = activ;
    }

    public Proprietate getProprietate() {
        return proprietate;
    }

    public void setProprietate(Proprietate proprietate) {
        this.proprietate = proprietate;
    }

    public AgentImobiliar getAgentImobiliar() {
        return agentImobiliar;
    }

    public void setAgentImobiliar(AgentImobiliar agentImobiliar) {
        this.agentImobiliar = agentImobiliar;
    }

    @Override
    public String toString() {
        return "Anunt{" +
                "id=" + id +
                ", titlu='" + titlu + '\'' +
                ", descriere='" + descriere + '\'' +
                ", dataPublicare='" + dataPublicare + '\'' +
                ", activ=" + activ +
                ", proprietate=" + proprietate.getTitlu() +
                ", agentImobiliar=" + agentImobiliar.getNume() +
                '}';
    }
}
