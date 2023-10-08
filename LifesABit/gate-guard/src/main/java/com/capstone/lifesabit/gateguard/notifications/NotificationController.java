package com.capstone.lifesabit.gateguard.notifications;

import java.time.LocalDateTime;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.sql.Update;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.capstone.lifesabit.gateguard.SQLLinker;
import com.capstone.lifesabit.gateguard.login.Member;
import com.capstone.lifesabit.gateguard.login.Session;
import com.capstone.lifesabit.gateguard.login.SessionManager;
import com.capstone.lifesabit.gateguard.login.Member.MemberType;


@RestController
public class NotificationController {

    public static class LoadNotifsRequest {
        
    }

    public static class LoadNotifsResponse {
        public Notification[] notificationList;
        public String message;
    }

    public static class CreateNotifRequest {
        public String sessionKey;
        public NotificationType type;
        public String title;
        public String description;
        public LocalDateTime timestamp;
    }

    public static class CreateNotifResponse {
        public boolean success;
        public String message;
    }

    public static class DeleteNotifRequest {
        public String sessionKey;
        public String notificationID;
    }

    public static class DeleteNotifResponse {
        public String message;
        public boolean success;
    }

    public static class UpdateNotifRequest {
        public String notifID;
        public boolean read;
    }

    public static class UpdateNotifResponse {
        public boolean success;
        public String message;
        public Notification notif;
    }

    /** 
     * Processes the frontend request to load all notifications for the user
     * @param inputs
     * @return ResponseEntity<LoadNotifsResponse> Returns a list of the users notifications and "Passes loaded successfully" 
     * if the loading works, if not it displays the proper error message
     */
    @RequestMapping(value = "/load-notifs", method = RequestMethod.POST)
    ResponseEntity<LoadNotifsResponse> loadNotifsHandler(HttpServletRequest request, @RequestBody LoadNotifsRequest inputs) {
        LoadNotifsResponse resp = new LoadNotifsResponse();

        String sessionKey = SessionManager.getSessionCookie(request.getCookies());
        if (sessionKey == null || sessionKey.isEmpty()) {
            resp.message = "Error: Invalid session key";
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resp);
        }
        Session userSession = SessionManager.getSession(UUID.fromString(sessionKey));
        if (!SessionManager.isAuthenticated(sessionKey)) {
            resp.message = "Error: Invalid session key";
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resp);
        }

        ArrayList<Notification> notificationsList = SQLLinker.getInstance().loadNotifications(userSession.getMember());

        if (notificationsList == null) {
            resp.message = "Error loading notifications";
        } else {
            resp.message = "Notifications loaded successfully";
            resp.notificationList = new Notification[notificationsList.size()];
            for (int i = 0; i < notificationsList.size(); i++) {
                resp.notificationList[i] = notificationsList.get(i);
            }
        }

        return ResponseEntity.ok(resp);
    }

    
    /** 
     * Processes the frontend request to update a notification for the user as read/unread
     * @param request
     * @param inputs the notificationID and the boolean for whether the notification is read/unread
     * @return ResponseEntity<UpdateNotifResponse> Returns true, the Notification itself, and "Notification successfully updated" if the edit was successful and false with the proper error message if the edit was unsuccessful
     */
    @RequestMapping(value = "/update-notif", method = RequestMethod.POST)
    ResponseEntity<UpdateNotifResponse> readNotifHandler(HttpServletRequest request, @RequestBody UpdateNotifRequest inputs) {
        UpdateNotifResponse resp = new UpdateNotifResponse();

        String sessionKey = SessionManager.getSessionCookie(request.getCookies());
        if (sessionKey == null || sessionKey.isEmpty()) {
            resp.message = "Error: Invalid session key";
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resp);
        }
        Session userSession = SessionManager.getSession(UUID.fromString(sessionKey));
        if (!SessionManager.isAuthenticated(sessionKey)) {
            resp.message = "Error: Invalid session key";
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resp);
        }

        // Check if notification exists
        Notification notif = SQLLinker.getInstance().getNotification(inputs.notifID);

        if (notif == null) {
            resp.success = false;
            resp.message = "That notification does not exist";
            return ResponseEntity.ok(resp);
        }

        // Update notification as read/unread
        notif.setRead(inputs.read);
        resp.success = SQLLinker.getInstance().updateNotification(UUID.fromString(inputs.notifID), notif);
        if (!resp.success) {
            resp.message = "Error updating notification";
        } else {
            resp.message = "Notification successfully updated";
            resp.notif = notif;
        }

        return ResponseEntity.ok(resp);
    }

}