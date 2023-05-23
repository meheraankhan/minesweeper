package model;

/**
 * MSTile observer for the Minesweeper class.
 */
public interface MinesweeperObserver {
    /**
     * Called when the state of MSTile changes: Uncovered, flagged, revealed mine
     * 
     * @param loc The Location which changed.
     */
    void tileUpdated(Location loc);
}
