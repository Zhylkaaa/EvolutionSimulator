/*
    N
  W   E
    S
 */

public enum MapDirection {

    NORTH,
    SOUTH,
    WEST,
    EAST,
    NORTH_EAST,
    NORTH_WEST,
    SOUTH_EAST,
    SOUTH_WEST;

    private Vector2d north = new Vector2d(0,1);
    private Vector2d east = new Vector2d(1, 0);
    private Vector2d west = new Vector2d(-1, 0);
    private Vector2d south = new Vector2d(0, -1);
    private Vector2d north_east = new Vector2d(1, 1);
    private Vector2d north_west = new Vector2d(-1, 1);
    private Vector2d south_east = new Vector2d(1, -1);
    private Vector2d south_west = new Vector2d(-1, -1);

    public String toString() {
        switch (this) {
            case NORTH: return "N";
            case EAST: return "E";
            case SOUTH: return "S";
            case WEST: return "W";
            case NORTH_EAST: return "NE";
            case NORTH_WEST: return "NW";
            case SOUTH_EAST: return "SE";
            case SOUTH_WEST: return "SW";
            default: return "unknown";
        }
    }

    public MapDirection next(){
        return getMapDirection(EAST, SOUTH, WEST, NORTH);
    }

    public MapDirection previous(){
        return getMapDirection(WEST, NORTH, EAST, SOUTH);
    }

    public MapDirection turn(MoveDirection turn){
        int intTurn = turn.ordinal();
        int intDirection = this.ordinal();
        MapDirection[] direction = MapDirection.values();

        int intNextDirection = (intDirection + intTurn) % direction.length;

        return direction[intNextDirection];
    }

    private MapDirection getMapDirection(MapDirection west, MapDirection north, MapDirection east, MapDirection south) {
        switch (this){
            case NORTH: return west;
            case EAST: return north;
            case SOUTH: return east;
            case WEST: return south;
            default: return null;
        }
    }

    public Vector2d toUnitVector(){
        switch (this) {
            case NORTH: return north;
            case EAST: return east;
            case WEST: return west;
            case SOUTH: return south;
            case NORTH_EAST: return north_east;
            case NORTH_WEST: return north_west;
            case SOUTH_EAST: return south_east;
            case SOUTH_WEST: return south_west;
            default: return null;
        }
    }
}
