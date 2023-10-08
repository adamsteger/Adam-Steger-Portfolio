import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.io.*;

/**
 * Class for a user of type Student
 * @author Byte Me
 */
public class Student extends User {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    private int gradYear;
    private double gpa;
    private boolean showGPA;
    private double rating;
    private ArrayList<InternshipPost> favoritePosts;
    private ArrayList<Review> reviews;
    private UUID id;
    private Resume resume;

    /**
     * Creates a student with a new UUID for UI
     * @param firstName A string of the first name of the student
     * @param lastName A string of the last name of the student
     * @param username A string of the username of the student
     * @param password A string of the password of the student
     * @param gradYear An integer of the graduation year of the student
     * @param email A string of the email of the student
     * @param address A string of the address of the student 
     * @param phone A string of the phone number of the student
     * @param gpa A double of the gpa of the student
     * @param showGPA A boolean that is true if the student wants their GPA on their current gpa
     */
    public Student(String firstName, String lastName, String username, String password, int gradYear, String email, 
                    String address, String phone, double gpa, boolean showGPA) {
        super(username, password);
        this.id = UUID.randomUUID();
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.gradYear = gradYear;
        this.email = email;
        this.address = address;
        this.phone = phone;
        this.gpa = gpa;
        this.showGPA = showGPA;
        rating = -1;
        favoritePosts = new ArrayList<InternshipPost>();
        resume = new Resume();
        reviews = new ArrayList<Review>();
    }
    /**
     * Creates a student with an existing UUID for DataLoader
     * @param id The unique UUID of the student
     * @param firstName A string of the first name of the student
     * @param lastName A string of the last name of the student
     * @param username A string of the username of the student
     * @param password A string of the password of the student
     * @param gradYear An integer of the graduation year of the student
     * @param email A string of the email of the student
     * @param address A string of the address of the student 
     * @param phone A string of the phone number of the student
     * @param gpa A double of the gpa of the student
     * @param showGPA A boolean that is true if the student wants their GPA on their current gpa
     * @param rating A double of the average rating of the student based on their reviews
     * @param reviews An arraylist of reviews on the student
     * @param favPosts An arraylist of internship posts that are favorited by the student 
     * @param resume The resume of the student
     */
    public Student(UUID id, String firstName, String lastName, String username, String password, int gradYear,
                    String email, String address, String phone, double gpa, boolean showGPA, double rating, ArrayList<Review> reviews,
                    ArrayList<InternshipPost> favPosts, Resume resume) {
        super(username, password);
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.gradYear = gradYear;
        this.email = email;
        this.address = address;
        this.phone = phone;
        this.gpa = gpa;
        this.showGPA = showGPA;
        this.rating = rating;
        favoritePosts = favPosts;
        this.reviews = reviews;
        this.resume = resume;
    }
    /**
     * gets first name of student
     * @return String firstname
     */
    public String getFirstName() {
        return firstName;
    }
    /**
     * gets last name of student
     * @return String lastname
     */
    public String getLastName() {
        return lastName;
    }
    /**
     * gets username of student
     * @return String username
     */
    public String getUserName() {
        return username;
    }
    /**
     * gets password of student
     * @return String password
     */
    public String getPassword() {
        return password;
    }
    /**
     * gets grad year of student
     * @return int gradYear
     */
    public int getGradYear() {
        return gradYear;
    }
    /**
     * gets email of student
     * @return String email
     */
    public String getEmail() {
        return email;
    }
    /**
     * gets phone number of student
     * @return String phone
     */
    public String getPhone() {
        return phone;
    }
    /**
     * gets address of student
     * @return String address
     */
    public String getAddress() {
        return address;
    }
    /**
     * gets GPA of student
     * @return double gpa
     */
    public double getGPA() {
        return gpa;
    }
    /**
     * gets boolean if GPA is shown
     * @return boolean whether or not to show GPA
     */
    public boolean getShowGPA() {
        return showGPA;
    }
    /**
     * gets rating of student
     * @return double rating
     */
    public double getRating() {
        return rating;
    }
    /**
     * gets resume of student
     * @return Resume resume
     */
    public Resume getResume() {
        return resume;
    }
    /**
     * gets favorite posts of student
     * @return ArrayList of InternshipPosts
     */
    public ArrayList<InternshipPost> getFavoritePosts() {
        return favoritePosts;
    }
    /**
     * gets reviews of student
     * @return ArrayList of Reviews
     */
    public ArrayList<Review> getReviews() {
        return reviews;
    }
    /**
     * gets uuid of student
     * @return UUID id
     */
    public UUID getUUID() {
        return id;
    }
    /**
     * gets full name of student
     * @return String firstname + lastname
     */
    public String getName() {
        return firstName + " " + lastName;
    }
    /**
     * adds a favorite post to student's favorite posts
     * @param post
     */
    public void addFavoritePost(InternshipPost post) {
        favoritePosts.add(post);
    }

