package view;

import java.util.List;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.GameState;
import model.Location;
import model.Minesweeper;

public class GameGUI extends Application{

    public static final String IURL = "file:media/images/original/";
    public static final Image MINE = new Image(IURL + "mine24.png");
    public static final Image FLAG = new Image(IURL + "flag24.png");
    public static final Image TILE = new Image(IURL + "tile24.png");
    public static final Image HINT = new Image(IURL + "hint24.png");
    public static final Image COVERED = new Image(IURL + "covered24.png");
    public static final Image DETONATED = new Image(IURL + "detonated24.png");

    public static final Font MSF_FONT = Font.loadFont("file:media/fonts/mine-sweeper.ttf", 11);

    private Button makeMineButton(Minesweeper minesweeper, int col, int row) {
        Button tile = new Button();

        tile.setBackground(
            new Background(
                new BackgroundImage(
                COVERED,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                BackgroundSize.DEFAULT)
            )
        );

        tile.setFont(MSF_FONT);
        tile.setTextFill(Color.BLUE);
        tile.setContentDisplay(ContentDisplay.CENTER);
        tile.setPadding(new Insets(0));
        tile.setPrefSize(24, 24);

        return tile;
    }

    private Button makeControlButton(String text, EventHandler<ActionEvent> handler) {
        Button button = new Button(text);

        button.setAlignment(Pos.CENTER);
        button.setPadding(new Insets(5));
        button.setMaxSize(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
        HBox.setHgrow(button, Priority.ALWAYS);
        button.setOnAction(handler);

        return button;
    }

    private Label makeLabel(String text) {
        Label label = new Label(text);

        label.setAlignment(Pos.CENTER);
        label.setPadding(new Insets(5));
        label.setMaxSize(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
        HBox.setHgrow(label, Priority.ALWAYS);

        return label;
    }

    @Override
    public void start(Stage stage) throws Exception {
        Minesweeper game = new Minesweeper(15, 15, 45);

        Button solve = makeControlButton("Solve", (arg) -> {
            if (game.getGameState() != GameState.IN_PROGRESS)
                return;

            List<Location> solveSelections = game.solve();
            new Thread(()-> { // Make new thread as to not interfere w/GUI.
                for (Location loc : solveSelections) {

                    // Jump ship if the user steps in:
                    if (game.getGameState() != GameState.IN_PROGRESS)
                        break;

                        Platform.runLater(() -> {
                            try { game.makeSelection(loc);
                            } catch (Exception e) { /* squash */ }});

                        try { Thread.sleep(50);
                        } catch (Exception e) { /* squash */ }
                }
            }).start();
        });
        
        Button reset = makeControlButton("Reset", (arg) -> game.reset());
        Button hint = makeControlButton("Hint", (arg) -> game.giveHint());
        Label gameState = makeLabel("Status: " + game.getGameState().toString());
        Label moveCount = makeLabel("Moves: " + String.valueOf(game.getMoveCount()));
        Label mineCount = makeLabel("Mines: " + String.valueOf(game.getMineCount()));

        MinesweeperUpdater updater = new MinesweeperUpdater(game, moveCount, gameState);
        GridPane pane=new GridPane();

        for (int col = 0 ; col < game.getCols() ; col++) {
            for (int row = 0 ; row < game.getRows() ; row++) {
                Button tile = makeMineButton(game, col, row);
                pane.add(tile, col, row);

                updater.setTileButton(row, col, tile);
                tile.setOnMouseClicked(
                    new SelectionHandler(
                        new Location(row, col), game)
                );
            }
        } 
        
        game.register(updater);

        HBox gameStatus = new HBox();
        gameStatus.getChildren().addAll(gameState);
        
        HBox controls = new HBox();
        controls.getChildren().addAll(reset, hint, solve, moveCount, mineCount);

        BorderPane window = new BorderPane();
        window.setBottom(gameStatus);
        window.setTop(controls);
        window.setCenter(pane);

        stage.setTitle("Minesweeper");
        stage.setScene(new Scene(window));
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
