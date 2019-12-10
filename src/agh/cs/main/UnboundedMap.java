import java.util.List;

public class UnboundedMap extends AbstractWorldMap {

    public UnboundedMap(){}


    public UnboundedMap(List<Plant> plants){
        for(Plant plant : plants){
            elements.put(plant.getPosition(), plant);
            boundary.place(plant);
        }
    }
}
