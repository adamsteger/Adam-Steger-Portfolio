import static org.junit.Assert.*;
import org.junit.*;
import java.util.ArrayList;
import java.util.Hashtable;

public class DataLoaderTest {
    private EmployerList employerList = EmployerList.getInstance();
    private InternshipList internshipList = InternshipList.getInstance();
    private AdminList adminList = AdminList.getInstance();
    private ResumeList resumeList = ResumeList.getInstance();
    private StudentList studentList = StudentList.getInstance();

    @Before
    public void setup() {
        studentList.getStudents().clear();

        Student adam = new Student("Adam", "Steger", "asteger", "mypassword", 2024, "asteger@email.sc.edu", "1238 Axtell Dr Cayce, SC 29033", "(803)730-3278", 4.0, true);
        adam.getReviews().add(new Review("Google", 4, "Very Good Student"));
        adam.getReviews().add(new Review("Amazon", 2, "Mid"));

        Student chris = new Student("Chris", "Bacon", "chrispbacon", "hispassword", 2022, "baconbits@gmail.com", "123 BLT St, Irmo, SC 29063", "(803)123-4567", 3.5, false);
        InternshipPost post = new InternshipPost("Google", "Intern", "Intern things", "Columbia, SC", new ArrayList<Skill>(), "May 2020", "August 2020", false, true, 1000, 1500);
        chris.getFavoritePosts().add(post);

        studentList.addStudent(adam);
        studentList.addStudent(chris);


        employerList.getEmployers().clear();

        Employer google = new Employer("Google", "ImGoogle", "password", "google@gmail.com", "Columbia, SC", "Google forever");
        google.getReviews().add(new Review("Adam Steger", 5, "Great company"));
        google.getReviews().add(new Review("Amy Smith", 3, "Solid employer"));

        Employer google2 = new Employer("Google2", "ImGoogle2", "password", "google@gmail.com", "Columbia, SC", "Google forever");

        employerList.addEmployer(google);
        employerList.addEmployer(google2);


        internshipList.getInternships().clear();

        ArrayList<Skill> postSkills = new ArrayList<Skill>();
        postSkills.add(Skill.C);
        postSkills.add(Skill.CPP);
        InternshipPost googlePost = new InternshipPost("Google", "Intern", "Intern at Google", "Columbia, SC", postSkills, "May 2020", "August 2020", false, true, 1000, 1500);
        ArrayList<Skill> post2Skills = new ArrayList<Skill>();
        post2Skills.add(Skill.JAVA);
        post2Skills.add(Skill.JAVASCRIPT);
        InternshipPost google2Post = new InternshipPost("Google", "Intern2", "Intern at Google", "Columbia, SC", post2Skills, "May 2020", "August 2020", false, true, 1000, 1500);

        internshipList.addInternship(googlePost);
        internshipList.addInternship(google2Post);

        resumeList.getResumes().clear();
        ArrayList<Education> edu1 = new ArrayList<Education>();
        edu1.add(new Education("UofSC", "Columbia, SC", "Computer Science", 2024, true));
        ArrayList<Education> edu2 = new ArrayList<Education>();
        edu2.add(new Education("UofSC", "Columbia, SC", "Computer Information Systems", 2023, true));
        Hashtable<Skill, Boolean> skill1 = new Hashtable<Skill, Boolean>();
        skill1.put(Skill.C, true);
        skill1.put(Skill.HTML, true);
        Hashtable<Skill, Boolean> skill2 = new Hashtable<Skill, Boolean>();
        skill2.put(Skill.JAVA, true);
        skill2.put(Skill.CPP, true);
        Hashtable<String, Boolean> course1 = new Hashtable<String, Boolean>();
        course1.put("Java Fundamentals", true);
        course1.put("UNIX/LINUX Fundamentals", true);
        Hashtable<String, Boolean> course2 = new Hashtable<String, Boolean>();
        course2.put("Intro to Computer Networks", true);
        course2.put("Software Engineering", true);
        ArrayList<WorkExperience> work1 = new ArrayList<WorkExperience>();
        ArrayList<String> desc1 = new ArrayList<String>();
        desc1.add("Ate apples");
        desc1.add("Coded apples");
        work1.add(new WorkExperience("Design Intern", "Apple", "Santa Ana, CA", "May 2020", "August 2020", true, desc1));
        ArrayList<WorkExperience> work2 = new ArrayList<WorkExperience>();
        ArrayList<String> desc2 = new ArrayList<String>();
        desc2.add("Googled things");
        desc2.add("Worked a lot");
        work2.add(new WorkExperience("Software Engineer Intern", "Google", "Mountain View, CA", "May 2021", "August 2021", true, desc2));
        ArrayList<Extracurricular> extra1 = new ArrayList<Extracurricular>();
        extra1.add(new Extracurricular("Coding Club", "Member", "August 2020", "Present", true));
        extra1.add(new Extracurricular("Women in Stem", "President", "August 2021", "December 2021", true));
        ArrayList<Extracurricular> extra2 = new ArrayList<Extracurricular>();
        extra2.add(new Extracurricular("Dance Marathon", "Campaigner", "January 2020", "November 2021", true));
        ArrayList<Honor> honor1 = new ArrayList<Honor>();
        ArrayList<Honor> honor2 = new ArrayList<Honor>();
        honor1.add(new Honor("President's List", "UofSC", "All A's", 2020, true));
        honor2.add(new Honor("Palmetto Fellows Scholarship", "UofSC", "Awarded for high SAT", 2019, true));
        resumeList.getResumes().add(new Resume(edu1, skill1, course1, work1, extra1, honor1));
        resumeList.getResumes().add(new Resume(edu2, skill2, course2, work2, extra2, honor2));
        DataWriter.saveEmployers();
    }

