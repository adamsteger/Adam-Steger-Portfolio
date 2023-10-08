package decorator;

/**
 * A type of character that can add decorations to the character
 * @author Adam Steger
 */
public abstract class CharacterDecorator extends Character{
    protected Character character;

    /**
     * Copies the sections list from the existing character and calls customize
     * @param character The existing character that the decorator is adding to
     */
    public CharacterDecorator(Character character) {
        this.character = character;
        for(String string : character.sections) {
            this.sections.add(string);
        }
        customize();
    }

    /**
     * Edits the sections list to add a decoration
     */
    public abstract void customize();
}
