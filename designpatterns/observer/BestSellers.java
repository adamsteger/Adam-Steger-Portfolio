package observer;

import java.util.ArrayList;

/**
 * A type of subject that keeps of list of best selling books and people subscribed to this list
 * @author Adam Steger
 */
public class BestSellers implements Subject {
    private ArrayList<Observer> observers;
    private ArrayList<Book> bestSellers;

    /**
     * Initializes the list of books and subscribers
     */
    public BestSellers() {
        observers = new ArrayList<Observer>();
        bestSellers = new ArrayList<Book>();
    }

    /**
     * Adds an observer to the subscriber list so they get updated
     * @param observer The observer that is subscribing
     */
    public void registerObserver(Observer observer) {
        observers.add(observer);
    }

    /**
     * Removes an observer from the subscriber list
     * @param observer The observer that is getting removed
     */
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    /**
     * Notifies each observer when a new book is added to the list
     * @param book The book that was added to the list
     */
    public void notifyObservers(Book book) {
        for(Observer observer : observers) {
            observer.update(book);
        }
    }

    /**
     * Adds a new book to the list of best sellers and notifies the observers
     * @param book The book that is being added to the list
     */
    public void addBook(Book book) {
        bestSellers.add(book);
        this.notifyObservers(book);
    }
}