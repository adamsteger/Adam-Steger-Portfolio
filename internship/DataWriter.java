import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Formats and writes out information to the JSON files 
 * @author Byte Me
 */
public class DataWriter extends DataConstants {
	
	/**
	 * Retrieves the student list and writes it out to the JSON file
	 */
	public static void saveStudents() {
		StudentList students = StudentList.getInstance();
		ArrayList<Student> studentList = students.getStudents();
		JSONArray JSONStudents = new JSONArray();

		for (int i = 0; i < studentList.size(); i++) {
			JSONStudents.add(getStudentJSON(studentList.get(i)));
		}

		try (FileWriter file = new FileWriter(STUDENT_FILE_NAME)) {
			file.write(JSONStudents.toJSONString());
			file.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Retrieves the employer list and writes it out to the JSON file
	 */
	public static void saveEmployers() {
		EmployerList employers = EmployerList.getInstance();
		ArrayList<Employer> employerList = employers.getEmployers();
		JSONArray JSONEmployers = new JSONArray();

		for (int i = 0; i < employerList.size(); i++) {
			JSONEmployers.add(getEmployerJSON(employerList.get(i)));
		}

		
		try (FileWriter file = new FileWriter(EMPLOYER_FILE_NAME)) {
			file.write(JSONEmployers.toJSONString());
			file.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Retrieves the admin list and writes it out to the JSON file
	 */
	public static void saveAdmins() {
		AdminList admins = AdminList.getInstance();
		ArrayList<Admin> adminList = admins.getAdmins();
		JSONArray JSONAdmins = new JSONArray();

		for (int i = 0; i < adminList.size(); i++) {
			JSONAdmins.add(getAdminJSON(adminList.get(i)));
		}

		
		try (FileWriter file = new FileWriter(ADMIN_FILE_NAME)) {
			file.write(JSONAdmins.toJSONString());
			file.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Retrieves the list of internship posts and writes it out to the JSON file
	 */
	public static void saveInternshipPosts() {
		InternshipList internships = InternshipList.getInstance();
		ArrayList<InternshipPost> internshipList = internships.getInternships();
		JSONArray JSONPosts = new JSONArray();

		for (int i = 0; i < internshipList.size(); i++) {
			JSONPosts.add(getPostJSON(internshipList.get(i)));
		}

		
		try (FileWriter file = new FileWriter(INTERNSHIP_FILE_NAME)) {
			file.write(JSONPosts.toJSONString());
			file.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	} 

	/**
	 * Retrieves the list of resumes and writes it out to the JSON file
	 */
	public static void saveResumes() {
		ResumeList resumes = ResumeList.getInstance();
		ArrayList<Resume> resumeList = resumes.getResumes();
		JSONArray JSONResumes = new JSONArray();

		for (int i = 0; i < resumeList.size(); i++) {
			JSONResumes.add(getResumeJSON(resumeList.get(i)));
		}

		
		try (FileWriter file = new FileWriter(RESUME_FILE_NAME)) {
			file.write(JSONResumes.toJSONString());
			file.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	} 

	/**
	 * Retrieves the list of applications and writes it out to the JSON file
	 */
	public static void saveApplications() {
		InternshipList internships = InternshipList.getInstance();
		ArrayList<InternshipPost> internshipList = internships.getInternships();
		JSONArray JSONApplicants = new JSONArray();

		for (int i = 0; i < internshipList.size(); i++) {
			JSONApplicants.add(getApplicationJSON(internshipList.get(i)));
		}

		
		try (FileWriter file = new FileWriter(APPLICATIONS_FILE_NAME)) {
			file.write(JSONApplicants.toJSONString());
			file.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	/**
	 * Converts a student to a JSON Object
	 * @param student the student that is being converted
	 * @return Returns a JSON Object representation of the student
	 */
	public static JSONObject getStudentJSON(Student student) {
		HashMap<String, Object> studentDetails = new HashMap<String, Object>();
		studentDetails.put(USER_ID, student.getUUID().toString());
		studentDetails.put(USER_USER_NAME, student.getUserName());
		studentDetails.put(USER_PASSWORD, student.getPassword());
		studentDetails.put(STUDENT_FIRST_NAME, student.getFirstName());
		studentDetails.put(STUDENT_LAST_NAME, student.getLastName());
		studentDetails.put(STUDENT_GRAD_YEAR, student.getGradYear());
		studentDetails.put(USER_EMAIL, student.getEmail());
		studentDetails.put(STUDENT_ADDRESS, student.getAddress());
		studentDetails.put(STUDENT_PHONE_NUMBER, student.getPhone());
		studentDetails.put(STUDENT_GPA, student.getGPA());
		studentDetails.put(STUDENT_SHOW_GPA, student.getShowGPA());
		studentDetails.put(USER_RATING, student.getRating());
		studentDetails.put(STUDENT_RESUME_ID, student.getResume().getID().toString());
		
		JSONArray JSONReviews = new JSONArray();
		for (int i = 0; i < student.getReviews().size(); i++) {
			HashMap<String, Object> reviewDetails = new HashMap<String, Object>();
			Review review = student.getReviews().get(i);
			reviewDetails.put(REVIEW_RATING, review.getRating());
			reviewDetails.put(REVIEW_WRITER, review.getWriter());
			reviewDetails.put(REVIEW_COMMENT, review.getComment());
			JSONObject reviewDetailsJSON = new JSONObject(reviewDetails);

			JSONReviews.add(reviewDetailsJSON);
		}

		studentDetails.put(STUDENT_REVIEWS, JSONReviews);

		JSONArray JSONFavPosts = new JSONArray();
		for (int i = 0; i < student.getFavoritePosts().size(); i++) {
			HashMap<String, Object> favPostsDetails = new HashMap<String, Object>();
			InternshipPost post = student.getFavoritePosts().get(i);
			favPostsDetails.put(INTERNSHIP_ID, post.getUUID().toString());
			JSONObject favPostsDetailsJSON = new JSONObject(favPostsDetails);

			JSONFavPosts.add(favPostsDetailsJSON);
		}

		studentDetails.put(STUDENT_FAV_POSTS, JSONFavPosts);

		JSONObject studentDetailsJSON = new JSONObject(studentDetails);
		return studentDetailsJSON;
	}

	/**
	 * Converts an employer to a JSON Object
	 * @param employer The employer that is being converted
	 * @return Returns a JSON Object representation of the employer
	 */
	public static JSONObject getEmployerJSON(Employer employer) {
		HashMap<String, Object> employerDetails = new HashMap<String, Object>();
		employerDetails.put(USER_ID, employer.getUUID().toString());
		employerDetails.put(EMPLOYER_TITLE, employer.getTitle());
		employerDetails.put(USER_USER_NAME, employer.getUsername());
		employerDetails.put(USER_PASSWORD, employer.getPassword());
		employerDetails.put(USER_EMAIL, employer.getEmail());
		employerDetails.put(USER_RATING, employer.getRating());
		employerDetails.put(EMPLOYER_LOCATION, employer.getLocation());
		employerDetails.put(EMPLOYER_MISSION, employer.getMission());
		
		JSONArray JSONReviews = new JSONArray();
		for (int i = 0; i < employer.getReviews().size(); i++) {
			HashMap<String, Object> reviewDetails = new HashMap<String, Object>();
			Review review = employer.getReviews().get(i);
			reviewDetails.put(REVIEW_RATING, review.getRating());
			reviewDetails.put(REVIEW_WRITER, review.getWriter());
			reviewDetails.put(REVIEW_COMMENT, review.getComment());
			JSONObject reviewDetailsJSON = new JSONObject(reviewDetails);

			JSONReviews.add(reviewDetailsJSON);
		}

		employerDetails.put(EMPLOYER_REVIEWS, JSONReviews);

		JSONObject employerDetailsJSON = new JSONObject(employerDetails);

		return employerDetailsJSON;
	}

	/**
	 * Converts an admin to a JSON Object
	 * @param admin The admin that is being converted
	 * @return Returns a JSON Object representation of the admin
	 */
	public static JSONObject getAdminJSON(Admin admin) {
		HashMap<String, Object> adminDetails = new HashMap<String, Object>();
		adminDetails.put(USER_ID, admin.getID().toString());
		adminDetails.put(USER_USER_NAME, admin.getUsername());
		adminDetails.put(USER_PASSWORD, admin.getPassword());

		JSONObject adminDetailsJSON = new JSONObject(adminDetails);
		
		return adminDetailsJSON;
	}

	/**
	 * Converts an internship post to a JSON Object
	 * @param post The internship post that is being converted
	 * @return Returns a JSON Object representation of the post
	 */
	public static JSONObject getPostJSON(InternshipPost post) {
		HashMap<String, Object> postDetails = new HashMap<String, Object>();
		postDetails.put(USER_ID, post.getUUID().toString());
		postDetails.put(INTERNSHIP_EMPLOYER_TITLE, post.getEmployerTitle());
		postDetails.put(INTERNSHIP_POSITION_TITLE, post.getPosTitle());
		postDetails.put(INTERNSHIP_DESCRIPTION, post.getDescription());
		postDetails.put(INTERNSHIP_LOCATION, post.getLocation());
		postDetails.put(INTERNSHIP_START_DATE, post.getStartDate());
		postDetails.put(INTERNSHIP_END_DATE, post.getEndDate());
		postDetails.put(INTERNSHIP_IS_REMOTE, post.getRemote());
		postDetails.put(INTERNSHIP_IS_OPEN, post.getOpen());
		postDetails.put(INTERNSHIP_LOW_PAY, post.getLowPay());
		postDetails.put(INTERNSHIP_HIGH_PAY, post.getHighPay());
		
		JSONArray JSONSkills = new JSONArray();
		for (int i = 0; i < post.getSkillReq().size(); i++) {
			HashMap<String, Object> skillDetails = new HashMap<String, Object>();
			Skill skill = post.getSkillReq().get(i);
			skillDetails.put(SKILLS_SKILL, skill.toString());
			JSONObject skillDetailsJSON = new JSONObject(skillDetails);

			JSONSkills.add(skillDetailsJSON);
		}

		postDetails.put(INTERNSHIP_SKILLS_REQ, JSONSkills);

		JSONObject postDetailsJSON = new JSONObject(postDetails);
		return postDetailsJSON;
	}

	/**
	 * Converts the resume to a JSON Object
	 * @param resume The resume that is being converted
	 * @return Returns a JSON Object representation of the resume
	 */
	public static JSONObject getResumeJSON(Resume resume) {
		HashMap<String, Object> resumeDetails = new HashMap<String, Object>();
		resumeDetails.put(USER_ID, resume.getID().toString());
		
		
		JSONArray JSONEducations = new JSONArray();
		for (int i = 0; i < resume.getEducations().size(); i++) {
			HashMap<String, Object> eduDetails = new HashMap<String, Object>();
			Education edu = resume.getEducations().get(i);
			eduDetails.put(EDUCATION_SCHOOL, edu.getSchoolTitle());
			eduDetails.put(EDUCATION_LOCATION, edu.getLocation());
			eduDetails.put(EDUCATION_MAJOR, edu.getMajor());
			eduDetails.put(EDUCATION_GRAD_YEAR, edu.getGradYear());
			eduDetails.put(RESUME, edu.getResume());
			JSONObject eduDetailsJSON = new JSONObject(eduDetails);

			JSONEducations.add(eduDetailsJSON);
		}

		resumeDetails.put(RESUME_EDUCATIONS, JSONEducations);

		JSONArray JSONSkills = new JSONArray();
		for (int i = 0; i < resume.getSkills().size(); i++) {
			HashMap<String, Object> skillDetails = new HashMap<String, Object>();
			List keys = new ArrayList(resume.getSkills().keySet());
			Object skillObj = keys.get(i);
			Boolean resumeBoolean = resume.getSkills().get(skillObj);
			skillDetails.put(SKILLS_SKILL, skillObj.toString());
			skillDetails.put(RESUME, resumeBoolean);
			JSONObject skillDetailsJSON = new JSONObject(skillDetails);

			JSONSkills.add(skillDetailsJSON);
		}

		resumeDetails.put(RESUME_SKILLS, JSONSkills);

		JSONArray JSONCourses = new JSONArray();
		for (int i = 0; i < resume.getCourses().size(); i++) {
			HashMap<String, Object> courseDetails = new HashMap<String, Object>();
			List keys = new ArrayList(resume.getCourses().keySet());
			Object courseObj = keys.get(i);
			courseDetails.put(COURSES_COURSE, courseObj.toString());
			courseDetails.put(RESUME, resume.getCourses().get(courseObj));
			JSONObject courseDetailsJSON = new JSONObject(courseDetails);

			JSONCourses.add(courseDetailsJSON);
		}

		resumeDetails.put(RESUME_COURSES, JSONCourses);

		JSONArray JSONWorks = new JSONArray();
		for (int i = 0; i < resume.getWork().size(); i++) {
			HashMap<String, Object> workDetails = new HashMap<String, Object>();
			WorkExperience work = resume.getWork().get(i);
			workDetails.put(WORK_POSITION_TITLE, work.getPosTitle());
			workDetails.put(WORK_EMPLOYER, work.getEmployerTitle());
			workDetails.put(WORK_LOCATION, work.getLocation());
			workDetails.put(WORK_START_DATE, work.getStartDate());
			workDetails.put(WORK_END_DATE, work.getEndDate());

			JSONArray JSONDescription = new JSONArray();
			for (int j = 0; j < work.getDescription().size(); j++) {
				HashMap<String, Object> descriptionDetails = new HashMap<String, Object>();
				String string = work.getDescription().get(j);
				descriptionDetails.put(DESCRIPTION_STRING, string);
				JSONObject descriptionDetailsJSON = new JSONObject(descriptionDetails);

				JSONDescription.add(descriptionDetailsJSON);
			}


			workDetails.put(WORK_DESCRIPTION, JSONDescription);
			workDetails.put(RESUME, work.getResume());
			JSONObject workDetailsJSON = new JSONObject(workDetails);

			JSONWorks.add(workDetailsJSON);
		}

		resumeDetails.put(RESUME_WORK, JSONWorks);

		JSONArray JSONExtras = new JSONArray();
		for (int i = 0; i < resume.getExtracurriculars().size(); i++) {
			HashMap<String, Object> extraDetails = new HashMap<String, Object>();
			Extracurricular extra = resume.getExtracurriculars().get(i);
			extraDetails.put(EXTRA_TITLE, extra.getTitle());
			extraDetails.put(EXTRA_POSITION, extra.getPosition());
			extraDetails.put(EXTRA_START_DATE, extra.getStartDate());
			extraDetails.put(EXTRA_END_DATE, extra.getEndDate());
			extraDetails.put(RESUME, extra.getResume());
			JSONObject extraDetailsJSON = new JSONObject(extraDetails);

			JSONExtras.add(extraDetailsJSON);
		}

		resumeDetails.put(RESUME_EXTRACURRICULARS, JSONExtras);

		JSONArray JSONHonors = new JSONArray();
		for (int i = 0; i < resume.getHonors().size(); i++) {
			HashMap<String, Object> honorDetails = new HashMap<String, Object>();
			Honor honor = resume.getHonors().get(i);
			honorDetails.put(HONORS_TITLE, honor.getTitle());
			honorDetails.put(HONORS_ORGAN, honor.getOrganization());
			honorDetails.put(HONORS_DESCRIPTION, honor.getDescription());
			honorDetails.put(HONORS_YEAR, honor.getYear());
			honorDetails.put(RESUME, honor.getResume());
			JSONObject honorDetailsJSON = new JSONObject(honorDetails);

			JSONHonors.add(honorDetailsJSON);
		}

		resumeDetails.put(RESUME_HONORS, JSONHonors);

		JSONObject resumeDetailsJSON = new JSONObject(resumeDetails);
		return resumeDetailsJSON;
	}

	/**
	 * Converts the arraylist of applicants in a post to a JSON Object
	 * @param post The post with the arraylist of applicants
	 * @return Returns a JSON Object representation of the post and its applicants
	 */
	public static JSONObject getApplicationJSON(InternshipPost post) {
		HashMap<String, Object> applicationDetails = new HashMap<String, Object>();

		applicationDetails.put(APPLICATIONS_POST_ID, post.getUUID().toString());
		
		JSONArray JSONApplicants = new JSONArray();
		for (int i = 0; i < post.getApplicants().size(); i++) {
			HashMap<String, Object> applicantDetails = new HashMap<String, Object>();
			String studentID = post.getApplicants().get(i).getUUID().toString();
			applicantDetails.put(APPLICATIONS_STUDENT_ID, studentID);
			JSONObject applicantDetailsJSON = new JSONObject(applicantDetails);

			JSONApplicants.add(applicantDetailsJSON);
		}

		applicationDetails.put(APPLICATIONS_APPLICANTS, JSONApplicants);
		String employerID = EmployerList.getInstance().getEmployerByTitle(post.getEmployerTitle()).getUUID().toString();
		applicationDetails.put(APPLICATIONS_EMPLOYER_ID, employerID);

		JSONObject applicationDetailsJSON = new JSONObject(applicationDetails);
		return applicationDetailsJSON;
	}
}