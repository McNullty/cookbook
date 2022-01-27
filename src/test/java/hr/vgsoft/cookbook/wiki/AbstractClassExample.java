package hr.vgsoft.cookbook.wiki;

abstract class BaseClass {
    public void algorithm() {
        stepOne();
        stepTwo();
    }

    protected abstract void stepTwo();

    protected abstract void stepOne();
}

class KlasaA extends BaseClass {

    @Override
    protected void stepTwo() {
        System.out.println("Korak lijevo");
    }

    @Override
    protected void stepOne() {
        System.out.println("Korak naprijed");
    }
}

final class KlasaB extends BaseClass {

    @Override
    protected void stepTwo() {
        System.out.println("Korak desno");
    }

    @Override
    protected void stepOne() {
        System.out.println("Korak natrag");
    }
}

public class AbstractClassExample {

    public static void main(String[] args) {
        final BaseClass klasaA = new KlasaA();
        final BaseClass klasaB = new KlasaB();

        klasaA.algorithm();
        klasaB.algorithm();
    }
}
