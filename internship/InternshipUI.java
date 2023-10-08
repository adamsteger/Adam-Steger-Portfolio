import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Scanner;

public class InternshipUI {
    private InternshipApplication internApp = new InternshipApplication();
    private static final String WELCOME_MESSAGE = "Welcome to our UrTern";
    private String[] startMenuOptions = { "Exit Program", "Sign in as Admin", "Sign in as Student",
            "Sign in as Employer", "Create Student account", "Create Employer account" };
    private String[] studentHomeOptions = { "Sign out", "See/Edit Educations", "See/Edit Extra Curriculars",
            "See/Edit Work Experiences", "See/Edit Honors", "See/Edit Courses", "See/Edit Skills", "See Resume",
            "Browse Internship Posts", "Print Resume to a File", "Leave a Review" };
    private String[] employerHomeOptions = { "Sign out", "See/Edit internship posts", "See Reviews",
            "Leave Review on a Student" };
    private String[] studentInternshipOptions = { "Go Home", "Sort by Pay", "Filter By Location",
            "Filter By Position Title", "Filter By Company Name", "Filter By Skill", "Apply to a Listing" };
    private String[] employerInternshipOptions = { "Go Home", "View applicants", "Add Post", "Remove Post" };
    private String[] adminHomeOptions = { "Sign out", "Delete Post", "Delete Review" };
    private Scanner scanner = new Scanner(System.in);
    private String[] skills = { "JAVA", "C", "PYTHON", "CPP", "VBNET", "CPOUND", "PHP", "JAVASCRIPT", "SQL",
            "OBJECTIVEC", "RUBY", "MATLAB", "SWIFT", "GO", "PERL", "R", "HTML" };

    public static void main(String[] args) {
        InternshipUI iUI = new InternshipUI();

        iUI.run();

    }

    InternshipUI() {
    }

    public void run() {
        boolean run = true;
        while (run) {
            displayStartMenu();
            int userOpt = getUserOpt(startMenuOptions.length);
            run = executeStartOpt(userOpt);
        }

        System.out.println("Shutting Down");
        System.exit(1);
    }

    private int getUserOpt(int numOfOpts) {
        while (true) {
            System.out.print("\nWhat would you like to do?: ");

            String input = scanner.nextLine();
            int command = Integer.parseInt(input);
            if (command >= 0 && command <= numOfOpts - 1) {
                return command;
            } else {
                System.out.print("Invalid input. Please Try again");
            }
        }

    }

    private boolean yesNo() {
        boolean ret = true;
        while (ret) {
            System.out.print(" (Y/N): ");
            String ans = scanner.nextLine();
            if (!ans.equalsIgnoreCase("y") && !ans.equalsIgnoreCase("n")) {
                System.out.println("Invalid Input.");
            } else if (ans.equalsIgnoreCase("y")) {
                break;
            } else {
                ret = false;
            }
        }
        return ret;
    }

    private void displayStartMenu() {
        System.out.println("~~ " + WELCOME_MESSAGE + " ~~\n");
        for (int i = 1; i < startMenuOptions.length; i++) {
            System.out.println("    " + (i) + ". " + startMenuOptions[i]);
        }
        System.out.println("    0. " + startMenuOptions[0]);
    }

    private void displayStudentHome(Student student) {
        System.out.println("\n~~ " + student.getName() + " ~~");

        System.out.println("Email: " + student.getEmail());
        System.out.println("Phone: " + student.getPhone());
        System.out.println("Rating: " + student.getRating() + "/5 stars");
        System.out.println("Skill(s): " + student.getResume().getSkills().size());
        System.out.println("Educations(s): " + student.getResume().getEducations().size());

        System.out.println("ExtraCurricular(s): " + student.getResume().getExtracurriculars().size());
        System.out.println("Work Experience(s): " + student.getResume().getWork().size());
        System.out.println("Honor(s): " + student.getResume().getHonors().size());

        System.out.println("\nWhat would you like to do?");
        for (int i = 1; i < studentHomeOptions.length; i++) {
            System.out.println("    " + (i) + ". " + studentHomeOptions[i]);
        }
        System.out.println("    " + ("0") + ". " + studentHomeOptions[0]);
    }

