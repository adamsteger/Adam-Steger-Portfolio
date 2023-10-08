package com.capstone.lifesabit.gateguard;

import java.beans.PropertyVetoException;
import java.sql.*;
import java.util.ArrayList;
import java.util.UUID;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.capstone.lifesabit.gateguard.login.Member;
import com.capstone.lifesabit.gateguard.login.Member.MemberType;
import com.capstone.lifesabit.gateguard.passes.Pass;
import com.capstone.lifesabit.gateguard.settings.AdminSettings;
import com.capstone.lifesabit.gateguard.settings.UserSettings;
import com.capstone.lifesabit.gateguard.notifications.Notification;
import com.capstone.lifesabit.gateguard.notifications.NotificationType;
import com.mchange.v2.c3p0.ComboPooledDataSource;

/*
 * Singleton to access the database
 */
@Configuration
@ComponentScan(basePackages = "com.capstone.lifesabit.gateguard")
@Component
public class SQLLinker implements DisposableBean {
    private ComboPooledDataSource cpds = new ComboPooledDataSource();
    private static SQLLinker linker;

    private static final String SQL_CREATE_USER_TABLE = "CREATE TABLE IF NOT EXISTS UserTable"
            + "("
            + "user_id VARCHAR(36) PRIMARY KEY,"
            + "username varchar(32) NOT NULL,"
            + "hashed_password VARCHAR(256),"
            + "first_name VARCHAR(32) NOT NULL,"
            + "last_name VARCHAR(32) NOT NULL,"
            + "phone varchar(15) NOT NULL,"
            + "email VARCHAR(64) NOT NULL,"
            + "user_type VARCHAR(16) NOT NULL"
            + ")";

    private static final String SQL_CREATE_PASS_TABLE = "CREATE TABLE IF NOT EXISTS PassTable"
            + "("
            + "pass_id VARCHAR(36) PRIMARY KEY,"
            + "user_id VARCHAR(36),"
            + "usage_based boolean NOT NULL,"
            + "first_name VARCHAR(32) NOT NULL,"
            + "last_name varchar(32) NOT NULL,"
            + "email varchar(64) NOT NULL,"
            + "expired boolean NOT NULL,"
            + "expiration_date BIGINT,"
            + "uses_left int,"
            + "uses_total int"
            // +"CONSTRAINT fk_user"
            // +"FOREIGN KEY(user_id)"
            // +"REFERENCES UserTable(user_id)"
            + ")";

    private static final String SQL_CREATE_USER_SETTINGS_TABLE = "CREATE TABLE IF NOT EXISTS UserSettingsTable"
            + "("
            + "user_id varchar(36) PRIMARY KEY,"
            + "NOTIFICATIONS_MARKETING BOOLEAN NOT NULL DEFAULT 'true',"
            + "NOTIFICATIONS_PASS_USAGE BOOLEAN NOT NULL DEFAULT 'true',"
            + "NOTIFICATIONS_PASS_EXPIRATION BOOLEAN NOT NULL DEFAULT 'true',"
            + "NOTIFICATIONS_PASS_EXPIRES_SOON BOOLEAN NOT NULL DEFAULT 'true',"
            + "NOTIFICATIONS_ACCOUNT_SIGN_IN BOOLEAN NOT NULL DEFAULT 'true',"
            + "LIGHT_MODE BOOLEAN NOT NULL DEFAULT 'false'"
            + ")";

    private static final String SQL_CREATE_NOTIFICATIONS_TABLE = "CREATE TABLE IF NOT EXISTS NotificationsTable"
            + "("
            + "notification_id VARCHAR(36) PRIMARY KEY,"
            + "TITLE varchar(128) NOT NULL,"
            + "TYPE varchar(64) NOT NULL,"
            + "TIMESTAMP BIGINT NOT NULL,"
            + "pass_id VARCHAR(36) NOT NULL,"
            + "user_id VARCHAR(36) NOT NULL,"
            + "READ BOOLEAN NOT NULL,"
            + "ip_address VARCHAR(45)"
            + ")";
    
    private static final String SQL_CREATE_ADMIN_SETTINGS_TABLE = "CREATE TABLE IF NOT EXISTS AdminSettingsTable"
            + "("
            + "max_pass_duration int NOT NULL,"
            + "max_pass_usage int NOT NULL,"
            + "max_passes_per_user int NOT NULL"
            + ")";

    public static void main(String[] args) {
        SQLLinker sqlLinker = new SQLLinker();
        sqlLinker.createAllTables();
    }

    /*
     * Creates the instance of the class and all the tables in the database
     * @return SQLLinker returns the instance of the class
     */
    @Bean
    public SQLLinker start() {
        linker = new SQLLinker();
        linker.createAllTables();
        return linker;
    }

