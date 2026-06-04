package com.pao.proiect.imobiliare.model;

import java.util.Objects;

public final class CodProprietate {
    private final String valoare;

    public CodProprietate(int id, String oras) {
        String orasNormalizat = Objects.requireNonNull(oras, "oras").trim().toUpperCase();
        if (orasNormalizat.isEmpty()) {
            throw new IllegalArgumentException("Orasul nu poate fi gol.");
        }
        this.valoare = orasNormalizat.substring(0, Math.min(3, orasNormalizat.length())) + "-" + id;
    }

    public String getValoare() {
        return valoare;
    }

    @Override
    public String toString() {
        return valoare;
    }

    @Override
    public boolean equals(Object obiect) {
        if (this == obiect) {
            return true;
        }
        if (!(obiect instanceof CodProprietate cod)) {
            return false;
        }
        return Objects.equals(valoare, cod.valoare);
    }

    @Override
    public int hashCode() {
        return Objects.hash(valoare);
    }
}
