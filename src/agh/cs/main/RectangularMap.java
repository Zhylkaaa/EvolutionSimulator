public class RectangularMap extends AbstractWorldMap implements IPositionChangeObserver {
    private int width;
    private int height;

    public RectangularMap(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return position.x <= width && position.x >= 0 && position.y <= height && position.y >= 0 && super.canMoveTo(position);
    }
}
