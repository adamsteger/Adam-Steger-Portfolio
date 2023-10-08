package observer;

/**
 * A book that can be added to a subject list
 * @author Adam Steger
 */
public class Book {
    private String title;
    private String authorFirstName;
    private String authorLastName;

    /**
     * Creates an instance of a book
     * @param title A string that represents the title of the book
     * @param authorFirstName A string that represents the author's first name
     * @param authorLastName A string that represents the author's last name
     */
    public Book(String title, String authorFirstName, String authorLastName) {
        this.title = title;
        this.authorFirstName = authorFirstName;
        this.authorLastName = authorLastName;
    }

    /**
     * Accesses the title of the book
     * @return Returns a string that is the title of the book
     */
    public String getTitle() {
        return title;
    }

    /**
     * Accesses the author's first name
     * @return Returns a string that is the author's first name
     */
    public String getAuthorFirstName() {
        return authorFirstName;
    }

    /**
     * Accesses the author's last name
     * @return Returns a string that is the author's last name
     */
    public String getAuthorLastName() {
        return authorLastName;
    }

    /**
     * Converts the book into a String that can be displayed to the console
     * @return Returns a string representation of a book
     */
    public String toString() {
        return (title+" by: "+authorFirstName+" "+authorLastName);
    }
}