public class ForestMap extends AbstractWorldMap {

    private Vector2d forestLower, forestUpper;
    private static int MAX_SAMPLES = 100;

    private int plantsToGrowInForest;
    private int plantsToGrow;

    public ForestMap(int width, int height, float forest_ratio, int startAnimalCount, int plantsToGrowInForest, int plantsToGrow, int stepCost){
        this.plantsToGrowInForest = plantsToGrowInForest;
        this.plantsToGrow = plantsToGrow;
        this.stepCost = stepCost;
        this.width = width;
        this.height = height;

        forestLower = new Vector2d( (int) (width * forest_ratio), (int) (height * forest_ratio));
        forestUpper = new Vector2d(width - (int) (width * forest_ratio), height - (int) (height * forest_ratio));

        fillForest();
        putInitialAnimals(startAnimalCount);
    }

    private void putInitialAnimals(int startAnimalCount) {
        for(int i = 0;i<startAnimalCount;i++){
            Animal animal = new Animal(this);
        }
    }

    private void fillForest() {
        int x_max = forestUpper.x;
        int y_max = forestUpper.y;

        for(int x = forestLower.x; x < x_max;x++){
            for(int y = forestLower.y; y < y_max; y++){
                Vector2d position = new Vector2d(x, y);
                if(plants.get(position) == null){
                    plants.put(position, new Plant(position));
                }
            }
        }
    }

    @Override
    protected void flourish() {
        for(int i = 0;i<plantsToGrowInForest;i++)plantInForest();
        for(int i = 0;i<plantsToGrow - plantsToGrowInForest; i++)plantInSteppe();
    }

    private void plantInSteppe() {
        int i = 0;
        for(; i<MAX_SAMPLES; i++){
            int x = sampler.nextInt(width);
            int y = sampler.nextInt(height);

            Vector2d position = new Vector2d(x, y);

            if(notInForest(position) && plants.get(position) == null){
                plants.put(position, new Plant(position));
                break;
            }
        }
    }

    private boolean notInForest(Vector2d position) {
        return position.x < forestLower.x || position.x >= forestUpper.x || position.y < forestLower.y || position.y >= forestUpper.y;
    }

    private void plantInForest() {
        int rangeX = forestUpper.x - forestLower.x;
        int rangeY = forestUpper.y - forestLower.y;

        for(int i = 0; i<MAX_SAMPLES; i++){
            int x = forestLower.x + sampler.nextInt(rangeX);
            int y = forestLower.y + sampler.nextInt(rangeY);

            Vector2d position = new Vector2d(x, y);

            if(plants.get(position) == null){
                plants.put(position, new Plant(position));
                break;
            }
        }
    }
}
