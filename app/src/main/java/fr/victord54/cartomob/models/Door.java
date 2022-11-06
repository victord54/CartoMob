package fr.victord54.cartomob.models;

import android.graphics.Rect;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.HashMap;

public class Door implements Serializable {
    //private final Rect rectangle;
    private final HashMap<String, Integer> rectangle;
    private final Room src;
    private Room dst = null;

    public Door(Rect rectangle, Room room) {
        this.rectangle = new HashMap<>();
        this.rectangle.put("top", rectangle.top);
        this.rectangle.put("bottom", rectangle.bottom);
        this.rectangle.put("left", rectangle.left);
        this.rectangle.put("right", rectangle.right);
        this.src = room;
    }

    public Rect getRectangle() {
        return new Rect(rectangle.get("left"), rectangle.get("top"), rectangle.get("right"), rectangle.get("bottom"));
    }

    public Room getDst() {
        return dst;
    }

    public void setDst(Room r) {
        dst = r;
    }

    @NonNull
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Door{" + "rectangle=" + rectangle + ", room=" + src.getName());
        if (dst == null)
            sb.append("}");
        else
            sb.append(", dst=").append(dst.getName()).append("}");
        return sb.toString();
    }
}