    private void displayEmployerHome(Employer employer) {
        System.out.println("~~ " + employer.getUsername() + " ~~\n");

        System.out.println(employer.getMission() + "\n");

        System.out.println("Email: " + employer.getEmail());
        System.out.println("Rating: " + employer.getRating() + "/5 stars");
        System.out.println("Location: " + employer.getLocation());
        System.out.println("Post(s): " + employer.getPosts().size());

        System.out.println("\nWhat would you like to do?");
        for (int i = 1; i < employerHomeOptions.length; i++) {
            System.out.println("    " + (i) + ". " + employerHomeOptions[i]);
        }
        System.out.println("    " + ("0") + ". " + studentHomeOptions[0]);

    }

    private void displayEmployerInternships(Employer employer) {
        System.out.println("~~ See/Edit My internship Post(s) ~~\n");
        ArrayList<InternshipPost> posts = internApp.getInternships(employer);
        for (InternshipPost post : posts) {
            int num = posts.indexOf(post) + 1;
            System.out.println(num + ". " + post + "\n");
        }

    }

    private boolean displayAplicants(InternshipPost post) {
        boolean run = true;
        System.out.println("~~ See/Edit My internship Post(s) ~~\n");
        ArrayList<Student> applicants = internApp.getApplicants(post);
        for (Student applicant : applicants) {
            int num = applicants.indexOf(applicant) + 1;
            System.out.println(num + ". " + applicant);
        }
        System.out.println("\n 1. View an applicants Resume\n 0. Go back");
        int userOpt = getUserOpt(2);
        switch (userOpt) {
        case (0):
            return false;
        case (1):
            System.out.println("Which applicant: ");
            int ind = scanner.nextInt() - 1;
            scanner.nextLine();
            System.out.println(applicants.get(ind).getResume().toString());// to txt file?
            break;
        }
        return true;
    }

    private void displayStudentInternships(Student student, ArrayList<InternshipPost> posts) {

        for (int i = 0; i < posts.size(); i++) {
            System.out.println("\n" + (i + 1) + ". " + posts.get(i).toStringNoApplicants());
        }

        System.out.println("\nWhat would you like to do?");
        for (int i = 1; i < studentInternshipOptions.length; i++) {
            System.out.println("    " + (i) + ". " + studentInternshipOptions[i]);
        }
        System.out.println("    " + ("0") + ". " + studentInternshipOptions[0]);

    }

    private void displayAdminHome(Admin admin) {
        System.out.println("~~ " + admin.getUsername() + " ~~" + "\n");

        System.out.println("\nWhat would you like to do?");
        for (int i = 1; i < adminHomeOptions.length; i++) {
            System.out.println("    " + (i) + ". " + adminHomeOptions[i]);
        }
        System.out.println("    " + ("0") + ". " + studentHomeOptions[0]);
    }

    private boolean executeStartOpt(int opt) {
        boolean run = true;
        Student student;
        Employer employer;
        Admin admin;
        switch (opt) {
        case (0):// exit
            return false;

        case (1):// Sign in as Admin
            admin = adminSignIn();
            while (run) {
                displayAdminHome(admin);
                int userOpt = getUserOpt(adminHomeOptions.length);
                run = executeAdminHomeOpt(admin, userOpt);
            }

            break;

        case (2):// Sign in as Student
            student = studentSignIn();
            while (run) {
                displayStudentHome(student);
                int userOpt = getUserOpt(studentHomeOptions.length);
                run = executeStudentHomeOpt(student, userOpt);
            }

            break;
        case (3):// Sign in as Employer
            employer = employerSignIn();
            while(run) {
                displayEmployerHome(employer);
                int userOpt = getUserOpt(employerHomeOptions.length);
                run = executeEmployerHomeOpt(employer, userOpt);
            }
            break;

        case (4):// Create Student account
            student = createStudent();
            while (run) {
                displayStudentHome(student);
                int userOpt = getUserOpt(studentHomeOptions.length);
                executeStudentHomeOpt(student, userOpt);

                break;
            }

            break;
        case (5):// Create Employer account
            employer = createEmployer();
            while (run) {
                displayEmployerHome(employer);
                int userOpt = getUserOpt(employerHomeOptions.length);
                executeEmployerHomeOpt(employer, userOpt);
            }
            break;
        }

        return true;

    }

