import java.util.*;

public class Animal implements IMapElement {
    private static final int MAX_ENERGY = 5;

    private static Random energy_sampler = new Random();
    private List<IPositionChangeObserver> observers = new LinkedList<>();
    private Vector2d position;
    private IWorldMap map;
    private Genome genome;
    private int energy;

    public Genome getGenome() {
        return genome;
    }

    @Override
    public Vector2d getPosition() {
        return position;
    }

    public Animal(IWorldMap map, Vector2d initialPosition){
        energy = 3; //Animal.energy_sampler.nextInt(MAX_ENERGY + 1);
        this.map = map;
        position = initialPosition; //TODO: Check if position is valid
        this.map.place(this);
        this.genome = new Genome(46);
    }

    @Override
    public String toString() {
       return "A";
    }

    public void addObserver(IPositionChangeObserver observer){
        observers.add(observer);
    }

    public void removeObserver(IPositionChangeObserver observer){
        observers.remove(observer);
    }

    public boolean isAlive(){
        return energy > 0;
    }

    public void move(){
        if(!isAlive()){
            return;
        }

        MapDirection direction = genome.chooseMove();

        Vector2d nextPosition = position.add(direction.toUnitVector());

        while(!map.canMoveTo(nextPosition)) {
            direction = genome.chooseMove();

            nextPosition = position.add(direction.toUnitVector());
        }

        Vector2d oldPosition = this.position;
        this.position = nextPosition;

        positionChanged(oldPosition, nextPosition);
        energy--;
    }

    private void positionChanged(Vector2d oldPosition, Vector2d newPosition){
        for(IPositionChangeObserver observer : observers){
            observer.positionChanged(oldPosition, newPosition);
        }
    }
}
