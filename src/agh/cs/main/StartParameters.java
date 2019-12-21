public class StartParameters {
    private int width_in_tiles;
    private int height_in_tiles;
    private float forest_ratio;
    private int animal_start_count;
    private int plants_to_grow_in_forest;
    private int plants_to_grow_in_steppe;
    private int step_cost;
    private int max_nutrition_value;
    private int animal_max_energy;
    private int animal_min_energy_to_populate;
    private String animation_delay;

    public int getWidth_in_tiles() {
        return width_in_tiles;
    }

    public int getHeight_in_tiles() {
        return height_in_tiles;
    }

    public float getForest_ratio() {
        return forest_ratio;
    }

    public int getAnimal_start_count() {
        return animal_start_count;
    }

    public int getPlants_to_grow_in_forest() {
        return plants_to_grow_in_forest;
    }

    public int getPlants_to_grow_in_steppe() {
        return plants_to_grow_in_steppe;
    }

    public int getStep_cost() {
        return step_cost;
    }

    public int getMax_nutrition_value() {
        return max_nutrition_value;
    }

    public int getAnimal_max_energy() {
        return animal_max_energy;
    }

    public int getAnimal_min_energy_to_populate() {
        return animal_min_energy_to_populate;
    }

    public String getAnimation_delay() {
        return animation_delay;
    }
}
