package model.tests;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import org.junit.platform.commons.annotation.Testable;

import model.Location;
import model.Minesweeper;
import model.MinesweeperException;
import model.MinesweeperObserver;
import model.TileState;

@Testable

public class MinesweeperObserverTest {
    public class FakeObserver implements MinesweeperObserver {
        private Location location;

        @Override
        public void tileUpdated(Location loc) {
            this.location = loc;
        }
    }

    @Test
    public void testObserver() throws MinesweeperException {

        // setup
        Location location=new Location(1, 1);
        TileState expected=TileState.UNCOVERED;
        MinesweeperObserver observer = new FakeObserver();
        Minesweeper subject = new Minesweeper(1, 1, 0);
        subject.register(observer);

        // invoke
        subject.makeSelection(new Location(0, 0));

        // analyze
        assertEquals(location,location);
    }
}
