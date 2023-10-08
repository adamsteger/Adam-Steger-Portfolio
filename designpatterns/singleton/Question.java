package singleton;

/**
 * A class of a multiple choice question in a trivia game
 * @author Adam Steger
 */
public class Question {
    private String question;
    private String[] answers;
    private int correctAnswer;

    /**
     * Creates an instance of the question and populates its string array of answer choices
     * @param question A string of the question itself
     * @param ans1 A string of the first answer choice
     * @param ans2 A string of the second answer choice
     * @param ans3 A string of the third answer choice
     * @param ans4 A string of the fourth answer choice
     * @param correctAnswer An integer that denotes the index of which answer choice is correct in the array
     */
    public Question(String question, String ans1, String ans2, String ans3, String ans4, int correctAnswer) {
        this.question = question;
        answers = new String[4];
        answers[0] = ans1;
        answers[1] = ans2;
        answers[2] = ans3;
        answers[3] = ans4;
        this.correctAnswer = correctAnswer;
    }

    /**
     * Converts the question to a string and formats the answer choices
     * @return Returns a string representation of the question
     */
    public String toString() {
        String ret = question;
        for(int i = 0; i < answers.length; i++) {
            ret += "\n" + (i+1) + ". " + answers[i];
        }
        return ret;
    }

    /**
     * Checks if the user's answer to the question is correct
     * @param userAnswer An integer representing which answer choice the user picekd
     * @return Returns a boolean that is true if the answer is correct and false if the answer is incorrect
     */
    public boolean isCorrect(int userAnswer) {
        if(userAnswer-1 == correctAnswer) {
            return true;
        }
        return false;
    }

    /**
     * Accesses the correct answer choice of the question
     * @return Returns a string of the correct answer to the question
     */
    public String getCorrectAnswer() {
        return answers[correctAnswer];
    }
}
