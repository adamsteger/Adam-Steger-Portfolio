package state;

import java.util.Random;

/**
 * A class for a hard level of a math game
 * @author Adam Steger
 */
public class Hard implements State {
    private ArithemeticGame game;
    private Random rand = new Random();

    /**
     * Constructs a new hard state
     * @param game The arithemetic game that the state is a level of
     */
    public Hard(ArithemeticGame game) {
        this.game = game;
    }

    /**
     * Accesses a random number for the hard level
     * @return Returns a random integer between 1 and 100 inclusively
     */
    public int getNum() {
        return rand.nextInt(100)+1;
    }

    /**
     * Accesses a random operation (either +, -, *, or /)
     * @return Returns a string with the random operation
     */
    public String getOperation() {
        int randInt = rand.nextInt(4);
        String ret;
        if (randInt == 0) {
            ret = "+";
        } else if (randInt == 1) {
            ret = "-";
        } else if (randInt == 2) {
            ret = "*";
        } else {
            ret = "/";
        }
        return ret;
    }

    /**
     * Alerts the user they are doing well (cannot advance to higher state)
     */
    public void levelUp() {
        System.out.println("You are doing so well!!");
    }

    /**
     * Retreats to the medium level of the game and alerts the user
     */
    public void levelDown() {
        game.setState(game.getMediumState());
        System.out.println("You are struggling, let's go to medium mode");
    }
}
