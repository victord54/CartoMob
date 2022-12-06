package fr.victord54.cartomob.models;

import android.graphics.Rect;

import junit.framework.TestCase;

import org.jgrapht.Graph;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultEdge;

public class CartoMobTest extends TestCase {
    CartoMob cartoMob;
    public void setUp() {
        cartoMob = new CartoMob();
        cartoMob.setName("Maison");
        cartoMob.addRoom(new Room("Salon", "salon"));
        cartoMob.addRoom(new Room("Cuisine", "cuisine"));
        cartoMob.addRoom(new Room("Chambre", "chambre"));
        cartoMob.addRoom(new Room("SDB", "sdb"));
        cartoMob.addRoom(new Room("Couloir", "couloir"));
        cartoMob.getRoom(0).addWall("N", new Wall("N", ""));
        cartoMob.getRoom(0).addWall("S", new Wall("S", ""));
        cartoMob.getRoom(0).addWall("E", new Wall("E", ""));
        cartoMob.getRoom(0).addWall("O", new Wall("O", ""));

        cartoMob.getRoom(1).addWall("N", new Wall("N", ""));
        cartoMob.getRoom(1).addWall("S", new Wall("S", ""));
        cartoMob.getRoom(1).addWall("E", new Wall("E", ""));
        cartoMob.getRoom(1).addWall("O", new Wall("O", ""));

        cartoMob.getRoom(2).addWall("N", new Wall("N", ""));
        cartoMob.getRoom(2).addWall("S", new Wall("S", ""));
        cartoMob.getRoom(2).addWall("E", new Wall("E", ""));
        cartoMob.getRoom(2).addWall("O", new Wall("O", ""));

        cartoMob.getRoom(3).addWall("N", new Wall("N", ""));
        cartoMob.getRoom(3).addWall("S", new Wall("S", ""));
        cartoMob.getRoom(3).addWall("E", new Wall("E", ""));
        cartoMob.getRoom(3).addWall("O", new Wall("O", ""));

        cartoMob.getRoom(4).addWall("N", new Wall("N", ""));
        cartoMob.getRoom(4).addWall("S", new Wall("S", ""));
        cartoMob.getRoom(4).addWall("E", new Wall("E", ""));
        cartoMob.getRoom(4).addWall("O", new Wall("O", ""));

        cartoMob.getRoom(0).getWall("N").addDoor(new Door(new Rect(), cartoMob.getRoom(0)));
        cartoMob.getRoom(0).getWall("N").getDoor(0).setDst(cartoMob.getRoom(2));

        cartoMob.getRoom(2).getWall("N").addDoor(new Door(new Rect(), cartoMob.getRoom(2)));
        cartoMob.getRoom(2).getWall("N").getDoor(0).setDst(cartoMob.getRoom(3));


        /*
        |---------|-----------------|
        |         |                 |
        |   SDB   |    Couloir      |
        |---------|       |---------|
        |         |       |         |
        | Chambre |       | Cuisine |
        |         |       |         |
        |---------|-------|---------|
        |         Salon             |
        |---------------------------|
         */
    }

    public void testModelToGraph() {
        System.out.println(cartoMob.modelToGraph().toString());
        Graph <Room, DefaultEdge> graph = cartoMob.modelToGraph();
        DijkstraShortestPath<Room, DefaultEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);

        System.out.println(dijkstraShortestPath.getPath(cartoMob.getRoom(0), cartoMob.getRoom(3)).getEdgeList().toString());
    }
}