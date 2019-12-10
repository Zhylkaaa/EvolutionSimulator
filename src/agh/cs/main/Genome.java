import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class Genome {
    private int size;

    public int getSize() {
        return size;
    }

    public List<Integer> getGenes() {
        return genes;
    }

    private static Random sampler = new Random();
    private List<Integer> genes;

    private static Comparator<Integer> comparator = Comparator.comparingInt(a -> a);

    public Genome(int n){
        this.size = n;
        genes = new ArrayList<>();

        for(int i = 0;i<this.size;i++){
            genes.add(Genome.sampler.nextInt(4));
        }

        genes.sort(Genome.comparator);
    }

    public Genome(Animal p1, Animal p2){
        Genome g1 = p1.getGenome();
        Genome g2 = p2.getGenome();

        this.size = g1.getSize();

        this.genes = inheritGenesFromParents(g1, g2);
    }

    private MapDirection intToDirection(int d){
        switch (d){
            case 0:
                return MapDirection.NORTH;
            case 1:
                return MapDirection.EAST;
            case 2:
                return MapDirection.SOUTH;
            case 3:
                return MapDirection.WEST;
            default:
                return null;
        }
    }

    public MapDirection chooseMove(){
        return intToDirection(this.genes.get(Genome.sampler.nextInt(this.size)));
    }

    private List<Integer> inheritGenesFromParents(Genome genome1, Genome genome2){

        List<Integer> g1 = genome1.getGenes();
        List<Integer> g2 = genome2.getGenes();

        List<Integer> genes = new ArrayList<>(this.size);
        int i = 0;

        for(;i<this.size/2;i++){
            genes.set(i, g1.get(Genome.sampler.nextInt(g1.size())));
        }

        for(;i<this.size;i++){
            genes.set(i, g2.get(Genome.sampler.nextInt(g2.size())));
        }

        genes.sort(Genome.comparator);

        return genes;
    }
}
