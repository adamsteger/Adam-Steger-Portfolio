package com.capstone.lifesabit.gateguard.settings;

import java.util.*;

// Class to store user settings for notification preferences and light mode versus dark mode
public class UserSettings {
    UUID userID;
    boolean notifPassUsage;
    boolean notifPassExpiration;
    boolean notifPassExpiresSoon;
    boolean lightMode;

    // Constructor for when a new user is made, set to default values
    public UserSettings(UUID userID) {
        this.userID = userID;
        notifPassUsage = true;
        notifPassUsage = true;
        notifPassExpiresSoon = true;
        lightMode = true;
    }

    // Constructor for loading from the database
    public UserSettings(UUID userID, boolean notifPassUsage, boolean notifPassExpiration, boolean notifPassExpiresSoon, boolean lightMode) {
        this.userID = userID;
        this.notifPassUsage = notifPassUsage;
        this.notifPassExpiration = notifPassExpiration;
        this.notifPassExpiresSoon = notifPassExpiresSoon;
        this.lightMode = lightMode;
    }

    
    public UUID getUserID() {
        return userID;
    }

    public void setUserID(UUID userID) {
        this.userID = userID;
    }

    public boolean getNotifPassUsage() {
        return notifPassUsage;
    }

    public void setNotifPassUsage(boolean notifPassUsage) {
        this.notifPassUsage = notifPassUsage;
    }

    public boolean getNotifPassExpiration() {
        return notifPassExpiration;
    }

    public void setNotifPassExpiration(boolean notifPassExpiration) {
        this.notifPassExpiration = notifPassExpiration;
    }

    public boolean getNotifPassExpiresSoon() {
        return notifPassExpiresSoon;
    }

    public void setNotifPassExpiresSoon(boolean notifPassExpiresSoon) {
        this.notifPassExpiresSoon = notifPassExpiresSoon;
    }

    public boolean getLightMode() {
        return lightMode;
    }

    public void setLightMode(boolean lightMode) {
        this.lightMode = lightMode;
    }
}
