import java.util.Objects;

public class Vector2d {
    public final int x, y;

    public Vector2d(int x, int y){
        this.x = x;
        this.y = y;
    }

    public String toString(){
        return "("+this.x+","+this.y+")";
    }

    public boolean precedes(Vector2d v){
        return this.x <= v.x && this.y <= v.y;
    }

    public boolean follows(Vector2d v){
        return this.x >= v.x && this.y >= v.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    public Vector2d upperRight(Vector2d v){
        return new Vector2d(this.x > v.x ? this.x : v.x, this.y > v.y ? this.y : v.y);
    }

    public Vector2d lowerLeft(Vector2d v){
        return new Vector2d(this.x < v.x ? this.x : v.x, this.y < v.y ? this.y : v.y);
    }

    public Vector2d add(Vector2d v){
        return new Vector2d(this.x + v.x, this.y + v.y);
    }

    public Vector2d subtract(Vector2d v){
        return new Vector2d(this.x - v.x, this.y - v.y);
    }

    public boolean equals(Object other){
        if(this == other)return true;

        if(!(other instanceof Vector2d))return false;
        else{
            Vector2d v = (Vector2d) other;
            return this.x == v.x && this.y == v.y;
        }
    }

    public Vector2d opposite(){
        return new Vector2d(-this.x, -this.y);
    }
}
