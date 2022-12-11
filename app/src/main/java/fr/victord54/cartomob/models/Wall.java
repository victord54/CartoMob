package fr.victord54.cartomob.models;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Classe représentant un mur d'une pièce (Room) du batiment (CartoMob)
 */
public class Wall implements Serializable, Iterable<Door> {

    /**
     * Orientation du mur (N/S/E/W)
     */
    private final String orientation;

    /**
     * Nom de la photo pour représenter le mur
     */
    private final String nameOfPhoto;
    /**
     * Liste des portes composant le mur
     */
    private final ArrayList<Door> doors;

    /**
     * Constructeur pour le mur
     *
     * @param o    Orientation du mur
     * @param name Nom de la photo
     */
    public Wall(String o, String name) {
        orientation = o;
        nameOfPhoto = name;
        doors = new ArrayList<>();
    }

    /**
     * Getter sur l'orientation du mur
     *
     * @return L'orientation du mur
     */
    public String getOrientation() {
        return orientation;
    }

    /**
     * Getter sur le nom de la photo
     *
     * @return Le nom de la photo
     */
    public String getNameOfPhoto() {
        return nameOfPhoto;
    }

    /**
     * Getter sur une porte du mur
     *
     * @param i L'indice de la porte dans la liste
     * @return La porte à l'indice 'i' de la liste
     */
    public Door getDoor(int i) {
        return doors.get(i);
    }

    /**
     * Getter sur le nombre de portes sur le mur
     *
     * @return Le nombre de portes du mur
     */
    public int nbDoors() {
        return doors.size();
    }

    /**
     * Getter sur la liste de portes du mur
     *
     * @return La liste de portes
     */
    public ArrayList<Door> getDoors() {
        return doors;
    }

    /**
     * Setter sur l'ajout d'une porte au mur
     *
     * @param d La porte à ajouter au mur
     */
    public void addDoor(Door d) {
        doors.add(d);
    }

    /**
     * Fonction toString
     *
     * @return L'objet sous forme de String
     */
    @NonNull
    @Override
    public String toString() {
        return "Wall {\n" + "\t\t\torientation='" + orientation + "',\n" + "\t\t\tnameOfPhoto='" + nameOfPhoto + "',\n" + "\t\t\tdoors=" + doors + "\n" + "\t\t}";
    }

    /**
     * Itérator sur les portes du mur
     *
     * @return L'itérator
     */
    @NonNull
    @Override
    public Iterator<Door> iterator() {
        return doors.iterator();
    }
}
