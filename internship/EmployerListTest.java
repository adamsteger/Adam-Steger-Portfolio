import static org.junit.Assert.*;
import org.junit.*;
import java.util.ArrayList;
import java.util.UUID;

public class EmployerListTest {
    private EmployerList employers = EmployerList.getInstance();
    private ArrayList<Employer> employerList = employers.getEmployers();

    @Before
    public void setup() {
        employerList.clear();
        employerList.add(new Employer("ana", "anaboca", "1234567", "ana@gmail.com", "Columbia, SC", "Do good"));
        employerList.add(new Employer("asteger", "codykolover"));
        DataWriter.saveEmployers();
    }

    @After
    public void tearDown() {
        EmployerList.getInstance().getEmployers().clear();
        DataWriter.saveEmployers();
    }

    @Test
    public void testHaveEmployerValidFirstItem() {
        boolean hasAna = employers.haveEmployer("anaboca");
        assertTrue(hasAna);
    }

    @Test
    public void testHaveEmployerValidLastItem() {
        boolean hasAdam = employers.haveEmployer("asteger");
        assertTrue(hasAdam);
    }

    @Test
	public void testHaveEmployerInValid() {
		boolean hasJoe = employers.haveEmployer("jsmith");
		assertFalse(hasJoe);
	}
	
	@Test
	public void testHaveEmployerEmpty() {
		boolean hasEmpty = employers.haveEmployer("");
		assertFalse(hasEmpty);
	}
	
	@Test
	public void testHaveEmployerNull() {
		boolean hasNull = employers.haveEmployer(null);
		assertFalse(hasNull);
	}

    @Test
    public void testGetEmployerByTitleValid() {
        String test = employers.getEmployerByTitle("ana").getTitle();
        assertEquals("ana", test);
    }

    @Test
    public void testGetEmployerByTitleInvalid() {
        String test = employers.getEmployerByTitle("adam").getTitle();
        assertEquals(null, test);
    }
    
    @Test
	public void testGetEmployerByTitleEmpty() {
		String test = employers.getEmployerByTitle("").getTitle();
		assertEquals(null, test);
	}
	
	@Test
	public void testGetEmployerByTitleNull() {
		String test = employers.getEmployerByTitle(null).getTitle();
		assertEquals(null, test);
	}

    @Test
    public void testGetEmployerByUserFirst() {
        String test = employers.getEmployerByUser("anaboca").getUsername();
        assertEquals("anaboca", test);
    }

    @Test
    public void testGetEmployerByUserLast() {
        String test = employers.getEmployerByUser("asteger").getUsername();
        assertEquals("asteger", test);
    }

    @Test
    public void testGetEmployerByUserInValid() {
        String test = employers.getEmployerByUser("joesmith").getUsername();
        assertEquals(null, test);
    }

    @Test
	public void testGetEmployerByUserEmpty() {
		String test = employers.getEmployerByUser("").getUsername();
		assertEquals(null, test);
	}
	
	@Test
	public void testGetEmployerByUserNull() {
		String test = employers.getEmployerByUser(null).getUsername();
		assertEquals(null, test);
	}

    @Test
    public void testGetEmployerByIDFirst() {
        UUID id = employers.getEmployerByUser("anaboca").getUUID();
        String test = employers.getEmployerByID(id).getUsername();
        assertEquals("anaboca", test);
    }

    @Test
    public void testGetEmployerByIDLast() {
        UUID id = employers.getEmployerByUser("asteger").getUUID();
        String test = employers.getEmployerByID(id).getUsername();
        assertEquals("asteger", test);
    }
	
	@Test
	public void testGetEmployerByIDNull() {
		Employer test = employers.getEmployerByID(null);
		assertEquals(null, test);
	}

    @Test
    public void testAddEmployerValid() {
        boolean test = employers.addEmployer(new Employer("Employer", "password"));
        assertTrue(test);
    }

    @Test
    public void testAddEmployerInValid() {
        boolean test = employers.addEmployer(new Employer("anaboca", "12345678"));
        assertFalse(test);
    }

    @Test
    public void testAddEmployerEmpty() {
        boolean test = employers.addEmployer(new Employer("", ""));
        assertFalse(test);
    }

    @Test
    public void testAddEmployerNull() {
        boolean test = employers.addEmployer(null);
        assertFalse(test);
    }
}
