import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.UUID;

/**
 * A class of resume that each student has
 * @author Byte Me
 */
public class Resume {
    private ArrayList<Education> educations;
    private Hashtable<Skill, Boolean> skills;
    private Hashtable<String, Boolean> courses;
    private ArrayList<WorkExperience> work;
    private ArrayList<Extracurricular> extracurriculars;
    private ArrayList<Honor> honors;
    private UUID id;
    
    /**
     * Constructs a new instance of a resume for the UI
     * @param educations An arraylist of type education of all the educations the student has
     * @param skills A hashtable of skill and boolean that contains all of the skills the student 
     * has and a boolean to determine if they want the skill on their resume or not
     * @param courses A hashtable of string and boolean that contains all of the courses the student 
     * has taken and a boolean to determine if they want the course on their resume or not
     * @param work An arraylist of type work experience of all the work experiences the student has
     * @param extracurriculars An arraylist of type extracurriculars of all the extracurriculars the student has
     * @param honors An arraylist of type honor of all the honors the student has
     */
    public Resume(ArrayList<Education> educations, Hashtable<Skill, Boolean> skills, Hashtable<String, Boolean> courses, 
      ArrayList<WorkExperience> work, ArrayList<Extracurricular> extracurriculars, ArrayList<Honor> honors) {
        this.educations = educations;
        this.skills = skills;
        this.courses = courses;
        this.work = work;
        this.extracurriculars = extracurriculars;
        this.honors = honors;
        id = UUID.randomUUID();
    }

    /**
     * Constructs a new instance of a resume for DataLoader
     * @param id The unique UUID of the resume
     * @param educations An arraylist of type education of all the educations the student has
     * @param skills A hashtable of skill and boolean that contains all of the skills the student 
     * has and a boolean to determine if they want the skill on their resume or not
     * @param courses A hashtable of string and boolean that contains all of the courses the student 
     * has taken and a boolean to determine if they want the course on their resume or not
     * @param work An arraylist of type work experience of all the work experiences the student has
     * @param extracurriculars An arraylist of type extracurriculars of all the extracurriculars the student has
     * @param honors An arraylist of type honor of all the honors the student has
     */
    public Resume(UUID id, ArrayList<Education> educations, Hashtable<Skill, Boolean> skills, Hashtable<String, Boolean> courses, 
      ArrayList<WorkExperience> work, ArrayList<Extracurricular> extracurriculars, ArrayList<Honor> honors) {
        this.educations = educations;
        this.skills = skills;
        this.courses = courses;
        this.work = work;
        this.extracurriculars = extracurriculars;
        this.honors = honors;
        this.id = id;
    }

    /**
     * Default constructor for a resume
     */
    public Resume() {
        educations = new ArrayList<Education>();
        skills = new Hashtable<Skill, Boolean>();
        courses = new Hashtable<String, Boolean>();
        work = new ArrayList<WorkExperience>();
        extracurriculars = new ArrayList<Extracurricular>();
        honors = new ArrayList<Honor>();
        id = UUID.randomUUID();
    }

    /**
     * Accesses the educations of the resume
     * @return Returns the arraylist of type education of the student
     */
    public ArrayList<Education> getEducations() {
        return this.educations;
    }

    /**
     * Accesses the id of the resume
     * @return Returns the UUID of the resume
     */
    public UUID getID() {
        return id;
    }

    /**
     * Accesses the honors of the resume
     * @return Returns the arraylist of type honor of the resume
     */
    public ArrayList<Honor> getHonors() {
        return honors;
    }

    /**
     * Accesses the skills list of the resume
     * @return Returns a hashtable of skill and boolean of the resume
     */
    public Hashtable<Skill, Boolean> getSkills() {
        return this.skills;
    }

    /**
     * Accesses the course list of the resume
     * @return Returns a hashtable of string and boolean of the resume
     */
    public Hashtable<String, Boolean> getCourses() {
        return this.courses;
    }

