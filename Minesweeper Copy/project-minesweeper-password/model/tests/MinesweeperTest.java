package model.tests;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import org.junit.platform.commons.annotation.Testable;

import model.GameState;
import model.Location;
import model.Minesweeper;
import model.MinesweeperException;

@Testable
public class MinesweeperTest {
    /**
     * test the monesweeper constructor
     * @throws MinesweeperException
     */
    @Test
    public void minesweeperConstructor() throws MinesweeperException{
        Minesweeper expected=new Minesweeper(10,10,30);
        assertEquals(10, expected.getRows());
        assertEquals(10, expected.getCols());
        assertEquals(30, expected.getMineCount());
        assertEquals(0, expected.getMoveCount());
    }
    
    /**
     * this is the test for the game state of minesweeper
     * @throws MinesweeperException
     */
    @Test
    public void getGameStateTest()throws MinesweeperException{
        Minesweeper minesweeper=new Minesweeper(1, 1, 1);
        Location loc=new Location(0,0);

        assertEquals(minesweeper.getGameState(), GameState.IN_PROGRESS);
        minesweeper.makeSelection(loc);
        assertEquals(minesweeper.getGameState(), GameState.LOST);

        minesweeper=new Minesweeper(1, 1, 0);

        assertEquals(minesweeper.getGameState(), GameState.IN_PROGRESS);
        minesweeper.makeSelection(loc);
        assertEquals(minesweeper.getGameState(), GameState.WON);
    }    

    /**
     * tests the minecount of minesweeper
     * @throws MinesweeperException
     */
    @Test
    public void getMineCountTest() throws MinesweeperException{
        Minesweeper minesweeper=new Minesweeper(2, 2, 1);
        assertEquals(1, minesweeper.getMineCount());
    }    

    /**
     * this is the test for get rows of the minesweeper
     * @throws MinesweeperException
     */
    @Test
    public void getRowsTest() throws MinesweeperException{
        Minesweeper minesweeper=new Minesweeper(2, 2, 1);
        assertEquals(2, minesweeper.getRows());
    }    

    /**
     * this is the test for get cols of the minesweeper
     * @throws MinesweeperException
     */
    @Test
    public void getColTest() throws MinesweeperException{
        Minesweeper minesweeper=new Minesweeper(2, 2, 1);
        assertEquals(2, minesweeper.getCols());
    }   

    /**
     * tests the make selection of the mine sweeper class
     */
    @Test
    public void makeSelectionTest() throws MinesweeperException{
        Minesweeper minesweeper=new Minesweeper(1, 1, 1);
        Location loc=new Location(0, 0);

        assertEquals(minesweeper.getMoveCount(), 0);
        minesweeper.makeSelection(loc);
        assertEquals(minesweeper.getMoveCount(), 1);

        try {
            loc=new Location(0, 0);
            minesweeper.makeSelection(loc);
        } catch (MinesweeperException me) {
           assert(true);
        }

        assertEquals(1,minesweeper.getMoveCount());
    }

    /**
     * tests the to string of the minesweeper class
     */
    @Test
    public void minesweeperToString()throws MinesweeperException{
        Minesweeper minesweeper=new Minesweeper(1, 1, 0);

        assertEquals("- ", minesweeper.toString());
        minesweeper.uncoverBoard();
        assertEquals("  ", minesweeper.toString());

        minesweeper=new Minesweeper(5, 5, 0);
        assertEquals("- - - - - \n- - - - - \n- - - - - \n- - - - - \n- - - - - ", minesweeper.toString());
        minesweeper.uncoverBoard();
        assertEquals("          \n          \n          \n          \n          ", minesweeper.toString());

    }    

    @Test
    public void resetTest() throws MinesweeperException{
        Minesweeper minesweeper=new Minesweeper(5, 5, 5);
        minesweeper.makeSelection(new Location(0, 0));

        minesweeper.reset();
        assertEquals(minesweeper.getMoveCount(), 0);
        assertEquals(minesweeper.getGameState(), GameState.IN_PROGRESS);

    }

    @Test
    public void giveHintTest() throws MinesweeperException{
        Minesweeper minesweeper=new Minesweeper(1, 1, 0);

        assertEquals(minesweeper.getPossibleSelections().contains(minesweeper.giveHint()), true);
    }


    
}