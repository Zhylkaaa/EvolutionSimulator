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

import java.util.ArrayList;
import java.util.List;

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

    private static int per_x = 30;
    private static int per_y = 30;

    private static AbstractWorldMap map;

    private static long timediff = 100000000L;

    private AnimationTimer timer = new AnimationTimer() {

        private long lastTimer = 0;

        @Override
        public void handle(long now) {
            if(now - lastTimer >= timediff){
                boolean canRun = map.run();

                update();
                lastTimer = now;
                if(!canRun){
                    timer.stop();
                }
            }
        }
    };

    public static void main(String[] args) {
        map = new ForestMap(per_x, per_y, 0.25f, 100, 6, 7);

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setScene(new Scene(createContent()));
        primaryStage.show();
    }

    private Parent createContent() {
        WorldTile.TILE_X_SIZE = map_width / per_x;
        WorldTile.TILE_Y_SIZE = map_height / per_y;

        root.setPrefSize(per_x * WorldTile.TILE_X_SIZE + statistic_width, per_y * WorldTile.TILE_X_SIZE);

        root.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        root.setPadding(new Insets(0, 10, 10, 10));
        List<WorldTile> tiles = new ArrayList<>();

        for(int i = 0;i<per_x;i++){
            for(int j = 0;j<per_y;j++){
                tiles.add(new WorldTile(i, j,"A", null));
            }
        }

        tilesPane.getChildren().addAll(tiles);

        root.setCenter(tilesPane);
        Button startPauseButton = new Button("Start");

        animalCount.setFill(Color.WHITE);
        plantCount.setFill(Color.WHITE);
        mostCommonGene.setFill(Color.WHITE);

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

        leftPane.getChildren().addAll(startPauseButton, animalCount, plantCount, mostCommonGene);

        //leftPane.setPrefSize(garbage_width, panes_height);

        root.setLeft(leftPane);

        //root.getChildren().addAll(tiles);
        update();

        return root;
    }

    private void update(){

        animalCount.setText("Animal count: " + Integer.toString(map.animalList.size()));
        plantCount.setText("Plant count: " + Integer.toString(map.plants.values().size()));

        Genome commonGene = map.getMostCommonGenome();
        String genome = commonGene == null ? "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx" : commonGene.toString();

        mostCommonGene.setText("Most common gene: \n"+genome);

        for(Node children : tilesPane.getChildren()){
            WorldTile tile = (WorldTile) children;

            Pair<String, Color> drawElement = map.getDrawElement(tile.getPosition());

            tile.setColor(drawElement.getValue());
            tile.setValue(drawElement.getKey());
        }
    }
}
