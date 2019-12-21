import javafx.scene.paint.Color;
import javafx.util.Pair;

/**
 * The interface responsible for interacting with the map of the world.
 * Assumes that Vector2d and MoveDirection classes are defined.
 *
 * @author apohllo
 * @author Zhylkaaa
 *
 */
public interface IWorldMap {
    /**
     * Place a animal on the map.
     *
     * @param animal
     *            The animal to place on the map.
     * @return True if the animal was placed. The animal cannot be placed if the map is already occupied.
     */
    boolean place(Animal animal);

    /**
     * Move the animal on the map according to the provided move directions. Every
     * n-th direction should be sent to the n-th animal on the map.
     *
     */
    boolean run();

    /**
     * Return an object at a given position.
     *
     * @param position
     *            The position of the object.
     * @return Object or null if the position is not occupied.
     */
     Pair<String, Color> getDrawElement(Vector2d position);
}
