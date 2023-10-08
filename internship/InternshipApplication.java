import java.util.ArrayList;
import java.util.UUID;
/**
 * A class for an internship application that is used by the UI
 * @author Byte Me
 */
public class InternshipApplication {
    private InternshipList internshipList;
    private StudentList studentList;
    private Student student;
    private EmployerList employerList;
    private Employer employer;
    private AdminList adminList;
    private Admin admin;
    /**
     * Default constructor that loads the lists from the JSON files
     */
    public InternshipApplication() {
        employerList = EmployerList.getInstance();
        internshipList = InternshipList.getInstance();
        studentList = StudentList.getInstance();
        adminList = AdminList.getInstance();
        DataLoader.getApplicants();
    }

    /**
     * Adds a student to the student list
     * @param student The student that is being added
     * @return Returns a boolean that is true if the student is successfully added
     */
    public boolean createStudent(Student student) {
        this.student = student;
        return studentList.addStudent(student);
    }

    /**
     * Adds an employer to the EmployerList
     * @param employer The employer that is being added
     * @return Returns a boolean that is true if the employer is successfully added
     */
    public boolean createEmployer(Employer employer) {
        this.employer = employer;
        return employerList.addEmployer(employer);
    }

    /**
     * Adds an admin to the admin list
     * @param admin The admin that is being added
     * @return Returns a boolean that is true if the admin is successfully added
     */
    public boolean createAdmin(Admin admin) {
        this.admin = admin;
        return adminList.addAdmin(admin);
    }

    /**
     * Checks to make sure a student with the given username exists then signs the student in
     * @param username A string of the username of the student
     * @param password A string of the password of the student
     * @return Returns the student with the given username and password
     */
    public Student studentLogin(String username, String password) {
        if (!studentList.haveStudent(username))
            return null;

        Student studentCheck = studentList.getStudentByUser(username);
        if (studentCheck.getPassword().equals(password)) {
            student = studentCheck;
            return student;
        }
        return null;
    }

    /**
     * Checks the users to see if a given username already exists
     * @param username A string of the username being checked
     * @return Returns a boolean that is true if the username does not already exist
     */
    public boolean validNewUsername(String username) {
        if (!studentList.haveStudent(username) || !employerList.haveEmployer(username) || !adminList.haveAdmin(username)) {
            return true;
        }
        return false;
    }

    /**
     * Checks to make sure an employer with the given username exists then signs the employer in if the password is correct
     * @param username A string of the username of the employer
     * @param password A string of the password of the employer
     * @return Returns the student with the given username and password if found and null if not
     */
    public Employer employerLogin(String username, String password) {
        if (!employerList.haveEmployer(username))
            return null;

        Employer employerCheck = employerList.getEmployerByUser(username);
        if (employerCheck.getPassword().equals(password)) {
            employer = employerCheck;
            return employer;
        }
        return null;
    }

    /**
     * Checks to make sure an admin with the given username exists then signs the admin in if the password is correct
     * @param username A string of the username of the admin
     * @param password A string of the password of the admin
     * @return Returns the admin with the given username and password if found and null if not
     */
    public Admin adminLogin(String username, String password) {
        if (!adminList.haveAdmin(username))
            return null;

        Admin adminCheck = adminList.getAdminByUser(username);
        if (adminCheck.getPassword().equals(password)) {
            admin = adminCheck;
            return admin;
        }
        return null;
    }

    /**
     * Returns the internship posts of an employer in a search
     * @param employer The employer that is being searched for
     * @return ArrayList of InternshipPosts by the employer
     */
    public ArrayList<InternshipPost> getInternships(Employer employer) {
        ArrayList<InternshipPost> ret = new ArrayList<InternshipPost>();
        for(int i = 0; i < internshipList.getInternships().size(); i++) {
            InternshipPost currentPost = internshipList.getInternships().get(i);
            if(currentPost.getEmployerTitle().equals(employer.getTitle())) {
                ret.add(currentPost);
            }
        }
        return ret;
    }

    /**
     * Accesses the list of internships
     * @return Returns the ArrayList of all internship posts
     */
    public ArrayList<InternshipPost> getInternships() {
        ArrayList<InternshipPost> ret = new ArrayList<InternshipPost>();
        for(int i = 0; i < internshipList.getInternships().size(); i++) {
            ret.add(internshipList.getInternships().get(i));
        }
        return ret;
    }

