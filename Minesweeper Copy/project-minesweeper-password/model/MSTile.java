package model;

public class MSTile {
    private boolean mine;
    private TileState state;
    
    /**
     * Constructs MSTile.
     */
    public MSTile() {
        mine = false;
        state = TileState.COVERED;
    }

    public MSTile(MSTile template) {
        mine = template.mine;
        state = template.state;
    }

    /**
     * Sets the TileState of the MSTile to UNCOVERED.
     */
    public void uncover() {
        if (state != TileState.UNCOVERED && state != TileState.DETONATED)
            state = TileState.UNCOVERED;
    }

    /**
     * Toggles the flag state of the MSTile.
     */
    public void toggleFlag() {
        if (isCovered() || isFlagged())
            state = (state == TileState.FLAGGED) ? TileState.COVERED : TileState.FLAGGED;
        else {
            if (state == TileState.HINTED)
                System.err.println("That location is a hint!");
            else
                System.err.println("Invalid location for a flag!");
        }
    }

    /**
     * Sets the hint state of the MSTile.
     * @param isHint Whether or not to set the MSTile as a hint.
     */
    public void setHint(boolean isHint) {
        if (state != TileState.UNCOVERED && !isMine())
            state = (isHint) ? TileState.HINTED : TileState.COVERED;
    }

    /**
     * Detonate the MSTile if it is a mine.
     */
    public void detonate() {
        if (isMine())
            state = TileState.DETONATED;
    }

    /**
     * Makes MSTile a mine.
     */
    public void setMine() { mine = true; }

    /**
     * @return True if the MSTile is a mine, False otherwise.
     */
    public boolean isMine() { return mine; }

    /**
     * @return True if the MSTile is flagged, False otherwise.
     */
    public boolean isFlagged() { return state == TileState.FLAGGED; }

    /**
     * @return True if the MSTile is covered, False otherwise.
     */
    public boolean isCovered() { return state == TileState.COVERED; }

     /**
     * @return True if the MSTile is a hint, False otherwise.
     */
    public boolean isHinted() { return state == TileState.HINTED; }

    /**
     * @return The TileState of the MSTile: UN/COVERED or FLAGGED
     */
    public TileState getState() { return state; }
}
