package fr.victord54.cartomob.models;

import androidx.annotation.NonNull;

import java.util.Iterator;

import fr.victord54.cartomob.tools.FactoryID;

public class Room implements Iterable<Room> {
    private final String id;
    private final String name;
//    private SuccessorHolder successorHolder;

    public Room() {
        this.id = FactoryID.getInstance().getRoomID();
        name = "home";
    }

    public Room(String nom) {
        this.id = FactoryID.getInstance().getRoomID();
        name = nom;
    }



    @NonNull
    @Override
    public Iterator<Room> iterator() {
        return null;
    }
}