    @After
    public void tearDown() {
        StudentList.getInstance().getStudents().clear();
        DataWriter.saveStudents();
    }

    @Test
    public void testGetStudentsSize() {
        assertEquals(2, studentList.getStudents().size());
    }

    @Test
    public void testGetStudentsSizeZero() {
        StudentList.getInstance().getStudents().clear();
        DataWriter.saveStudents();
        assertEquals(0,studentList.getStudents().size());
    }

    @Test
    public void testGetStudentsEmptyStudent() {
        StudentList.getInstance().getStudents().clear();
        Student empty = new Student("","","","",0,"","","",0.0,false);
        studentList.addStudent(empty);
        assertEquals(1,studentList.getStudents().size());
    }

    @Test
    public void testGetStudentsNullStudent() {
        StudentList.getInstance().getStudents().clear();
        Student nullStudent = null;
        studentList.addStudent(nullStudent);
        assertEquals(0, studentList.getStudents().size());
    }

    @Test
    public void testGetStudentsUsername() {
        assertEquals("asteger", studentList.getStudents().get(0).getUsername());
    }

    @Test
    public void testGetStudentsReviewsSize() {
        assertEquals(2, studentList.getStudents().get(0).getReviews().size());
    }

    @Test
    public void testGetStudentsReviewsFirstElement() {
        assertEquals("Google", studentList.getStudents().get(0).getReviews().get(0).getWriter());
    }

    @Test
    public void testGetStudentsReviewsLastElement() {
        assertEquals("Mid", studentList.getStudents().get(0).getReviews().get(1).getComment());
    }

    @Test
    public void testGetStudentsFavPostsSize() {
        assertEquals(1, studentList.getStudents().get(1).getFavoritePosts().size());
    }

    @Test
    public void testGetStudentsFavPosts() {
        assertEquals("Intern", studentList.getStudents().get(1).getFavoritePosts().get(0).getPosTitle());
    }

    @Test
    public void testGetFourStudents() {
        Student amy = new Student("Adam", "Steger", "asmith", "mypassword", 2024, "asteger@email.sc.edu", "1238 Axtell Dr Cayce, SC 29033", "(803)730-3278", 4.0, true);
        Student berry = new Student("Adam", "Steger", "bsmith", "mypassword", 2024, "asteger@email.sc.edu", "1238 Axtell Dr Cayce, SC 29033", "(803)730-3278", 4.0, true);
        studentList.addStudent(amy);
        studentList.addStudent(berry);
        assertEquals("bsmith", studentList.getStudents().get(3).getUsername());
    }

    // @Test
    // public void testGetStudentsResume() {
    //     assertEquals(expected, actual);
    // }

    @Test
    public void testGetEmployersSize() {
        assertEquals(2, employerList.getEmployers().size());
    }

    @Test
    public void testGetEmployersSizeZero() {
        employerList.getEmployers().clear();
        DataWriter.saveEmployers();
        assertEquals(0,employerList.getEmployers().size());
    }

    @Test
    public void testGetEmployersEmptyEmployer() {
        employerList.getEmployers().clear();
        Employer empty = new Employer("","","","","","");
        employerList.addEmployer(empty);
        assertEquals(1,employerList.getEmployers().size());
    }

