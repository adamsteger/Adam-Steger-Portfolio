package observer;

import java.util.LinkedList;
import java.util.Queue;

/**
 * A type of observer that keeps track of the top 5 best selling books
 * @author Adam Steger
 */
public class Store implements Observer {
    private Subject subject;
    private String title;
    private Queue<Book> bestSellers;

    /**
     * Creates an instance of store and registers it to the list of observers
     * @param subject
     */
    public Store(Subject subject) {
        this.subject = subject;
        bestSellers = new LinkedList<Book>();
        subject.registerObserver(this);
    }

    /**
     * Adds a book to the subject list
     * @param book The book that being added to the list
     */
    public void update(Book book) {
        if(bestSellers.size() < 5) {
            bestSellers.add(book);
        }
        else if(bestSellers.size() >= 5){
            bestSellers.remove();
            bestSellers.add(book);
        }
    }

    /**
     * Formats and outputs the subject list to the console
     */
    public void display() {
        System.out.println("Top 5 Best Sellers: ");
        for(Book book : bestSellers) {
            System.out.println(" - "+book.toString());
        }
    }
}