    private boolean executeStudentHomeOpt(Student student, int opt) {
        Resume res = student.getResume();
        Enumeration<String> enuStr;
        Enumeration<Skill> enuSkill;
        int i;
        switch (opt) {
        case (0):// Sign Out
            System.out.println("Logging out..\nGood Bye");
            return false;

        case (1):// See/Edit Educations
            for (i = 0; i < res.getEducations().size(); i++) {
                System.out.println((i + 1) + ". ");
                System.out.println(res.getEducations().get(i).toString());
            }
            editEducation(student);
            break;

        case (2):// See/Edit Extra Curriculars
            for (i = 0; i < res.getExtracurriculars().size(); i++) {
                System.out.println((i + 1) + ". ");
                System.out.println(res.getExtracurriculars().get(i).toString());
            }
            editExtraCurr(student);
            break;

        case (3):// See/Edit Work Experiences
            for (i = 0; i < res.getWork().size(); i++) {
                System.out.println((i + 1) + ". ");
                System.out.println(res.getWork().get(i).toString());
            }
            editWorkExp(student);
            break;

        case (4):// See/Edit Honors
            for (i = 0; i < res.getHonors().size(); i++) {
                System.out.println((i + 1) + ". ");
                System.out.println(res.getHonors().get(i).toString());
            }
            editHonor(student);
            break;

        case (5):// See/Edit Courses
            enuStr = res.getCourses().keys();
            i = 1;
            while (enuStr.hasMoreElements()) {
                System.out.println(i + ". " + enuStr.nextElement());
                i++;
            }
            editCourse(student);
            break;

        case (6):// See/Edit Skills
            System.out.println("\nSkills: ");
            enuSkill = res.getSkills().keys();
            i = 1;
            while (enuSkill.hasMoreElements()) {
                System.out.println("    " + i + ". " + enuSkill.nextElement());
                i++;
            }
            editSkill(student);
            break;

        case (7):// See Resume
            // to txt file
            System.out.println(student.resumeToString());
            break;

        case (8):// Browse Internship Posts
            ArrayList<InternshipPost> posts = internApp.getInternships();
            boolean run = true;
            while (run) {
                displayStudentInternships(student, posts);
                int userOpt = getUserOpt(studentInternshipOptions.length);
                run = executeStudentInternshipOpt(student, userOpt, posts);
            }
            break;

        case (9):// Print resume to file
            student.printResumeToFile();
            break;

        case (10):// Leave Review
            addReview(student);
            break;
        }
        return true;

    }

