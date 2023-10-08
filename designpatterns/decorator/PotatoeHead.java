package decorator;

/**
 * A Potato Head character
 * @author Adam Steger
 */
public class PotatoeHead extends Character{
    /**
     * Populates the sections list with strings to draw a potato head
     */
    public PotatoeHead() {
        super();
        this.sections.add(0,"");
        this.sections.add(1,"    ____    ");
        this.sections.add(2,"  /      \\ ");
        this.sections.add(3," |        | ");
        this.sections.add(4," |        | ");
        this.sections.add(5,"  \\      / ");
        this.sections.add(6,"   \\____/ ");
    }
}
