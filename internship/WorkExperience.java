import java.util.ArrayList;

/**
 * A class for a work experience on a resume
 * @author Byte Me
 */
public class WorkExperience {
    private String posTitle;
    private String employerTitle;
    private String startDate;
    private String endDate;
    private boolean resume;
    private ArrayList<String> description;
    private String location;

    /**
     * Constructs a new instnace of a work experience
     * @param posTitle A string of the position title of the work experience
     * @param employerTitle A string of the title of the employer where the student worked
     * @param location A string of the location where the student worked
     * @param startDate A string of when the student began working at the position
     * @param endDate A string of when the student finished working at the position
     * @param resume A boolean that is true if the student wants the work experience on their current resume
     * @param description An arraylist of type string of bullets for a description of the position
     */
    public WorkExperience(String posTitle, String employerTitle, String location, String startDate, String endDate, boolean resume, ArrayList<String> description) {
        this.posTitle = posTitle;
        this.employerTitle = employerTitle;
        this.startDate = startDate;
        this.endDate = endDate;
        this.resume = resume;
        this.description = description;
        this.location = location;
    }

    /**
     * Accesses the location
     * @return Returns a string of the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * Accesses the description
     * @return Returns an Arraylist of strings of the description bullets
     */
    public ArrayList<String> getDescription() {
        return description;
    }

    /**
     * Accesses the position title
     * @return Returns a string of the position title
     */
    public String getPosTitle() {
        return posTitle;
    }

    /**
     * Accesses the employer title
     * @return Returns a string of the employer title
     */
    public String getEmployerTitle() {
        return employerTitle;
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
     * Accesses the resume boolean
     * @return Returns a boolean that is true if the student 
     * wants this work experience on their current resume
     */
    public boolean getResume() {
        return resume;
    }

    /**
     * Converts the work experience to a string if it should be included on the resume
     */
    public String toString() {
        String ret = "";
        if (resume) {
            ret += "\t" + employerTitle + ", " + location + "\n\t" + posTitle + "\n\t" + startDate + " - " + endDate + "\n";
            for (String string : description) {
                ret += "\t\t- " + string + "\n";
            }
        }
        
        return ret;
    }
}
