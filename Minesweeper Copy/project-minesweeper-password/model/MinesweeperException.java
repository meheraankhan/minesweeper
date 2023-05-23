package model;

public class MinesweeperException extends Exception {
    public MinesweeperException(String message) {
        System.err.println(message);
    }
}
