package decorator;

import java.util.ArrayList;

/**
 * A character class that can draw itself
 * @author Adam Steger
 */
public abstract class Character {
    protected ArrayList<String> sections;

    /**
     * Initializes a character and the sections list 
     */
    public Character() {
        this.sections = new ArrayList<String>();
    }

    /**
     * Prints out each line of the sections list to the console to draw the character
     */
    public void draw() {
        for(String s : sections) {
            System.out.println(s);
        }
    }
}
