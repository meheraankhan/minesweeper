package view;

import java.util.List;
import java.util.Scanner;

import model.GameState;
import model.Location;
import model.Minesweeper;
import model.MinesweeperException;

public class Game {
    public static void main(String[] args) throws MinesweeperException {
        Minesweeper game = new Minesweeper(10, 10, 30);
        System.out.println("Mine count: " + game.getMineCount());
        String commands = "Commands: \n\t help: this help message \n\t pick <row> <col>: uncovers cell at row, col \n\t hint: displays a safe selection \n\t reset: resets to a new game \n\t quit: quits the game";
        System.out.println(commands);
        System.out.println("\n\n");
        Scanner scanner = new Scanner(System.in);
        while (game.getGameState() == GameState.IN_PROGRESS) {

            System.out.println();
            System.out.println(game);
            System.out.println("Moves: " + game.getMoveCount());
            System.out.println("Enter a command: ");

            String play = scanner.nextLine();
            if (play.equals("help")) {
                System.out.println(commands);
            }
            else if(play.equals("hint")){
                Location loc = game.giveHint();
                System.out.println(loc);
            }
            else if(play.equals("quit")||play.equals("")){
                break;
            }
            else if(play.equals("reset")) {
                game = new Minesweeper(10, 10, 30);
                continue;
            } 
            else if(play.equals("solve")) {
                List<Location> solevSelections = game.solve();
                for (Location loc : solevSelections) {
                    game.makeSelection(loc);
                    System.out.println("\nSelected: (" + loc + ")\n" + game);
                } break;
            }
            else {
            
                String[] tokens = play.split(" ");

                try {
                    String kw = tokens[0];
                    int row = Integer.valueOf(tokens[1]);
                    int col = Integer.valueOf(tokens[2]);

                    if (kw.equals("flag"))
                        game.flag(new Location(row, col));

                    if (kw.equals("pick"))
                        game.makeSelection(new Location(row, col));

                } catch (Exception e) { // Catches invalid commands OR MinesweeperExceptions and prints to console:
                    if ((e instanceof MinesweeperException) == false)
                        System.err.println("Invalid command!");
                }
            }
        }

        scanner.close();
        if (game.getGameState() == GameState.LOST){
            System.out.println("\nSorry you took the L mate");
            System.out.println(game);
        }
        if (game.getGameState() == GameState.WON) {
            System.out.println("\nYou won, big W");
            System.out.println(game);
        }

    }
}
