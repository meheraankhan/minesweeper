package model.tests; 

import org.junit.Test;
import org.junit.platform.commons.annotation.Testable;

import model.MSTile;
import model.TileState;

import static org.junit.Assert.assertEquals;

@Testable
public class MSTileTest {

    @Test
    public void MsTileTest(){
        MSTile tile = new MSTile();
        TileState expectedState = TileState.COVERED;
        boolean mineState = false;
        assertEquals(tile.getState(), expectedState);
        assertEquals(tile.isMine(), mineState);
    }

    @Test
    public void uncoverTest(){
        MSTile tile = new MSTile();
        tile.uncover();
        TileState expected = TileState.UNCOVERED;
        assert(expected == tile.getState());
    }
    
    @Test
    public void isflaggedTest(){
        MSTile tile = new MSTile();
        boolean expected = false;
        assertEquals(expected, tile.isFlagged());
    }

    @Test
    public void toggleFlagTest(){
        MSTile tile = new MSTile();
        tile.toggleFlag();
        TileState expected = TileState.FLAGGED;
        assert(expected == tile.getState());
    }

    @Test
    public void setMineTest(){
        MSTile tile = new MSTile();
        boolean expected = true;
        tile.setMine();
        assertEquals(expected, tile.isMine());
    }

    @Test
    public void isMineTest(){
        MSTile tile = new MSTile();
        boolean expected = false;
        assertEquals(expected, tile.isMine());
    }

    @Test
    public void isCoveredTest(){
        MSTile tile = new MSTile();
        boolean expected = true;
        assertEquals(expected, tile.isCovered());
    }

    @Test
    public void getStateTest(){
        MSTile tile = new MSTile();
        TileState expected = TileState.COVERED;
        assert(expected == tile.getState());
    }
}