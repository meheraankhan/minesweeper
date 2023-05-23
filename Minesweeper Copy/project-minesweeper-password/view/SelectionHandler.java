package view;

import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import model.Location;
import model.Minesweeper;
import model.MinesweeperException;

public class SelectionHandler implements EventHandler<MouseEvent>{
    private Location loc;
    private Minesweeper game;

    public SelectionHandler(Location loc,Minesweeper game){
        this.loc=loc;
        this.game=game;
    }

    @Override
    public void handle(MouseEvent arg0) {
        MouseButton button = arg0.getButton();
        try {
            if (button == MouseButton.PRIMARY)
                game.makeSelection(loc);
            else if (button == MouseButton.SECONDARY)
                game.flag(loc);
        } catch (MinesweeperException me) { /* squash */ }
    }
} 