    public void apply(InternshipPost post) {
        post.getApplicants().add(this);
        InternshipList.getInstance().save();
        DataWriter.saveApplications();
    }

    /**
     * creates a string for all aspects of student class
     * @return String including all arguments passed and corresponding names
     */
    public String toString() {
        String ret = "\nName: " + firstName + " " + lastName + "\nUsername: " + username + "\nPassword: " 
                    + password + "\nGrad Year: " + gradYear + "\nEmail: " + email + "\nPhone: "
                    + phone + "\nGPA: " + gpa + "\nRating: " + rating + "\nReviews: ";
        for (Review review : reviews) {
            ret += review + "\n";
        }
        ret += "Favorite Posts: ";
        for (InternshipPost post : favoritePosts) {
            ret += "\n\tEmployer: " + post.getEmployerTitle() + "\n\tPosition Title: " + post.getPosTitle();
        }
        ret += "\nResume: " + resume;
        return ret;
    }

    /**
     * Converts the resume to a string and formats it
     * @return Returns a string representation of the resume
     */
    public String resumeToString() {
        String resume = "";
        String contactInfo = "\t\t\t" + this.getName() + "\n";
        contactInfo += "\tAddress: " + this.getAddress() + "\n";
        contactInfo += "\tPhone: " + this.getPhone() + "\n";
        contactInfo += "\tEmail: " + this.getEmail() + "\n";
        resume += contactInfo;

        String educations = "\nEDUCATIONS\n";
        for (Education edu : this.getResume().getEducations()) {
            educations += edu;
        }
        resume += educations;

        String honors = "\nHONORS\n";
        for (Honor honor : this.getResume().getHonors()) {
            honors += honor;
        }
        resume += honors;

        String experience = "\nWORK EXPERIENCE\n";
        for (WorkExperience work : this.getResume().getWork()) {
            experience += work + "\n";
        }
        resume += experience;

        String courses = "\nCOURSES\n";
        Set<String> courseKeys = new HashSet<String>();
        Boolean value = true;
        for(Map.Entry<String,Boolean> entry : this.getResume().getCourses().entrySet()) {
            if(value.equals(entry.getValue()))
                courseKeys.add(entry.getKey());
        }
        for(String course : courseKeys) {
            courses += "\t- " + course + "\n";
        }
        resume += courses;    

        String skills = "\nSKILLS\n";
        Set<Skill> skillKeys = new HashSet<Skill>();
        for(Map.Entry<Skill,Boolean> entry : this.getResume().getSkills().entrySet()) {
            if(value.equals(entry.getValue()))
                skillKeys.add(entry.getKey());
        }
        for(Skill skill : skillKeys) {
            skills += "\t- " + skill + "\n";
        }    
        resume += skills;

        String extras = "\nEXTRACURRICULARS\n";
        for (Extracurricular extra : this.getResume().getExtracurriculars()) {
            extras += extra;
        }
        resume += extras;

        return resume;
    }

    /**
     * Writes the resume to a text file using the resumeToString method
     */
    public void printResumeToFile() {
        try {
            FileWriter writer = new FileWriter(this.getFirstName() + this.getLastName() + "Resume.txt");
            writer.write(this.resumeToString());
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
