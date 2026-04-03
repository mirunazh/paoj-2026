package com.pao.laboratory06.exercise2;

import java.util.Locale;

public abstract class Colaborator implements IOperatiiCitireScriere {
    protected String nume;
    protected String prenume;
    protected double venitBrutLunar;

    public abstract double calculeazaVenitNetAnual();

    public abstract TipColaborator getTip();

    public String formatAfisare() {
        return String.format(Locale.US, "%s: %s %s, venit net anual: %.2f lei",
                tipContract(), nume, prenume, calculeazaVenitNetAnual());
    }

    @Override
    public void afiseaza() {
        System.out.println(formatAfisare());
    }

    @Override
    public String tipContract() {
        return getTip().name();
    }
}
