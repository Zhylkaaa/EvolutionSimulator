import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Statistics {
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
}