    /**
     * Accesses the work experiences of the resume
     * @return Returns the arraylist of type work experience of the resume
     */
    public ArrayList<WorkExperience> getWork() {
        return this.work;
    }

    /**
     * Accesses the extracurriculars of the resume
     * @return Returns the arraylist of type extracurricular of the resume
     */
    public ArrayList<Extracurricular> getExtracurriculars() {
        return this.extracurriculars;
    }

    /**
     * Adds a new education to the educations list
     * @param edu The new education being added
     */
    public void addEducation(Education edu) {
        educations.add(edu);
    }

    /**
     * Adds a new honor to the honors list
     * @param honor The Honor that is being added
     */
    public void addHonor(Honor honor) {
        honors.add(honor);
    }

    /**
     * Removes an honor from the honors list
     * @param honor The honor that is being removed
     */
    public void removeHonor(Honor honor) {
        honors.remove(honor);
    }

    /**
     * Removes an education from the educations list
     * @param edu The education that is being removed
     */
    public void removeEducation(Education edu) {
        educations.remove(edu);
    }

    /**
     * Adds a new skill and its boolenan variable to the skills hashtable if it is not there already
     * @param skill The skill that is being added
     * @param bool A boolean that is true if the student wants the added skill on their current resume
     */
    public void addSkill(Skill skill, boolean bool) {
        List keys = new ArrayList(skills.keySet());
        if (!keys.contains(skill)) {
            skills.put(skill, bool);
        }
    }

    /**
     * Removes a skill from the skills hashtable
     * @param skill The skill that is being removed
     */
    public void removeSkill(Skill skill) {
        skills.remove(skill);
    }

    /**
     * Adds a new course and its boolean to the courses hashtable
     * @param course A string of the course being added
     * @param bool A boolean that is true if the student wants the added course on their current resume
     */
    public void addCourse(String course, boolean bool) {
        List keys = new ArrayList(courses.keySet());
        if (!keys.contains(course)) {
            courses.put(course, bool);
        }
        
    }

    /**
     * Removes a course from the courses hashtable
     * @param course A string of the course being removed
     */
    public void removeCourse(String course) {
        courses.remove(course);
    }

    /**
     * Adds a new work experience to the list of works
     * @param workExperience The work experience being added
     */
    public void addWork(WorkExperience workExperience) {
        work.add(workExperience);
    }

    /**
     * Removes a work experience from the list of works
     * @param workExperience The work experience being removed
     */
    public void removeWork(WorkExperience workExperience) {
        work.remove(workExperience);
    }

    /**
     * Adds a new extracurricular to the extracurriculars list
     * @param extracurricular The extracurricular being added
     */
    public void addExtracurricular(Extracurricular extracurricular) {
        extracurriculars.add(extracurricular);
    }

    /**
     * Removes an extracurricular from the extracurriculars list
     * @param extracurricular The extracurricular being removed
     */
    public void removeExtracurricular(Extracurricular extracurricular) {
        extracurriculars.remove(extracurricular);
    }
    
    /**
     * Converts the resume to a string
     * @return Returns a string representation of the resume
     */
    public String toString() {
        String ret = "\nEducations: ";
        for (Education edu : educations) {
            ret += edu + "\n";
        }
        ret += "\nSkills: ";
        Enumeration<Skill> skillsValues = skills.keys();
        while (skillsValues.hasMoreElements()) {
            ret += skillsValues.nextElement() + "\t";
        }
        ret += "\n\nCourses: ";
        Enumeration<String> coursesValues = courses.keys();
        while (coursesValues.hasMoreElements()) {
            ret += coursesValues.nextElement() + "\t";
        }
        ret += "\n\nWork Experiences: ";
        for (WorkExperience workExperience : work) {
            ret += workExperience + "\t";
        }
        ret += "\nExtracurriculars: ";
        for (Extracurricular extra : extracurriculars) {
            ret += extra + "\t";
        }
        ret += "\nHonors: ";
        for (Honor honor : honors) {
            ret += honor + "\t";
        }

        return ret;
    }
}
