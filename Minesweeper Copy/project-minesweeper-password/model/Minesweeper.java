package model;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import backtracker.Backtracker;
import backtracker.Configuration;

import java.util.List;
import java.util.Map;

public class Minesweeper implements Configuration{
    private static final char TILE = ' ';
    private static final char MINE = 'M';
    private static final char HINTED = 'H';
    private static final char FLAGGED = 'F';
    private static final char COVERED = '-';
    private static final char DETONATED = 'X';
    private static final Random rng = new Random();

    private static final int[][] DELTAS = {{-1,1},{0,1},{1,1},{1,0},
                                            {1,-1},{0,-1},{-1,-1},{-1,0}};

    private List<Location> solveSelections;
    private MinesweeperObserver observer;
    private Map<Location, MSTile> board;
    private GameState state;
    private int moveCount;
    private int mineCount;
    private int rows;
    private int cols;

    /**
     * @param rows The number of rows.
     * @param cols The number of columns.
     * @param mineCount The number of mines on the board (cannot be greater than the # of available tiles).
     * @throws MinesweeperException If the mineCount exceeds the # of available tiles.
     */
    public Minesweeper(int rows, int cols, int mineCount) throws MinesweeperException {
        if (mineCount > rows * cols)
            throw new MinesweeperException("Cannot instantiate Minesweeper with more mines than available tiles.");
    
        if (rows <= 0 || cols <= 0)
            throw new MinesweeperException("Invalid number of rows or columns.");

        solveSelections = new ArrayList<>();
        state = GameState.IN_PROGRESS;
        this.mineCount = mineCount;
        this.rows = rows;
        this.cols = cols;
        moveCount = 0;

        makeBoard();
    }

    /**
     * Clone constructor.
     * @param template The Minesweeper to make a deep copy of.
     */
    public Minesweeper(Minesweeper template) {
        moveCount = template.moveCount;
        mineCount = template.mineCount;
        state = template.state;
        rows = template.rows;
        cols = template.cols;

        observer = null;
        board = new HashMap<>();
        for (Location loc : template.board.keySet())
            board.put(loc, new MSTile(template.board.get(loc)));

        solveSelections = new ArrayList<>();
        for (Location loc : template.solveSelections)
            solveSelections.add(new Location(loc.getRow(), loc.getCol()));
    }

    /**
     * Creates the playing board and randomly generates mines based on the mineCount.
     */
    private void makeBoard() {
        board = new HashMap<>();
        int i = 0; // Keeps track of added mines.

        for (int row = 0 ; row < rows ; row++) {
            for (int col = 0 ; col < cols ; col++) {
                Location loc = new Location(row, col);
                board.put(loc, new MSTile());
                notifyObserver(loc);
            }
        }
        
        while (i < mineCount) {
            // Randomly selects a location and attempts to set as a mine until the mineCount is hit:
            int r_row = rng.nextInt(rows);
            int r_col = rng.nextInt(cols);
            MSTile tile = board.get(new Location(r_row, r_col));

            if (!tile.isMine() && tile.isCovered()) {
                tile.setMine();
                i++;
            }
        }
    }

    /**
     * Initializes an observer.
     * @param obs The observer.
     */
    public void register(MinesweeperObserver obs) { observer = obs; }

    /**
     * Notifies the obsever that a (Location : MSTile) has changed states.
     */
    private void notifyObserver(Location loc) {
        if (observer != null)
            observer.tileUpdated(loc);
    }

    /**
     * @return The number of moves made.
     */
    public int getMoveCount() { return moveCount; }

    /**
     * @return The current state of the game.
     */
    public GameState getGameState() { return state; }

    /**
     * @return The number of mines on the board.
     */
    public int getMineCount() { return mineCount; }

    /**
     * @return The number of rows on the board.
     */
    public int getRows() { return rows; }

    /**
     * @return The number of columns on the board.
     */
    public int getCols() { return cols; }

    /**
     * @param loc The checked MSTile's Location.
     * @return True if the MSTile is covered, False otherwise.
     */
    public boolean isCovered(Location loc) throws MinesweeperException {
        if (!board.containsKey(loc))
            throw new MinesweeperException("Invalid location!");

        return board.get(loc).isCovered();
    }

    /**
     * Uncovers all tiles on the board.
     */
    public void uncoverBoard() {
        for (Location loc : board.keySet()) {
            board.get(loc).uncover();
            notifyObserver(loc);
        }
    }

    /**
     * Resets the Minesweeper game and randomizes mines.
     */
    public void reset() {
        state = GameState.IN_PROGRESS;
        moveCount = 0;
        makeBoard();
    }

    /**
     * Makes a random MSTile a hint and return its Location.
     * @return The Location corresponding to a Hinted MSTile.
     */
    public Location giveHint() {
        List<Location> locs = new ArrayList<>(getPossibleSelections());
        Collections.shuffle(locs); // Randomize hints.

        // Find a covered, safe, and un-hinted location:
        for (Location loc : locs) {
            MSTile tile = board.get(loc);

            if (!tile.isHinted()) {
                tile.setHint(true); // Make MSTile a hint.
                notifyObserver(loc);
                return loc;
            }
        } return null; // No Location was available to hint.
    }

    /**
     * @return A Collection of available move options.
     */
    public Collection<Location> getPossibleSelections() {
        Collection<Location> selections = new LinkedList<>();

        for (Location loc : board.keySet()) {
            MSTile tile = board.get(loc);

            // Tile is covered and safe:
            if (tile.getState() != TileState.UNCOVERED && !tile.isMine()) {
                selections.add(loc);
            }
        }

        return selections;
    }

