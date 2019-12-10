import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractWorldMap implements IWorldMap, IPositionChangeObserver {
    protected Map<Vector2d, IMapElement> elements = new HashMap<>();
    protected MapBoundary boundary = new MapBoundary();
    private MapVisualizer visualizer = new MapVisualizer(this);

    @Override
    public boolean isOccupied(Vector2d position) {
        return objectAt(position) != null;
    }

    @Override
    public boolean run() {
        List<Animal> animals = new ArrayList<>();

        for(IMapElement element : elements.values()){
            if(element instanceof Animal){
                Animal animal = (Animal) element;
                if(animal.isAlive()) {
                    animals.add(animal);
                }
            }
        }

        if(animals.size() == 0){
            return false;
        }

        for(Animal animal : animals){
            if(animal.isAlive()) {
                animal.move();
            }/* else {
                elements.remove(animal.getPosition());
            }*/
        }

        return true;
    }

    @Override
    public boolean place(Animal animal) {
        if(canMoveTo(animal.getPosition())){
            elements.put(animal.getPosition(), animal);
            animal.addObserver(this);
            boundary.place(animal);

            return true;
        }

        throw new IllegalArgumentException(animal.getPosition() + " position is occupied");
    }

    @Override
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition) {
        Animal animal = (Animal) elements.remove(oldPosition);
        elements.put(newPosition, animal);
    }

    @Override
    public Object objectAt(Vector2d position) {
        return elements.get(position);
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return !isOccupied(position);
    }

    @Override
    public String toString() {
        return visualizer.draw(boundary.getLowerLeft(), boundary.getUpperRight());
    }
}
