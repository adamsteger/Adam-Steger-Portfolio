package state;

import java.util.Scanner;

/**
 * A class for a math game
 * @author Adam Steger
 */
public class ArithemeticGame {
    private State easyState;
    private State mediumState;
    private State hardState;
    private State state;
    private int score;
    private Scanner reader;

    /**
     * Creates an instance of the game and sets the default state
     */
    public ArithemeticGame() {
        easyState = new Easy(this);
        mediumState = new Medium(this);
        hardState = new Hard(this);
        state = easyState;
        reader = new Scanner(System.in);
    }

    /**
     * Displays a question from the current level and evaluates the user's answer
     */
    public void pressQuestionButton() {
        int num1 = state.getNum();
        int num2 = state.getNum();
        String operation = state.getOperation();

        int correctAnswer = -1000;
        if (operation == "+") {
            correctAnswer = num1 + num2;
        } else if (operation == "-") {
            correctAnswer = num1 - num2;
        } else if (operation == "*") {
            correctAnswer = num1 * num2;
        } else if (operation == "/") {
            correctAnswer = num1 / num2;
        }

        System.out.print(num1 + " " + operation + " " + num2 + ": ");
        int answer = reader.nextInt();
        reader.nextLine();

        if (answer == correctAnswer) {
            System.out.println("Correct");
            score++;
        } else {
            System.out.println("Incorrect");
            score--;
        }
        if (score >= 3) {
            state.levelUp();
            score = 0;
        } else if (score <= -3) {
            state.levelDown();
            score = 0;
        }
    }

    /**
     * Mutates the current state of the gamme
     * @param state The new state that is being changed to
     */
    public void setState(State state) {
        this.state = state;
    }

    /**
     * Accesses the easy level of the game
     * @return Returns the easy state of the game
     */
    public State getEasyState() {
        return easyState;
    }

    /**
     * Accesses the medium level of the game
     * @return Returns the medium state of the game
     */
    public State getMediumState() {
        return mediumState;
    }

    /**
     * Accesses the hard level of the game
     * @return Returns the hard state of the game
     */
    public State getHardState() {
        return hardState;
    }
}
