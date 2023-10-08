import java.util.UUID;

/**
 * A class of a review on a student or employer
 * @author Byte Me
 */
public class Review {
    private String writer;
    private int rating;
    private String comment;

    /**
     * Creates a new review
     * @param writer A string of the person that wrote the review
     * @param rating An integer of the overall rating by the writer out of 5
     * @param comment A string of the comment on the review
     */
    public Review(String writer, int rating, String comment) {
        this.writer = writer;
        this.rating = rating;
        this.comment = comment;
    }

    /**
     * Accesses the writer of the review
     * @return Returns a string of the writer
     */
    public String getWriter() {
        return writer;
    }

    /**
     * Accesses the rating of the review
     * @return Returns an integer of the rating of the review
     */
    public int getRating() {
        return rating;
    }

    /**
     * Accesses the comment of the review
     * @return Returns a string of the comment of the review
     */
    public String getComment() {
        return comment;
    }

    /**
     * Converts the Review to a string 
     * @return Returns a string representation of the review
     */
    public String toString() {
        return "\n\tWriter: " + writer + "\n\tReview Rating: " + rating + "\n\tComment: " + comment;
    }
}
