import static org.junit.Assert.*;
import org.junit.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.spi.AbstractResourceBundleProvider;

public class ResumeListTest {
    private ResumeList resumes = ResumeList.getInstance();
    private ArrayList<Resume> resumeList = resumes.getResumes();

    @Before
    public void setup() {
        resumeList.clear();
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
        resumeList.add(new Resume(edu1, skill1, course1, work1, extra1, honor1));
        resumeList.add(new Resume(edu2, skill2, course2, work2, extra2, honor2));
        DataWriter.saveResumes();
    }

    @After
    public void tearDown() {
        ResumeList.getInstance().getResumes().clear();
        DataWriter.saveResumes();
    }

    @Test
    public void testAddResumeValid() {
        ArrayList<Education> edu = new ArrayList<Education>();
        edu.add(new Education("UofSC", "Columbia, SC", "Computer Information Systems", 2023, true));
        Hashtable<Skill, Boolean> skills = new Hashtable<Skill, Boolean>();
        skills.put(Skill.CPP, true);
        skills.put(Skill.JAVA, true);
        Hashtable<String, Boolean> course = new Hashtable<String, Boolean>();
        course.put("Introduction to Computer Architecture", true);
        course.put("Introduction to Software Engineering", true);
        ArrayList<WorkExperience> work = new ArrayList<WorkExperience>();
        ArrayList<String> desc1 = new ArrayList<String>();
        desc1.add("worked");
        desc1.add("coded");
        work.add(new WorkExperience("Software Intern", "Google", "Mountain View, CA", "May 2020", "August 2020", true, desc1));
        ArrayList<Extracurricular> extra1 = new ArrayList<Extracurricular>();
        extra1.add(new Extracurricular("Dance Marathon", "Participant", "August 2020", "Present", true));
        ArrayList<Honor> honor1 = new ArrayList<Honor>();
        resumes.addResume(new Resume(edu, skills, course, work, extra1, honor1));
        assertEquals(3, resumeList.size());
    }

    @Test
    public void testAddResumeEmpty() {
        resumes.addResume(new Resume());
        assertEquals(3, resumeList.size());
    }

    @Test
    public void testAddResumeNull() {
        resumes.addResume(null);
        assertEquals(2, resumeList.size());
    }
}
