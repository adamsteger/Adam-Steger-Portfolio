package state;

import java.util.Random;

/**
 * A class for a medium level of a math game
 * @author Adam Steger
 */
public class Medium implements State {
    private ArithemeticGame game;
    private Random rand = new Random();

    /**
     * Constructs a new medium state
     * @param game The arithemetic game that the state is a level of
     */
    public Medium(ArithemeticGame game) {
        this.game = game;
    }

    /**
     * Accesses a random number for the medium level
     * @returns Returns a random integer between 1 and 50 inclusively
     */
    public int getNum() {
        return rand.nextInt(50)+1;
    }

    /**
     * Accesses a random operation in the medium level (either +, -, or *)
     * @returns Returns a string with the random operation
     */
    public String getOperation() {
        int randInt = rand.nextInt(3);
        String ret;
        if (randInt == 0) {
            ret = "+";
        } else if (randInt == 1) {
            ret = "-";
        } else {
            ret = "*";
        }
        return ret;
    }

    /**
     * Advances to the hard level of the game and alerts the user
     */
    public void levelUp() {
        game.setState(game.getHardState());
        System.out.println("You've been advanced to the hardest mode.");
    }

    /**
     * Retreats to the easy level of the game and alerts the user
     */
    public void levelDown() {
        game.setState(game.getEasyState());
        System.out.println("You are struggling, let's go to easy mode.");
    }
}