    private boolean executeStudentInternshipOpt(Student student, int opt, ArrayList<InternshipPost> posts) {
        int userOpt;
        switch (opt) {
        case (0):// Go Home
            return false;

        case (1):// Sort by Pay
            System.out.println("Enter desired Wage: ");
            int pay = scanner.nextInt();
            scanner.nextLine();
            posts = internApp.filterByPay(pay);
            displayStudentInternships(student, posts);
            userOpt = getUserOpt(studentInternshipOptions.length);
            return executeStudentInternshipOpt(student, userOpt, posts);

        case (2):// Filter by Loction INClude Remote
            System.out.println("Enter Location (enter \"remote\" for remote jobs): ");
            if (scanner.nextLine().equalsIgnoreCase("remote")) {
                posts = internApp.filterByRemote(true);
                displayStudentInternships(student, posts);
                userOpt = getUserOpt(studentInternshipOptions.length);
                return executeStudentInternshipOpt(student, userOpt, posts);
            } else {
                posts = internApp.filterByLocation(scanner.nextLine());
                displayStudentInternships(student, posts);
                userOpt = getUserOpt(studentInternshipOptions.length);
                return executeStudentInternshipOpt(student, userOpt, posts);
            }

        case (3):// Filter By position Title
            System.out.println("Enter Position title: ");
            posts = internApp.filterByPosTitle(scanner.nextLine());
            displayStudentInternships(student, posts);
            userOpt = getUserOpt(studentInternshipOptions.length);
            return executeStudentInternshipOpt(student, userOpt, posts);

        case (4):// filter By Company Name
            System.out.println("Enter Company Name: ");
            posts = internApp.filterByEmployerTitle(scanner.nextLine());
            displayStudentInternships(student, posts);
            userOpt = getUserOpt(studentInternshipOptions.length);
            return executeStudentInternshipOpt(student, userOpt, posts);

        case (5):// Filter By skill
            System.out.println("Enter one of the following to filter for ");
            for (String skill : skills) {
                System.out.println(skill);
            }
            System.out.print("\nSkill: ");
            Skill skill = Skill.valueOf(scanner.nextLine());
            posts = internApp.filterBySkill(skill);
            displayStudentInternships(student, posts);
            userOpt = getUserOpt(studentInternshipOptions.length);
            return executeStudentInternshipOpt(student, userOpt, posts);

        case (6):// apply to listing
            System.out.print("Which listing would you like to apply to? ");
            int temp = scanner.nextInt() - 1;
            scanner.nextLine();
            InternshipPost post = posts.get(temp);
            student.apply(post);
            posts.get(temp).toString();// for testing purposes
            break;
        }

        return true;

    }

    private boolean executeEmployerHomeOpt(Employer employer, int opt) {
        int userOpt;
        boolean run = true;
        while (run) {
            switch (opt) {
            case (0):// Sign Out
                System.out.println("Logging out..\nGood Bye");
                return false;
            case (1):// See/Edit internship posts
                displayEmployerInternships(employer);
                userOpt = getUserOpt(employerInternshipOptions.length);
                run = executeEmployerInternshipOpt(employer, userOpt);
                break;

            case (2):// See Reviews
                ArrayList<Review> reviews = employer.getReviews();
                for (Review review : reviews) {
                    System.out.println(review.toString());
                }
                break;

            case (3):// Leave Review on a Student
                addReview(employer);

                break;
            }
        }
        return true;
    }

    private boolean executeEmployerInternshipOpt(Employer employer, int opt) {
        boolean run = true;
        switch (opt) {
        case (0):// Go Home
            return false;

        case (1):// View Aplicants
            System.out.println("Which Post: ");
            int post = scanner.nextInt();
            scanner.nextLine();
            while (run) {
                run = displayAplicants(internApp.getInternships(employer).get(post + 1));
            }
            break;

        case (2):// Add Post
            addIntershipPost(employer);
            break;
        case (3):// Remove Post
            System.out.println("Which Post: ");
            post = scanner.nextInt();
            scanner.nextLine();
            internApp.removeInternship(internApp.getInternships(employer).get(post + 1));
            break;
        }

        return true;

    }

    private boolean executeAdminHomeOpt(Admin admin, int opt) {

        switch (opt) {
        case (0):// Sign Out
            System.out.println("Logging out..\nGood Bye");
            return false;
        case (1):// Delete Post

            break;

        case (2):// Delete Review

            break;
        }

        return true;
    }

    private Admin adminSignIn() {
        String userName = null;
        String pass = null;
        Admin admin;
        while (true) {
            System.out.println("~~ Admin Sign In ~~\n");
            System.out.println("Enter Username: ");
            userName = scanner.nextLine();

            System.out.println("Enter Password: ");
            pass = scanner.nextLine();

            admin = internApp.adminLogin(userName, pass);
            if (admin != null) {
                break;
            }
        }
        return admin;
    }

