package fr.victord54.cartomob.tools;

public class FactoryID {
    private static FactoryID INSTANCE = null;

    private int buildingID;
    private int roomID;
    private int doorID;
    private int wallID;

    private FactoryID() {}

    public static FactoryID getInstance() {
        if (INSTANCE == null)
            INSTANCE = new FactoryID();
        return INSTANCE;
    }

    public String getBuildingID() {
        buildingID++;
        return "building"+buildingID;
    }

    public String getRoomID() {
        roomID++;
        return "room"+roomID;
    }

    public String getDoorID() {
        doorID++;
        return "door"+doorID;
    }

    public String getWallID() {
        wallID++;
        return "wall"+wallID;
    }
}
