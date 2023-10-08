import java.util.ArrayList;
import java.util.UUID;

/**
 * A singleton for the list of resumes
 * @author Byte Me
 */
public class ResumeList {
    private static ResumeList resumeList;
    private ArrayList<Resume> resumes;

    /**
     * Creates an instance of the resume list by loading the JSON file
     */
    private ResumeList() {
        resumes = DataLoader.getResumes();
    }

    /**
     * Retrieves a resume list and ensures only 1 exists at a time
     * @return Returns an instance of the resume list
     */
    public static ResumeList getInstance() {
        if(resumeList == null)
            resumeList = new ResumeList();
        return resumeList;
    }

    /**
     * Accesses the arraylist of resumes
     * @return Returns the arraylist of all the resumes in the system
     */
    public ArrayList<Resume> getResumes() {
        return resumes;
    }

    /**
     * Searches the list of resumes for a given id
     * @param id The UUID tha is being searched for
     * @return Returns the resume with the given UUID
     */
    public Resume getResumeByID(UUID id) {
        for(Resume resume : resumes) {
            if(resume.getID().equals(id)) {
                return resume;
            }
        }
        return null;
    }

    public boolean addResume(Resume resume) {
        if (resume == null)
            return false;
        resumes.add(resume);
        this.save();
        return true;
    }

    /**
     * Saves the resume list to the JSON file
     */
    public void save() {
        DataWriter.saveResumes();
    }
}