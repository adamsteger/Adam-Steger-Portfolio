import static org.junit.Assert.*;
import org.junit.*;
import java.util.ArrayList;

public class StudentListTest {
    private StudentList students = StudentList.getInstance();
    private ArrayList<Student> studentList = students.getStudents();

    @Before
    public void setup() {
        studentList.clear();
        studentList.add(new Student("Ana", "Boccanfuso", "anaboca", "12345678", 2024, "boccanfa@email.sc.edu", "123 Spring Street, Charleston, SC", "123-456-7890", 4.0, true));
        studentList.add(new Student("Adam", "Steger", "asteger", "codykolover", 2024, "asteger@email.sc.edu", "100 Main Street, Columbia, SC", "098-765-4321", 3.9, true));
        DataWriter.saveStudents();
    }

    @After
    public void tearDown() {
        StudentList.getInstance().getStudents().clear();
        DataWriter.saveStudents();
    }

    @Test
    public void testHaveStudentValidFirstItem() {
        boolean hasAna = students.haveStudent("anaboca");
        assertTrue(hasAna);
    }

    @Test
    public void testHaveStudentValidLastItem() {
        boolean hasAdam = students.haveStudent("asteger");
        assertTrue(hasAdam);
    }

    @Test
	public void testHaveStudentInValid() {
		boolean hasJoe = students.haveStudent("jsmith");
		assertFalse(hasJoe);
	}
	
	@Test
	public void testHaveStudentEmpty() {
		boolean hasEmpty = students.haveStudent("");
		assertFalse(hasEmpty);
	}
	
	@Test
	public void testHaveStudentNull() {
		boolean hasNull = students.haveStudent(null);
		assertFalse(hasNull);
	}

    @Test
    public void testGetStudentByUserFirst() {
        String test = students.getStudentByUser("anaboca").username;
        assertEquals("anaboca", test);
    }

    @Test
    public void testGetStudentByUserLast() {
        String test = students.getStudentByUser("asteger").username;
        assertEquals("asteger", test);
    }

    @Test
    public void testGetStudentByUserInvalid() {
        String test = students.getStudentByUser("joesmith").username;
        assertEquals(null, test);
    }

    @Test
    public void testGetStudentByUserEmpty() {
        String test = students.getStudentByUser("").username;
        assertEquals(null, test);
    }

    @Test
    public void testGetStudentByUserNull() {
        String test = students.getStudentByUser(null).username;
        assertEquals(null, test);
    }

    @Test
    public void testAddStudentValid() {
        boolean hasValid = students.addStudent(new Student("Vaughn", "Eugenio", "veugenio", "password", 2023, "vaughn@gmail.com", "somewhere", "8031234567", 4.0, true));
        assertTrue(hasValid);
    }

    @Test
    public void testAddStudentDuplicateUser() {
        boolean hasInvalid = students.addStudent(new Student("Ana", "Boccanfuso", "anaboca", "password", 2024, "email", "somewhere", "8036031487", 4.0, true));
        assertFalse(hasInvalid);
    }

    @Test
    public void testAddStudentEmpty() {
        boolean hasEmpty = students.addStudent(new Student("", "", "", "", 0, "", "", "", 0.0, true));
        assertFalse(hasEmpty);
    }

    @Test
    public void testAddStudentNull() {
        boolean hasNull = students.addStudent(null);
        assertFalse(hasNull);
    }

}
