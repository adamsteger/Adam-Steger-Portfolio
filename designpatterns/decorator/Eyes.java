package decorator;

/**
 * A type of character decorator that adds eyes to the character
 * @author Adam Steger
 */
public class Eyes extends CharacterDecorator{
    
    /**
     * Initializes the eyes and populates its sections list
     * @param character The character that eyes are being added to
     */
    public Eyes(Character character) {
        super(character);
    }

    /**
     * Edits the sections list to add eyes
     */
    public void customize() {
        sections.set(3," |  o  o  | ");
    }
}
