package fr.victord54.cartomob.models;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.Iterator;

import fr.victord54.cartomob.tools.FactoryID;

public class Building implements Iterable<Room>, Serializable {
    private final String id;
    private final String name;
    private final RoomsHolder roomsHolder;

    public Building() {
        this.id = FactoryID.getInstance().getBuildingID();
        this.name = "Home";
        roomsHolder = new RoomsHolder();
    }

    public Building(String name) {
        this.id = FactoryID.getInstance().getBuildingID();
        this.name = name;
        roomsHolder = new RoomsHolder();
    }

    public boolean isBuildingEmpty() {
        return roomsHolder.isEmpty();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Room getRoom(int i) {
        return roomsHolder.getRoom(i);
    }

    public void addRoom(Room...r) {
        roomsHolder.addRoom(r);
    }

    @NonNull
    @Override
    public Iterator<Room> iterator() {
        return roomsHolder.iterator();
    }

    @NonNull
    @Override
    public String toString() {
        return "Building{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
