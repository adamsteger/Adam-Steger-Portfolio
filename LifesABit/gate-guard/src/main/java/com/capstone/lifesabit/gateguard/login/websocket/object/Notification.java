package com.capstone.lifesabit.gateguard.login.websocket.object;

import java.time.LocalDateTime;
import java.util.UUID;

import com.capstone.lifesabit.gateguard.notifications.NotificationType;

/**
 * POJO version of {@link com.capstone.lifesabit.gateguard.notifications.Notification}
 * for usage in websocket object construction
 */
public class Notification {
  UUID notificationID;
  UUID passID;
  UUID userID;
  NotificationType type;
  String title;
  Long timestamp;
  boolean read;


  public Notification(UUID notificationID, UUID passID, UUID userID, NotificationType type, 
                      String title, Long timestamp) {
    this.notificationID = notificationID;
    this.passID = passID;
    this.userID = userID;
    this.type = type;
    this.title = title;
    this.timestamp = timestamp;
  }

  public Notification(com.capstone.lifesabit.gateguard.notifications.Notification notification) {
    this.notificationID = notification.getNotificationID();
    this.passID = notification.getPassID();
    this.userID = notification.getUserID();
    this.type = notification.getType();
    this.title = notification.getTitle();
    this.timestamp = notification.getTimestamp();
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
}
