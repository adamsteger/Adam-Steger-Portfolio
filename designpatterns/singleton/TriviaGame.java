package singleton;

import java.util.*;

/**
 * A trivia game of multiple choice questions
 * @author Adam Steger
 */
public class TriviaGame {
    private static TriviaGame triviaGame;
    private int score;
    private Random rand;
    private Scanner reader;
    private ArrayList<Question> questions;

    /**
     * Creates a trivia game and populates the array of questions
     */
    private TriviaGame() {
        score = 0;
        rand = new Random();
        reader = new Scanner(System.in);
        DataLoader loader = new DataLoader();
        questions = loader.getTriviaQuestions();
    }

    /**
     * Accesses a single instance of trivia game and ensures there is only one
     * @return Returns an instance of a trivia game
     */
    public static TriviaGame getInstance() {
        if(triviaGame == null) {
            triviaGame = new TriviaGame();
        }
        return triviaGame;
    }

    /**
     * Runs until the user quits the game. Keeps the game continually running round after round.
     */
    public void play() {
        boolean play = true;
        while(play) {
            System.out.print("(P)lay or (Q)uit: ");
            char input = reader.next().charAt(0);
            reader.nextLine();
            if(input == 'q' || input == 'Q') {
                System.out.println("You won " + score + " games.");
                System.out.println("Goodbye");
                play = false;
            } else if(input == 'p' || input == 'P') {
                System.out.println();
                playRound();
            } else {
                System.out.println("Invalid input.");
            }
        }
    }

    /**
     * Displays a random question to the user, checks for valid input, and then displays whether the answer is correct or incorrect.
     * @return Returns a boolean that is true if the user got the question right and false if the user got the question wrong.
     */
    private boolean playRound() {
        int questionNum = rand.nextInt(10);
        Question question = questions.get(questionNum);
        System.out.println(question);
        System.out.print("\nEnter Answer: ");
        int answer = reader.nextInt();
        reader.nextLine();
        if(isValid(answer)) {
            if(question.isCorrect(answer)) {
                System.out.println("YAY! You got it right!");
                score++;
                return true;
            } else if(!question.isCorrect(answer)){
                System.out.println("You got it wrong.\nThe correct answer is " + question.getCorrectAnswer());
                return false;
            }
        } else {
            System.out.println("Invalid input.");
        }
        return false;
    }

    /**
     * Checks to see if the user's answer to a question is valid
     * @param input An integer that is the user's answer
     * @return Returns a boolean that is true if the input is valid and false if the input is invalid
     */
    private boolean isValid(int input) {
        if(input == 1 || input == 2 || input == 3 || input == 4) {
            return true;
        }
        return false;
    }
}
