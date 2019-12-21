import javafx.scene.paint.Color;
import javafx.util.Pair;

import java.util.*;

public abstract class AbstractWorldMap implements IWorldMap, IPositionChangeObserver {

    // TODO: extract information to class Ceil

    protected static Random sampler = new Random();
    protected LinkedList<Animal> animalList = new LinkedList<>();
    protected Map<Vector2d, LinkedList<Animal>> animals = new HashMap<>();
    protected Map<Vector2d, Plant> plants = new HashMap<>();

    protected int width;
    protected int height;

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    private boolean move() {
        LinkedList<Animal> animalsToDelete = new LinkedList<>();
        boolean canMove = false;

        for (Animal animal : animalList) {
            if (animal.isAlive()) {
                animal.move();
                canMove = true;
            } else {
                animalsToDelete.add(animal);
            }
        }

        for (Animal animal : animalsToDelete) {
            animals.get(animal.getPosition()).remove(animal);
            animalList.remove(animal);
        }

        return canMove;
    }

    private void feed() {
        LinkedList<Vector2d> plantsToRemove = new LinkedList<>();

        for (Map.Entry<Vector2d, Plant> position_plant : plants.entrySet()) {
            Vector2d position = position_plant.getKey();
            Plant plant = position_plant.getValue();

            if (animals.get(position) == null || animals.get(position).size() == 0) continue;

            LinkedList<Animal> animalsToFeed = getAnimalsToFeedAtPosition(position);

            if(animalsToFeed.size() > 0 && animalsToFeed.get(0).getEnergy() >= Animal.getMaxEnergy())continue;

            int receivedEnergy;
            int counter = 0;

            if (plant.getNutritionalValue() < animalsToFeed.size()) {
                receivedEnergy = 1;
            } else {
                receivedEnergy = plant.getNutritionalValue() / animalsToFeed.size();
            }

            plantsToRemove.add(position);

            for (Animal animal : animalsToFeed) {
                animal.feed(receivedEnergy);
                counter += receivedEnergy;
                if (counter >= plant.getNutritionalValue()) break;
            }
        }

        for(Vector2d position : plantsToRemove){
            plants.remove(position);
        }
    }

    private LinkedList<Animal> getAnimalsToFeedAtPosition(Vector2d position) {

        int maxEnergy = -1;

        if (animals.get(position) == null) return new LinkedList<>();

        for (Animal animal : animals.get(position)) {
            maxEnergy = animal.getEnergy() > maxEnergy ? animal.getEnergy() : maxEnergy;
        }

        LinkedList<Animal> result = new LinkedList<>();

        for (Animal animal : animals.get(position)) {
            if (animal.getEnergy() == maxEnergy) result.add(animal);
        }

        return result;
    }

    private void populate() {
        List<Vector2d> positions = new LinkedList<>();

        for (Map.Entry<Vector2d, LinkedList<Animal>> position_animals : animals.entrySet()) {
            if (!position_animals.getValue().isEmpty()) {
                positions.add(position_animals.getKey());
            }
        }

        populateAtPositions(positions);
    }

    private void populateAtPositions(List<Vector2d> positions) {
        List<Animal> animalsToAdd = new LinkedList<>();

        for (Vector2d position : positions) {
            LinkedList<Animal> animals = this.animals.get(position);
            Pair<Animal, Animal> parents = findAnimalsToPopulate(animals);
            if (parents.getKey() != null && parents.getValue() != null) {

                Animal animal = Animal.populate(parents.getKey(), parents.getValue());

                if (animal != null) {
                    animalsToAdd.add(animal);
                }
            }
        }

        for (Animal animal : animalsToAdd) {
            place(animal);
        }
    }

    private Pair<Animal, Animal> findAnimalsToPopulate(LinkedList<Animal> animals) {
        int firstEnergy = -1, secondEnergy = -1;
        Animal first = null, second = null;

        for (Animal animal : animals) {
            if (animal.getEnergy() > firstEnergy) {
                secondEnergy = firstEnergy;
                second = first;
                first = animal;
                firstEnergy = first.getEnergy();
            } else if (animal.getEnergy() > secondEnergy) {
                secondEnergy = animal.getEnergy();
                second = animal;
            } else if (animal.getEnergy() == firstEnergy) {
                if (secondEnergy == -1) {
                    second = animal;
                    secondEnergy = animal.getEnergy();
                } else {
                    int p = AbstractWorldMap.sampler.nextInt(2);
                    if (p == 1) {
                        second = first;
                        secondEnergy = firstEnergy;
                        first = animal;
                        firstEnergy = animal.getEnergy();
                    }
                }
            } else if (animal.getEnergy() == secondEnergy) {
                int p = AbstractWorldMap.sampler.nextInt(2);
                if (p == 1) {
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
        if (canMove) {
            feed();
            populate(); //fuck();
        } else {
            return false;
        }

        return true;
    }

    @Override
    public boolean place(Animal animal) {

        animalList.add(animal);
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
        if (animals.get(animal.getPosition()) != null) {
            animals.get(animal.getPosition()).add(animal);
        } else {
            animals.put(animal.getPosition(), new LinkedList<>(Collections.singletonList(animal)));
        }
    }

    public Pair<String, Color> getDrawElement(Vector2d position){
        LinkedList<Animal> animalsAtPosition = animals.get(position);
        String toDisplay = "";

        if(animalsAtPosition != null && animalsAtPosition.size() > 0){
            Animal animalToDisplay = animalsAtPosition.get(0);
            for(Animal animal : animalsAtPosition){
                animalToDisplay = animalToDisplay.getEnergy() >= animal.getEnergy() ? animalToDisplay : animal;
            }
            toDisplay = animalToDisplay.toString();
        }
        return new Pair<>(toDisplay, plants.get(position) != null ? Color.GREEN : Color.BLACK);
    }

    public Genome getMostCommonGenome(){
        return Statistics.getMostCommonGene(animalList);
    }
}
