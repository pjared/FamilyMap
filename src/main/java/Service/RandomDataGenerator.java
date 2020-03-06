package Service;

import java.util.Random;

public class RandomDataGenerator {
    private int year;
    private Random rand = new Random();

    RandomDataGenerator() {
        //this.year = rand.nextInt(1021) + 1000;
        year = 1000;
    }

    public void subtractYears(int years) {
        this.year -= years;
    }

    public void addYears(int years) {
        this.year += years;
    }

    public int getYear() {
        return this.year;
    }
}
