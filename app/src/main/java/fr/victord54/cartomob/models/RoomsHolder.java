package fr.victord54.cartomob.models;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class RoomsHolder implements Iterable<Room> {
    private final ArrayList<Room> rooms;

    public RoomsHolder() {
        rooms = new ArrayList<>();
    }

    public int getNbRooms() {
        return rooms.size();
    }

    public Room getRoom(int i) {
        return rooms.get(i);
    }

    public boolean contains(Room r) {
        return rooms.contains(r);
    }

    public void addRoom(Room... r) {
        rooms.addAll(Arrays.asList(r));
    }

    @NonNull
    @Override
    public Iterator<Room> iterator() {
        return rooms.iterator();
    }

    @NonNull
    @Override
    public String toString() {
        return "RoomsHolder{" + "rooms=" + rooms + '}';
    }
}
