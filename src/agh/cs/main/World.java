import com.google.gson.Gson;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.exit;

public class World extends Application {

    private static final int statistic_width = 300;

    private static final int map_width = 600;
    private static final int map_height = 600;


    private boolean isRuning = false;

    private BorderPane root = new BorderPane();
    private Pane tilesPane = new Pane();
    private VBox leftPane = new VBox(10);
    private Text animalCount = new Text("");
    private Text plantCount = new Text("");
    private Text mostCommonGene = new Text("");
    private Text days = new Text("Current day: 0");
    private Text daysLived = new Text();
    private Text descendantsCount = new Text();
    private Text trackedEnergy = new Text();

    private int dayCounter = 0;

    private static int per_x = 20;
    private static int per_y = 20;

    private static AbstractWorldMap map;

    private static long timediff = Long.parseLong("100000000");

    private AnimationTimer timer = new AnimationTimer() {

        private long lastTimer = 0;

        @Override
        public void handle(long now) {
            if(now - lastTimer >= timediff){
                boolean canRun = map.run();
                dayCounter++;
                update();
                lastTimer = now;
                if(!canRun){
                    timer.stop();
                }
            }
        }
    };

    private static void setupMapParameters(String filename){
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(filename));
        } catch (FileNotFoundException e) {
            System.out.println("Shit happened, no file found!");
            exit(1);
        }

        Gson gson = new Gson();
        StartParameters parameters = gson.fromJson(reader, StartParameters.class);

        map = new ForestMap(parameters.getWidth_in_tiles(),
                parameters.getHeight_in_tiles(),
                parameters.getForest_ratio(),
                parameters.getAnimal_start_count(),
                parameters.getPlants_to_grow_in_forest(),
                parameters.getPlants_to_grow(),
                parameters.getStep_cost());

        per_x = parameters.getWidth_in_tiles();
        per_y = parameters.getHeight_in_tiles();

        Animal.MAX_ENERGY = parameters.getAnimal_max_energy();
        Animal.MIN_ENERGY_TO_POPULATE = parameters.getAnimal_min_energy_to_populate();

        Plant.MAX_NUTRITIONAL_VALUE = parameters.getMax_nutrition_value();

        timediff = Long.parseLong(parameters.getAnimation_delay());
    }

    public static void main(String[] args) {
        //map = new ForestMap(per_x, per_y, 0.25f, 30, 6, 10, 1);
        setupMapParameters(args[0]);

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setScene(new Scene(createContent()));
        primaryStage.show();
    }

    EventHandler<MouseEvent> trackHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            if(!isRuning){
                WorldTile tile = ((WorldTile)event.getSource());
                Animal animal = map.getAnimalToTrack( tile.getPosition() );
                if(animal == null)return;

                if(map.getStatistic().getTrackedAnimal() != null){
                    Animal prevTracked = map.getStatistic().getTrackedAnimal();
                    map.getStatistic().setTrackedAnimal(animal);

                    Pair<String, Color> element = map.getDrawElement(prevTracked.getPosition());
                    for(Node children : tilesPane.getChildren()){
                        WorldTile t = (WorldTile) children;
                        if(t.getPosition().equals(prevTracked.getPosition())){
                            t.setColor(element.getValue());
                            break;
                        }
                    }
                } else {
                    map.getStatistic().setTrackedAnimal(animal);
                }

                tile.setColor(Color.RED);
                trackedEnergy.setText("Energy: " + animal.getEnergy());
            }
        }
    };

    private Parent createContent() {
        WorldTile.TILE_X_SIZE = map_width / per_x;
        WorldTile.TILE_Y_SIZE = map_height / per_y;

        root.setPrefSize(per_x * WorldTile.TILE_X_SIZE + statistic_width, per_y * WorldTile.TILE_X_SIZE);

        root.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        root.setPadding(new Insets(0, 10, 10, 10));
        List<WorldTile> tiles = new ArrayList<>();

        for(int i = 0;i<per_x;i++){
            for(int j = 0;j<per_y;j++){
                tiles.add(new WorldTile(i, j,"A", null, trackHandler));
            }
        }

        tilesPane.getChildren().addAll(tiles);

        root.setCenter(tilesPane);
        Button startPauseButton = new Button("Start");
        Text statisticTitle = new Text("Tracked Statistics:");

        animalCount.setFill(Color.WHITE);
        plantCount.setFill(Color.WHITE);
        mostCommonGene.setFill(Color.WHITE);
        days.setFill(Color.WHITE);
        daysLived.setFill(Color.WHITE);
        descendantsCount.setFill(Color.WHITE);
        trackedEnergy.setFill(Color.WHITE);
        statisticTitle.setFill(Color.WHITE);

        startPauseButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(isRuning){
                    timer.stop();
                    isRuning = false;
                    startPauseButton.setText("Start");
                } else {
                    timer.start();
                    isRuning = true;
                    startPauseButton.setText("Pause");
                }
            }
        });

        leftPane.getChildren().addAll(startPauseButton, animalCount, plantCount, mostCommonGene, days, statisticTitle, daysLived, descendantsCount, trackedEnergy);

        //leftPane.setPrefSize(garbage_width, panes_height);

        root.setLeft(leftPane);

        //root.getChildren().addAll(tiles);
        update();

        return root;
    }

    private void update(){

        days.setText("Current day: " + Integer.toString(dayCounter));
        animalCount.setText("Animal count: " + Integer.toString(map.animalList.size()));
        plantCount.setText("Plant count: " + Integer.toString(map.plants.values().size()));

        Genome commonGene = map.getMostCommonGenome();
        String genome = commonGene == null ? "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx" : commonGene.toString();

        mostCommonGene.setText("Most common gene: \n"+genome);

        Summary stats = map.getStatistic().getTrackedAnimalStatistics();

        if(stats == null){
            daysLived.setText("Not tracing any animal");
            descendantsCount.setText("To start pause animation and click on animal");
            trackedEnergy.setText("");
        } else {
            daysLived.setText("Day Lived: " + stats.getDaysLived());
            descendantsCount.setText("Descendants Count: " + stats.getDescendantsCount());
            trackedEnergy.setText("Energy: " + stats.getEnergy());
        }

        for(Node children : tilesPane.getChildren()){
            WorldTile tile = (WorldTile) children;

            Pair<String, Color> drawElement = map.getDrawElement(tile.getPosition());

            tile.setColor(drawElement.getValue());
            tile.setValue(drawElement.getKey());
        }
    }
}
