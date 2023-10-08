import java.util.ArrayList;
import java.util.UUID;

/**
 * A singleton for the list of admins
 * @author Byte Me
 */
public class AdminList {
    private static AdminList adminList;
    private ArrayList<Admin> admins;
    
    /**
     * Creates an instance of admin list by loading the JSON files
     */
    private AdminList() {
        admins = DataLoader.getAdmins();
    }

    /**
     * Retrieves an admin list and ensures only 1 exists at a time
     * @return Returns an instance of the admin list
     */
    public static AdminList getInstance() {
        if(adminList == null) 
            adminList = new AdminList();
        return adminList;
    }

    /**
     * Checks to see if an admin with the given username is in the list
     * @param username A string of the username that is being checked
     * @return Returns a boolean that is true if the list has an admin with the username and false if not
     */
    public boolean haveAdmin(String username) {
        for(Admin admin : admins) {
            if(admin.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Accesses the arraylist of admins
     * @return Returns the arraylist of admins
     */
    public ArrayList<Admin> getAdmins() {
        return admins;
    }

    /**
     * Searches the list of admins for a given username
     * @param username A string of the username that is being searched for
     * @return Returns the admin that has the given username
     */
    public Admin getAdminByUser(String username) {
        for(Admin admin : admins) {
            if(admin.getUsername().equals(username)) {
                return admin;
            }
        }
        return null;
    }

    /**
     * Searches the list of admins for a given UUID
     * @param id The UUID that is being searched for
     * @return Returns the admin that has the given UUID
     */
    public Admin getAdminByID(UUID id) {
        for(Admin admin : admins) {
            if(admin.getUUID().equals(id)) {
                return admin;
            }
        }
        return null;
    }

    /**
     * Adds an admin to the admin list
     * @param admin the admin that is being added
     * @return Returns true if the admin is successfully added and false if not
     */
    public boolean addAdmin(Admin admin){
        if(haveAdmin(admin.getUsername()))
            return false;

        admins.add(admin);
        return true;
    }
    /**
     * Saves the admin list to the JSON file
     */
    public void save() {
        DataWriter.saveAdmins();
    }
}

