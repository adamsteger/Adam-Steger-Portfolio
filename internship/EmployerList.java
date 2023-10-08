import java.util.ArrayList;
import java.util.UUID;

/**
 * A singleton for the list of employers
 * @author Byte Me
 */
public class EmployerList {
    private static EmployerList employerList;
    private ArrayList<Employer> employers;
    
    /**
     * Creates an instance of employer list by loading the JSON files
     */
    private EmployerList() {
        employers = DataLoader.getEmployers();
    }

    /**
     * Retrieves an employer list and ensures only 1 exists at a time 
     * @return Returns an instance of the employer list
     */
    public static EmployerList getInstance() {
        if(employerList == null) 
            employerList = new EmployerList();
        return employerList;
    }

    /**
     * Checks to see if an employer with the given username is in the list
     * @param username A string of the username that is being checked
     * @return Returns a boolean that is true if the list has an employer with the username and false if not
     */
    public boolean haveEmployer(String username) {
        for(Employer employer : employers) {
            if(employer.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Accesses the arraylist of employers
     * @return Returns the arraylist of employers
     */
    public ArrayList<Employer> getEmployers() {
        return employers;
    }

    /**
     * Searches the list of employers for a given title
     * @param title The name of the employer that is being searched for
     * @return Returns the employer that has the given title
     */
    public Employer getEmployerByTitle(String title) {
        for(Employer employer : employers) {
            if(employer.getTitle().contains(title)) {
                return employer;
            }
        }
        return null;
    }

    /**
     * Searches the list of employers for a given username
     * @param username A string of the username that is being searched for
     * @return Returns the employer with the given username
     */
    public Employer getEmployerByUser(String username) {
        for(Employer employer : employers) {
            if(employer.getUsername().contains(username)) {
                return employer;
            }
        }
        return null;
    }

    /**
     * Searches the list of employers for a given ID
     * @param id The UUID that is being searched for
     * @return Returns the employer that has the given UUID
     */
    public Employer getEmployerByID(UUID id) {
        for (Employer employer : employers) {
            if (employer.getUUID().equals(id)) {
                return employer;
            }
        }
        return null;
    }

    /**
     * Adds an employer to the lsit
     * @param employer The employer that is being added
     * @return Returns true if the employer is successfully added and false if not
     */
    public boolean addEmployer(Employer employer) {
        if(employer == null || haveEmployer(employer.getUsername()))
            return false;
        
        employers.add(employer);
        return true;
    }

    /**
     * Saves the employer list to the JSON file
     */
    public void save() {
        DataWriter.saveEmployers();
    }
}
