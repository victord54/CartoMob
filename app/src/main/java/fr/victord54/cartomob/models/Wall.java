package fr.victord54.cartomob.models;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

public class Wall implements Serializable, Iterable<Door> {
    private final String orientation;
    private final String nameOfPhoto;
    private final ArrayList<Door> doors;

    public Wall(String o, String name) {
        orientation = o;
        nameOfPhoto = name;
        doors = new ArrayList<>();
    }

    public String getOrientation() {
        return orientation;
    }

    public String getNameOfPhoto() {
        return nameOfPhoto;
    }

    public Door getDoor(int i) {
        return doors.get(i);
    }

    public int nbDoors() {
        return doors.size();
    }

    public ArrayList<Door> getDoors() {
        return doors;
    }

    public void addDoor(Door d) {
        doors.add(d);
    }

    @NonNull
    @Override
    public String toString() {
        return "Wall {\n" +
                "\t\t\torientation='" + orientation + "',\n" +
                "\t\t\tnameOfPhoto='" + nameOfPhoto + "',\n" +
                "\t\t\tdoors=" + doors + "\n" +
                "\t\t}";
    }

    @NonNull
    @Override
    public Iterator<Door> iterator() {
        return doors.iterator();
    }
}
