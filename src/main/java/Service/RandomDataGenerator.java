package Service;

import Handlers.Deserialize;

import java.io.*;
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

    Deserialize cereal = new Deserialize();



    public String getFemaleName() throws FileNotFoundException {
        File femaleNamesFile = new File("json/fnames.json");
        InputStream readNames = new FileInputStream(femaleNamesFile);
        StringBuilder femaleNamesString = null;
        try {
            femaleNamesString = new StringBuilder(readString(readNames));
        } catch (IOException e) {
            e.printStackTrace();
        }

        String femaleData = femaleNamesString.substring(femaleNamesString.indexOf("["),femaleNamesString.indexOf("]")+1);
        //Female data is an array of names, looks like : [\n    "Jolynn",\n "Maryland"
        String[] femaleN = cereal.deserialize(femaleData, String[].class);
        //get a random number within the array.
        return femaleN[rand.nextInt(femaleN.length + 1)];
    }

    private String readString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while ((len = sr.read(buf)) > 0) {
            sb.append(buf, 0, len);
        }
        return sb.toString();
    }
}
