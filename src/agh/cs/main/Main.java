public class Main {

    public static void main(String[] args) {
        RectangularMap map = new RectangularMap(20, 20);

        Animal a1 = new Animal(map, new Vector2d(4, 5));
        Animal a2 = new Animal(map, new Vector2d(0, 6));
        Animal a3 = new Animal(map, new Vector2d(10, 10));

        boolean canRun = true;

        while(canRun){
            canRun = map.run();
            System.out.println(map);
        }
    }
}
