package Service;

import Handlers.Deserialize;
import Model.City;

import java.io.*;
import java.util.Random;

public class RandomDataGenerator {
    private int year;
    private Random rand = new Random();

    public RandomDataGenerator() {
        this.year = rand.nextInt(1021) + 1000;
        //year = 1000;
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
        return femaleN[rand.nextInt(femaleN.length)];
    }

    public String getMaleName() throws FileNotFoundException {
        File femaleNamesFile = new File("json/mnames.json");
        InputStream readNames = new FileInputStream(femaleNamesFile);
        StringBuilder maleNamesString = null;
        try {
            maleNamesString = new StringBuilder(readString(readNames));
        } catch (IOException e) {
            e.printStackTrace();
        }

        String maleData = maleNamesString.substring(maleNamesString.indexOf("["),maleNamesString.indexOf("]")+1);
        String[] maleN = cereal.deserialize(maleData, String[].class);
        //get a random number within the array.
        return maleN[rand.nextInt(maleN.length)];
    }

    public String getSurName() throws FileNotFoundException {
        File surNamesFile = new File("json/snames.json");
        InputStream readNames = new FileInputStream(surNamesFile);
        StringBuilder surNamesString = null;
        try {
            surNamesString = new StringBuilder(readString(readNames));
        } catch (IOException e) {
            e.printStackTrace();
        }

        String surData = surNamesString.substring(surNamesString.indexOf("["),surNamesString.indexOf("]")+1);
        String[] surN = cereal.deserialize(surData, String[].class);
        //get a random number within the array.
        return surN[rand.nextInt(surN.length)];
    }

    public City getCity() throws FileNotFoundException {
        File surNamesFile = new File("json/locations.json");
        InputStream readNames = new FileInputStream(surNamesFile);
        StringBuilder locationsString = null;
        try {
            locationsString = new StringBuilder(readString(readNames));
        } catch (IOException e) {
            e.printStackTrace();
        }

        String locData = locationsString.substring(locationsString.indexOf("["),locationsString.indexOf("]")+1);
        //how to grab the entire city? make an object
        //String[] locs = cereal.deserialize(locData, String[].class);
        City[] cities = cereal.deserialize(locData, City[].class);
        //get a random number within the array.
        return cities[rand.nextInt(cities.length)];
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
