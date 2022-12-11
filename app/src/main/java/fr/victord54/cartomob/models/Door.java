package fr.victord54.cartomob.models;

import android.graphics.Rect;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Classe décrivant une porte située sur un mur (Wall) d'une pièce (Room) du bâtiment (CartoMob)
 */
public class Door implements Serializable {
    /**
     * Liste décrivant le rectangle de la porte
     */
    private final HashMap<String, Integer> rectangle;

    /**
     * La pièce source de la porte
     */
    private final Room src;

    /**
     * La pièce de destination de la porte
     */
    private Room dst = null;

    /**
     * Constructeur pour la porte
     *
     * @param rectangle Le rectangle représentant la porte
     * @param room      La pièce source de la porte
     */
    public Door(Rect rectangle, Room room) {
        this.rectangle = new HashMap<>();
        this.rectangle.put("top", rectangle.top);
        this.rectangle.put("bottom", rectangle.bottom);
        this.rectangle.put("left", rectangle.left);
        this.rectangle.put("right", rectangle.right);
        this.src = room;
    }

    /**
     * Getter sur le rectangle représentant la porte
     *
     * @return Le rectangle
     */
    public Rect getRectangle() {
        return new Rect(rectangle.get("left"), rectangle.get("top"), rectangle.get("right"), rectangle.get("bottom"));
    }

    /**
     * Getter sur la pièce de destination de la porte
     *
     * @return La pièce de destination
     */
    public Room getDst() {
        return dst;
    }

    /**
     * Setter sur la porte de destination de la porte
     *
     * @param r La pièce de destination
     */
    public void setDst(Room r) {
        dst = r;
    }

    /**
     * Fonction toString
     *
     * @return L'objet sous forme de String
     */
    @NonNull
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Door{" + "rectangle=" + rectangle + ", room=" + src.getName());
        if (dst == null) sb.append("}");
        else sb.append(", dst=").append(dst.getName()).append("}");
        return sb.toString();
    }
}
