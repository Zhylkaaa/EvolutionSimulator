public class Plant implements IMapElement {

    public static int MAX_NUTRITIONAL_VALUE = 10;

    private Vector2d position;
    private int nutritionalValue;

    public int getNutritionalValue() {
        return nutritionalValue;
    }

    @Override
    public Vector2d getPosition() {
        return position;
    }

    public Plant(Vector2d position){
        this.position = position;
        this.nutritionalValue = AbstractWorldMap.sampler.nextInt(MAX_NUTRITIONAL_VALUE) + 1;
    }

    @Override
    public String toString() {
        return "P";
    }
}
