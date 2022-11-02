package fr.victord54.cartomob.models;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import fr.victord54.cartomob.tools.FactoryID;

public class Room implements Iterable<Room>, Serializable {
    private final String id;
    private final String name;
    private final HashMap<String, Wall> walls;

    public Room(String nom, String id) {
        this.id = id;
        name = nom;
        walls = new HashMap<>(4);
    }

    public boolean isRoomEmpty() {
        return true;
    }

    public String getName() {
        return name;
    }

    public void addWall(String k, Wall w) {
        walls.put(k, w);
    }

    public Wall getWall(String key) {
        return walls.get(key);
    }

    @NonNull
    @Override
    public Iterator<Room> iterator() {
        return null;
    }

    @NonNull
    @Override
    public String toString() {
        return "Room {\n" +
                "\tid= '" + id + "',\n" +
                "\tname= '" + name + "'\n" +
                "}";
    }
}
