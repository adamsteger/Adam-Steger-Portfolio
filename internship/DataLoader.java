import java.io.FileReader;
import java.util.ArrayList;
import java.util.UUID;
import java.util.Hashtable;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Reads in the JSON Files and converts them into objects
 * 
 * @author Byte Me
 */
public class DataLoader extends DataConstants {

	/**
	 * Reads the student JSON file and populates the list of students
	 * 
	 * @return Returns an arraylist of type student of all the students in the
	 *         system
	 */
	public static ArrayList<Student> getStudents() {
		ArrayList<Student> students = new ArrayList<Student>();

		try {
			FileReader reader = new FileReader(STUDENT_FILE_NAME);
			JSONParser parser = new JSONParser();
			JSONArray peopleJSON = (JSONArray) parser.parse(reader);

			for (int i = 0; i < peopleJSON.size(); i++) {
				JSONObject personJSON = (JSONObject) peopleJSON.get(i);
				UUID id = UUID.fromString((String) personJSON.get(USER_ID));
				String userName = (String) personJSON.get(USER_USER_NAME);
				String password = (String) personJSON.get(USER_PASSWORD);
				String firstName = (String) personJSON.get(STUDENT_FIRST_NAME);
				String lastName = (String) personJSON.get(STUDENT_LAST_NAME);
				int gradYear = ((Long) personJSON.get(STUDENT_GRAD_YEAR)).intValue();
				String email = (String) personJSON.get(USER_EMAIL);
				String address = (String) personJSON.get(STUDENT_ADDRESS);
				String phone = (String) personJSON.get(STUDENT_PHONE_NUMBER);
				double gpa = (double) personJSON.get(STUDENT_GPA);
				boolean showGPA = (boolean) personJSON.get(STUDENT_SHOW_GPA);
				double rating = (double) personJSON.get(USER_RATING);

				JSONArray reviewsJSON = (JSONArray) personJSON.get(STUDENT_REVIEWS);
				ArrayList<Review> reviews = new ArrayList<Review>();
				for (int j = 0; j < reviewsJSON.size(); j++) {
					JSONObject reviewJSON = (JSONObject) reviewsJSON.get(j);
					String writer = (String) reviewJSON.get(REVIEW_WRITER);
					int reviewRating = ((Long) reviewJSON.get(REVIEW_RATING)).intValue();
					String comment = (String) reviewJSON.get(REVIEW_COMMENT);

					reviews.add(new Review(writer, reviewRating, comment));
				}

				JSONArray favPostsJSON = (JSONArray) personJSON.get(STUDENT_FAV_POSTS);
				ArrayList<InternshipPost> favPosts = new ArrayList<InternshipPost>();
				for (int j = 0; j < favPostsJSON.size(); j++) {
					JSONObject favPostJSON = (JSONObject) favPostsJSON.get(j);
					UUID postID = UUID.fromString((String) favPostJSON.get(INTERNSHIP_ID));
					InternshipPost post = InternshipList.getInstance().getPostByID(postID);

					favPosts.add(post);
				}

				UUID resumeID = UUID.fromString((String) personJSON.get(STUDENT_RESUME_ID));
				Resume resume = ResumeList.getInstance().getResumeByID(resumeID);

				students.add(new Student(id, firstName, lastName, userName, password, gradYear, email, address, phone,
						gpa, showGPA, rating, reviews, favPosts, resume));
			}

			return students;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Reads in the employer JSON file and populates the list of employers
	 * 
	 * @return Returns an arraylist of type employer of all the employers in the
	 *         system
	 */
	public static ArrayList<Employer> getEmployers() {
		ArrayList<Employer> employers = new ArrayList<Employer>();

		try {
			FileReader reader = new FileReader(EMPLOYER_FILE_NAME);
			JSONParser parser = new JSONParser();
			JSONArray peopleJSON = (JSONArray) new JSONParser().parse(reader);

			for (int i = 0; i < peopleJSON.size(); i++) {
				JSONObject personJSON = (JSONObject) peopleJSON.get(i);
				UUID id = UUID.fromString((String) personJSON.get(USER_ID));
				String title = (String) personJSON.get(EMPLOYER_TITLE);
				String userName = (String) personJSON.get(USER_USER_NAME);
				String password = (String) personJSON.get(USER_PASSWORD);
				String email = (String) personJSON.get(USER_EMAIL);
				double rating = (double) personJSON.get(USER_RATING);
				String location = (String) personJSON.get(EMPLOYER_LOCATION);
				String mission = (String) personJSON.get(EMPLOYER_MISSION);

				JSONArray reviewsJSON = (JSONArray) personJSON.get(EMPLOYER_REVIEWS);
				ArrayList<Review> reviews = new ArrayList<Review>();
				for (int j = 0; j < reviewsJSON.size(); j++) {
					JSONObject reviewJSON = (JSONObject) reviewsJSON.get(j);
					String writer = (String) reviewJSON.get(REVIEW_WRITER);
					int reviewRating = ((Long) reviewJSON.get(REVIEW_RATING)).intValue();
					String comment = (String) reviewJSON.get(REVIEW_COMMENT);

					reviews.add(new Review(writer, reviewRating, comment));
				}

				employers.add(new Employer(id, title, userName, password, email, rating, location, mission, reviews));
			}

			return employers;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Reads in the internship post JSON file and populates the list of internship
	 * posts
	 * 
	 * @return Returns an arraylist of type internship posts of all the posts in the
	 *         system
	 */
	public static ArrayList<InternshipPost> getInternshipPosts() {
		ArrayList<InternshipPost> posts = new ArrayList<InternshipPost>();

		try {
			FileReader reader = new FileReader(INTERNSHIP_FILE_NAME);
			JSONParser parser = new JSONParser();
			JSONArray postsJSON = (JSONArray) new JSONParser().parse(reader);

			for (int i = 0; i < postsJSON.size(); i++) {
				JSONObject postJSON = (JSONObject) postsJSON.get(i);
				UUID postID = UUID.fromString((String) postJSON.get(INTERNSHIP_ID));
				String employerTitle = (String) postJSON.get(INTERNSHIP_EMPLOYER_TITLE);
				String posTitle = (String) postJSON.get(INTERNSHIP_POSITION_TITLE);
				String description = (String) postJSON.get(INTERNSHIP_DESCRIPTION);
				String location = (String) postJSON.get(INTERNSHIP_LOCATION);
				String startDate = (String) postJSON.get(INTERNSHIP_START_DATE);
				String endDate = (String) postJSON.get(INTERNSHIP_END_DATE);
				boolean isRemote = (boolean) postJSON.get(INTERNSHIP_IS_REMOTE);
				boolean isOpen = (boolean) postJSON.get(INTERNSHIP_IS_OPEN);
				int lowPay = ((Long) postJSON.get(INTERNSHIP_LOW_PAY)).intValue();
				int highPay = ((Long) postJSON.get(INTERNSHIP_HIGH_PAY)).intValue();

				JSONArray skillsJSON = (JSONArray) postJSON.get(INTERNSHIP_SKILLS_REQ);
				ArrayList<Skill> skills = new ArrayList<Skill>();
				for (int j = 0; j < skillsJSON.size(); j++) {
					JSONObject skillJSON = (JSONObject) skillsJSON.get(j);
					String skillString = (String) skillJSON.get(SKILLS_SKILL);
					Skill skill = Skill.valueOf(skillString);
					skills.add(skill);
				}

				posts.add(new InternshipPost(postID, employerTitle, posTitle, description, location, startDate, endDate,
						isRemote, isOpen, lowPay, highPay, skills));
			}

			return posts;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Reads in the resume JSON file and populates the list of resumes
	 * 
	 * @return Returns an arraylist of type resume of all the resumes in the system
	 */
	public static ArrayList<Resume> getResumes() {
		ArrayList<Resume> resumes = new ArrayList<Resume>();

		try {
			FileReader reader = new FileReader(RESUME_FILE_NAME);
			JSONParser parser = new JSONParser();
			JSONArray resumesJSON = (JSONArray) parser.parse(reader);

			for (int i = 0; i < resumesJSON.size(); i++) {
				JSONObject resumeJSON = (JSONObject) resumesJSON.get(i);
				UUID id = UUID.fromString((String) resumeJSON.get(RESUME_ID));

				JSONArray educationsJSON = (JSONArray) resumeJSON.get(RESUME_EDUCATIONS);
				ArrayList<Education> educations = new ArrayList<Education>();
				for (int j = 0; j < educationsJSON.size(); j++) {
					JSONObject educationJSON = (JSONObject) educationsJSON.get(j);
					String schoolTitle = (String) educationJSON.get(EDUCATION_SCHOOL);
					String location = (String) educationJSON.get(EDUCATION_LOCATION);
					String major = (String) educationJSON.get(EDUCATION_MAJOR);
					int gradYear = ((Long) educationJSON.get(EDUCATION_GRAD_YEAR)).intValue();
					Boolean resume = (Boolean) educationJSON.get(RESUME);

					educations.add(new Education(schoolTitle, location, major, gradYear, resume));
				}

				JSONArray skillsJSON = (JSONArray) resumeJSON.get(SKILLS);
				Hashtable<Skill, Boolean> skills = new Hashtable<Skill, Boolean>();
				for (int j = 0; j < skillsJSON.size(); j++) {
					JSONObject skillJSON = (JSONObject) skillsJSON.get(j);
					String skillString = (String) skillJSON.get(SKILLS_SKILL);
					Boolean resume = (Boolean) skillJSON.get(RESUME);
					Skill skill = Skill.valueOf(skillString);

					skills.put(skill, resume);
				}

				JSONArray coursesJSON = (JSONArray) resumeJSON.get(RESUME_COURSES);
				Hashtable<String, Boolean> courses = new Hashtable<String, Boolean>();
				for (int j = 0; j < coursesJSON.size(); j++) {
					JSONObject courseJSON = (JSONObject) coursesJSON.get(j);
					String course = (String) courseJSON.get(COURSES_COURSE);
					Boolean resume = (Boolean) courseJSON.get(RESUME);

					courses.put(course, resume);
				}

				JSONArray worksJSON = (JSONArray) resumeJSON.get(RESUME_WORK);
				ArrayList<WorkExperience> works = new ArrayList<WorkExperience>();
				for (int j = 0; j < worksJSON.size(); j++) {
					JSONObject workJSON = (JSONObject) worksJSON.get(j);
					String posTitle = (String) workJSON.get(WORK_POSITION_TITLE);
					String employer = (String) workJSON.get(WORK_EMPLOYER);
					String location = (String) workJSON.get(WORK_LOCATION);
					String startDate = (String) workJSON.get(WORK_START_DATE);
					String endDate = (String) workJSON.get(WORK_END_DATE);

					JSONArray descriptionsJSON = (JSONArray) workJSON.get(WORK_DESCRIPTION);
					ArrayList<String> description = new ArrayList<String>();
					for (int k = 0; k < descriptionsJSON.size(); k++) {
						JSONObject descriptionJSON = (JSONObject) descriptionsJSON.get(k);
						String string = (String) descriptionJSON.get(DESCRIPTION_STRING);
						description.add(string);
					}

					Boolean resume = (Boolean) workJSON.get(RESUME);
					works.add(
							new WorkExperience(posTitle, employer, location, startDate, endDate, resume, description));
				}

				JSONArray extrasJSON = (JSONArray) resumeJSON.get(RESUME_EXTRACURRICULARS);
				ArrayList<Extracurricular> extracurriculars = new ArrayList<Extracurricular>();
				for (int j = 0; j < extrasJSON.size(); j++) {
					JSONObject extraJSON = (JSONObject) extrasJSON.get(j);
					String title = (String) extraJSON.get(EXTRA_TITLE);
					String position = (String) extraJSON.get(EXTRA_POSITION);
					String startDate = (String) extraJSON.get(EXTRA_START_DATE);
					String endDate = (String) extraJSON.get(EXTRA_END_DATE);
					Boolean resume = (Boolean) extraJSON.get(RESUME);

					extracurriculars.add(new Extracurricular(title, position, startDate, endDate, resume));
				}

				JSONArray honorsJSON = (JSONArray) resumeJSON.get(RESUME_HONORS);
				ArrayList<Honor> honors = new ArrayList<Honor>();
				for (int j = 0; j < honorsJSON.size(); j++) {
					JSONObject honorJSON = (JSONObject) honorsJSON.get(j);
					String title = (String) honorJSON.get(HONORS_TITLE);
					String organ = (String) honorJSON.get(HONORS_ORGAN);
					String description = (String) honorJSON.get(HONORS_DESCRIPTION);
					int year = ((Long) honorJSON.get(HONORS_YEAR)).intValue();
					Boolean resume = (Boolean) honorJSON.get(RESUME);

					honors.add(new Honor(title, organ, description, year, resume));
				}

				resumes.add(new Resume(id, educations, skills, courses, works, extracurriculars, honors));
			}

			return resumes;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;

	}

	/**
	 * Reads in the admin JSON file and populates the list of admins
	 * 
	 * @return Returns an arraylist of type admin of all the admins in the system
	 */
	public static ArrayList<Admin> getAdmins() {
		ArrayList<Admin> admins = new ArrayList<Admin>();

		try {
			FileReader reader = new FileReader(ADMIN_FILE_NAME);
			JSONParser parser = new JSONParser();
			JSONArray adminsJSON = (JSONArray) new JSONParser().parse(reader);

			for (int i = 0; i < adminsJSON.size(); i++) {
				JSONObject adminJSON = (JSONObject) adminsJSON.get(i);
				UUID id = UUID.fromString((String) adminJSON.get(USER_ID));
				String username = (String) adminJSON.get(USER_USER_NAME);
				String password = (String) adminJSON.get(USER_PASSWORD);

				admins.add(new Admin(id, username, password));
			}

			return admins;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Reads in the application JSON file and populates the list of applicants for
	 * each post and list of posts for each employer
	 */
	public static void getApplicants() {
		try {
			FileReader reader = new FileReader(APPLICATIONS_FILE_NAME);
			JSONParser parser = new JSONParser();
			JSONArray applicationsJSON = (JSONArray) new JSONParser().parse(reader);

			for (int i = 0; i < applicationsJSON.size(); i++) {
				JSONObject applicationJSON = (JSONObject) applicationsJSON.get(i);

				JSONArray applicantsJSON = (JSONArray) applicationJSON.get(APPLICATIONS_APPLICANTS);
				ArrayList<Student> applicants = new ArrayList<Student>();
				for (int j = 0; j < applicantsJSON.size(); j++) {
					JSONObject applicantJSON = (JSONObject) applicantsJSON.get(j);
					UUID studentID = UUID.fromString((String) applicantJSON.get(APPLICATIONS_STUDENT_ID));
					Student student = StudentList.getInstance().getStudentByID(studentID);

					applicants.add(student);
				}

				UUID postID = UUID.fromString((String) applicationJSON.get(APPLICATIONS_POST_ID));
				UUID employerID = UUID.fromString((String) applicationJSON.get(APPLICATIONS_EMPLOYER_ID));

				Employer employer = EmployerList.getInstance().getEmployerByID(employerID);
				InternshipPost post = InternshipList.getInstance().getPostByID(postID);

				post.setApplicants(applicants);
				employer.getPosts().add(post);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
