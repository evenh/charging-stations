package net.evenh.chargingstations.models;

/**
 * Created by evenh on 03/11/14.
 */
public class Charger {
    String name;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Charger{" +
                "name='" + name + '\'' +
                '}';
    }
}
