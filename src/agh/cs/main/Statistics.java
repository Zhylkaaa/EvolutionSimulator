import javafx.util.Pair;

import java.util.*;

public class Statistics {

    private Animal trackedAnimal;
    private int dayLived = 0;
    private Set<Animal> descendants = new HashSet<>();

    public static Genome getMostCommonGene(LinkedList<Animal> animals){
        Map<Genome, Integer> counts = new HashMap<>();

        for(Animal animal : animals){
            counts.putIfAbsent(animal.getGenome(), 0);

            counts.put(animal.getGenome(), counts.get(animal.getGenome()) + 1);
        }

        int max_count = 0;
        Genome result = null;

        for(Map.Entry<Genome, Integer> genome_count : counts.entrySet()){
            if(max_count < genome_count.getValue()){
                max_count = genome_count.getValue();
                result = genome_count.getKey();
            }
        }

        return result;
    }

    public void setTrackedAnimal(Animal trackedAnimal) {
        if(trackedAnimal == null || trackedAnimal == this.trackedAnimal)return;

        this.trackedAnimal = trackedAnimal;
        this.descendants = new HashSet<>();
        this.dayLived = 0;
    }

    public Animal getTrackedAnimal() {
        return trackedAnimal;
    }

    public Summary getTrackedAnimalStatistics(){
        if(trackedAnimal == null) return null;

        if(trackedAnimal.isAlive()){
            dayLived++;
        }

        return new Summary(dayLived, descendants.size(), trackedAnimal.getEnergy());
    }

    public void notifyPopulation(Animal parent1, Animal parent2, Animal child){
        if(child == null)return;

        if(trackedAnimal == parent1 || trackedAnimal == parent2){
            descendants.add(child);
            return;
        }

        if(descendants.contains(parent1)){
            descendants.add(child);
            return;
        }

        if(descendants.contains(parent2)){
            descendants.add(child);
            return;
        }
    }
}