    private Student studentSignIn() {
        String userName = null;
        String pass = null;
        Student student;
        while (true) {
            System.out.println("\n~~ Student Sign In ~~");
            System.out.print("Enter Username: ");
            userName = scanner.nextLine();

            System.out.print("Enter Password: ");
            pass = scanner.nextLine();

            student = internApp.studentLogin(userName, pass);
            if (student != null) {
                break;
            }
        }
        return student;
    }

    private Employer employerSignIn() {
        String userName = null;
        String pass = null;
        Employer employer;
        while (true) {
            System.out.println("\n~~ Employer Sign In ~~");
            System.out.print("Enter Username: ");
            userName = scanner.nextLine();

            System.out.print("Enter Password: ");
            pass = scanner.nextLine();

            employer = internApp.employerLogin(userName, pass);
            if (employer != null) {
                break;
            }
        }
        return employer;
    }

    private Student createStudent() {
        String username, password, firstName, lastName, email, phone, address;
        double gpa;
        int gradYear;
        boolean showGPA;

        System.out.println("~~CREATE STUDENT ACCOUNT~~\n");
        username = createUsername();
        password = createPassword();

        System.out.println("Enter your first name: ");
        firstName = scanner.nextLine();

        System.out.println("Enter your last name: ");
        lastName = scanner.nextLine();

        System.out.println("Enter your email: ");
        email = scanner.nextLine();

        System.out.println("Enter your phone number: ");
        phone = scanner.nextLine();

        System.out.println("Enter your permanent address: ");
        address = scanner.nextLine();

        System.out.println("Enter your expected graduation Year: ");
        gradYear = scanner.nextInt();
        scanner.nextLine();

        System.out.println("What is your GPA: ");
        gpa = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("Add this GPA to Resume?");
        showGPA = yesNo();

        Student student = new Student(firstName, lastName, username, password, gradYear, email, address, phone, gpa,
                showGPA);

        internApp.createStudent(student);

        System.out.print("Would you like to add Work Experience(s)?");
        if (yesNo()) {
            System.out.print("How many: ");
            int num = scanner.nextInt();
            scanner.nextLine();
            addWorkExp(num);
        }

        System.out.print("Would you like to add Education(s)?");
        if (yesNo()) {
            System.out.print("How many: ");
            int num = scanner.nextInt();
            scanner.nextLine();
            addEducation(num);
        }

        System.out.print("Would you like to add Course(s)?");
        if (yesNo()) {
            System.out.print("How many: ");
            int num = scanner.nextInt();
            scanner.nextLine();
            addCourse(num);
        }

        System.out.print("Would you like to add Extracurricular(s)?");
        if (yesNo()) {
            System.out.print("How many: ");
            int num = scanner.nextInt();
            scanner.nextLine();
            addExtracurricular(num);
        }

        System.out.print("Would you like to add Honor(s)?");
        if (yesNo()) {
            System.out.print("How many: ");
            int num = scanner.nextInt();
            scanner.nextLine();
            addHonor(num);
        }

        System.out.print("Would you like to add Skill(s)?");
        if (yesNo()) {
            System.out.print("How many: ");
            int num = scanner.nextInt();
            scanner.nextLine();
            addSkill(num);
        }

        return student;

    }

    private Employer createEmployer() {
        String title, username, password, email, location, mission;

        System.out.println("~~CREATE EMPLOYER ACCOUNT~~\n");

        username = createUsername();
        password = createPassword();

        System.out.println("Enter your email: ");
        email = scanner.nextLine();

        System.out.println("Company Name: ");
        title = scanner.nextLine();

        System.out.println("Enter The Location of the Company HQ: ");
        location = scanner.nextLine();

        System.out.println("What is your Company's mission?: ");
        mission = scanner.nextLine();

        Employer employer = new Employer(title, username, password, email, location, mission);

        internApp.createEmployer(employer);

        return employer;
    }

