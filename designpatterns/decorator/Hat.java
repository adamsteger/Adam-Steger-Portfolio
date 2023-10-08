package decorator;

/**
 * A type of character decorator that adds a hat to the character
 * @author Adam Steger
 */
public class Hat extends CharacterDecorator{
    
    /**
     * Initializes the hat and populates its sections list
     * @param character The character that a hat is being added to
     */
    public Hat(Character character) {
        super(character);
    }

    /**
     * Edits the sections list to add a hat to the character
     */
    public void customize() {
        sections.set(0,"    ____");
        sections.set(1," __|____|____");
    }
}
