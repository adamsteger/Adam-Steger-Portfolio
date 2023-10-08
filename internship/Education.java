/**
 * A class for an education on a resume
 * @author Byte Me
 */
public class Education {
    private String schoolTitle;
    private String location;
    private String major;
    private int gradYear;
    private boolean resume;

    /**
     * Constructs a new instance of an education
     * @param schoolTitle A string of the title of school
     * @param location A string of the location of the education
     * @param major A string of the student's major for the education
     * @param gradYear An integer of when the student graduated from the school
     * @param resume A boolean that is true if the student wants this on their current resume and false if they do not want it
     */
    public Education(String schoolTitle, String location, String major, int gradYear, boolean resume) {
        this.schoolTitle = schoolTitle;
        this.location = location;
        this.major = major;
        this.gradYear = gradYear;
        this.resume = resume;
    }

    /**
     * Acceses the school title
     * @return Returns a string of the school title
     */
    public String getSchoolTitle() {
        return this.schoolTitle;
    }

    /**
     * Accesses the location of the education
     * @return Returns a string of the location
     */
    public String getLocation() {
        return this.location;
    }

    /**
     * Acceses the major of the education
     * @return Returns a string of the major of the student
     */
    public String getMajor() {
        return this.major;
    }

    /**
     * Accesses the graduation year of the student
     * @return Returns a string of the graduation year 
     */
    public int getGradYear() {
        return this.gradYear;
    }

    /**
     * Accesses the resume boolean of the education
     * @return Returns a boolean that represents whether the student wants the education on their resume
     */
    public Boolean getResume() {
        return this.resume;
    }

    /**
     * Converts the education to a string if it should be included on the resume
     * @return Returns a string representation of the education
     */
    public String toString() {
        if(resume)
            return "\t" + this.schoolTitle + ", " + this.location + "\n\t" + this.major + "\n\tExpected Graduation: " + this.gradYear + "\n";
        return "";
    }
}
