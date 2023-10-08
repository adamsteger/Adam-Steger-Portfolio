import java.util.ArrayList;
import java.util.UUID;
/**
 *  A type of user that can add/delete posts and reviews
 *  @author Byte Me 
 */
public class Admin extends User {
    private static UUID id;

    /**
    *  Creates a new Admin without UUID for UI
    *  @param username A string of the username of the admin
    *  @param password A string of the password for the admin
    */
    public Admin(String username, String password) {
        super(username, password);
        id = super.getUUID();
    }

    /**
    *  Creates a new Admin from existing UUID for DataLoader
    *  @param username A string of the username of the admin
    *  @param password A string of the password of the admin
    *  @param id The UUID of the admin
    */
    public Admin(UUID id, String username, String password) {
        super(username, password);
        this.id = id;
    }

    /**
     *  @return UUID of Admin
     */
    public UUID getID() {
        return id;
    }

    /**
     * Deletes a post as Admin 
     * @param post The post that is being deleted
     */
    public void deletePost(InternshipPost post) {
        InternshipList internshipList = InternshipList.getInstance();
        for (InternshipPost internship : internshipList.getInternships()) {
            if (internship.equals(post)) {
                internshipList.removeInternship(internship);
            }
        }
        internshipList.save();
    }

    /**
     * Adds a post to the system as an Admin
     * @param employerTitle A string of the title of the employer that has the internship
     * @param posTitle A string of the position title of the internship being added
     * @param description A string of the description of the internship being added
     * @param location A string of the location of the internship being added
     * @param skillReq An arraylist of skills that are required for the internship being added
     * @param startDate A string of when the internship starts that is being added
     * @param endDate A string of when the internship ends that is being added
     * @param isRemote A boolean that is true if the internship is remote and false if it is in person
     * @param isOpen A boolean that is true if the internship being added is still accepting applications
     * @param lowPay An integer of the lowest pay an applicant could receive for the internship being added
     * @param highPay An integer of the highest pay an applicant could receive for the internship being added
     */
    public void addPost(String employerTitle, String posTitle, String description, String location, ArrayList<Skill> skillReq, String startDate, String endDate, boolean isRemote, boolean isOpen, int lowPay, int highPay) {
       InternshipList internshipList = InternshipList.getInstance();
       internshipList.addInternship(new InternshipPost(employerTitle, posTitle, description, location, skillReq, startDate, endDate, isRemote, isOpen, lowPay, highPay));
       internshipList.save();
    }

    /**
     * Deletes a review created by an Employer
     * @param student The student that has the review
     * @param review The review that needs to be deleted
     */
    public void deleteStudentReview(Student student, Review review) {
        student.getReviews().remove(review);
        DataWriter.saveStudents();
    }

    /**
     * Deletes a review created by a Student
     * @param employer The employer that has the review
     * @param review The review that needs to be deleted
     */
    public void deleteEmployerReview(Employer employer, Review review) {
        employer.getReviews().remove(review);
    }

    /**
     * Converts the admin to a String representation
     * @return Returns a string representation of an admin
     */
    public String toString() {
        return "\nUsername: " + username + "\nPassword: " + password;
    }

}
