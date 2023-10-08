import java.util.ArrayList;
import java.util.UUID;
/**
 *  A singleton for the list of internship posts
 *  @author Byte Me 
 */
public class InternshipList {
    private static InternshipList internshipList;
    private ArrayList<InternshipPost> internships;

    /**
     * Creates an instance of internship list by loading the JSON file
     */
    private InternshipList() {
        internships = DataLoader.getInternshipPosts();
    }

    /**
     * Retrieves an instance of internship list and ensures only 1 exists at a time
     * @return Returns an instance of internship list
     */
    public static InternshipList getInstance() {
        if(internshipList == null)
            internshipList = new InternshipList();
        return internshipList;
    }

    /**
     * Searches the ArrayList of InternshipPosts for a specific post based on keywords
     * @param employerTitle A string of the employer title that is being searched for
     * @param posTitle A string of the position title that is being searched for
     * @return boolean if successful
     */
    public boolean haveInternshipPost(String employerTitle, String posTitle) {
        for(InternshipPost post : internships) {
            if(post.getEmployerTitle().equals(employerTitle) && post.getPosTitle().equals(posTitle)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Accesses the arraylist of internship posts
     * @return Returns the ArrayList of InternshipPosts
     */
    public ArrayList<InternshipPost> getInternships() {
        return internships;
    }

    /**
     * Searches ArrayList for a post by UUID
     * @param id The UUID that is being searched for
     * @returns Returns the InternshipPost of specified UUID
     */
    public InternshipPost getPostByID(UUID id) {
        for(InternshipPost post : internships) {
            if(post.getUUID().equals(id)) {
                return post;
            }
        }
        return null;
    }

    /**
     * Adds a passed internship to the ArrayList
     * @param InternshipPost The internship post that is being added
     * @return Returns a boolean that is true if the post is successfully added
     */
    public boolean addInternship(InternshipPost post) { 
        if(post == null || haveInternshipPost(post.getEmployerTitle(), post.getPosTitle())) 
            return false;
        
        internships.add(post);
        return true;
    }
    
    /**
     * Removes an internship from the ArrayList
     * @param InternshipPost The internship post that is being removed
     * @return Returns a boolean that is true if the post is successfully removed
     */
    public boolean removeInternship(InternshipPost internship) {
        for(InternshipPost post : internships) {
            if(internship.equals(post)) {
                internships.remove(post);
                DataWriter.saveInternshipPosts();
                return true;
            }
        }
        return false;   
    }

    /**
     * Saves internship posts with DataWriter
     */
    public void save() {
        DataWriter.saveInternshipPosts();
        // DataWriter.saveApplications();
    }
}