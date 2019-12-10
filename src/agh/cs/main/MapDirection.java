public enum MapDirection {
    NORTH,
    SOUTH,
    WEST,
    EAST;

    public String toString() {
        switch (this) {
            case NORTH: return "Północ";
            case EAST: return "Wschód";
            case SOUTH: return "Południe";
            case WEST: return "Zachód";
            default: return "unknown";
        }
    }

    public MapDirection next(){
        return getMapDirection(EAST, SOUTH, WEST, NORTH);
    }

    public MapDirection previous(){
        return getMapDirection(WEST, NORTH, EAST, SOUTH);
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
            case NORTH: return new Vector2d(0,1);
            case EAST: return new Vector2d(1, 0);
            case WEST: return new Vector2d(-1, 0);
            case SOUTH: return new Vector2d(0, -1);
            default: return null;
        }
    }
}
