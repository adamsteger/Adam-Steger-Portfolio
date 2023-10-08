package decorator;

/**
 * A type of character decorator that adds a nose to a character
 * @author Adam Steger
 */
public class Nose extends CharacterDecorator{
    
    /**
     * Initializes the nose and populates its sections list
     * @param character
     */
    public Nose(Character character) {
        super(character);
    }

    /**
     * Edits the sections list to add a nose to the character
     */
    public void customize() {
        sections.set(4," |   >    | ");
    }
}