    private String createUsername() {
        boolean loop = true;
        String userName = "";
        while (loop) {
            System.out.println("Create a Username(6-15 Characters): ");
            userName = scanner.nextLine();

            if (userName.length() >= 6 && userName.length() <= 15) {
                if (internApp.validNewUsername(userName)) {
                    break;
                } else {
                    System.out.println("This username already exists");
                }
            } else {
                System.out.println("The username must be 6-15 Characters long");
            }
        }
        return userName;
    }

    private String createPassword() {
        boolean loop = true;
        String password = null;
        while (loop) {
            System.out.println("Create a Password(8-20 Characters): ");
            password = scanner.nextLine();
            if (password.length() >= 8 && password.length() <= 20) {
                break;
            } else {
                System.out.println("Password must be 8-20 Characters long");
                continue;
            }
        }
        return password;

    }

    private void addReview(Employer employer) {
        System.out.println("Enter the username of whom you would like to leave a review on: ");
        String username = scanner.nextLine();
        Student student = StudentList.getInstance().getStudentByUser(username);
        // get instance of student name it userB

        System.out.println("How would you rate this user(1 - 5): ");
        int rating = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Comment: ");
        String comment = scanner.nextLine();

        internApp.addStudentReview(student, employer.getTitle(), rating, comment);
    }

    private void addReview(Student student) {
        System.out.println("Enter the username of whom you would like to leave a review on: ");
        String username = scanner.nextLine();
        Employer employer = EmployerList.getInstance().getEmployerByUser(username);
        // get instance of student name it userB

        System.out.println("How would you rate this employer(1 - 5): ");
        int rating = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Comment: ");
        String comment = scanner.nextLine();

        internApp.addEmployerReview(employer, student.getName(), rating, comment);
    }

    private void addWorkExp(int num) {
        for (int i = 0; i < num; i++) {
            String title, position, location, startDate, endDate;
            boolean resume;
            ArrayList<String> description = new ArrayList<String>();

            System.out.println("\n~~ADD WORK EXPERIENCE~~");

            System.out.print("Company Name: ");
            title = scanner.nextLine();

            System.out.print("Job Title: ");
            position = scanner.nextLine();

            System.out.print("Location: ");
            location = scanner.nextLine();

            System.out.print("Start Date: ");
            startDate = scanner.nextLine();

            System.out.print("End Date: ");
            endDate = scanner.nextLine();

            System.out.print("Would you like to add descriptions? ");
            if (yesNo()) {
                System.out.print("How many would you like to add? ");
                int works = scanner.nextInt();
                scanner.nextLine();
                for (int j = 0; j < works; j++) {
                    System.out.print("Enter description " + (j + 1) + ": ");
                    description.add(scanner.nextLine());
                }
            }

            System.out.print("Add this to Resume?");
            resume = yesNo();

            WorkExperience work = new WorkExperience(position, title, location, startDate, endDate, resume,
                    description);
            internApp.addWorkExperience(work);
        }
    }

    private void addEducation(int num) {
        for (int i = 0; i < num; i++) {
            String title, location, major;
            int gradYear;
            boolean resume;

            System.out.println("\n~~ADD EDUCATION~~");

            System.out.print("Institution : ");
            title = scanner.nextLine();

            System.out.print("Location : ");
            location = scanner.nextLine();

            System.out.print("Major : ");
            major = scanner.nextLine();

            System.out.print("Graduation Year: ");
            gradYear = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Add this to Resume?");
            resume = yesNo();

            Education edu = new Education(title, location, major, gradYear, resume);
            internApp.addEducation(edu);
        }

    }

