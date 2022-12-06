package fr.victord54.cartomob.models;

import androidx.annotation.NonNull;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;

import java.io.Serializable;
import java.util.ArrayList;

public class CartoMob implements Serializable {
    private String name;
    private final ArrayList<Room> rooms;

    public CartoMob() {
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

    public ArrayList<Room> getRooms() {
        return rooms;
    }

    public int getIndiceFromRoom(String s) {
        for (int i = 0; i < rooms.size(); i++) {
            if (rooms.get(i).getName().equals(s))
                return i;
        }
        return -1;
    }

    public Room getRoomFromName(String s) {
        for (Room r: rooms) {
            if (r.getName().equals(s))
                return r;
        }
        return null;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addRoom(Room r) {
        rooms.add(r);
    }

    public Graph<Room, DefaultEdge> modelToGraph() {
        Graph<Room, DefaultEdge> graph = new SimpleDirectedGraph<>(DefaultEdge.class);

        for (Room r: getRooms()) {
            graph.addVertex(r);
        }

        for (Room r: getRooms()) {
            for (Wall w: r.getWalls().values()) {
                for (Door d: w.getDoors()) {
                    graph.addEdge(r, d.getDst());
                }
            }
        }

        return graph;
    }

    @NonNull
    @Override
    public String toString() {
        return "CartoMob {\n" +
                "\tname='" + name + "',\n" +
                "\trooms=" + rooms + "\n" +
                "}";
    }
}
