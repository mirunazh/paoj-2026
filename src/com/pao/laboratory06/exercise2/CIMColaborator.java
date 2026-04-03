package com.pao.laboratory06.exercise2;

import java.util.Scanner;

public class CIMColaborator extends Colaborator implements PersoanaFizica {
    private boolean bonus;

    @Override
    public void citeste(Scanner in) {
        nume = in.next();
        prenume = in.next();
        venitBrutLunar = in.nextDouble();
        bonus = in.hasNext() && "DA".equals(in.next());
    }

    @Override
    public boolean areBonus() {
        return bonus;
    }

    @Override
    public double calculeazaVenitNetAnual() {
        double venitNetAnual = venitBrutLunar * 12 * 0.55;
        if (areBonus()) {
            venitNetAnual *= 1.1;
        }
        return venitNetAnual;
    }

    @Override
    public TipColaborator getTip() {
        return TipColaborator.CIM;
    }
}
