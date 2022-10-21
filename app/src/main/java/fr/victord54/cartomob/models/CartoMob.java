package fr.victord54.cartomob.models;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;

public class CartoMob implements Serializable {
    private final ArrayList<Building> buildings;
    public CartoMob() {
        buildings = new ArrayList<>();
    }

    public void setBuilding(Building building) {
        buildings.add(building);
    }

    public boolean isEmpty() {
        return buildings.isEmpty();
    }

    public Building getBuilding(int i) {
        return buildings.get(i);
    }

    @NonNull
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Building b: buildings) {
            sb.append(b.toString()).append("\n");
        }
        return sb.toString();
    }
}
