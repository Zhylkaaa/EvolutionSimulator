import java.util.*;

public class Animal implements IMapElement {
    public static int getMaxEnergy() {
        return MAX_ENERGY;
    }

    public static int MAX_ENERGY = 10;
    public static int MIN_ENERGY_TO_POPULATE = 5;

    private static Random sampler = new Random();
    private List<IPositionChangeObserver> observers = new LinkedList<>();
    private Vector2d position;
    private AbstractWorldMap map;
    private Genome genome;
    private int energy;

    public static Animal populate(Animal parent1, Animal parent2) {
        if(parent1.getEnergy() < MIN_ENERGY_TO_POPULATE || parent2.getEnergy() < MIN_ENERGY_TO_POPULATE) return null;

        return new Animal(parent1, parent2);
    }

    public int getEnergy() {
        return energy;
    }

    private MapDirection direction;

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        int hash = 0;

        for(int gen : genome.getGenes()){
            hash = gen + 31*hash;
        }

        return genome.hashCode();
    }

    public Genome getGenome() {
        return genome;
    }

    @Override
    public Vector2d getPosition() {
        return position;
    }


    public Animal(AbstractWorldMap map){
        energy = Animal.sampler.nextInt(MAX_ENERGY - MIN_ENERGY_TO_POPULATE) + MIN_ENERGY_TO_POPULATE + 1;
        this.map = map;
        this.genome = new Genome();
        this.direction = MapDirection.NORTH;

        this.position = new Vector2d(Animal.sampler.nextInt(map.getWidth()), Animal.sampler.nextInt(map.getHeight()));
        this.map.place(this);
    }

    public void setEnergy(int energy) {
        if(energy > MAX_ENERGY){
            this.energy = MAX_ENERGY;
        } else {
            this.energy = energy;
        }
    }

    public AbstractWorldMap getMap() {
        return map;
    }

    public Animal(Animal p1, Animal p2){
        energy = (p1.getEnergy() + 3) / 4 + p2.getEnergy() / 4; //Animal.energy_sampler.nextInt(MAX_ENERGY + 1);

        p1.setEnergy(p1.getEnergy() - ((p1.getEnergy() + 3) / 4));
        p2.setEnergy(p2.getEnergy() - (p2.getEnergy() / 4));

        this.map = p1.getMap();
        this.genome = new Genome(p1, p2);
        this.direction = MapDirection.NORTH;

        this.position = new Vector2d(p1.getPosition().x + Animal.sampler.nextInt(3) - 1,
                p1.getPosition().y + Animal.sampler.nextInt(3) - 1);
    }

    @Override
    public String toString() {
       return this.direction.toString();
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

    public Vector2d calculatePosition(){
        int x = this.position.x;
        int y = this.position.y;
        Vector2d directionVector = this.direction.toUnitVector();

        x += directionVector.x % this.map.getWidth();
        y += directionVector.y % this.map.getHeight();

        if(x < 0) {
            x = this.map.getWidth() + x;
        }

        if(y < 0) {
            y = this.map.getHeight() + y;
        }

        return new Vector2d(x, y);
    }

    public void move(){
        if(!isAlive()){
            return;
        }

        this.direction = this.direction.turn(genome.chooseTurn());

        Vector2d nextPosition = calculatePosition();

        Vector2d oldPosition = this.position;
        this.position = nextPosition;

        positionChanged(oldPosition, nextPosition);
        energy-=map.getStepCost();
    }

    private void positionChanged(Vector2d oldPosition, Vector2d newPosition){
        for(IPositionChangeObserver observer : observers){
            observer.positionChanged(this, oldPosition, newPosition);
        }
    }

    public void feed(int nutritionalValue){
        this.energy += nutritionalValue;
    }
}
