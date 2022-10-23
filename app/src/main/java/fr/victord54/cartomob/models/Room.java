package fr.victord54.cartomob.models;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.Iterator;

import fr.victord54.cartomob.tools.FactoryID;

public class Room implements Iterable<Room>, Serializable {
    private final String id;
    private final String name;
//    private SuccessorHolder successorHolder;

    public Room() {
        this.id = FactoryID.getInstance().getRoomID();
        name = "Living room";
    }

    public Room(String nom) {
        this.id = FactoryID.getInstance().getRoomID();
        name = nom;
    }

    public boolean isRoomEmpty() {
        return true;
    }

    public String getName() {
        return name;
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
