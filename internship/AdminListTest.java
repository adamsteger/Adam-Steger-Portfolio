import static org.junit.Assert.*;
import org.junit.*;
import java.util.ArrayList;
import java.util.UUID;

public class AdminListTest {
    private AdminList admins = AdminList.getInstance();
    private ArrayList<Admin> adminList = admins.getAdmins();

    @Before
    public void setup() {
        adminList.clear();
        Admin ana = new Admin("anaboca", "12345678");
        Admin adam = new Admin("asteger", "codykolover");
        adminList.add(ana);
        adminList.add(adam);
        DataWriter.saveAdmins();
    }

    @After
    public void tearDown() {
        AdminList.getInstance().getAdmins().clear();
        DataWriter.saveAdmins();
    }

    @Test
    public void testHaveAdminValidFirstItem() {
        boolean hasAna = admins.haveAdmin("anaboca");
        assertTrue(hasAna);
    }

    @Test
    public void testHaveAdminValidLastItem() {
        boolean hasAdam = admins.haveAdmin("asteger");
        assertTrue(hasAdam);
    }

    @Test
	public void testHaveAdminInValid() {
		boolean hasJoe = admins.haveAdmin("jsmith");
		assertFalse(hasJoe);
	}
	
	@Test
	public void testHaveAdminEmpty() {
		boolean hasEmpty = admins.haveAdmin("");
		assertFalse(hasEmpty);
	}
	
	@Test
	public void testHaveAdminNull() {
		boolean hasNull = admins.haveAdmin(null);
		assertFalse(hasNull);
	}

    @Test
    public void testGetAdminByUserFirst() {
        String test = admins.getAdminByUser("anaboca").getUsername();
        assertEquals("anaboca", test);
    }

    @Test
    public void testGetAdminByUserLast() {
        String test = admins.getAdminByUser("asteger").getUsername();
        assertEquals("asteger", test);
    }

    @Test
    public void testGetAdminByUserInValid() {
        Admin test = admins.getAdminByUser("joesmith");
        assertEquals(null, test);
    }

    @Test
	public void testGetAdminByUserEmpty() {
		Admin test = admins.getAdminByUser("");
		assertEquals(null, test);
	}
	
	@Test
	public void testGetAdminByUserNull() {
		Admin test = admins.getAdminByUser(null);
		assertEquals(null, test);
	}

    @Test
    public void testGetAdminByIDFirst() {
        UUID id = admins.getAdminByUser("anaboca").getUUID();
        String test = admins.getAdminByID(id).getUsername();
        assertEquals("anaboca", test);
    }

    @Test
    public void testGetAdminByIDLast() {
        UUID id = admins.getAdminByUser("asteger").getUUID();
        String test = admins.getAdminByID(id).getUsername();
        assertEquals("asteger", test);
    }
	
	@Test
	public void testGetAdminByIDNull() {
		Admin test = admins.getAdminByID(null);
		assertEquals(null, test);
	}

    @Test
    public void testAddAdminValid() {
        boolean test = admins.addAdmin(new Admin("Administrator", "password"));
        assertTrue(test);
    }

    @Test
    public void testAddAdminInValid() {
        boolean test = admins.addAdmin(new Admin("anaboca", "12345678"));
        assertFalse(test);
    }

    @Test
    public void testAddAdminEmpty() {
        boolean test = admins.addAdmin(new Admin("", ""));
        assertFalse(test);
    }

    @Test
    public void testAddAdminNull() {
        boolean test = admins.addAdmin(null);
        assertFalse(test);
    }
}
