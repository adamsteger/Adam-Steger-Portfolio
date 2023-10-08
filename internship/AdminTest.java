import org.junit.*;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.UUID;

public class AdminTest {
    private InternshipList internshipList = InternshipList.getInstance();
    private ArrayList<InternshipPost> internshipPosts = internshipList.getInternships();
    private Admin testAdmin = new Admin("adamsanfacon", "password");


    @Before
    public void setup() {
        internshipPosts.clear();
        }

    @After 
    public void tearDown() {
        internshipPosts.clear();
    }

    @Test
    public void testAddPost() {
        ArrayList<Skill> skills = new ArrayList<Skill>();
        skills.add(Skill.SQL);
        skills.add(Skill.CPP);
        testAdmin.addPost("Meta", "NFT salesman or something", "Make magic money for pixels", "Anywhere", skills, "April 2022", "December 2022", true, true, 25000, 40000);
        assertEquals(true, internshipList.haveInternshipPost("Meta", "NFT salesman or something"));
    }
    
    @Test
    public void testDeletePost() {
        ArrayList<Skill> skills = new ArrayList<Skill>();
        skills.add(Skill.SQL);
        skills.add(Skill.CPP);
        testAdmin.addPost("Meta", "NFT salesman or something", "Make magic money for pixels", "Anywhere", skills, "April 2022", "December 2022", true, true, 25000, 40000);
        InternshipPost current = internshipList.getInternships().get(0);
        testAdmin.deletePost(current);
        assertEquals(false, internshipList.haveInternshipPost("Meta", "NFT salesman or something"));
    }

    @Test
    public void testDeleteStudentReview() {
        Student testStudent = new Student("adam", "sanfacon", "adamsanf", "12345", 2023, "sanfacoa@email.sc.edu", "2331 preston st", "123456789", 3.5, true);     
        Review testReview = new Review("IBM", 1, "trash intern");
        testStudent.getReviews().add(testReview);
        testAdmin.deleteStudentReview(testStudent, testReview);
        assertEquals(false, testStudent.getReviews().get(0));
    }

    @Test
    public void testDeleteEmployerReview() {
        Employer testEmployer = new Employer("IBM", "IBM", "12345678", "IBM@IBM.com", "California", "THINK");
        Review testReview = new Review("adamsanf", 1, "trash company");
        testEmployer.getReviews().add(testReview);
        testAdmin.deleteEmployerReview(testEmployer, testReview);
        assertEquals(false, testEmployer.getReviews().get(0));
    }
}