    private void addIntershipPost(Employer employer) {
        ArrayList<Skill> skillReq = new ArrayList<Skill>();
        System.out.println("\n~~NEW INTERNSHIP POST~~");
        System.out.print("Enter position title: ");
        String posTitle = scanner.nextLine();
        System.out.print("Enter a description: ");
        String description = scanner.nextLine();
        System.out.print(
                "Would you like to enter skill requirements from the following?\nType \'yes\' or \'no\'\n" + skills);
        if (yesNo()) {
            System.out.print("How many skills would you like to add?");
            int number = scanner.nextInt();
            if (number >= 1 && number <= 17) {
                for (int i = 0; i < number; i++) {
                    System.out.println("Skill requirement " + (i + 1) + ": ");
                    Skill reqSkill = Skill.valueOf(scanner.nextLine());
                    skillReq.add(reqSkill);
                }
            } else {
                System.out.println("Invalid input");
                System.exit(0); // maybe change this to a loop later
            }
        }
        System.out.println("Enter start date: ");
        String startDate = scanner.nextLine();
        System.out.println("Enter end date: ");
        String endDate = scanner.nextLine();
        System.out.println("Is this internship remote? Type \'yes\' or \'no\': ");
        String remote = scanner.nextLine();
        boolean isRemote = false;
        if (yesNo()) {
            isRemote = true;
        }
        System.out.println("Is this internship currently open? Type \'yes\' or \'no\': ");
        boolean isOpen = false;
        if (yesNo()) {
            isOpen = true;
        }
        System.out.println("Enter a low pay for the internship: ");
        int lowPay = scanner.nextInt();
        System.out.println("Enter a high pay for the internship: ");
        int highPay = scanner.nextInt();
        internApp.addInternship(employer.getTitle(), posTitle, description, employer.getLocation(), skillReq, startDate,
                endDate, isRemote, isOpen, lowPay, highPay);

    }

    private void addSkill(int num) {
        for (int i = 0; i < num; i++) {
            boolean resume;
            System.out.println("Enter one of the following: ");
            for (String skill : skills) {
                System.out.println(skill);
            }

            System.out.print("\nSkill (all uppercase): ");
            Skill skill = Skill.valueOf(scanner.nextLine());

            System.out.print("Add this to Resume?");
            resume = yesNo();

            internApp.addSkill(skill, resume);
        }
    }

    private void addCourse(int num) {
        for (int i = 0; i < num; i++) {
            String course;
            boolean resume;

            System.out.print("Name of course: ");
            course = scanner.nextLine();

            System.out.print("Add this to Resume?");
            resume = yesNo();

            internApp.addCourse(course, resume);
        }
    }

    private void addExtracurricular(int num) {
        for (int i = 0; i < num; i++) {
            String title, position, startDate, endDate;
            boolean resume;

            System.out.println("~~ADD EXTRACURRICULAR~~\n");

            System.out.println("Organization: ");
            title = scanner.nextLine();

            System.out.println("Position: ");
            position = scanner.nextLine();

            System.out.println("Start Date: ");
            startDate = scanner.nextLine();

            System.out.println("End Date: ");
            endDate = scanner.nextLine();

            System.out.print("Add this to Resume?");
            resume = yesNo();

            Extracurricular excurr = new Extracurricular(title, position, startDate, endDate, resume);

            internApp.addExtracurricular(excurr);
        }

    }

    private void addHonor(int num) {

        for (int i = 0; i < num; i++) {
            String title, organization, description;
            int date;

            boolean resume;

            System.out.println("~~ADD HONOR~~\n");

            System.out.println("Organization Name: ");
            organization = scanner.nextLine();

            System.out.println("Honor Title: ");
            title = scanner.nextLine();

            System.out.println("Descrition: ");
            description = scanner.nextLine();

            System.out.println("Year Recieved: ");
            date = scanner.nextInt();

            System.out.print("Add this to Resume?");
            resume = yesNo();

            Honor honor = new Honor(title, organization, description, date, resume);

            internApp.addHonor(honor);
        }
    }