    /**
     * Accesses the applicants of an InternshipPost
     * @param post The internship post with the list of applicants
     * @return Returns the ArrayList of Students that are the applicants
     */
    public ArrayList<Student> getApplicants(InternshipPost post) {
        return post.getApplicants();
    }

    /**
     * Searches the list of internships for a given employer and position
     * @param employerTitle A string of the employer title being searched for
     * @param posTitle A string of the position title being searched for
     * @return Returns a boolean that is true if an internship post exists with the given employer title and position title
     */
    public boolean findInternship(String employerTitle, String posTitle) {
        return internshipList.haveInternshipPost(employerTitle, posTitle);
    }

    /**
     * Removes an internship from the InternshipList
     * @param post The internship post being removed
     */
    public void removeInternship(InternshipPost post) {
        InternshipList.getInstance().removeInternship(post);
        DataWriter.saveInternshipPosts();
    }

    /**
     * Filters internships by pay
     * @param pay
     * @return array of corresponding InternshipPosts
     */
    public ArrayList<InternshipPost> filterByPay(int pay) {
        ArrayList<InternshipPost> retList = new ArrayList<InternshipPost>();
        for (int i = 0; i < internshipList.getInternships().size(); i++) {
            InternshipPost currentPost = internshipList.getInternships().get(i);
            if (currentPost.getLowPay() >= pay && currentPost.getHighPay() <= pay)
                retList.add(currentPost);
        }
        return retList;
    }

    /**
     * Filters internships by location
     * @param location
     * @return ArrayList of corresponding InternshipPosts
     */
    public ArrayList<InternshipPost> filterByLocation(String location) {
        ArrayList<InternshipPost> retList = new ArrayList<InternshipPost>();
        for (int i = 0; i < internshipList.getInternships().size(); i++) {
            InternshipPost currentPost = internshipList.getInternships().get(i);
            if (currentPost.getLocation().contains(location))
                retList.add(currentPost);
        }
        return retList;
    }

    /**
     * Filters internships by skill requirement
     * @param skill The skill that is being searched for 
     * @return Returns an ArrayList of InternshipPosts that have the given skill as a requirement
     */
    public ArrayList<InternshipPost> filterBySkill(Skill skill) {
        ArrayList<InternshipPost> retList = new ArrayList<InternshipPost>();
        for (int i = 0; i < internshipList.getInternships().size(); i++) {
            InternshipPost currentPost = internshipList.getInternships().get(i);
            for (int j = 0; j < currentPost.getSkillReq().size(); j++) {
                if (currentPost.getSkillReq().get(j).equals(skill))
                    retList.add(currentPost);
            }
        }
        return retList;
    }

    /**
     * filters by whether or not an internship is remote
     * @param isRemote The boolean that is being searched for; true if the post is virtual
     * @return Returns a corresponding ArrayList of InternshipPosts that are remote 
     */
    public ArrayList<InternshipPost> filterByRemote(boolean isRemote) {
        ArrayList<InternshipPost> retList = new ArrayList<InternshipPost>();
        for (int i = 0; i < internshipList.getInternships().size(); i++) {
            InternshipPost currentPost = internshipList.getInternships().get(i);
            if (currentPost.getRemote() == isRemote)
                retList.add(currentPost);
        }
        return retList;
    }
    /**
     * filters by position at internship
     * @param keyword A string of the position title keyword that is being searched for
     * @return Returns an ArrayList of corresponding InternshipPosts that have the given keyword
     */
    public ArrayList<InternshipPost> filterByPosTitle(String keyword) {
        ArrayList<InternshipPost> retList = new ArrayList<InternshipPost>();
        for (int i = 0; i < internshipList.getInternships().size(); i++) {
            InternshipPost currentPost = internshipList.getInternships().get(i);
            if (currentPost.getPosTitle().contains(keyword)) {
                retList.add(currentPost);
            }
        }
        return retList;
    }
    /**
     * filters internship posts by employer title
     * @param keyword A string of the employer keyword that is being searched for
     * @return Returns an ArrayList of corresponding InternshipPosts that match the keyword in their employer title
     */
    public ArrayList<InternshipPost> filterByEmployerTitle(String keyword) {
        ArrayList<InternshipPost> retList = new ArrayList<InternshipPost>();
        for (int i = 0; i < internshipList.getInternships().size(); i++) {
            InternshipPost currentPost = internshipList.getInternships().get(i);
            if (currentPost.getEmployerTitle().contains(keyword)) {
                retList.add(currentPost);
            }
        }
        return retList;
    }
    
