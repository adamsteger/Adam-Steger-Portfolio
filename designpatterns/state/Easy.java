package state;

import java.util.Random;

/**
 * A class for an easy level of the math game
 * @author Adam Steger
 */
public class Easy implements State {
    private ArithemeticGame game;
    private Random rand = new Random();

    /**
     * Constructs a new easy state
     * @param game The arithmetic game that the state is a level of
     */
    public Easy(ArithemeticGame game) {
        this.game = game;
    }

    /**
     * Accesses a random number for the easy level
     * @return Returns a random integer within the range of 1 and 10 inclusively
     */
    public int getNum() {
        return rand.nextInt(10)+1;
    }

    /**
     * Accesses a random operation (either + or -)
     * @return Returns a string with the random operation
     */
    public String getOperation() {
        int randInt = rand.nextInt(2);
        String ret;
        if (randInt == 0) {
            ret = "+";
        } else {
            ret = "-";
        }
        return ret;
    }

    /**
     * Advances to the next medium level of the game and alerts the user
     */
    public void levelUp() {
        game.setState(game.getMediumState());
        System.out.println("You've been advanced to medium mode");
    }

    /**
     * Alerts the user about their progress (cannot change to lower state)
     */
    public void levelDown() {
        System.out.println("You seem to be struggling, you may want to study");
    }
}