    /*
     * Constructor to link to database
     */
    public SQLLinker() {
        try {
            cpds.setDriverClass("org.postgresql.Driver");
            cpds.setJdbcUrl("jdbc:postgresql://localhost:5432/GateGuard");
            cpds.setUser("postgres");
            cpds.setPassword("password1234");
            cpds.setUnreturnedConnectionTimeout(10000);
            cpds.setAcquireRetryAttempts(5);
            cpds.setAcquireRetryDelay(1000);
        } catch (PropertyVetoException e) {
            e.printStackTrace();
            // handle the exception
        }
    }

    public void createAllTables() {
        this.createUserTable();
        this.createUserSettingsTable();
        this.createNotificationsTable();
        this.createPassTable();
        this.createAdminSettingsTable();
    }

    
    public void createUserTable() {
        try {
            Connection conn = cpds.getConnection();
            PreparedStatement st = conn.prepareStatement(SQL_CREATE_USER_TABLE);
            st.execute();
            st.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createAdminSettingsTable() {
        try {
            Connection conn = cpds.getConnection();
            PreparedStatement st = conn.prepareStatement(SQL_CREATE_ADMIN_SETTINGS_TABLE);
            st.execute();
            st.close();

            st = conn.prepareStatement("INSERT INTO AdminSettingsTable(max_pass_duration, max_pass_usage, max_passes_per_user) VALUES (?,?,?)");
            st.setInt(1, AdminSettings.DEFAULT_MAX_PASS_DURATION);
            st.setInt(2, AdminSettings.DEFAULT_MAX_PASS_USAGE);
            st.setInt(3, AdminSettings.DEFAULT_MAX_PASSES_PER_USER);
            st.execute();
            st.close();
            
            conn.close();
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createUserSettingsTable() {
        try {
            Connection conn = cpds.getConnection();
            PreparedStatement st = conn.prepareStatement(SQL_CREATE_USER_SETTINGS_TABLE);
            st.execute();
            st.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createPassTable() {
        try {
            Connection conn = cpds.getConnection();
            PreparedStatement st = conn.prepareStatement(SQL_CREATE_PASS_TABLE);
            st.execute();
            st.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createNotificationsTable() {
        try {
            Connection conn = cpds.getConnection();
            PreparedStatement st = conn.prepareStatement(SQL_CREATE_NOTIFICATIONS_TABLE);
            st.execute();
            st.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    /** 
     * Retrieves the member with the given user
     * @param username the username of the user being retrieved
     * @return Member returns the Member object from the database
     */
    public Member getMember(String username) {
        Member member = null;
        try {
            Connection conn = cpds.getConnection();
            PreparedStatement st = conn.prepareStatement("select * from UserTable where username = ?");
            st.setString(1, username);
            ResultSet r1 = st.executeQuery();
            if (r1.next()) {
                member = new Member(UUID.fromString(r1.getString("user_id")),
                        r1.getString("username"),
                        r1.getString("first_name"),
                        r1.getString("last_name"),
                        r1.getString("phone"),
                        r1.getString("email"),
                        r1.getString("hashed_password"),
                        Member.MemberType.valueOf(r1.getString("user_type").toUpperCase()));
            }
            st.close();
            r1.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return member;
    }

    
    /** 
     * Retrieves the member with the given ID
     * @param userID the UUID of the member being retrieved
     * @return Member returns the Member object from the database
     */
    public Member getMemberByUUID(String userID) {
        Member member = null;
        try {
            Connection conn = cpds.getConnection();
            PreparedStatement st = conn.prepareStatement("select * from UserTable where user_id = ?");
            st.setString(1, userID);
            ResultSet r1 = st.executeQuery();
            if (r1.next()) {
                member = new Member(UUID.fromString(r1.getString("user_id")),
                        r1.getString("username"),
                        r1.getString("first_name"),
                        r1.getString("last_name"),
                        r1.getString("phone"),
                        r1.getString("email"),
                        r1.getString("hashed_password"),
                        Member.MemberType.valueOf(r1.getString("user_type").toUpperCase()));
            }
            st.close();
            r1.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return member;
    }

    /** 
     * Retrieves the member with the given email
     * @param email String of the email of the member being retrieved
     * @return Member returns the Member object from the database
     */
    public Member getMemberByEmail(String email) {
        Member member = null;
        try {
            Connection conn = cpds.getConnection();
            PreparedStatement st = conn.prepareStatement("select * from UserTable where email = ?");
            st.setString(1, email);
            ResultSet r1 = st.executeQuery();
            if (r1.next()) {
                member = new Member(UUID.fromString(r1.getString("user_id")),
                        r1.getString("username"),
                        r1.getString("first_name"),
                        r1.getString("last_name"),
                        r1.getString("phone"),
                        r1.getString("email"),
                        r1.getString("hashed_password"),
                        Member.MemberType.valueOf(r1.getString("user_type").toUpperCase()));
            }
            st.close();
            r1.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return member;
    }

    
    /** 
     * Changes the password of the user with the UUID in the database
     * @param memberUUID the UUID of the member changing their password
     * @param hashedPassword String of the new hashedPassword
     * @return boolean Returns true if the update is successful and false if it is not
     */
    public boolean updatePassword(UUID memberUUID, String hashedPassword) {
        boolean success = false;

        try {
            Connection conn = cpds.getConnection();
            PreparedStatement st = conn.prepareStatement("UPDATE UserTable SET hashed_password = ? where user_id = ?");
            st.setString(1, hashedPassword);
            st.setString(2, memberUUID.toString());
            success = (st.executeUpdate() > 0);
            st.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return success;
    }

    
    
    /** 
     * Retrieves all members of the system for the security log
     * @return ArrayList<Member> Returns a list of all members in the system retrieved from the database
     */
    public ArrayList<Member> getMembers() {
        ArrayList<Member> ret = new ArrayList<>();
        try {
            Connection conn = cpds.getConnection();
            PreparedStatement st = conn.prepareStatement("select * from UserTable;");
            ResultSet r1 = st.executeQuery();
            while (r1.next()) {
                Member member = new Member(UUID.fromString(r1.getString("user_id")),
                        r1.getString("username"),
                        r1.getString("first_name"),
                        r1.getString("last_name"),
                        r1.getString("phone"),
                        r1.getString("email"),
                        r1.getString("hashed_password"),
                        Member.MemberType.valueOf(r1.getString("user_type").toUpperCase()));
                ret.add(member);
            }
            st.close();
            r1.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ret;
    }

    
    
    /** 
     * Adds a new user to the UserTable
     * @param uuid UUID of the user
     * @param username String of the username
     * @param firstName String of the first name
     * @param lastName String of the last name
     * @param phoneNumber String of the phone number
     * @param email String of the email
     * @param hashedPassword String of the password
     * @param userType MemberType of the user (USER, ADMIN, OWNER)
     * @return boolean Returns true if the add is successful and false if it is not
     */
    public boolean addUser(String uuid, String username, String firstName, String lastName, String phoneNumber,
            String email, String hashedPassword, MemberType userType) {
        boolean addSuccessful = false;

        try {
            Connection conn = cpds.getConnection();
            PreparedStatement st = conn.prepareStatement(
                    "INSERT INTO UserTable (user_id, username, hashed_password, first_name, last_name, phone, email, user_type) VALUES (?,?,?,?,?,?,?,?)");
            st.setString(1, uuid);
            st.setString(2, username);
            st.setString(3, hashedPassword);
            st.setString(4, firstName);
            st.setString(5, lastName);
            st.setString(6, phoneNumber);
            st.setString(7, email);
            st.setString(8, userType.toString().toUpperCase());
            st.executeUpdate();
            addSuccessful = true;
            st.close();
            conn.close();

        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return addSuccessful;
    }

    
    /** 
     * Edits the member with the new information in the database
     * @param member Member object that is being editted
     * @param newFirstName String of the new first name
     * @param newLastName String of the new last name
     * @param newEmail String of the new email
     * @param newUsername String of the new username
     * @param newPhoneNumber String of the new new phone number
     * @return boolean Returns true if the edit is successful and false if it is not
     */
    public boolean editMember(Member member, String newFirstName, String newLastName, String newEmail, String newUsername, String newPhoneNumber) {
        boolean editSuccessful = false;

        try {
            Connection conn = cpds.getConnection();
            PreparedStatement st = conn.prepareStatement(
                    "UPDATE UserTable SET first_name=?, last_name=?, email=?, username=?, phone=? WHERE user_id=?");
            st.setString(1, newFirstName);
            st.setString(2, newLastName);
            st.setString(3, newEmail);
            st.setString(4, newUsername);
            st.setString(5, newPhoneNumber);
            st.setString(6, member.getUuid().toString());
            st.executeUpdate();
            editSuccessful = true;
            st.close();
            conn.close();

        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return editSuccessful;
    }

    
    /** 
     * Deletes a user with the given userID from the database
     * @param userID UUID of the user being deleted
     * @return boolean Returns true if the delete is successful and false if it is not
     */
    public boolean deleteUser(UUID userID) {
        boolean deleteSuccessful = false;
        try {
            Connection conn = cpds.getConnection();
            PreparedStatement st = conn.prepareStatement("DELETE FROM UserTable where user_id = ?");
            st.setString(1, userID.toString());
            st.executeUpdate();
            deleteSuccessful = true;
            st.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return deleteSuccessful;
    }

    
    
    /** 
     * Adds a new pass to the database
     * @param pass Pass being added
     * @return boolean Returns true if the add is successful and false if it is not
     */
    public boolean addPass(Pass pass) {
        boolean addSuccessful = false;
        try {
            Connection conn = cpds.getConnection();
            PreparedStatement st = conn.prepareStatement(
                    "INSERT INTO PassTable (pass_id, user_id, usage_based, first_name, last_name, email, expired, expiration_date, uses_left, uses_total) VALUES (?,?,?,?,?,?,?,?,?,?)");
            st.setString(1, pass.getPassID().toString());
            st.setString(2, pass.getUserID().toString());
            st.setBoolean(3, pass.getUsageBased());
            st.setString(4, pass.getFirstName());
            st.setString(5, pass.getLastName());
            st.setString(6, pass.getEmail());
            st.setBoolean(7, pass.getExpired());
            st.setLong(8, pass.getUsageBased() ? -1 : pass.getExpirationDate());
            st.setInt(9, pass.getUsageBased() ? pass.getUsesLeft() : -1);
            st.setInt(10, pass.getUsageBased() ? pass.getUsesTotal() : -1);
            st.executeUpdate();
            addSuccessful = true;
            st.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return addSuccessful;
    }

    
    /** 
     * Edits a notification in the database
     * @param notifID UUID of the notification that is being updateed
     * @param notif Notification with the new information
     * @return boolean Returns true if the update is successful and false if it is not
     */
    public boolean updateNotification(UUID notifID, Notification notif) {
        boolean updateSuccessful = false;
        try {
            Connection conn = cpds.getConnection();
            PreparedStatement st = conn.prepareStatement("UPDATE NotificationsTable SET notification_id=?, pass_id=?, user_id=?, type=?, title=?, timestamp=?, read=? WHERE notification_id=?");
            st.setString(1, notif.getNotificationID().toString());
            st.setString(2, notif.getPassID().toString());
            st.setString(3, notif.getUserID().toString());
            st.setString(4, notif.getType().toString());
            st.setString(5,notif.getTitle());
            st.setLong(6, notif.getTimestamp());
            st.setBoolean(7, notif.getRead());
            st.setString(8, notif.getNotificationID().toString());
            st.executeUpdate();
            updateSuccessful = true;
            st.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return updateSuccessful;
    }

    
    /** 
     * Updates a pass in the database with new information
     * @param passID UUID of the pass being edited
     * @param pass Pass with the new information
     * @return boolean Returns true if the update is successful and false if it is not
     */
    public boolean updatePass(UUID passID, Pass pass) {
        boolean addSuccessful = false;
        try {
            Connection conn = cpds.getConnection();
            PreparedStatement st = conn.prepareStatement(
                    "UPDATE PassTable SET pass_id=?, user_id=?, usage_based=?, first_name=?, last_name=?, email=?, expired=?, expiration_date=?, uses_left=?, uses_total=? WHERE pass_id=?");
            st.setString(1, pass.getPassID().toString());
            st.setString(2, pass.getUserID().toString());
            st.setBoolean(3, pass.getUsageBased());
            st.setString(4, pass.getFirstName());
            st.setString(5, pass.getLastName());
            st.setString(6, pass.getEmail());
            st.setBoolean(7, pass.getExpired());
            st.setLong(8, pass.getUsageBased() ? -1 : pass.getExpirationDate());
            st.setInt(9, pass.getUsageBased() ? pass.getUsesLeft() : -1);
            st.setInt(10, pass.getUsageBased() ? pass.getUsesTotal() : -1);
            st.setString(11, pass.getPassID().toString());
            st.executeUpdate();
            addSuccessful = true;
            st.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return addSuccessful;
    }


    
    /** 
     * Refreshes a pass in the database (Resets uses left for usage-based passes)
     * @param pass Pass that is being refreshed
     * @return boolean Returns true if the pass is successfully refreshed and false if it is not
     */
    public boolean refreshPass(Pass pass) {
        boolean addSuccessful = false;
        try {
            Connection conn = cpds.getConnection();
            PreparedStatement st = conn.prepareStatement("UPDATE PassTable SET user_id=?, usage_based=?, first_name=?, last_name=?, email=?, expired=?, expiration_date=?, uses_left=?, uses_total=? WHERE pass_id=?");
            st.setString(1, pass.getUserID().toString());
            st.setBoolean(2, pass.getUsageBased());
            st.setString(3, pass.getFirstName());
            st.setString(4, pass.getLastName());
            st.setString(5, pass.getEmail());
            st.setBoolean(6, pass.getExpired());
            st.setLong(7, pass.getUsageBased() ? -1 : pass.getExpirationDate());
            st.setInt(8, pass.getUsageBased() ? pass.getUsesTotal() : -1);
            st.setInt(9, pass.getUsageBased() ? pass.getUsesTotal() : -1);
            st.setString(10, pass.getPassID().toString());
            st.executeUpdate();
            addSuccessful = true;
            st.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return addSuccessful;
    }

    
    /** 
     * Deletes a pass from the database
     * @param passID UUID of the pass being deleted
     * @return boolean Returns true if the delete is successful and false if it is not
     */
    public boolean deletePass(UUID passID) {
        boolean passDeleted = false;
        try {
            Connection conn = cpds.getConnection();
            PreparedStatement st = conn.prepareStatement("DELETE FROM PassTable where pass_id = ?");
            st.setString(1, passID.toString());
            st.executeUpdate();
            passDeleted = true;
            st.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return passDeleted;
    }

    
    /** 
     * Loads passes of the member from the database
     * @param member Member that owns the passes
     * @return ArrayList<Pass> Returns a list of the passes created by the given member
     */
    public ArrayList<Pass> loadPasses(Member member) {
        ArrayList<Pass> passes = new ArrayList<Pass>();
        try {
            Connection conn = cpds.getConnection();
            PreparedStatement st = conn.prepareStatement("SELECT * from PassTable where user_id = ?");
            st.setString(1, member.getUuid().toString());
            ResultSet r1 = st.executeQuery();
            while (r1.next()) {
                boolean usageBased = r1.getBoolean("usage_based");
                if (!usageBased) {
                    Pass pass = new Pass(UUID.fromString(r1.getString("pass_id")),
                            r1.getString("first_name"),
                            r1.getString("last_name"),
                            r1.getString("email"),
                            UUID.fromString(r1.getString("user_id")),
                            r1.getLong("expiration_date"));
                    passes.add(pass);
                } else if (usageBased) {
                    Pass pass = new Pass(UUID.fromString(r1.getString("pass_id")),
                            r1.getString("first_name"),
                            r1.getString("last_name"),
                            r1.getString("email"),
                            UUID.fromString(r1.getString("user_id")),
                            r1.getInt("uses_left"),
                            r1.getInt("uses_total"));
                    passes.add(pass);
                }
            }
            st.close();
            r1.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return passes;
    }

    
    /** 
     * Edits the pass with the given passID to the new information in the database (for date-based passes)
     * @param passID UUID of the pass
     * @param firstName String of the new first name
     * @param lastName String of the new last name
     * @param email String of the email
     * @param expired Boolean of whether or not it is expired
     * @param usageBased Boolean of whether the pass is usage-based or not
     * @param expirationDate Long of the expiration date of the pass
     * @return boolean Returns true if the edit is successful and false if it not
     */
    public boolean editPass(UUID passID, String firstName, String lastName, String email, boolean expired,
            boolean usageBased, long expirationDate) {
        boolean editSuccessful = false;

        try {
            Connection conn = cpds.getConnection();
            PreparedStatement st = conn.prepareStatement(
                    "UPDATE PassTable SET first_name=?, last_name=?, email=?, expired=?, usage_based=?, expiration_date=? WHERE pass_id=?");
            st.setString(1, firstName);
            st.setString(2, lastName);
            st.setString(3, email);
            st.setBoolean(4, expired);
            st.setBoolean(5, usageBased);
            st.setLong(6, expirationDate);
            st.setString(7, passID.toString());

            st.executeUpdate();
            editSuccessful = true;
            st.close();
            conn.close();

        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return editSuccessful;
    }

    
    /** 
     * Edits the pass with the given passID to the new information in the database (for usage-based passes)
     * @param passID UUID of the pass
     * @param firstName String of the new first name
     * @param lastName String of the new last name
     * @param email String of the email
     * @param expired Boolean of whether or not it is expired
     * @param usageBased Boolean of whether the pass is usage-based or not
     * @param usesTotal Integer of the total uses of the pass
     * @return boolean Returns true if the edit is successful and false if it not
     */
    public boolean editPass(UUID passID, String firstName, String lastName, String email, boolean expired,
            boolean usageBased, int usesTotal) {
        boolean editSuccessful = false;
        try {
            Connection conn = cpds.getConnection();
            PreparedStatement st = conn.prepareStatement(
                    "UPDATE PassTable SET first_name=?, last_name=?, email=?, expired=?, usage_based=?, uses_left=?, uses_total=? WHERE pass_id=?");
            st.setString(1, firstName);
            st.setString(2, lastName);
            st.setString(3, email);
            st.setBoolean(4, expired);
            st.setBoolean(5, usageBased);
            st.setInt(6, usesTotal);
            st.setInt(7, usesTotal);
            st.setString(8, passID.toString());

            st.executeUpdate();
            editSuccessful = true;
            st.close();
            conn.close();

        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return editSuccessful;
    }

    
    /** 
     * Edits both usage-based and date-based passes by calling respective methods
     * @param pass Pass that is being edited
     * @return boolean Returns true if the pass is successfully edited and false if it is not
     */
    public boolean editPass(Pass pass) {
        boolean ret = false;
        if(pass.getUsageBased()) {
            ret = this.editPass(pass.getPassID(), pass.getFirstName(), pass.getLastName(), pass.getEmail(), pass.getExpired(), pass.getUsageBased(), pass.getUsesTotal());
        } else {
            ret = this.editPass(pass.getPassID(), pass.getFirstName(), pass.getLastName(), pass.getEmail(), pass.getExpired(), pass.getUsageBased(), pass.getExpirationDate());
        }
        return ret;
    }

    
    /** 
     * Deletes all passes from the given user
     * @param userID UUID of the user with the passes being deleted
     * @return boolean Returns true if the passes are successfully deleted and false if they are not
     */
    public boolean deletePasses(UUID userID) {
        boolean deleteSuccessful = false;
        try {
            Connection conn = cpds.getConnection();
            PreparedStatement st = conn.prepareStatement("DELETE FROM PassTable where user_id = ?");
            st.setString(1, userID.toString());
            st.executeUpdate();
            deleteSuccessful = true;
            st.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return deleteSuccessful;
    }

    
    /** 
     * Adds a new user to the database with a member parameter
     * @param member Member that is being added
     * @return boolean Returns true if the member is succesfully added and false if they are not
     */
    public boolean addUser(Member member) {
        boolean status = this.addUser(member.getUuid().toString(), member.getUsername(), member.getFirstName(),
                member.getLastName(), member.getPhoneNumber(), member.getEmail(), member.getSaltedHashedPassword(),
                member.getType());
        return status;
    }

    
    /** 
     * Loads all of the notifications of the given member
     * @param member Member with the notifications being loaded
     * @return ArrayList<Notification> Returns a list of all notifications associated with the member
     */
    public ArrayList<Notification> loadNotifications(Member member) {
        ArrayList<Notification> notifications = new ArrayList<>();

        try {
            Connection conn = cpds.getConnection();
            PreparedStatement st = conn.prepareStatement("SELECT n.*, p.* FROM notificationstable n LEFT JOIN passtable p ON p.pass_id=n.pass_id WHERE n.user_id=?;");
            st.setString(1, member.getUuid().toString());
            ResultSet r1 = st.executeQuery();
            while (r1.next()) {
                UUID passID = UUID.fromString(r1.getString("pass_id"));
                String email = r1.getString("email");
                Long expirationDate = r1.getLong("expiration_date");
                int usesLeft = r1.getInt("uses_left");
                Boolean usageBased = r1.getBoolean("usage_based");
                if (r1.wasNull()) {
                    usageBased = null;
                }
                Notification notif = new Notification(UUID.fromString(r1.getString("notification_id")),
                        passID,
                        UUID.fromString(r1.getString("user_id")), NotificationType.valueOf(r1.getString("type")),
                        r1.getString("title"),
                        r1.getLong("timestamp"),
                        r1.getBoolean("read"),
                        r1.getString("ip_address"));
                notif.setEmail(email);
                notif.setUsesLeft(usesLeft);
                notif.setExpirationDate(expirationDate);
                notif.setUsageBased(usageBased);
                notifications.add(notif);
            }
                
            conn.close();
            r1.close();
            st.close();

        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return notifications;
    }

    
    /** 
     * Loads all passes from the database to check for expiration
     * @return ArrayList<Pass> Returns a list of all passes in the PassTable
     */
    public ArrayList<Pass> loadPasses() {
        ArrayList<Pass> passes = new ArrayList<>();

        try {
            Connection conn = cpds.getConnection();
            PreparedStatement st = conn.prepareStatement("SELECT * from PassTable");
            ResultSet r1 = st.executeQuery();
            while (r1.next()) {
                boolean usageBased = r1.getBoolean("usage_based");
                if (!usageBased) {
                    Pass pass = new Pass(UUID.fromString(r1.getString("pass_id")),
                            r1.getString("first_name"),
                            r1.getString("last_name"),
                            r1.getString("email"),
                            UUID.fromString(r1.getString("user_id")),
                            r1.getLong("expiration_date"));
                    passes.add(pass);
                } else if (usageBased) {
                    Pass pass = new Pass(UUID.fromString(r1.getString("pass_id")),
                            r1.getString("first_name"),
                            r1.getString("last_name"),
                            r1.getString("email"),
                            UUID.fromString(r1.getString("user_id")),
                            r1.getInt("uses_left"),
                            r1.getInt("uses_total"));
                    passes.add(pass);
                }
            }
            conn.close();
            r1.close();
            st.close();

        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return passes;
    }

    
    /** 
     * Retrieves the pass with the given ID
     * @param passID String of the ID of the pass
     * @return Pass Returns the pass object with the given ID from the database
     */
    public Pass getPass(String passID) {
        Pass ret = null;
        try {
            Connection conn = cpds.getConnection();
            PreparedStatement st = conn.prepareStatement("SELECT * FROM PassTable WHERE pass_id=?;");
            st.setString(1, passID);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                boolean usageBased = rs.getBoolean("usage_based");
                if (usageBased) {
                    ret = new Pass(UUID.fromString(rs.getString("pass_id")), rs.getString("first_name"),
                            rs.getString("last_name"), rs.getString("email"), UUID.fromString(rs.getString("user_id")),
                            rs.getInt("uses_left"), rs.getInt("uses_total"));
                } else {
                    ret = new Pass(UUID.fromString(rs.getString("pass_id")), rs.getString("first_name"),
                            rs.getString("last_name"), rs.getString("email"), UUID.fromString(rs.getString("user_id")),
                            rs.getLong("expiration_date"));
                }
            }
            conn.close();
            rs.close();
            st.close();

        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    
    /** 
     * Retrieves the notification with the given ID
     * @param notificationID String of the ID of the notification
     * @return Notification returns the Notification with the given ID from the database
     */
    public Notification getNotification(String notificationID) {
        Notification ret = null;
        try {
            Connection conn = cpds.getConnection();
            PreparedStatement st = conn.prepareStatement("SELECT * FROM NotificationsTable WHERE notification_id=?;");
            st.setString(1, notificationID);
            ResultSet rs = st.executeQuery();
            // expired, usage_based, expiration_date, uses_left, uses_total
            if (rs.next()) {
                
                ret = new Notification(UUID.fromString(rs.getString("notification_id")), 
                                    UUID.fromString(rs.getString("pass_id")), UUID.fromString(rs.getString("user_id")), 
                                    NotificationType.valueOf(rs.getString("type")), rs.getString("title"),
                                    rs.getLong("timestamp"), rs.getBoolean("read"), rs.getString("ip_address"));
                
            }
            conn.close();
            rs.close();
            st.close();

        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    
    /** 
     * Accesses notification with the given title and userID
     * @param title String of the title of the notification
     * @param userID String of the ID of the user the notification is associated with
     * @return Notification Returns the notification object from the database with the given parameters
     */
    public Notification getNotificationByTitle(String title, String userID) {
        Notification ret = null;
        try {
            Connection conn = cpds.getConnection();
            PreparedStatement st = conn.prepareStatement("SELECT * FROM NotificationsTable WHERE title=? AND user_id=?;");
            st.setString(1, title);
            st.setString(2, userID);
            ResultSet rs = st.executeQuery();
            // expired, usage_based, expiration_date, uses_left, uses_total
            if (rs.next()) {
                
                ret = new Notification(UUID.fromString(rs.getString("notification_id")), 
                                    UUID.fromString(rs.getString("pass_id")), UUID.fromString(rs.getString("user_id")), 
                                    NotificationType.valueOf(rs.getString("type")), rs.getString("title"),
                                    rs.getLong("timestamp"), rs.getBoolean("read"), rs.getString("ip_address"));
                
            }
            conn.close();
            rs.close();
            st.close();

        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    
    /** 
     * Adds a new notification to the NotificationsTable
     * @param notif Notification that is being added
     * @return boolean Returns true if the add is successful and false if it is not
     */
    public boolean addNotification(Notification notif) {
        boolean addSuccessful = false;
        try {
            Connection conn = cpds.getConnection();
            PreparedStatement st = conn.prepareStatement("INSERT INTO NotificationsTable (notification_id, title, type, timestamp, pass_id, user_id, read, ip_address) VALUES (?,?,?,?,?,?,?,?)");
            st.setString(1, notif.getNotificationID().toString());
            st.setString(2, notif.getTitle());
            st.setString(3, notif.getType().toString());
            st.setLong(4, notif.getTimestamp());
            st.setString(5, notif.getPassID().toString());
            st.setString(6, notif.getUserID().toString());
            st.setBoolean(7, notif.getRead());
            st.setString(8, notif.getIpAddress());
            st.executeUpdate();
            addSuccessful = true;
            st.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return addSuccessful;
    }

    
    /** 
     * Loads the user settings from the UserSettingsTable with the given userID
     * @param userID String of the ID of the user associated with the UserSettings
     * @return UserSettings Returns the UserSettings object with the given userID
     */
    public UserSettings loadUserSettings(String userID) {
        UserSettings userSettings = null;
        
        try {
            Connection conn = cpds.getConnection();
            PreparedStatement st = conn.prepareStatement("SELECT * FROM UserSettingsTable WHERE user_id=?");
            st.setString(1, userID);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                userSettings = new UserSettings(UUID.fromString(rs.getString("user_id")), rs.getBoolean("notifications_pass_usage"), 
                                                rs.getBoolean("notifications_pass_expiration"), rs.getBoolean("notifications_pass_expires_soon"), rs.getBoolean("light_mode"));
            }
            conn.close();
            rs.close();
            st.close();

        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return userSettings;
    }

    
    /** 
     * Edits the user settings in the UserSettingsTable
     * @param userSettings UserSettings with the new information
     * @return boolean Returns true if the settings are successfully edited and false if they are not
     */
    public boolean editUserSettings(UserSettings userSettings) {
        boolean ret = false;
        try {
            Connection conn = cpds.getConnection();
            PreparedStatement st = conn.prepareStatement(
                    "UPDATE UserSettingsTable SET notifications_pass_usage=?, notifications_pass_expiration=?, notifications_pass_expires_soon=?, light_mode=? WHERE user_id=?");
                    st.setBoolean(1, userSettings.getNotifPassUsage());
                    st.setBoolean(2, userSettings.getNotifPassExpiration());
                    st.setBoolean(3, userSettings.getNotifPassExpiresSoon());
                    st.setBoolean(4, userSettings.getLightMode());
                    st.setString(5, userSettings.getUserID().toString());
            st.executeUpdate();
            ret = true;
            st.close();
            conn.close();

        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }


        return ret;
    }

    
    /** 
     * Adds a new UserSettings to the UserSettingsTable
     * @param userSettings UserSettings that are being added
     * @return boolean Returns true if the settings are successfully added and false if they are not
     */
    public boolean addUserSettings(UserSettings userSettings) {
        boolean ret = false;
        try {
            Connection conn = cpds.getConnection();
            PreparedStatement st = conn.prepareStatement("INSERT INTO UserSettingsTable (notifications_pass_usage, notifications_pass_expiration, notifications_pass_expires_soon, light_mode, user_id) VALUES (?,?,?,?,?)");
            st.setBoolean(1, userSettings.getNotifPassUsage());
            st.setBoolean(2, userSettings.getNotifPassExpiration());
            st.setBoolean(3, userSettings.getNotifPassExpiresSoon());
            st.setBoolean(4, userSettings.getLightMode());
            st.setString(5, userSettings.getUserID().toString());
            st.executeUpdate();
            ret = true;
            st.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    /** 
     * Loads the admin settings from the AdminSettingsTable
     * @return AdminSettings Returns the AdminSettings from the database
     */
    public AdminSettings loadAdminSettings() {
        AdminSettings adminSettings = null;
        
        try {
            Connection conn = cpds.getConnection();
            PreparedStatement st = conn.prepareStatement("SELECT * FROM AdminSettingsTable ORDER BY RANDOM() LIMIT 1");
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                adminSettings = new AdminSettings(rs.getInt("max_pass_duration"), 
                                                  rs.getInt("max_pass_usage"), rs.getInt("max_passes_per_user"));
            }
            conn.close();
            rs.close();
            st.close();

        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return adminSettings;
    }

    
    /** 
     * Edits the admin settings in the AdminSettingsTable
     * @param adminSettings AdminSettings with the new information
     * @return boolean Returns true if the settings are successfully edited and false if they are not
     */
    public boolean editAdminSettings(AdminSettings adminSettings) {
        boolean ret = false;
        try {
            Connection conn = cpds.getConnection();
            PreparedStatement st = conn.prepareStatement(
                    "UPDATE AdminSettingsTable SET max_pass_duration=?, max_pass_usage=?, max_passes_per_user=?");
            st.setInt(1, adminSettings.getMaxPassDuration());
            st.setInt(2, adminSettings.getMaxPassUsage());
            st.setInt(3, adminSettings.getMaxPassesPerUser());
            st.executeUpdate();
            ret = true;
            st.close();
            conn.close();

        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }


        return ret;
    }

    /** 
     * Adds a new AdminSettings to the AdminSettingsTable
     * @param adminSettings AdminSettings that are being added
     * @return boolean Returns true if the settings are successfully added and false if they are not
     */
    public boolean addAdminSettings(AdminSettings adminSettings) {
        boolean ret = false;
        try {
            Connection conn = cpds.getConnection();
            PreparedStatement st = conn.prepareStatement("INSERT INTO AdminSettingsTable (max_pass_duration, max_pass_usage, max_passes_per_user) VALUES (?,?,?)");
            st.setInt(1, adminSettings.getMaxPassDuration());
            st.setInt(2, adminSettings.getMaxPassUsage());
            st.setInt(3, adminSettings.getMaxPassesPerUser());
            st.executeUpdate();
            ret = true;
            st.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    
    
    /** 
     * Edits the user with the given ID to the new user type in the UserTable
     * @param userID String of the user being edited
     * @param type MemberType of the new type
     * @return boolean Returns true if the edit is successful and false if it is not
     */
    public boolean editUserType(String userID, MemberType type) {
        boolean ret = false;
        try {
            Connection conn = cpds.getConnection();
            PreparedStatement st = conn.prepareStatement(
                    "UPDATE UserTable SET user_type=? WHERE user_id=?");
                    st.setString(1, type.toString());
                    st.setString(2, userID);
            st.executeUpdate();
            ret = true;
            st.close();
            conn.close();

        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ret;
    }

    /*
     * Destroy method to close connection
     */
    @Override
    public void destroy() throws Exception {
        if (this.cpds != null) {
            this.cpds.close();
        }
    }

    /*
     * Singleton method to ensure there is only one instance
     */
    public static SQLLinker getInstance() {
        return linker;
    }
}
