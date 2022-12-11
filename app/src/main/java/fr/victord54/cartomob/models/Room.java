package fr.victord54.cartomob.models;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Classe décrivant une pièce du bâtiment (CartoMob)
 */
public class Room implements Iterable<Wall>, Serializable {

    /**
     * Identifiant de la pièce pour la sauvegarde
     */
    private final String id;

    /**
     * Nom de la pièce
     */
    private String name;

    /**
     * Liste des murs de la pièce
     */
    private final HashMap<String, Wall> walls;

    /**
     * Etat de la pièce
     */
    private boolean isComplete;

    /**
     * Constructeur pour la pièce
     *
     * @param nom Nom donné à la pièce
     * @param id  Identifiant de la pièce
     */
    public Room(String nom, String id) {
        this.id = id;
        name = nom;
        walls = new HashMap<>(4);
        isComplete = false;
    }

    /**
     * Getter sur l'état de complétude de la pièce
     *
     * @return true si la pièce a 4 murs avec photos, false sinon
     */
    public boolean isComplete() {
        return isComplete;
    }

    /**
     * Getter sur le nom de la pièce
     *
     * @return Le nom de la pièce
     */
    public String getName() {
        return name;
    }

    /**
     * Setter sur l'ajout d'un mur à la pièce
     *
     * @param k Points cardinaux (N/S/E/W) pour repérer le mur
     * @param w Le mur à ajouter à la pièce
     */
    public void addWall(String k, Wall w) {
        walls.put(k, w);
        if (walls.size() == 4) isComplete = true;
    }

    /**
     * Getter sur un mur de la pièce
     *
     * @param key Points cardinaux (N/S/E/W) pour repérer le mur
     * @return Le mur souhaité
     */
    public Wall getWall(String key) {
        return walls.get(key);
    }

    /**
     * Getter sur la liste de murs de la pièce
     *
     * @return La liste de murs de la pièce
     */
    public HashMap<String, Wall> getWalls() {
        return walls;
    }

    /**
     * Getter sur l'existence d'un mur
     *
     * @param k Points cardinaux (N/S/E/W) pour repérer le mur
     * @return true si le mur existe, false sinon
     */
    public boolean isWallExist(String k) {
        return walls.containsKey(k);
    }

    /**
     * Getter sur le nombre de murs de la pièce
     *
     * @return Le nombre de murs de la pièce
     */
    public int getNbWalls() {
        return walls.size();
    }

    /**
     * Setter sur le nom de la pièce
     *
     * @param s Le nouveau nom de la pièce
     */
    public void setName(String s) {
        name = s;
    }

    /**
     * Itérator sur les murs de la pièce
     *
     * @return L'itérator
     */
    @NonNull
    @Override
    public Iterator<Wall> iterator() {
        return walls.values().iterator();
    }

    /**
     * Fonction toString
     *
     * @return L'objet sous forme de String
     */
    @NonNull
    @Override
    public String toString() {
        return id;
    }
}
