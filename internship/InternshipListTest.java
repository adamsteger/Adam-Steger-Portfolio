import static org.junit.Assert.*;
import org.junit.*;
import java.util.ArrayList;

public class InternshipListTest {
    private InternshipList internships = InternshipList.getInstance();
    private ArrayList<InternshipPost> internshipList = internships.getInternships();

    @Before
    public void setup() {
        internshipList.clear();
        ArrayList<Skill> skills = new ArrayList<Skill>();
        skills.add(Skill.JAVA);
        skills.add(Skill.PYTHON);
        internshipList.add(new InternshipPost("Google", "Software Development Intern", "coding things", "Mountain View, CA", skills, "May 2022", "August 2022", false, true, 1000, 2500));
        internshipList.add(new InternshipPost("Microsoft", "Application Design Intern", "design things", "Seattle, WA", skills, "Jan 2022", "December 2022", true, true, 2000, 3000));
        DataWriter.saveInternshipPosts();
    }

    @After
    public void tearDown() {
        InternshipList.getInstance().getInternships().clear();
        DataWriter.saveInternshipPosts();
    }

    @Test
    public void testHaveInternshipValidFirstItem() {
        boolean hasGoogle = internships.haveInternshipPost("Google", "Software Development Intern");
        assertTrue(hasGoogle);
    }

    @Test
    public void testHaveIntershipValidLastItem() {
        boolean hasMicro = internships.haveInternshipPost("Microsoft", "Application Design Intern");
        assertTrue(hasMicro);
    }

    @Test
	public void testHaveInternshipInValid() {
		boolean hasIBM = internships.haveInternshipPost("IBM", "Coding Internship");
		assertFalse(hasIBM);
	}
	
	@Test
	public void testHaveInternshipEmpty() {
		boolean hasEmpty = internships.haveInternshipPost("", "");
		assertFalse(hasEmpty);
	}
	
	@Test
	public void testHaveInternshipNull() {
		boolean hasNull = internships.haveInternshipPost(null, null);
		assertFalse(hasNull);
	}

    @Test
    public void testAddInternshipValid() {
        ArrayList<Skill> skills = new ArrayList<Skill>();
        skills.add(Skill.C);
        skills.add(Skill.PYTHON);
        boolean addValid = internships.addInternship(new InternshipPost("Google", "Computer Application Developer", "Developing", "Mountain View, CA", skills, "May 2022", "August 2022", true, true, 2500, 3000));
        assertTrue(addValid);
    }

    public void testAddInternshipInvalid() {
        ArrayList<Skill> skills = new ArrayList<Skill>();
        skills.add(Skill.C);
        skills.add(Skill.PYTHON);
        boolean addInvalid = internships.addInternship(new InternshipPost("Google", "Software Development Intern", "coding things", "Mountain View, CA", skills, "May 2022", "August 2022", false, true, 1000, 2500));
        assertFalse(addInvalid);
    }

    public void testAddInternshipEmpty() {
        boolean addEmpty = internships.addInternship(new InternshipPost("", "", "", "", null, "", "", true, true, 0, 0));
        assertFalse(addEmpty);
    }
   
    public void testAddInternshipNull() {
        boolean addNull = internships.addInternship(null);
        assertFalse(addNull);
    }

    public void testRemoveInternshipValid() {
        InternshipPost test = internships.getInternships().get(0);
        boolean removeValid = internships.removeInternship(test);
        assertTrue(removeValid);
    }

    public void testRemoveInternshipInValid() {
        InternshipPost test = new InternshipPost("IBM", "CEO", "work", "somewhere", null, "now", "never", true, true, 0, 0);
        boolean removeInvalid = internships.removeInternship(test);
        assertFalse(removeInvalid);
    }

    public void testRemoveInternshipEmpty() {
        InternshipPost test = new InternshipPost("", "", "", "", null, "", "", true, true, 0, 0);
        boolean removeEmpty = internships.removeInternship(test);
        assertFalse(removeEmpty);
    }

    public void testRemoveInternshipNull() {
        boolean removeInvalid = internships.removeInternship(null);
        assertFalse(removeInvalid);
    }
}