    /**
     * @param loc The location selected for a move.
     * @throws MinesweeperException On an invalid move.
     */
    public void flag(Location loc) throws MinesweeperException {
        MSTile tile = board.get(loc);

        if (tile.getState() == TileState.UNCOVERED)
            throw new MinesweeperException("That location is already uncovered!");

        tile.toggleFlag();
        notifyObserver(loc);
    }

    /**
     * @param loc The location selected for a move.
     * @throws MinesweeperException On an invalid move.
     */
    public void makeSelection(Location loc) throws MinesweeperException {
        MSTile tile;

        if ((tile = board.get(loc)) != null && tile.getState() != TileState.UNCOVERED) {
            if (tile.isFlagged()) { // Unflag location if flagged BUT don't uncover.
                flag(loc);
                return;
            } 
            
            moveCount++;

            if (tile.isMine()) { // If our move was revealing a mine, we lost + detonation.
                state = GameState.LOST;
                tile.detonate();

            } else if (state != GameState.LOST && (moveCount >= (rows * cols) - mineCount)) // If our move has won the game.
                state = GameState.WON;

            if (state != GameState.IN_PROGRESS) // Win or lose, uncover the board.
                uncoverBoard();

        } else
            throw new MinesweeperException("Invalid move!");

        board.get(loc).uncover();
        clearSurrounding(loc);
        notifyObserver(loc);
    }

    /**
     * Recursively uncovers all adjacent tiles if no mines surround the given location.
     * @param loc The initial location (DOES NOT UNCOVER).
     */
    private void clearSurrounding(Location loc) throws MinesweeperException {
        if (getUncoveredState(loc) == TILE) {
            for (int[] delta : DELTAS) {
                Location d_loc = new Location(loc.getRow() + delta[0], loc.getCol() + delta[1]);
                MSTile tile;

                // If the location exists and hasn't been uncovered:
                // Uncover, notify, and potentially uncover its surrounding tiles.
                if ((tile = board.get(d_loc)) != null && tile.getState() != TileState.UNCOVERED) // TODO Figure out how to remove redundancy.
                    makeSelection(d_loc);
            }
        }
    }

    @Override
    public String toString() {
        String s = "";

        try {
            for (int row = 0 ; row < rows ; row++) {
                for (int col = 0 ; col < cols ; col++) {
                    Location loc = new Location(row, col);
                    s += getSymbol(loc) + " "; // Get symbol and add spacing
                }
                s += "\n";
            }
        } catch (MinesweeperException e) { /* squash */ }

        return s.substring(0, s.length()-1);
    }

    /**
     * @param loc The Location checked for a CLI symbol.
     * @return The CLI symbol at the supplied Location.
     */
    public char getSymbol(Location loc) throws MinesweeperException {
        if (!board.containsKey(loc))
            throw new MinesweeperException("Invalid location!");

        MSTile tile = board.get(loc);
        char c;

        if (tile.isCovered())
            c = COVERED;
        else if (tile.isFlagged())
            c = FLAGGED;
        else if (tile.isHinted())
            c = HINTED;
        else if (tile.getState() == TileState.DETONATED)
            c = DETONATED;
        else if (tile.isMine()) // Uncovered and unexploded mine check.
            c = MINE;
        else // Uncovered and not flagged check.
            c = getUncoveredState(loc);

        return c;
    }

    /**
     * @param loc The location being checked for adjacent uncovered/mine tiles.
     * @return A black space if the location is surrounded by uncovered tiles or the number of adjacent mines.
     */
    private char getUncoveredState(Location loc) {
        int row = loc.getRow();
        int col = loc.getCol();
        boolean isBlank = true;
        int mines = 0;
        
        for (int[] delta : DELTAS) {
            Location d_loc = new Location(row + delta[0], col + delta[1]);

            if (board.containsKey(d_loc)) {
                MSTile tile = board.get(d_loc);

                if (tile.isMine()) {
                    mines++;
                    isBlank = false;
                }
            }
        }

        if (isBlank) // If the location is surrounded entirely by safe tiles.
            return TILE;
        return (char)(mines + '0');
    }

    public List<Location> solve() {
        Backtracker backtracker = new Backtracker(false);
        Minesweeper solution = (Minesweeper)backtracker.solve(this);

        if (solution!=null && solution.solveSelections != null)
            return solution.solveSelections;
        return null;
    }

    @Override
    public Collection<Configuration> getSuccessors(){
        Collection<Configuration> successors = new ArrayList<>();
        for (Location loc : getPossibleSelections()) {
            Minesweeper newMS = new Minesweeper(this);
            try {
                newMS.makeSelection(loc);
                newMS.solveSelections.add(loc);

                successors.add(newMS);
            } catch (MinesweeperException e) {/* squash */}
        }
        return successors;
    }

    @Override
    public boolean isValid() {
        return state != GameState.LOST;
    }

    @Override
    public boolean isGoal() {
        return state == GameState.WON;
    }

    public static void main(String[] args) {
        try {
            Minesweeper ms = new Minesweeper(10, 10, 30);
            Minesweeper newMS = new Minesweeper(ms);

            System.out.println("Orinal Covered:\n" + ms);

            newMS.uncoverBoard();
            System.out.println("\nCopy Uncovered:\n" + newMS);
            System.out.println("\nOrinal After Copy Uncover:\n" + ms);

            ms.uncoverBoard();
            System.out.println("\nOriginal Uncovered:\n" + ms);

        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
