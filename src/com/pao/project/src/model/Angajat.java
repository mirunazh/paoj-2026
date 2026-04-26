package model;

import java.util.Objects;

public abstract class Angajat extends Persoana {
    protected AgentieImobiliara agentieImobiliara;

    protected Angajat(int id, String nume, String telefon, String email, AgentieImobiliara agentieImobiliara) {
        super(id, nume, telefon, email);
        this.agentieImobiliara = Objects.requireNonNull(agentieImobiliara, "agentieImobiliara");
    }

    public AgentieImobiliara getAgentieImobiliara() {
        return agentieImobiliara;
    }

    public void setAgentieImobiliara(AgentieImobiliara agentieImobiliara) {
        this.agentieImobiliara = Objects.requireNonNull(agentieImobiliara, "agentieImobiliara");
    }
}
