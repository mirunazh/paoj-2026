package model;

import java.util.Objects;

public class Client extends Persoana {
    private double buget;
    private String tipClient;

    public Client(int id, String nume, String telefon, String email, double buget, String tipClient) {
        super(id, nume, telefon, email);
        this.buget = buget;
        this.tipClient = Objects.requireNonNull(tipClient, "tipClient");
    }

    public double getBuget() {
        return buget;
    }

    public void setBuget(double buget) {
        this.buget = buget;
    }

    public String getTipClient() {
        return tipClient;
    }

    public void setTipClient(String tipClient) {
        this.tipClient = Objects.requireNonNull(tipClient, "tipClient");
    }

    @Override
    public String getRol() {
        return "client";
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", nume='" + nume + '\'' +
                ", telefon='" + telefon + '\'' +
                ", email='" + email + '\'' +
                ", buget=" + buget +
                ", tipClient='" + tipClient + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obiect) {
        if (this == obiect) {
            return true;
        }
        if (!(obiect instanceof Client client)) {
            return false;
        }
        return id == client.id && Objects.equals(email, client.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }
}
