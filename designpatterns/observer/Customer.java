package observer;

import java.util.ArrayList;

/**
 * A type of Observer that tracks a specific subject of books
 * @author Adam Steger
 */
public class Customer implements Observer {
    private Subject subject;
    private String firstName;
    private String lastName;
    private ArrayList<Book> wishList;

    /**
     * Creates an instance of customer and registers it to the subscriber list
     * @param subject The subject of the books that are being tracked
     * @param firstName The first name of the customer
     * @param lastName The last name of the customer
     */
    public Customer(Subject subject, String firstName, String lastName) {
        this.subject = subject;
        this.firstName = firstName;
        this.lastName = lastName;
        wishList = new ArrayList<Book>();
        subject.registerObserver(this);
    }

    /**
     * Adds to the book to the wish list
     * @param book The book that is being added to the list
     */
    public void update(Book book) {
        wishList.add(book);
    }

    /**
     * Formats and outputs the wish list to the console
     */
    public void display() {
        System.out.println("\nWish List: ");
        for(Book book : wishList) {
            System.out.println(" - "+book.toString());
        }
    }
}