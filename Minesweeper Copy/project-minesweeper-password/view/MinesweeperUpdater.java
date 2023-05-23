package view;

import model.Location;
import model.Minesweeper;
import model.MinesweeperObserver;

import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;

/**
 * MSTile observer for the Minesweeper class.
 */
public class MinesweeperUpdater implements MinesweeperObserver {
    private Button[][] tileButtons;
    private Minesweeper game;
    private int rows;
    private int cols;
    private Label moveCount;
    private Label gameStatus;

    /**
     * Creates a MSTileUpdater.
     * @param game The observed Minesweeper game.
     */
    public MinesweeperUpdater(Minesweeper game, Label moveCount, Label gameStatus) {
        this.game = game;
        this.rows = game.getRows();
        this.cols = game.getCols();
        this.moveCount = moveCount;
        this.gameStatus = gameStatus;

        tileButtons = new Button[rows][cols];
    }

    /**
     * Links the given GUI tile to an MSTile in Minesweeper game.
     * @param row The row of the GUI tile.
     * @param col The column of the GUI tile.
     * @param tileButton A GUI tile.
     */
    public void setTileButton(int row, int col, Button tileButton) throws Exception {
        if (!(tileButton instanceof Button) || row < 0 || row >= rows || col < 0 || col >= cols)
            throw new Exception("Invalid button or location!");

        tileButtons[col][row] = tileButton;
    }

    /**
     * Updates the GUI tile at the given location.
     * @param loc The Location which changed.
     */
    @Override
    public void tileUpdated(Location loc) {
        Color color = Color.WHITE;
        String text = "";
        Image image;

        try {
            char symbol = game.getSymbol(loc);
            switch (symbol) {
                case '-':
                    image = GameGUI.COVERED;
                    break;

                case 'F':
                    image = GameGUI.FLAG;
                    break;

                case 'H':
                    image = GameGUI.HINT;
                    break;
                
                case 'M':
                    image = GameGUI.MINE;
                    break;

                case 'X':
                    image = GameGUI.DETONATED;
                    break;

                default:
                    image = GameGUI.TILE;
                    text += symbol;

                    switch (symbol) {
                        // Original Minesweeper # colors:
                        case '1':
                            color = Color.BLUE;
                            break;

                        case '2':
                            color = Color.GREEN;
                            break;

                        case '3':
                            color = Color.RED;
                            break;

                        case '4':
                            color = Color.PURPLE;
                            break;

                        case '5':
                            color = Color.MAROON;
                            break;

                        case '6':
                            color = Color.TURQUOISE;
                            break;

                        case '7':
                            color = Color.BLACK;
                            break;

                        case '8':
                            color = Color.GRAY;
                            break;
                    }
            }

            Button tile = tileButtons[loc.getCol()][loc.getRow()];
            tile.setGraphic(new ImageView(image));
            tile.setTextFill(color);
            tile.setText(text);

            moveCount.setText(String.valueOf("Moves: " + game.getMoveCount()));
            gameStatus.setText("Game Status: " + game.getGameState().toString());

        } catch (Exception e) { System.err.println(e); }
    }
}
