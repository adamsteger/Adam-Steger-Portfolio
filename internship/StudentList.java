import java.util.ArrayList;
import java.util.UUID;

/**
 * A singleton for the list of students
 * @author Byte Me
 */
public class StudentList {
    private static StudentList studentList;
    private ArrayList<Student> students;
    
    /**
     * Creates an instance of student list by loading the JSON file
     */
    private StudentList() {
        students = DataLoader.getStudents();
    }

    /**
     * Retrieves a student list and ensures only 1 exists at a time
     * @return Returns an instance of student list
     */
    public static StudentList getInstance() {
        if(studentList == null)
            studentList = new StudentList();
        return studentList;
    }

    /**
     * Checks to see if a student with the given username is in the list
     * @param username A string of the username being checked
     * @return Returns a boolean that is true if the list has the student with the given username
     */
    public boolean haveStudent(String username) {
        for(Student student : students) {
            if(student.getUserName().equals(username)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Accesses the arraylist of students
     * @return Returns the arraylist of students
     */
    public ArrayList<Student> getStudents() {
        return students;
    }

    /**
     * Searches the list of students for a given username
     * @param username A string of the username being searched for
     * @return Returns the student with the given username
     */
    public Student getStudentByUser(String username) {
        for(Student student : students) {
            if(student.getUserName().equals(username)) {
                return student;
            }
        }
        return null;
    }
    
    /**
     * Searches the list of students for a given ID
     * @param id The UUID that is being searched for
     * @return Returns the employer with the given UUID
     */
    public Student getStudentByID(UUID id) {
        for (Student student : students) {
            if (student.getUUID().equals(id)) {
                return student;
            }
        }
        return null;
    }

    /**
     * Adds a student to the list
     * @param student The student that is being added
     * @return Returns true if the student is successfully added and false if not
     */
    public boolean addStudent(Student student) { 
        if(student == null || haveStudent(student.getUserName())) 
            return false;
        
        students.add(student);
        this.save();
        return true;
    }

    /**
     * Saves the student list to the JSON file
     */
    public void save() {
        DataWriter.saveStudents();
    }
}
