package fr.victord54.cartomob.models;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;

public class Room implements Iterable<Wall>, Serializable {
    private final String id;
    private String name;
    private final HashMap<String, Wall> walls;
    private boolean isComplete;

    public Room(String nom, String id) {
        this.id = id;
        name = nom;
        walls = new HashMap<>(4);
        isComplete = false;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public String getName() {
        return name;
    }

    public void addWall(String k, Wall w) {
        walls.put(k, w);
        if (walls.size() == 4)
            isComplete = true;
    }

    public Wall getWall(String key) {
        return walls.get(key);
    }

    public HashMap<String, Wall> getWalls() {
        return walls;
    }

    public boolean isWallExist(String k) {
        return walls.containsKey(k);
    }

    public int getNbWalls() {
        return walls.size();
    }

    public void setName(String s) {
        name = s;
    }

    @NonNull
    @Override
    public Iterator<Wall> iterator() {
        return walls.values().iterator();
    }

    @NonNull
    @Override
    public String toString() {
        return "Room {\n" +
                "\t\tid='" + id + "',\n" +
                "\t\tname='" + name + "',\n" +
                "\t\twalls=" + walls + "\n" +
                "\t}";
    }
}
