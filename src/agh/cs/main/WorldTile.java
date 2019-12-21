import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class WorldTile extends StackPane {

    public static int TILE_X_SIZE;
    public static int TILE_Y_SIZE;
    private String value;
    private Color color;
    private Text text;
    private Rectangle border;

    private Vector2d position;

    public void setValue(String value) {
        this.value = value;
        text.setText(value);
    }

    public void setColor(Color color) {
        this.color = color;
        border.setFill(color);
    }

    public WorldTile(int x, int y, String value, Color color, EventHandler<MouseEvent> handler){
        this.value = value;
        this.color = color;

        position = new Vector2d(x, y);

        border = new Rectangle(TILE_X_SIZE, TILE_Y_SIZE);
        border.setFill(color);
        border.setStroke(Color.WHITE);

        text = new Text(value);
        text.setFill(Color.WHITE);

        setTranslateX(x*TILE_X_SIZE);
        setTranslateY(y*TILE_Y_SIZE);

        setAlignment(Pos.CENTER);
        getChildren().addAll(border, text);

        setOnMouseClicked(handler);
    }

    public Vector2d getPosition() {
        return position;
    }
}
