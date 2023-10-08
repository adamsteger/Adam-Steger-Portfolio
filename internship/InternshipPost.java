import java.util.ArrayList;
import java.util.UUID;
/**
 *  A class of an internship posting by an employer
 *  @author Byte Me 
 */
public class InternshipPost {
    private String employerTitle;
    private String posTitle;
    private String description;
    private String location;
    private ArrayList<Skill> skillReq;
    private String startDate;
    private String endDate;
    private boolean isRemote;
    private boolean isOpen;
    private int lowPay;
    private int highPay;
    private ArrayList<Student> applicants;
    private UUID id;
    /**
     * Constructor for new InternshipPost for the UI
     * @param employerTitle A string of the employer title that made the post
     * @param posTitle A string of the position title of the post
     * @param description A string of a description of the position
     * @param location A string of the location where the position is
     * @param startDate A string of when the position begins
     * @param endDate A string of when the position ends
     * @param isRemote A boolean that is true if the position is virtual
     * @param isOpen A boolean that is true if the employer is still taking applications
     * @param lowPay An integer of the lowest a student could be paid for the position
     * @param highPay An integer of the highest a student could be paid for the position
     * @param skills An arraylist of type skill of all the skills required for the position
     */
    public InternshipPost(String employerTitle, String posTitle, String description, String location, ArrayList<Skill> skillReq, String startDate, String endDate, boolean isRemote, boolean isOpen, int lowPay, int highPay) {
        this.id = UUID.randomUUID();
        this.posTitle = posTitle;
        this.employerTitle = employerTitle;
        this.description = description;
        this.location = location;
        this.skillReq = skillReq;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isRemote = isRemote;
        this.isOpen = isOpen;
        this.lowPay = lowPay;
        this.highPay = highPay;
        applicants = new ArrayList<Student>();
    }
    /**
     * Constructor for InternshipPost that passes in existing UUID for DataLoader
     * @param id UUID that is unique to each post
     * @param employerTitle A string of the employer title that made the post
     * @param posTitle A string of the position title of the post
     * @param description A string of a description of the position
     * @param location A string of the location where the position is
     * @param startDate A string of when the position begins
     * @param endDate A string of when the position ends
     * @param isRemote A boolean that is true if the position is virtual
     * @param isOpen A boolean that is true if the employer is still taking applications
     * @param lowPay An integer of the lowest a student could be paid for the position
     * @param highPay An integer of the highest a student could be paid for the position
     * @param skills An arraylist of type skill of all the skills required for the position
     */
    public InternshipPost(UUID id, String employerTitle, String posTitle, String description, String location, String startDate, String endDate, boolean isRemote, boolean isOpen, int lowPay, int highPay, ArrayList<Skill> skills) {
        this.id = id;
        this.employerTitle = employerTitle;
        this.posTitle = posTitle;
        this.description = description;
        this.location = location;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isRemote = isRemote;
        this.isOpen = isOpen;
        this.lowPay = lowPay;
        this.highPay = highPay;
        this.skillReq = skills;
        applicants = new ArrayList<Student>();
    }
    /**
     * Accesses the employer title
     * @return Returns a string of the employer that has the position
     */
    public String getEmployerTitle() {
        return employerTitle;
    }
    /**
     * Accesses the position title
     * @return Returns a string of the position title
     */
    public String getPosTitle() {
        return posTitle;
    }
    /**
     * Accesses the description of the post
     * @return Returns a string of the description of the post
     */
    public String getDescription() {
        return description;
    }
    /**
     * Accesses the location
     * @return Returns a string of the location 
     */
    public String getLocation() {
        return location;
    }
    /**
     * Accesses the skill requirement 
     * @return Returns the arraylist of the skills needed for the position
     */
    public ArrayList<Skill> getSkillReq() {
        return skillReq;
    }
    /**
     * Accesses the start date
     * @return Returns a string of the start date
     */
    public String getStartDate() {
        return startDate;
    }
    /**
     * Accesses the end date 
     * @return Returns a string of the end date
     */
    public String getEndDate() {
        return endDate;
    }
    /**
     * Accesses the remote boolean
     * @return Returns a boolean that is true if the position is online or remote
     */
    public boolean getRemote() {
        return isRemote;
    }
    /**
     * Accesses the open boolean
     * @return Returns a boolean that is true if the post is still taking applications
     */
    public boolean getOpen() {
        return isOpen;
    }
    /**
     * Accesses the low pay variable
     * @return Returns an integer of the lowest a student would be paid for the position
     */
    public int getLowPay() {
        return lowPay;
    }
    /**
     * Accesses the high pay variable
     * @return Returns an integer of the high pay
     */
    public int getHighPay() {
        return highPay;
    }

    /**
     * Accesses the applicants of the post
     * @return Returns an arraylist of type student of all the applicants
     */
    public ArrayList<Student> getApplicants() {
        return applicants;
    }

    /**
     * Mutates the list of applicants
     * @param applicants the new arraylist of applicants
     */
    public void setApplicants(ArrayList<Student> applicants) {
        this.applicants = applicants;
    }

    /**
     * Adds a new student to the applicants if they have not already applied
     * @param applicant The student that is applying to the post
     */
    public void addApplicant(Student applicant) {
        if (!applicants.contains(applicant)) {
            applicants.add(applicant);
        }
    }

    /**
     * @return UUID
     */
    public UUID getUUID() {
        return id;
    }
    
    /**
     * Converts the post to a string
     * @return Returns a string with values of the post
     */
    public String toString() {
        String ret = "Employer: " + employerTitle + "\nPosition Title: " + posTitle + "\nDescription: " + description +
                        "\nLocation: " + location + "\nStart Date: " + startDate + "\nEnd Date: " + endDate + "\nRemote? " + 
                        isRemote + "\nOpen? " + isOpen + "\nPay: " + lowPay + "-" + highPay + "\nSkills Required: ";
        for (Skill skill : skillReq) {
            ret += skill + "    ";
        }
        ret += "\nApplicants: ";
        for (Student student : applicants) {
            ret += "\n\tName: " + student.getName() + "\tGraduation Year: " + student.getGradYear();
        }
        return ret;
    }

    /**
     * Converts the post to a string, but excludes the applicants for displaying posts for the student
     * @return Returns a string representation of the post without the applicants
     */
    public String toStringNoApplicants() {
        String ret = "\nEmployer: " + employerTitle + "\nPosition Title: " + posTitle + "\nDescription: " + description +
                        "\nLocation: " + location + "\nStart Date: " + startDate + "\nEnd Date: " + endDate + "\nRemote? " + 
                        isRemote + "\nOpen? " + isOpen + "\nPay: " + lowPay + "-" + highPay + "\nSkills Required: ";
        for (Skill skill : skillReq) {
            ret += skill + "\t";
        }
        return ret;
    }

}
