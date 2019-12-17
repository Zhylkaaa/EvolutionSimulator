import javafx.util.Pair;

import java.util.*;

public abstract class AbstractWorldMap implements IWorldMap, IPositionChangeObserver {
    protected static Random sampler = new Random();
    protected Map<Vector2d, HashSet<Animal>> animals = new HashMap<>();
    protected Map<Vector2d, Plant> plants = new HashMap<>();

    private MapVisualizer visualizer = new MapVisualizer(this);
    protected int width;
    protected int height;

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    private boolean move(){
        LinkedList<Animal> animalsToDelete = new LinkedList<>();
        boolean canMove = false;

        for(HashSet<Animal> animalsAtPosition : animals.values()){
            for(Animal animal : animalsAtPosition){
                if(animal.isAlive()){
                    animal.move();
                    canMove = true;
                } else {
                    animalsToDelete.add(animal);
                }
            }
        }

        for(Animal animal : animalsToDelete){
            animals.get(animal.getPosition()).remove(animal);
        }

        return canMove;
    }

    private void feed(){
        for(Map.Entry<Vector2d, Plant> position_plant : plants.entrySet()){
            Vector2d position = position_plant.getKey();
            Plant plant = position_plant.getValue();

            if(animals.get(position).size() == 0) continue;

            LinkedList<Animal> animalsToFeed = getAnimalsToFeedAtPosition(position);

            int receivedEnergy;
            int counter = 0;

            if(plant.getNutritionalValue() < animalsToFeed.size()){
                receivedEnergy = 1;
            } else {
                receivedEnergy = plant.getNutritionalValue() / animalsToFeed.size();
            }

            for(Animal animal : animalsToFeed){
                animal.feed(receivedEnergy);
                counter += receivedEnergy;
                if(counter >= plant.getNutritionalValue())break;
            }
        }
    }

    private LinkedList<Animal> getAnimalsToFeedAtPosition(Vector2d position) {

        int maxEnergy = -1;

        for(Animal animal : animals.get(position)){
            maxEnergy = animal.getEnergy() > maxEnergy ? animal.getEnergy() : maxEnergy;
        }

        LinkedList<Animal> result = new LinkedList<>();

        for(Animal animal : animals.get(position)){
           if(animal.getEnergy() == maxEnergy) result.add(animal);
        }

        return result;
    }

    private void populate(){
        List<Vector2d> positions = new LinkedList<>();

        for(Map.Entry<Vector2d, HashSet<Animal>> position_animals : animals.entrySet()){
            if(!position_animals.getValue().isEmpty()){
                positions.add(position_animals.getKey());
            }
        }

        populateAtPositions(positions);
    }

    private void populateAtPositions(List<Vector2d> positions) {
        List<Animal> animalsToAdd = new LinkedList<>();

        for(Vector2d position : positions){
            HashSet<Animal> animals = this.animals.get(position);
            Pair<Animal, Animal> parents = findAnimalsToPopulate(animals);
            if(parents.getKey() != null && parents.getValue() != null) {
                Animal animal = Animal.populate(parents.getKey(), parents.getValue());

                if(animal != null){
                    animalsToAdd.add(animal);
                }
            }
        }

        for(Animal animal : animalsToAdd){
            addToAnimals(animal);
        }
    }

    private Pair<Animal, Animal> findAnimalsToPopulate(HashSet<Animal> animals) {
        int firstEnergy = -1, secondEnergy = -1;
        Animal first = null, second = null;

        for(Animal animal : animals){
            if(animal.getEnergy() > firstEnergy){
                secondEnergy = firstEnergy;
                second = first;
                first = animal;
                firstEnergy = first.getEnergy();
            } else if(animal.getEnergy() > secondEnergy){
                secondEnergy = animal.getEnergy();
                second = animal;
            } else if(animal.getEnergy() == firstEnergy){
                if(secondEnergy == -1){
                    second = animal;
                    secondEnergy = animal.getEnergy();
                } else {
                    int p = AbstractWorldMap.sampler.nextInt(2);
                    if(p == 1){
                        second = first;
                        secondEnergy = firstEnergy;
                        first = animal;
                        firstEnergy = animal.getEnergy();
                    }
                }
            } else if(animal.getEnergy() == secondEnergy){
                int p = AbstractWorldMap.sampler.nextInt(2);
                if(p == 1){
                    second = animal;
                    secondEnergy = animal.getEnergy();
                }
            }
        }

        return new Pair<>(first, second);
    }

    protected abstract void flourish();

    @Override
    public boolean run() {
        flourish();
        boolean canMove = move();
        if(canMove) {
            feed();
            populate(); //fuck();
        } else {
            return false;
        }

        return true;
    }

    @Override
    public boolean place(Animal animal) {

        addToAnimals(animal);
        animal.addObserver(this);

        return true;
    }

    @Override
    public void positionChanged(Animal animal, Vector2d oldPosition, Vector2d newPosition) {
        animals.get(oldPosition).remove(animal);
        addToAnimals(animal);
    }

    private void addToAnimals(Animal animal) {
        if(animals.get(animal.getPosition()) != null){
            animals.get(animal.getPosition()).add(animal);
        } else {
            animals.put(animal.getPosition(), new HashSet<>(Collections.singletonList(animal)));
        }
    }

    @Override
    public Object[] objectsAt(Vector2d position) {
        return animals.get(position).toArray();
    }

    @Override
    public String toString() {
        return visualizer.draw(new Vector2d(0, 0), new Vector2d(this.width, this.height));
    }
}
