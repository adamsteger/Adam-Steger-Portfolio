/**
 * A class for a honor on a resume
 * @author Byte Me
 */
public class Honor {
    private String title;
    private String organization;
    private String description;
    private int year;
    private boolean resume;

    /**
     * Constructs a new instance of a honor
     * @param title A string of the title of the honor
     * @param organization A string of the organization from which the student received the honor
     * @param description A string of a description of the honor
     * @param year An integer of the year the student received the honor
     * @param resume A boolean that is true if the student wants this honor on their current resume
     */
    public Honor(String title, String organization, String description, int year, boolean resume) {
        this.title = title;
        this.organization = organization;
        this.description = description;
        this.year = year;
        this.resume = resume;
    }

    /**
     * Accesses the title 
     * @return Returns a string of the title
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * Accesses the organization
     * @return Returns a string of the organization
     */
    public String getOrganization() {
        return this.organization;
    }

    /**
     * Accesses the description of the honor
     * @return Returns a string of the description
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Accesses the year 
     * @return Returns an integer of the year
     */
    public int getYear() {
        return this.year;
    }

    /**
     * Accesses the resume boolean of the honor
     * @return Returns a boolean that represents whether the student wants the honor on their current resume
     */
    public Boolean getResume() {
        return this.resume;
    }

    /**
     * Converts the honor to a string
     * @return Returns a string representation of the honor
     */
    public String toString() {
        if (resume)
            return "\t" + this.title + ", " + this.year + "\n\t" + this.organization + "\n";
        return "";
    }
}
