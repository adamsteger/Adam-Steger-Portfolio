package observer;

/**
 * An interface for different types of subscribers
 * @author Adam Steger
 */
public interface Observer {
    /**
     * Adds a book to the subject list
     * @param book The book that being added to the list
     */
    public void update(Book book);

    /**
     * Formats and outputs the subject list to the console
     */
    public void display();
}