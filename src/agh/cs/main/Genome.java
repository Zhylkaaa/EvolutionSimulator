import com.sun.deploy.util.StringUtils;

import java.util.*;

public class Genome {
    public static final int SIZE = 32;

    public List<Integer> getGenes() {
        return genes;
    }

    private static Random sampler = new Random();
    private List<Integer> genes;

    private static Comparator<Integer> comparator = Comparator.comparingInt(a -> a);

    public Genome(){
        genes = new ArrayList<>();

        for(int i = 0;i<Genome.SIZE;i++){
            genes.add(Genome.sampler.nextInt(8));
        }


        genes.sort(Genome.comparator);
    }

    @Override
    public int hashCode() {
        int hash = 0;

        for(int gen : this.genes){
            hash = gen + 31*hash;
        }

        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Genome))return super.equals(obj);

        boolean isEquals = true;

        Genome other = (Genome) obj;

        for(int i=0;i<genes.size(); i++){
            if(!this.genes.get(i).equals(other.genes.get(i))){
                isEquals = false;
                break;
            }
        }
        return isEquals;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        for(Integer i : genes){
            result.append(i.toString());
        }
        return result.toString();
    }

    public Genome(Animal p1, Animal p2){
        Genome g1 = p1.getGenome();
        Genome g2 = p2.getGenome();

        this.genes = inheritGenesFromParents(g1, g2);
    }

    public MoveDirection chooseTurn() {
        MoveDirection[] directions = MoveDirection.values();
        return directions[genes.get(Genome.sampler.nextInt(Genome.SIZE))];
    }

    private List<Integer> validate(List<Integer> resultingGenes, Map<Integer, Integer> count){
        for(int i = 0;i<8;i++){
            if(count.get(i) == 0){
                int pos = Genome.sampler.nextInt(SIZE);
                while(count.get(resultingGenes.get(pos)) < 2){
                    pos = Genome.sampler.nextInt(SIZE);
                }
                count.put(resultingGenes.get(pos), count.get(resultingGenes.get(pos)) - 1);
                resultingGenes.set(pos, i);
            }
        }

        return resultingGenes;
    }

    private List<Integer> inheritGenesFromParents(Genome genome1, Genome genome2){

        List<Integer> g1 = genome1.getGenes();
        List<Integer> g2 = genome2.getGenes();

        int p1 = Genome.sampler.nextInt(SIZE);
        int p2 = Genome.sampler.nextInt(p1+1);

        HashMap<Integer, Integer> count = new HashMap<>();

        List<Integer> resultingGenes = new ArrayList<>();

        int i;

        for(i = 0;i<8;i++){
            count.put(i, 0);
        }
        for(i = 0; i <= p2; i++){
            resultingGenes.add(g1.get(i));
            count.put(g1.get(i), count.get(g1.get(i)) + 1);
        }
        for(;i<=p1; i++){
            resultingGenes.add(g2.get(i));
            count.put(g1.get(i), count.get(g1.get(i)) + 1);
        }
        for(;i<SIZE; i++){
            resultingGenes.add(g1.get(i));
            count.put(g1.get(i), count.get(g1.get(i)) + 1);
        }

        validate(resultingGenes, count);

        resultingGenes.sort(Genome.comparator);
        return resultingGenes;
    }
}
