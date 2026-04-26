package model;

import java.util.Objects;

public class AgentImobiliar extends Angajat {
    private String specializare;
    private int aniExperienta;

    public AgentImobiliar(int id, String nume, String telefon, String email, String specializare, int aniExperienta, AgentieImobiliara agentieImobiliara) {
        super(id, nume, telefon, email, agentieImobiliara);
        this.specializare = Objects.requireNonNull(specializare, "specializare");
        this.aniExperienta = aniExperienta;
    }

    public String getSpecializare() {
        return specializare;
    }

    public void setSpecializare(String specializare) {
        this.specializare = Objects.requireNonNull(specializare, "specializare");
    }

    public int getAniExperienta() {
        return aniExperienta;
    }

    public void setAniExperienta(int aniExperienta) {
        this.aniExperienta = aniExperienta;
    }

    @Override
    public String getRol() {
        return "agent imobiliar";
    }

    @Override
    public String toString() {
        return "AgentImobiliar{" +
                "id=" + id +
                ", nume='" + nume + '\'' +
                ", telefon='" + telefon + '\'' +
                ", email='" + email + '\'' +
                ", specializare='" + specializare + '\'' +
                ", aniExperienta=" + aniExperienta +
                ", agentieImobiliara=" + (agentieImobiliara == null ? "necunoscuta" : agentieImobiliara.getNume()) +
                ", rol='" + getRol() + '\'' +
                '}';
    }
}
