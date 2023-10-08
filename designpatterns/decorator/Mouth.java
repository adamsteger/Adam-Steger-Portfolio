package decorator;

/**
 * A type of character decorator that adds a mouth to a character
 * @author Adam Steger
 */
public class Mouth extends CharacterDecorator{
    
    /**
     * Initializes the mouth and populates its sections list
     * @param character The character that a mouth is being added to
     */
    public Mouth(Character character) {
        super(character);
    }

    /**
     * Edits the sections list to add a mouth to the character
     */
    public void customize() {
        sections.set(5,"  \\ ---- / ");
    }
}
