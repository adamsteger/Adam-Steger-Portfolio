import static org.junit.Assert.*;

import java.util.ArrayList;
import org.junit.*;

public class DataWriterTest {

   
	private ArrayList<Student> studentList = StudentList.getInstance().getStudents();
    private ArrayList<Employer> employerList = EmployerList.getInstance().getEmployers();
    private ArrayList<Admin> adminList = AdminList.getInstance().getAdmins();
    private ArrayList<InternshipPost> internshipList = InternshipList.getInstance().getInternships();
    private ArrayList<Resume> resumeList = ResumeList.getInstance().getResumes();

    //Applicants/applications?

    
    @Before
	public void setup() {
		StudentList.getInstance().getStudents().clear();
		DataWriter.saveStudents();

        EmployerList.getInstance().getEmployers().clear();
		DataWriter.saveEmployers();

        AdminList.getInstance().getAdmins().clear();
		DataWriter.saveAdmins();

        InternshipList.getInstance().getInternships().clear();
		DataWriter.saveInternshipPosts();

        ResumeList.getInstance().getResumes().clear();
		DataWriter.saveResumes();

	}

    @After
	public void tearDown() {
		StudentList.getInstance().getStudents().clear();
		DataWriter.saveStudents();
        
        EmployerList.getInstance().getEmployers().clear();
		DataWriter.saveEmployers();

        AdminList.getInstance().getAdmins().clear();
		DataWriter.saveAdmins();

        InternshipList.getInstance().getInternships().clear();
		DataWriter.saveInternshipPosts();

        ResumeList.getInstance().getResumes().clear();
		DataWriter.saveResumes();
	}

/////////////////////////////////////////////////////////////////////////////////////////////////////
    @Test
	public void testWritingZeroStudents() {
		studentList = DataLoader.getStudents();

		assertEquals(0, studentList.size());
	}

    @Test
	public void testWritingOneStudent() {
		studentList.add(new Student("Vaughn", "Eugenio", "Veugenio", "password", 2023, "email@email.com", "123 house street", "123-456-7890", 4.0, true));
		DataWriter.saveStudents();

		assertEquals("Veugenio", DataLoader.getStudents().get(0).getUserName());
	}

    @Test
	public void testWritingFiveStudents() {
        studentList.add(new Student("Vaughn", "Eugenio", "Veugenio", "password", 2023, "email@email.com", "123 house street", "123-456-7890", 4.0, true));
        studentList.add(new Student("Vaughn", "Eugenio", "Veugenio1", "password", 2023, "email@email.com", "123 house street", "123-456-7890", 4.0, true));
        studentList.add(new Student("Vaughn", "Eugenio", "Veugenio2", "password", 2023, "email@email.com", "123 house street", "123-456-7890", 4.0, true));
        studentList.add(new Student("Vaughn", "Eugenio", "Veugenio3", "password", 2023, "email@email.com", "123 house street", "123-456-7890", 4.0, true));
        studentList.add(new Student("Vaughn", "Eugenio", "Veugenio4", "password", 2023, "email@email.com", "123 house street", "123-456-7890", 4.0, true));
        DataWriter.saveStudents();

		assertEquals("Veugenio4", DataLoader.getStudents().get(4).getUserName());
    }

    @Test
	public void testWritingEmptyStudent() {
		studentList.add(new Student("", "", "", "", 0, "", "", "", 0, false));
		DataWriter.saveStudents();

		assertEquals("", DataLoader.getStudents().get(0).getUserName());
	}

    @Test
	public void testWritingNullStudent() {
		studentList.add(new Student("", "", null, "", 0, "", "", "", 0, false));
		DataWriter.saveStudents();

		assertEquals(null, DataLoader.getStudents().get(0).getUserName());
	}

    @Test
	public void testWritingReviewOnStudent() {
		studentList.add(new Student("Vaughn", "Eugenio", "Veugenio", "password", 2023, "email@email.com", "123 house street", "123-456-7890", 4.0, true));
		DataWriter.saveStudents();
        studentList.get(0).getReviews().add(new Review("Google", 5, "Great worker"));
		DataWriter.saveStudents();

        assertEquals("Google", DataLoader.getStudents().get(0).getReviews().get(0).getWriter());
	}

///////////////////////////////////////////////////////////////////////////////////////

