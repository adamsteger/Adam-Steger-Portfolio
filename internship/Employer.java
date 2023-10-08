import java.util.ArrayList;
import java.util.UUID;

/**
 * A class for an employer user
 * @author Byte Me
 */
public class Employer extends User {
    private String title;
    private String email;
    private double rating;
    private String location;
    private String mission;
    private ArrayList<InternshipPost> posts;
    private ArrayList<Review> reviews;
    private UUID id;

    /**
     * Default constructor for Employer
     * @param username A string of username of the employer
     * @param password A string of the password of the employer
     */
    public Employer(String username, String password) {
        super(username, password);
        title = "";
        email = "";
        rating = 0.0;
        location = "";
        mission = "";
        posts = new ArrayList<InternshipPost>();
        reviews = new ArrayList<Review>();
        id = super.getUUID();
    }

    /**
     * Constructor for employer with already defined UUID for DataLoader
     * @param id The unique UUID of the employer
     * @param title A string of the title of the employer
     * @param username A string of the username of the employer
     * @param password A string of the password of the employer
     * @param email A string of the email of the employer
     * @param rating A double of the rating of the employer (Averge of all ratings in reviews)
     * @param location A string of the location of the employer
     * @param mission A string of the mission statement of the employer
     * @param reviews An arraylist of reviews on the employer by students
     */
    public Employer(UUID id, String title, String username, String password, String email, double rating, String location, String mission, ArrayList<Review> reviews) {
        super(username, password);
        this.id = id;
        this.title = title;
        this.username = username;
        this.password = password;
        this.email = email;
        this.rating = rating;
        this.location = location;
        this.mission = mission;
        posts = new ArrayList<InternshipPost>();
        this.reviews = reviews;
    }

    /**
     * Constructor for employer for the UI
     * @param title A string of the title of the employer
     * @param username A string of the username of the employer
     * @param password A string of the password of the employer
     * @param email A string of the email of the employer
     * @param location A string of the location of the employer
     * @param mission A string of the mission statement of the employer
     */
    public Employer(String title, String username, String password, String email, String location, String mission) {
        super(username, password);
        this.title = title;
        this.username = username;
        this.password = password;
        this.email = email;
        rating = 5;
        this.location = location;
        this.mission = mission;
        reviews = new ArrayList<Review>();
        id = UUID.randomUUID();
    }

    /**
     * Accesses the email 
     * @return Returns a string of the email of Employer
     */
    public String getEmail() {
        return email;
    }

    /**
     * Accesses the rating
     * @return Returns a double of Employer's rating
     */
    public double getRating() {
        return rating;
    }

    /**
     * Accesses the location
     * @return String of Employer's location
     */
    public String getLocation() {
        return location;
    }

    /**
     * Accesses the mission
     * @return String of Employer mission statement
     */
    public String getMission() {
        return mission;
    }

    /**
     * Accesses the internship posts of the employer
     * @return Returns the ArrayList<InternshipPost> of Employer
     */
    public ArrayList<InternshipPost> getPosts() {
        return posts;
    }

    /**
     * Accesses the reviews
     * @return Returns the ArrayList<Review> of Reviews made about Employer
     */
    public ArrayList<Review> getReviews() {
        return reviews;
    }

    /**
     * Accesses the UUID of the employer
     * @return Returns the UUID of Employer
     */
    public UUID getUUID() {
        return id;
    }

    /**
     * Accesses the title of the employer
     * @return Returns a String of title of the Employer
     */
    public String getTitle() {
        return title;
    }

    /**
     * Converts the employer to a string
     * @return Returns a string representation of the employer
     */
    public String toString() {
        String ret = "\nTitle: " + title + "\nUsername: " + username + "\nPassword: " + password + "\nEmail: " + email + "\nRating: " +
                        rating + "\nLocation: " + location + "\nMission: " + mission + "\nReviews: ";
        for (Review review : reviews) {
            ret += review;
        }
        ret += "\nInternship Posts: ";
        for (InternshipPost post : posts) {
            ret += "\n\tPosition Title: " + post.getPosTitle() + "\tLocation: " + post.getLocation();
        }
        return ret;
    }

    /**
     * adds a post to the Employer's InternshipPosts
     * @param post The internship post that is being added
     */
    public void addPost(InternshipPost post) {
        posts.add(post);
    }
    
    /**
     * deletes a post from the employer's post
     * @param post The internship post that is being removed
     */
    public void deletePost(InternshipPost post) {
        posts.remove(post);
    }
}
