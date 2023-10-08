package com.capstone.lifesabit.gateguard.notifications;

import java.util.UUID;

public class Notification {
    UUID notificationID;
    UUID passID;
    UUID userID;
    String email;
    int usesLeft;
    Long expirationDate;
    NotificationType type;
    String title;
    Long timestamp;
    boolean read;
    Boolean usageBased = null;
    String ipAddress;

    /*
     * Constructor for loading from database
     */
    public Notification(UUID notificationID, UUID passID, UUID userID, NotificationType type, String title, Long timestamp, boolean read, String ipAddress) {
        this.notificationID = notificationID;
        this.passID = passID;
        this.userID = userID;
        this.type = type;
        this.title = title;
        this.timestamp = timestamp;
        this.read = read;
        this.ipAddress = ipAddress;
    }

    /*
     * Constructor for a new notification
     */
    public Notification(String title, UUID passID, UUID userID, Long timestamp, NotificationType type, String ipAddress) {
        notificationID = UUID.randomUUID();
        this.passID = passID;
        this.userID = userID;
        this.title = title;
        this.timestamp = timestamp;
        this.read = false;
        this.type = type;
        this.ipAddress = ipAddress;
    }
    
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getUsesLeft() {
        return usesLeft;
    }

    public void setUsesLeft(int usesLeft) {
        this.usesLeft = usesLeft;
    }

    public Long getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Long expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Boolean getUsageBased() {
        return usageBased;
    }

    public void setUsageBased(Boolean usageBased) {
        this.usageBased = usageBased;
    }

    public UUID getNotificationID() {
        return notificationID;
    }

    public void setNotificationID(UUID notificationID) {
        this.notificationID = notificationID;
    }

    public UUID getPassID() {
        return passID;
    }

    public void setPassID(UUID passID) {
        this.passID = passID;
    }

    public UUID getUserID() {
        return userID;
    }

    public void setUserID(UUID userID) {
        this.userID = userID;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean getRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
}
