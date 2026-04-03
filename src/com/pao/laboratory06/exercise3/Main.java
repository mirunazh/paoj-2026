package com.pao.laboratory06.exercise3;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        Inginer[] ingineri = {
                new Inginer("Popescu", "Ana", "0711111111", 9000, 12000),
                new Inginer("Ionescu", "Vlad", "0722222222", 11000, 15000),
                new Inginer("Enache", "Mara", "0733333333", 8000, 10000)
        };

        System.out.println("Sortare naturala dupa nume:");
        Arrays.sort(ingineri);
        for (Inginer inginer : ingineri) {
            System.out.println(inginer);
        }

        System.out.println();
        System.out.println("Sortare descrescatoare dupa salariu:");
        Arrays.sort(ingineri, new ComparatorInginerSalariu());
        for (Inginer inginer : ingineri) {
            System.out.println(inginer);
        }

        System.out.println();
        System.out.println("Acces prin referinta PlataOnline:");
        PlataOnline plataOnline = ingineri[0];
        plataOnline.autentificare("ana_user", "parola123");
        System.out.println("Sold curent: " + plataOnline.consultareSold());
        System.out.println("Plata reusita: " + plataOnline.efectuarePlata(2500));
        System.out.println("Sold dupa plata: " + plataOnline.consultareSold());

        System.out.println();
        System.out.println("Acces prin referinta PlataOnlineSMS:");
        PersoanaJuridica firma = new PersoanaJuridica("Tech", "SRL", "0744444444", 50000);
        PlataOnlineSMS plataOnlineSMS = firma;
        plataOnlineSMS.autentificare("firma_user", "firma123");
        System.out.println("SMS trimis corect: " + plataOnlineSMS.trimiteSMS("Plata a fost aprobata."));
        System.out.println("SMS invalid: " + plataOnlineSMS.trimiteSMS(""));
        System.out.println("Lista SMS-uri: " + firma.getSmsTrimise());

        System.out.println();
        System.out.println("Persoana juridica fara telefon:");
        PersoanaJuridica faraTelefon = new PersoanaJuridica("NoPhone", "SRL", "", 10000);
        System.out.println("SMS trimis: " + faraTelefon.trimiteSMS("Mesaj test"));
        System.out.println("Lista SMS-uri: " + faraTelefon.getSmsTrimise());

        System.out.println();
        System.out.println("Constanta financiara:");
        System.out.println("TVA = " + ConstanteFinanciare.TVA.getValoare());

        System.out.println();
        System.out.println("Cazuri de eroare:");
        try {
            plataOnline.autentificare(null, "abc");
        } catch (IllegalArgumentException e) {
            System.out.println("Eroare autentificare: " + e.getMessage());
        }

        try {
            trimiteSMSPeEntitateFaraCapabilitate(plataOnline, "Mesaj interzis");
        } catch (UnsupportedOperationException e) {
            System.out.println("Eroare SMS: " + e.getMessage());
        }
    }

    private static void trimiteSMSPeEntitateFaraCapabilitate(PlataOnline plataOnline, String mesaj) {
        if (!(plataOnline instanceof PlataOnlineSMS plataOnlineSMS)) {
            throw new UnsupportedOperationException("Entitatea nu are capabilitate SMS.");
        }
        plataOnlineSMS.trimiteSMS(mesaj);
    }
}
