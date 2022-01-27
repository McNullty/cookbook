package hr.vgsoft.cookbook.wiki;

import java.util.ArrayList;

interface Trkac {
    void trci();
}

interface Suljaci {
    void suljajSe();
}

interface Zavijaci {
    void zavijaj();
}

class Macka implements Trkac, Suljaci {

    @Override
    public void trci() {
        System.out.println("Mačka trči");
    }

    public void mjau() {
        System.out.println("mjau");
    }

    @Override
    public void suljajSe() {
        System.out.println("šuljanje");
    }
}

class Pas implements Trkac, Zavijaci {
    @Override
    public void trci() {
        System.out.println("Pas trči");
    }

    public void vau() {
        System.out.println("vau");
    }

    @Override
    public void zavijaj() {
        System.out.println("VUUUUUU");
    }
}

class Vuk implements Zavijaci {

    @Override
    public void zavijaj() {
        System.out.println("WWWWWWU");
    }
}

class Lav implements Suljaci {

    @Override
    public void suljajSe() {
        System.out.println(".........");
    }
}

public class InterfaceExample {

    public static void main(String[] args) {

        final Macka macka = new Macka();
        final Pas pas = new Pas();
        final Vuk vuk = new Vuk();
        final Lav lav = new Lav();

        final ArrayList<Trkac> trkaci = new ArrayList<>();
        trkaci.add(macka);
        trkaci.add(pas);

        trkaci.forEach(Trkac::trci);

        final ArrayList<Suljaci> suljaci = new ArrayList<>();
        suljaci.add(macka);
        suljaci.add(lav);

        final ArrayList<Zavijaci> zavijaci = new ArrayList<>();
        zavijaci.add(vuk);
        zavijaci.add(pas);

    }
}