    @Test
    public void testGetEmployersNullEmployer() {
        employerList.getEmployers().clear();
        Employer nullEmployer = null;
        employerList.addEmployer(nullEmployer);
        assertEquals(0, employerList.getEmployers().size());
    }

    @Test
    public void testGetEmployersUsername() {
        assertEquals("ImGoogle", employerList.getEmployers().get(0).getUsername());
    }

    @Test
    public void testGetEmployersReviewsSize() {
        assertEquals(2, employerList.getEmployers().get(0).getReviews().size());
    }

    @Test
    public void testGetEmployersReviewsFirstElement() {
        assertEquals("Adam Steger", employerList.getEmployers().get(0).getReviews().get(0).getWriter());
    }

    @Test
    public void testGetEmployersReviewsLastElement() {
        assertEquals("Solid employer", employerList.getEmployers().get(0).getReviews().get(1).getComment());
    }

    @Test
    public void testGetFourEmployers() {
        Employer google3 = new Employer("Google", "ImGoogle3", "password", "google@gmail.com", "Columbia, SC", "Google forever");
        Employer google4 = new Employer("Google", "ImGoogle4", "password", "google@gmail.com", "Columbia, SC", "Google forever");
        employerList.addEmployer(google3);
        employerList.addEmployer(google4);
        assertEquals("ImGoogle4", employerList.getEmployers().get(3).getUsername());
    }

    @Test
    public void testGetInternshipsSize() {
        assertEquals(2, internshipList.getInternships().size());
    }

    @Test
    public void testGetInternshipsSizeZero() {
        internshipList.getInternships().clear();
        DataWriter.saveInternshipPosts();
        assertEquals(0,internshipList.getInternships().size());
    }

    @Test
    public void testGetInternshipsEmptyInternship() {
        internshipList.getInternships().clear();
        InternshipPost empty = new InternshipPost("","","","", new ArrayList<Skill>(),"","",false,false,0,0);
        internshipList.addInternship(empty);
        assertEquals(1,internshipList.getInternships().size());
    }

    @Test
    public void testGetInternshipsNullInternship() {
        internshipList.getInternships().clear();
        InternshipPost nullPost = null;
        internshipList.addInternship(nullPost);
        assertEquals(0, internshipList.getInternships().size());
    }

    @Test
    public void testGetInternshipsPosTite() {
        assertEquals("Intern", internshipList.getInternships().get(0).getPosTitle());
    }

    @Test
    public void testGetInternshipsSkillsSize() {
        assertEquals(2, internshipList.getInternships().get(0).getSkillReq().size());
    }

    @Test
    public void testGetInternshipsSkillsFirstElement() {
        assertEquals(Skill.C, internshipList.getInternships().get(0).getSkillReq().get(0));
    }

    @Test
    public void testGetInternshipsSkillsLastElement() {
        assertEquals(Skill.JAVASCRIPT, internshipList.getInternships().get(1).getSkillReq().get(1));
    }

    @Test
    public void testGetResumesSize() {
        assertEquals(2, resumeList.getResumes().size());
    }

    @Test
    public void testGetResumesSizeZero() {
        resumeList.getResumes().clear();
        DataWriter.saveResumes();
        assertEquals(0,resumeList.getResumes().size());
    }

    @Test
    public void testGetResumesEmptyResume() {
        ResumeList.getInstance().getResumes().clear();
        Resume empty = new Resume(new ArrayList<Education>(), new Hashtable<Skill,Boolean>(), new Hashtable<String,Boolean>(), new ArrayList<WorkExperience>(), new ArrayList<Extracurricular>(), new ArrayList<Honor>());
        resumeList.addResume(empty);
        assertEquals(1,resumeList.getResumes().size());
    }

    @Test
    public void testGetResumesNullResume() {
        ResumeList.getInstance().getResumes().clear();
        Resume nullResume = null;
        resumeList.addResume(nullResume);
        assertEquals(0, resumeList.getResumes().size());
    }

    @Test
    public void testGetResumesEducations() {
        assertEquals("UofSC", resumeList.getResumes().get(0).getEducations().get(0).getSchoolTitle());
    }

    @Test
    public void testGetResumesSkills() {
        assertTrue(resumeList.getResumes().get(0).getSkills().get(Skill.C));
    }
}

//empty student, null student, normal students, 1 student 

