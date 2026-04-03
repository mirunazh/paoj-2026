package com.pao.laboratory06.exercise2;

import java.util.Scanner;

public class PFAColaborator extends Colaborator implements PersoanaFizica {
    private static final double SALARIU_MINIM_BRUT_ANUAL = 4050 * 12;
    private static final double CASS_FIX = 0.10 * (6 * SALARIU_MINIM_BRUT_ANUAL);
    private static final double CAS_MIDDLE = 0.10 * SALARIU_MINIM_BRUT_ANUAL;

    private double cheltuieliLunare;

    @Override
    public void citeste(Scanner in) {
        nume = in.next();
        prenume = in.next();
        venitBrutLunar = in.nextDouble();
        cheltuieliLunare = in.nextDouble();
    }

    @Override
    public double calculeazaVenitNetAnual() {
        double venitNet = (venitBrutLunar - cheltuieliLunare) * 12;
        double impozit = 0.10 * venitNet;
        double cas = 0.0;

        if (venitNet >= SALARIU_MINIM_BRUT_ANUAL && venitNet <= 2 * SALARIU_MINIM_BRUT_ANUAL) {
            cas = CAS_MIDDLE;
        }

        return venitNet - impozit - CASS_FIX - cas;
    }

    @Override
    public TipColaborator getTip() {
        return TipColaborator.PFA;
    }
}
