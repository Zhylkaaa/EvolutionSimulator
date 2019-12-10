import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

public class MapBoundary implements IPositionChangeObserver {
    private SortedSet<Vector2d> X = new TreeSet<>(new XComparator());
    private SortedSet<Vector2d> Y = new TreeSet<>(new YComparator());

    private class XComparator implements Comparator<Vector2d> {
        @Override
        public int compare(Vector2d v1, Vector2d v2) {
            return v1.x != v2.x ? v1.x - v2.x : v1.y - v2.y;
        }
    }

    private class YComparator implements Comparator<Vector2d> {
        @Override
        public int compare(Vector2d v1, Vector2d v2) {
            return v1.y != v2.y ? v1.y - v2.y : v1.x - v2.x;
        }
    }

    public void place(IMapElement element) {
        X.add(element.getPosition());
        Y.add(element.getPosition());
        if(element instanceof Animal){
            ((Animal) element).addObserver(this);
        }
    }

    @Override
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition) {
        X.remove(oldPosition);
        Y.remove(oldPosition);
        X.add(newPosition);
        Y.add(newPosition);
    }

    public Vector2d getLowerLeft() {
        return X.first().lowerLeft(Y.first());
    }

    public Vector2d getUpperRight() {
        return X.last().upperRight(Y.last());
    }
}
