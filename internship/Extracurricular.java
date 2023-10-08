/**
 * A class for an extracurricular on a resume
 * @author Byte Me
 */
public class Extracurricular {
    private String title;
    private String position;
    private String startDate;
    private String endDate;
    private boolean resume;

    /**
     * Constructs a new instance of an extracurricular
     * @param title A string of the title of the extracurricular
     * @param position A string of the position held by the student in the extracurricular
     * @param startDate A string of the start date of the extracurricular
     * @param endDate A string of when the extracurricular ended
     * @param resume A boolean that is true if the student wants this on their current resume
     */
    public Extracurricular(String title, String position, String startDate, String endDate, boolean resume) {
        this.title = title;
        this.position = position;
        this.startDate = startDate;
        this.endDate = endDate;
        this.resume = resume;
    }

    /**
     * Acceses the title
     * @return Returns a string of the title
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * Accesses the position
     * @return Returns a string of the position
     */
    public String getPosition() {
        return this.position;
    }

    /**
     * Accesses the start date
     * @return Returns a string of the start date
     */
    public String getStartDate() {
        return this.startDate;
    }

    /**
     * Accesses the end date 
     * @return Returns a string of the end date
     */
    public String getEndDate() {
        return this.endDate;
    }

    /**
     * Accesses the resume boolean
     * @return Returns a boolean that represents whether the student wants 
     * the extracurricular on their current resume
     */
    public boolean getResume() {
        return this.resume;
    }

    /**
     * Converts the extracurricular to a String
     * @return Returns a string representation of the education
     */
    public String toString() {
        if (resume)
            return "\t" + title + ", " + position + "\n\t" + startDate + " - " + endDate + "\n";
        return "";
    }
}