    /**
     * Adds an internship to the InternshipList using parameters
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
     * @return Returns a boolena that is true if the post was successfully added
     */
    public boolean addInternship(String employerTitle, String posTitle, String description, String location,
            ArrayList<Skill> skillReq, String startDate, String endDate, boolean isRemote, boolean isOpen, int lowPay,
            int highPay) {
        if (internshipList.haveInternshipPost(employerTitle, posTitle)) {
            return false;
        }
        internshipList.addInternship(new InternshipPost(employerTitle, posTitle, description, location, skillReq, startDate, endDate,
                isRemote, isOpen, lowPay, highPay));
        internshipList.save();
        return true;
    }
    /**
     * Adds a work experience to resume
     * @param work The work experience being added
     */
    public void addWorkExperience(WorkExperience work) {
        student.getResume().addWork(work);
        DataWriter.saveResumes();
    }
    /**
     * Removes a work experience from resume
     * @param work The work experience being removed
     */
    public void removeWorkExperience(WorkExperience work) {
        student.getResume().removeWork(work);
        DataWriter.saveResumes();
    }
    /**
     * add education to resume
     * @param edu The education being added
     */
    public void addEducation(Education edu) {
        student.getResume().addEducation(edu);
        DataWriter.saveResumes();
    }
    /**
     * remove education from resume
     * @param edu The education being removed
     */
    public void removeEducation(Education edu) {
        student.getResume().removeEducation(edu);
        DataWriter.saveResumes();
    }
    /**
     * add extracirricular to resume
     * @param extra The extracurricular being added
     */
    public void addExtracurricular(Extracurricular extra) {
        student.getResume().addExtracurricular(extra);
        DataWriter.saveResumes();
    }
    /**
     * remove extracirricular from resume
     * @param extra The extracurricular being removed
     */
    public void removeExtracurricular(Extracurricular extra) {
        student.getResume().removeExtracurricular(extra);
        DataWriter.saveResumes();
    }
    /**
     * add a skill to resume
     * @param skill The skill being added
     * @param resume A boolean that is true if the student wants the skill on their current resume
     */
    public void addSkill(Skill skill, boolean resume) {
        student.getResume().addSkill(skill, resume);
        DataWriter.saveResumes();
    }
    /**
     * Removes a skill from resume
     */
    public void removeSkill(Skill skill) {
        student.getResume().removeSkill(skill);
        DataWriter.saveResumes();
    }
    /**
     * Adds a course to resume
     * @param course A string of the course being added
     * @param resume A boolean that is true if the student wants the course on their current resume
     */
    public void addCourse(String course, boolean resume) {
        student.getResume().addCourse(course, resume);
        DataWriter.saveResumes();
    }
    /**
     * Removes a course from resume
     * @param course A string of the course being removed
     */
    public void removeCourse(String course) {
        student.getResume().removeCourse(course);
        DataWriter.saveResumes();
    }
    /**
     * Adds a honor to resume
     * @param honor The honor that is being added
     */
    public void addHonor(Honor honor) {
        student.getResume().addHonor(honor);
        DataWriter.saveResumes();
    }
    /**
     * Removes a honor from resume
     * @param honor The honor that is being removed
     */
    public void removeHonor(Honor honor) {
        student.getResume().removeHonor(honor);
        DataWriter.saveResumes();
    }

    /**
     * Adds an employer review to a student
     * @param student The student that the review is about
     * @param writer A string of the employer writer of the review
     * @param rating An integer of the rating by the employer
     * @param comment A string of the comment by the employer
     */
    public void addStudentReview(Student student, String writer, int rating, String comment) {
        student.getReviews().add(new Review(writer, rating, comment));
        DataWriter.saveStudents();
    }

    /**
     * Adds a student review to an employer
     * @param employer The employer that the review is about
     * @param writer A string of the student writer of the review
     * @param rating An integer of the rating by the student
     * @param comment A string of the comment by the student
     */
    public void addEmployerReview(Employer employer, String writer, int rating, String comment) {
        employer.getReviews().add(new Review(writer, rating, comment));
        DataWriter.saveEmployers();
    }

    /**
     * Prints the student's resume to a text file
     */
    public void printResumeToFile() {
        student.printResumeToFile();
    }
}