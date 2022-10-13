package fr.victord54.cartomob.models;

public class Building {
    private final String id;
    private final String name;
    private final RoomsHolder roomsHolder;

    public Building(String id, String name, RoomsHolder roomsHolder) {
        this.id = id;
        this.name = name;
        this.roomsHolder = roomsHolder;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void addRoom(Room...r) {
        roomsHolder.addRoom(r);
    }
}
