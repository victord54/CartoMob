package fr.victord54.cartomob.models;

import androidx.annotation.NonNull;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Classe principale décrivant un bâtiment qui sera cartographié
 */
public class CartoMob implements Serializable {
    /**
     * Le nom du bâtiment
     */
    private String name;

    /**
     * La liste des pièces du bâtiment
     */
    private final ArrayList<Room> rooms;

    /**
     * Constructeur par défaut de la classe
     */
    public CartoMob() {
        rooms = new ArrayList<>();
    }

    /**
     * Getter sur le nom du bâtiment
     *
     * @return Le nom du bâtiment
     */
    public String getName() {
        return name;
    }

    /**
     * Getter sur l'état de remplissage du bâtiment
     *
     * @return true si le bâtiment est vide, false sinon
     */
    public boolean isEmpty() {
        return rooms.isEmpty();
    }

    /**
     * Getter sur le nombre de pièces du bâtiment
     *
     * @return Le nombre de pièces du bâtiment
     */
    public int getSize() {
        return rooms.size();
    }

    /**
     * Getter sur une pièce
     *
     * @param i L'indice de la pièce dans la liste
     * @return La pièce situé à l'indice 'i' dans la liste
     */
    public Room getRoom(int i) {
        return rooms.get(i);
    }

    /**
     * Getter sur la liste de pièces
     *
     * @return La liste de pièces
     */
    public ArrayList<Room> getRooms() {
        return rooms;
    }

    /**
     * Getter sur l'indice d'une pièce
     *
     * @param s Le nom de la pièce dans la liste
     * @return L'indice de la pièce comportant le nom 's' dans la liste
     */
    public int getIndiceFromRoom(String s) {
        for (int i = 0; i < rooms.size(); i++) {
            if (rooms.get(i).getName().equals(s)) return i;
        }
        return -1;
    }

    /**
     * Getter sur une pièce
     *
     * @param s Le nom de la pièce dans la liste
     * @return La pièce comportant le nom 's' dans la liste
     */
    public Room getRoomFromName(String s) {
        for (Room r : rooms) {
            if (r.getName().equals(s)) return r;
        }
        return null;
    }

    /**
     * Setter sur le nom de bâtiment
     *
     * @param name Le nom souhaité
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Setter sur l'ajout d'une pièce à la liste de pièces
     *
     * @param r La nouvelle pièce à ajouter
     */
    public void addRoom(Room r) {
        rooms.add(r);
    }

    /**
     * Getter sur la mise sous forme de graphe du bâtiment
     *
     * @return Le graphe orienté du bâtiment
     */
    public Graph<Room, DefaultEdge> modelToGraph() {
        Graph<Room, DefaultEdge> graph = new SimpleDirectedGraph<>(DefaultEdge.class);

        for (Room r : getRooms()) {
            graph.addVertex(r);
        }

        for (Room r : getRooms()) {
            for (Wall w : r.getWalls().values()) {
                for (Door d : w.getDoors()) {
                    graph.addEdge(r, d.getDst());
                }
            }
        }

        return graph;
    }

    /**
     * Fonction toString
     *
     * @return L'objet sous forme de String
     */
    @NonNull
    @Override
    public String toString() {
        return "CartoMob {\n" + "\tname='" + name + "',\n" + "\trooms=" + rooms + "\n" + "}";
    }
}
