package model;

import java.util.Arrays;
import java.util.Objects;

public class ListaFavorite {
    private int id;
    private Client client;
    private Proprietate[] proprietati;

    public ListaFavorite(int id, Client client, Proprietate[] proprietati) {
        this.id = id;
        this.client = client;
        this.proprietati = proprietati;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = Objects.requireNonNull(client, "client");
    }

    public Proprietate[] getProprietati() {
        return proprietati;
    }

    public void setProprietati(Proprietate[] proprietati) {
        this.proprietati = proprietati == null ? new Proprietate[0] : proprietati;
    }

    @Override
    public String toString() {
        return "ListaFavorite{" +
                "id=" + id +
                ", client=" + (client == null ? "necunoscut" : client.getNume()) +
                ", proprietati=" + Arrays.toString(proprietati) +
                '}';
    }
}
