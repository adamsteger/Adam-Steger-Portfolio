package com.capstone.lifesabit.gateguard.settings;

import java.util.*;

import com.capstone.lifesabit.gateguard.SQLLinker;
import com.capstone.lifesabit.gateguard.login.Member;
import com.capstone.lifesabit.gateguard.passes.Pass;

public class AdminSettings {
    int maxPassDuration;
    int maxPassUsage;
    int maxPassesPerUser;

    /*
     * Static variables for the maximum days a date-based pass can have, 
     * maximum number of uses for usage-based passes, and the maximum number of passes per user
     */
    public static final int DEFAULT_MAX_PASS_DURATION = 5;
    public static final int DEFAULT_MAX_PASS_USAGE = 10;
    public static final int DEFAULT_MAX_PASSES_PER_USER = 5;

    // Constructor for loading from database
    public AdminSettings(int maxPassDuration, int maxPassUsage, int maxPassesPerUser) {
        this.maxPassDuration = maxPassDuration;
        this.maxPassUsage = maxPassUsage;
        this.maxPassesPerUser = maxPassesPerUser;
    }

    // Default constructor for when the site is made
    public AdminSettings() {
        this.maxPassDuration = DEFAULT_MAX_PASS_DURATION;
        this.maxPassUsage = DEFAULT_MAX_PASS_USAGE;
        this.maxPassesPerUser = DEFAULT_MAX_PASSES_PER_USER;
    }

    public int getMaxPassDuration() {
        return maxPassDuration;
    }

    public void setMaxPassDuration(int maxPassDuration) {
        this.maxPassDuration = maxPassDuration;
    }

    public int getMaxPassUsage() {
        return maxPassUsage;
    }

    public void setMaxPassUsage(int maxPassUsage) {
        this.maxPassUsage = maxPassUsage;
    }

    public int getMaxPassesPerUser() {
        return maxPassesPerUser;
    }

    public void setMaxPassesPerUser(int maxPassesPerUser) {
        this.maxPassesPerUser = maxPassesPerUser;
    }

    
    /** 
     * Checks to see if the user has too many current passes to create a new one
     * @param pass the new pass being created
     * @param member the member that created the pass
     * @return boolean returns true if the pass is valid, false if invalid
     */
    public boolean isPassesPerUserValid(Pass pass, Member member) {
        boolean valid = true;
        ArrayList<Pass> userPasses = SQLLinker.getInstance().loadPasses(member);
        if(userPasses.size() >= maxPassesPerUser) {
            valid = false;
        }

        return valid;
    }

    
    /** 
     * Checks to see if the number of uses for a pass is within the maximum number of uses
     * @param pass the pass being checked
     * @return boolean returns true if the pass is within the maximum number of uses and false if it is not
     */
    public boolean isPassUsesValid(Pass pass) {
        boolean valid = true;
        if(pass.getUsesTotal() > maxPassUsage) {
            valid = false;
        }
        return valid;
    }

    
    /** 
     * Checks to see if the pass is within the maximum pass duration
     * @param pass the pass being checked 
     * @return boolean returns true if the pass is within the maximum pass duration and false if it is not
     */
    public boolean isPassExpirationValid(Pass pass) {
        boolean valid = true;

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_MONTH, maxPassDuration);
        Long future = cal.getTimeInMillis();
        
        if(maxPassDuration == -1) {
            return valid;
        } else if(pass.getExpirationDate() != null && pass.getExpirationDate() != -1 && pass.getExpirationDate().compareTo(future) > 0) {
            valid = false;
        }

        return valid;
    }
    
    
    /** 
     * Checks to see if the member can create the new pass by calling the previous 3 checks
     * @param pass the new pass being created
     * @param member the member creating the pass
     * @return boolean returns true if the user can create the pass and false if the user can't
     */
    public boolean isPassValid(Pass pass, Member member) {
        boolean valid = true;

        if(!this.isPassExpirationValid(pass) || !this.isPassUsesValid(pass) || !this.isPassesPerUserValid(pass, member)) {
            valid = false;
        }

        return valid;
    }

    
    /** 
     * Checks to see if the user can edit the pass (Doesn't inlcude passesPerUser, as they are not creating a pass)
     * @param pass the new pass being created
     * @param member the member creating the pass
     * @return boolean returns true if the user can edit the pass and false if the user can't
     */
    public boolean isPassValidEdit(Pass pass, Member member) {
        boolean valid = true;

        if(!this.isPassExpirationValid(pass) || !this.isPassUsesValid(pass)) {
            valid = false;
        }

        return valid;
    }
}