    @Test
	public void testWritingZeroEmployers() {
		employerList = DataLoader.getEmployers();

		assertEquals(0, employerList.size());
	}

    @Test
	public void testWritingOneEmployer() {
		employerList.add(new Employer("G00Gl3", "secure"));
		DataWriter.saveEmployers();

		assertEquals("G00Gl3", DataLoader.getEmployers().get(0).getUsername());
	}

    @Test
	public void testWritingFiveEmployers() {
        employerList.add(new Employer("G00Gl3", "secure"));
        employerList.add(new Employer("G00Gl31", "secure"));
        employerList.add(new Employer("G00Gl32", "secure"));
        employerList.add(new Employer("G00Gl33", "secure"));
        employerList.add(new Employer("G00Gl34", "secure"));
        DataWriter.saveEmployers();

		assertEquals("G00Gl34", DataLoader.getEmployers().get(4).getUsername());
    }

    @Test
	public void testWritingEmptyEmployer() {
		employerList.add(new Employer("", ""));
		DataWriter.saveEmployers();

		assertEquals("", DataLoader.getEmployers().get(0).getUsername());
	}

    @Test
	public void testWritingNullEmployer() {
		employerList.add(new Employer(null, null));
		DataWriter.saveEmployers();

		assertEquals(null, DataLoader.getEmployers().get(0).getUsername());
	}

    @Test
	public void testWritingReviewOnEmployer() {
		employerList.add(new Employer("G00Gl3", "secure"));
		DataWriter.saveEmployers();
        employerList.get(0).getReviews().add(new Review("Vaughn Eugenio", 5, "I loved working here!!!"));
		DataWriter.saveEmployers();

        assertEquals("Vaughn Eugenio", DataLoader.getEmployers().get(0).getReviews().get(0).getWriter());
	}

	@Test
	public void testEmployerWritePost() {
		employerList.add(new Employer("G00Gl3", "secure"));
		DataWriter.saveEmployers();
		ArrayList<Skill> skills = new ArrayList<Skill>();
		InternshipPost post = new InternshipPost("employerTitle", "posTitle", "description", "location", skills, "startDate", "endDate", true, true, 0, 0);
		employerList.get(0).addPost(post);
		DataWriter.saveInternshipPosts();

		//assertEquals("employerTitle", DataLoader.getInternshipPosts().get(0).getEmployerTitle());
	}


///////////////////////////////////////////////////////////////////////////////////////


	@Test
	public void testWritingZeroAdmin() {
		adminList = DataLoader.getAdmins();

		assertEquals(0, adminList.size());
	}

    @Test
	public void testWritingOneAdmin() {
		adminList.add(new Admin("username", "password"));
		DataWriter.saveAdmins();

		assertEquals("username", DataLoader.getAdmins().get(0).getUsername());
	}

    @Test
	public void testWritingFiveAdmins() {
        adminList.add(new Admin("username", "password"));
		adminList.add(new Admin("username1", "password"));
		adminList.add(new Admin("username2", "password"));
		adminList.add(new Admin("username3", "password"));
		adminList.add(new Admin("username4", "password"));
        DataWriter.saveAdmins();

		assertEquals("username4", DataLoader.getAdmins().get(4).getUsername());
    }

    @Test
	public void testWritingEmptyAdmin() {
		adminList.add(new Admin("", ""));
		DataWriter.saveAdmins();

		assertEquals("", DataLoader.getAdmins().get(0).getUsername());
	}

    @Test
	public void testWritingNullAdmin() {
		adminList.add(new Admin(null, null));
		DataWriter.saveAdmins();

		assertEquals(null, DataLoader.getAdmins().get(0).getUsername());
	}

    @Test
	public void testAdminWritePost() {
		adminList.add(new Admin("username", "password"));
		DataWriter.saveAdmins();
		ArrayList<Skill> skills = new ArrayList<Skill>();
        adminList.get(0).addPost("employerTitle", "posTitle", "description", "location", skills, "startDate", "endDate", true, true, 0, 0);
		DataWriter.saveInternshipPosts();

		assertEquals("employerTitle" , DataLoader.getInternshipPosts().get(0).getEmployerTitle());
	}


///////////////////////////////////////////////////////////////////////////////////////









}