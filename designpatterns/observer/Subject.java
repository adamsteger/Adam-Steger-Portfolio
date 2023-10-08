package observer;

/**
 * An interface for different types of book lists to be subscribed to
 * @author Adam Steger
 */
public interface Subject {

    /**
     * Adds an observer to the subscriber list
     * @param observer The observer that is being added to the list
     */
    public void registerObserver(Observer observer);

    /**
     * Removes an observer from the subscriber list
     * @param observer The observer that is being removed from the list
     */
    public void removeObserver(Observer observer);

    /**
     * Adds a new book to the subject list and notifies the observers
     * @param book The book that is being added to the list
     */
    public void notifyObservers(Book book);
}