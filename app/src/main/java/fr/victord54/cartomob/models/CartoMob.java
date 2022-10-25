package fr.victord54.cartomob.models;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;

import fr.victord54.cartomob.tools.FactoryID;

public class CartoMob implements Serializable {
    private String id;
    private String name;
    private final ArrayList<Room> rooms;
    private static final FactoryID factory = FactoryID.getInstance();

    public CartoMob() {
        id = FactoryID.getInstance().getBuildingID();
        rooms = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public boolean isEmpty() {
        return rooms.isEmpty();
    }

    public int getSize() {
        return rooms.size();
    }

    public Room getRoom(int i) {
        return rooms.get(i);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addRoom(Room r) {
        rooms.add(r);
    }

    @Override
    public String toString() {
        return "CartoMob{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", rooms=" + rooms +
                '}';
    }
}