    private void editWorkExp(Student student) {

        System.out.println("    1. Remove a Work Experience");
        System.out.println("    2. Add a Work Experience");
        System.out.println("    0. Go Home");

        switch (getUserOpt(3)) {
        case (0):
            displayStudentHome(student);
            break;

        case (1):
            System.out.println("Which Work Experience would you like to remove?");
            int temp = scanner.nextInt() - 1;
            scanner.nextLine();
            internApp.removeWorkExperience(student.getResume().getWork().get(temp));
            break;

        case (2):
            System.out.print("How many: ");
            int num = scanner.nextInt();
            scanner.nextLine();
            addWorkExp(num);
            break;
        }

    }

    private void editEducation(Student student) {

        System.out.println("    1. Remove an Education");
        System.out.println("    2. Add an Education");
        System.out.println("    0. Go Home");

        switch (getUserOpt(3)) {
        case (0):
            displayStudentHome(student);
            break;
        case (1):
            System.out.print("Which Education would you like to remove?");
            int temp = scanner.nextInt() - 1;
            scanner.nextLine();
            internApp.removeEducation(student.getResume().getEducations().get(temp));
            break;

        case (2):
            System.out.print("How many: ");
            int num = scanner.nextInt();
            scanner.nextLine();
            addEducation(num);
            break;
        }

    }

    private void editExtraCurr(Student student) {

        System.out.println("    1. Remove an Extracurricular");
        System.out.println("    2. Add an Extracurricular");
        System.out.println("    0. Go Home");

        switch (getUserOpt(3)) {
        case (0):
            displayStudentHome(student);
            break;

        case (1):
            System.out.println("Which Extracurricular would you like to remove?");
            int temp = scanner.nextInt() - 1;
            scanner.nextLine();
            internApp.removeExtracurricular(student.getResume().getExtracurriculars().get(temp));
            break;

        case (2):
            System.out.print("How many: ");
            int num = scanner.nextInt();
            scanner.nextLine();
            addExtracurricular(num);
            break;
        }

    }

    private void editCourse(Student student) {

        System.out.println("    1. Remove a Course");
        System.out.println("    2. Add a Course");
        System.out.println("    0. Go Home");

        switch (getUserOpt(3)) {
        case (0):
            displayStudentHome(student);
            break;

        case (1):
            System.out.println("Which Course would you like to remove?(case senseitive)");
            String temp = scanner.nextLine();
            // add a check
            internApp.removeCourse(temp);
            break;

        case (2):
            System.out.print("How many: ");
            int num = scanner.nextInt();
            scanner.nextLine();
            addCourse(num);
            break;
        }

    }

    private void editHonor(Student student) {

        System.out.println("    1. Remove a Honor");
        System.out.println("    2. Add a Honor");
        System.out.println("    0. Go Home");

        switch (getUserOpt(3)) {
        case (0):
            displayStudentHome(student);
            break;

        case (1):
            System.out.println("Which Honor would you like to remove?");
            int temp = scanner.nextInt() - 1;
            scanner.nextLine();
            internApp.removeHonor(student.getResume().getHonors().get(temp));
            break;

        case (2):
            System.out.print("How many: ");
            int num = scanner.nextInt();
            scanner.nextLine();
            addHonor(num);
            break;
        }

    }

    private void editSkill(Student student) {
        System.out.println("\nOptions: ");
        System.out.println("    1. Remove a Skill");
        System.out.println("    2. Add a Skill");
        System.out.println("    0. Go Home");

        switch (getUserOpt(3)) {
        case (0):
            displayStudentHome(student);
            break;
        case (1):
            System.out.println("Which Skill would you like to remove?(case senseitive)");
            String temp = scanner.nextLine();
            // add a check
            Skill skill = Skill.valueOf(temp);
            internApp.removeSkill(skill);
            break;

        case (2):
            System.out.print("How many: ");
            int num = scanner.nextInt();
            scanner.nextLine();
            addSkill(num);
            break;
        }

    }

}
