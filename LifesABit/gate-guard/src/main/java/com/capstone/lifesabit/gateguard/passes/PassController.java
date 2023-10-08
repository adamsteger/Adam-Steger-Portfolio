package com.capstone.lifesabit.gateguard.passes;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.EncodeException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.capstone.lifesabit.gateguard.EmailSender;
import com.capstone.lifesabit.gateguard.SQLLinker;
import com.capstone.lifesabit.gateguard.login.Member;
import com.capstone.lifesabit.gateguard.login.Member.MemberType;
import com.capstone.lifesabit.gateguard.login.websocket.WebsocketController;
import com.capstone.lifesabit.gateguard.login.websocket.WebsocketTypes;
import com.capstone.lifesabit.gateguard.notifications.Notification;
import com.capstone.lifesabit.gateguard.notifications.NotificationType;
import com.capstone.lifesabit.gateguard.settings.AdminSettings;
import com.capstone.lifesabit.gateguard.settings.UserSettings;
import com.capstone.lifesabit.gateguard.login.Session;
import com.capstone.lifesabit.gateguard.login.SessionManager;


@RestController
public class PassController {

    public static class VerifyPassRequest {
        public String passID;
    }

    public static class VerifyPassResponse {
        public boolean isValid;
        public boolean usageBased;
        public long expirationDate;
        public int usesLeft;
        public int usesTotal;
        public String message;
    }

    public static class UsePassRequest {
        public String passID;
    }

    public static class UsePassResponse {
        public boolean isValid;
        public boolean usageBased;
        public long expirationDate;
        public int usesLeft;
        public int usesTotal;
        public String message;
    }
    
    public static class LoadPassesRequest {
        
    }

    public static class LoadPassesResponse {
        public Pass[] passList;
        public String message;
    }

    public static class LoadPassesAdminRequest {
        
        public String userID;
    }

    public static class RevokePassAdminRequest {
        
        public String passID;
    }

    public static class CreatePassRequest {
        public boolean usageBased;
        public String firstName;
        public String lastName;
        public String email;
        public long expirationDate;
        public int usesLeft;
        public int usesTotal;
    }

    public static class CreatePassResponse {
        public boolean success;
        public String message;
        public String passID;
    }

    public static class EditPassRequest {
        public String passID;
        public boolean usageBased;
        public String firstName;
        public String lastName;
        public String email;
        public long expirationDate;
        public int usesLeft;
        public int usesTotal;
    }

    public static class EditPassResponse {
        public boolean success;
        public String message;
    }

    public static class RevokePassRequest {
        public String passID;
    }

    public static class RevokePassResponse {
        public boolean success;
        public String message;
    }

    public static class RefreshPassRequest {
        public String passID;
    }

    public static class RefreshPassResponse {
        public boolean success;
        public String message;
    }

    public static class ResendPassEmailRequest {
        public String passID;
    }

    public static class ResendPassEmailResponse {
        public boolean success;
        public String message;
    }

    
    
    /** 
     * Processes the frontend request to load a user's passes
     * @param request
     * @param inputs 
     * @return ResponseEntity<LoadPassesResponse> Returns a list of the user's passes
     */
    @RequestMapping(value = "/load-passes", method = RequestMethod.POST)
    ResponseEntity<LoadPassesResponse> loadPassesHandler(HttpServletRequest request,
            @RequestBody LoadPassesRequest inputs) throws IOException, EncodeException {
        LoadPassesResponse resp = new LoadPassesResponse();
        String sessionKey = SessionManager.getSessionCookie(request.getCookies());
        if (sessionKey == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resp);
        }
        if (sessionKey == null || sessionKey.isEmpty()) {
            resp.message = "Error: Invalid session key";
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resp);
        }
        Session userSession = SessionManager.getSession(UUID.fromString(sessionKey));
        if (!SessionManager.isAuthenticated(sessionKey)) {
            resp.message = "Error: Invalid session key";
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resp);
        }
        ArrayList<Pass> passesList = SQLLinker.getInstance().loadPasses(userSession.getMember());

        if (passesList == null) {
            resp.message = "Error loading passes";
        } else {
            resp.message = "Passes loaded successfully";
            resp.passList = new Pass[passesList.size()];
            for (int i = 0; i < passesList.size(); i++) {
                resp.passList[i] = passesList.get(i);
            }
        }

        return ResponseEntity.ok(resp);
    }

    
    /** 
     * Processes the frontend request to load all passes of the site for admin security log
     * @param request
     * @param inputs
     * @return ResponseEntity<LoadPassesResponse> Returns a list of all passes in the system
     */
    @RequestMapping(value = "/load-passes-admin", method = RequestMethod.POST)
    ResponseEntity<LoadPassesResponse> loadPassesHandler(HttpServletRequest request,
            @RequestBody LoadPassesAdminRequest inputs) {
        LoadPassesResponse resp = new LoadPassesResponse();
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
        if (!userSession.getMember().isAdmin()) {
            resp.message = "You are not an administrator.";
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(resp);
        }
        ArrayList<Pass> passesList = SQLLinker.getInstance().loadPasses(userSession.getMember());
        if (passesList == null) {
            resp.message = "Error loading passes";
        } else {
            resp.message = "Passes loaded successfully";
            resp.passList = new Pass[passesList.size()];
            for (int i = 0; i < passesList.size(); i++) {
                resp.passList[i] = passesList.get(i);
            }
        }

        return ResponseEntity.ok(resp);
    }

    
    /** 
     * Processes the frontend request to create a new pass
     * @param request
     * @param inputs contains all of the pass information
     * @return ResponseEntity<CreatePassResponse> Returns true, "Pass successfully created", and the passID if the pass was created and added to the database
     */
    @RequestMapping(value = "/create-pass", method = RequestMethod.POST)
    ResponseEntity<CreatePassResponse> createPassHandler(HttpServletRequest request,
            @RequestBody CreatePassRequest inputs) throws ParseException {
        CreatePassResponse resp = new CreatePassResponse();
        String sessionKey = SessionManager.getSessionCookie(request.getCookies());
        if (sessionKey == null || sessionKey.isEmpty()) {
            resp.message = "Error: Invalid session key";
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resp);
        }
        if (!SessionManager.isAuthenticated(sessionKey)) {
            resp.message = "Error: Invalid session key";
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resp);
        }
        Session userSession = SessionManager.getSession(UUID.fromString(sessionKey));
        Pass pass = null;

        // Check if usage or date-based pass
        if (inputs.usageBased) {
            pass = new Pass(inputs.firstName, inputs.lastName, inputs.email, userSession.getMember().getUuid(),
                    inputs.usesTotal);
        } else {
            pass = new Pass(inputs.firstName, inputs.lastName, inputs.email, userSession.getMember().getUuid(),
                    inputs.expirationDate);
        }

        // Check if pass violates admin settings
        AdminSettings adminSettings = SQLLinker.getInstance().loadAdminSettings();

        if (userSession.getMember().isAdmin() || adminSettings.isPassValid(pass, userSession.getMember())) {
            resp.success = SQLLinker.getInstance().addPass(pass);
            if (resp.success) {
                resp.message = "Pass successfully created";
                resp.passID = pass.getPassID().toString();
            } else {
                resp.message = "Error creating pass";
            }
            resp.message = "Pass successfully created";
            EmailSender.getInstance().emailUserAboutNewPass(pass);
        } else {
            resp.success = false;
            if (!adminSettings.isPassExpirationValid(pass)) {
                resp.message = "Pass cannot be created as it outside of the max expiration date for passes in your organization";
            } else if(!adminSettings.isPassUsesValid(pass)) {
                resp.message = "Pass cannot be created as it outside of the max number of uses for passes in your organization";
            } else {
                resp.message = "Pass cannot be created as you have exceeded the max number of passes per user in your organization";
            }
        }

        return ResponseEntity.ok(resp);
    }

    
    /** 
     * Processes the frontend request to edit a pass
     * @param request
     * @param inputs all of the new information of the pass
     * @return ResponseEntity<EditPassResponse> Returns true and "Pass successfully edited" if the edit was successful and false with the proper error message if the edit was unsuccessful
     * @throws ParseException
     */
    @RequestMapping(value = "/edit-pass", method = RequestMethod.POST)
    ResponseEntity<EditPassResponse> editPassHandler(HttpServletRequest request, @RequestBody EditPassRequest inputs)
            throws ParseException {
        EditPassResponse resp = new EditPassResponse();
        String sessionKey = SessionManager.getSessionCookie(request.getCookies());
        if (sessionKey == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resp);
        }
        if (sessionKey == null || sessionKey.isEmpty()) {
            resp.message = "Error: Invalid session key";
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resp);
        }
        if (!SessionManager.isAuthenticated(sessionKey)) {
            resp.message = "Error: Invalid session key";
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resp);
        }

        Session userSession = SessionManager.getSession(UUID.fromString(sessionKey));
        Member member = userSession.getMember();

        // Check if pass violates admin settings
        AdminSettings adminSettings = SQLLinker.getInstance().loadAdminSettings();

        Pass pass = null;

        if (inputs.usageBased) {
            pass = new Pass(UUID.fromString(inputs.passID), inputs.firstName,
                                inputs.lastName, inputs.email, member.getUuid(), inputs.usesLeft,
                                inputs.usesTotal);
        } else if (!inputs.usageBased) {
            pass = new Pass(UUID.fromString(inputs.passID), inputs.firstName,
                                inputs.lastName, inputs.email, member.getUuid(), inputs.expirationDate);
        }
        
        if (userSession.getMember().isAdmin() || adminSettings.isPassValidEdit(pass, userSession.getMember())) {
            resp.success = SQLLinker.getInstance().editPass(pass);
            if (resp.success) {
                resp.message = "Pass successfully edited";
            } else {
                resp.message = "Error editting pass";
            }
        } else {
            resp.success = false;
            if (!adminSettings.isPassExpirationValid(pass)) {
                resp.message = "Pass cannot be edited as it outside of the max expiration date for passes in your organization";
            } else if(!adminSettings.isPassUsesValid(pass)) {
                resp.message = "Pass cannot be edited as it outside of the max number of uses for passes in your organization";
            } else {
                resp.message = "Pass cannot be edited as you have exceeded the max number of passes per user in your organization";
            }
        }

        return ResponseEntity.ok(resp);
    }

    
    /** 
     * Processes the frontend request to revoke a pass
     * @param request
     * @param inputs the passID of the pass that needs to be revoked
     * @return ResponseEntity<RevokePassResponse> Returns true and "Pass successfully revoked" if the revoke was successful and false with the proper error message if the revoke was unsuccessful
     */
    @RequestMapping(value = "/revoke-pass", method = RequestMethod.POST)
    ResponseEntity<RevokePassResponse> revokePassHandler(HttpServletRequest request,
            @RequestBody RevokePassRequest inputs) {
        RevokePassResponse resp = new RevokePassResponse();
        String sessionKey = SessionManager.getSessionCookie(request.getCookies());
        if (sessionKey == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resp);
        }
        if (sessionKey == null || sessionKey.isEmpty()) {
            resp.message = "Error: Invalid session key";
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resp);
        }
        if (!SessionManager.isAuthenticated(sessionKey)) {
            resp.message = "Error: Invalid session key";
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resp);
        }
        Session userSession = SessionManager.getSession(UUID.fromString(sessionKey));
        Member user = userSession.getMember();
        Pass pass = SQLLinker.getInstance().getPass(inputs.passID);
        if (pass.getUserID().equals(user.getUuid()) || user.isAdmin()) {
            resp.success = SQLLinker.getInstance().deletePass(UUID.fromString(inputs.passID));
        } else {
            resp.message = "Error: You do not have permission to delete this pass.";
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(resp);
        }

        if (!resp.success) {
            resp.message = "Error deleting pass";
        } else {
            resp.message = "Pass successfully deleted";
        }
        return ResponseEntity.ok(resp);
    }


    
    /** 
     * Processes the frontend request to verify a pass
     * @param inputs the passID of the pass being verified
     * @return ResponseEntity<VerifyPassResponse> Returns true, "successful", and the pass information if the pass is verified
     */
    @RequestMapping(value = "/verify-pass", method = RequestMethod.POST)
    ResponseEntity<VerifyPassResponse> revokePassHandler(HttpServletRequest request,
            @RequestBody VerifyPassRequest inputs) {
        VerifyPassResponse resp = new VerifyPassResponse();
        // Check if pass exists
        Pass pass = SQLLinker.getInstance().getPass(inputs.passID);
        if (pass == null) {
            resp.isValid = false;
            resp.message = "That pass does not exist";
            return ResponseEntity.ok(resp);
        }
        // Fill in pass info
        resp.isValid = !pass.isExpired();
        resp.expirationDate = (pass.getExpirationDate() == null) ? 0 : pass.getExpirationDate();
        resp.usageBased = pass.getUsageBased();
        resp.usesLeft = pass.getUsesLeft();
        resp.usesTotal = pass.getUsesTotal();
        resp.message = resp.isValid ? "Successful" : "Pass expired";
        return ResponseEntity.ok(resp);
    }

    
    
    /** 
     * Processes the frontend request to use a pass, as well as send notifications for the pass use
     * @param inputs the passID of the pass being used
     * @return ResponseEntity<UsePassResponse> Returns pass info of the pass being used
     */
    @RequestMapping(value = "/use-pass", method = RequestMethod.POST)
    ResponseEntity<UsePassResponse> revokePassHandler(HttpServletRequest request,
            @RequestBody UsePassRequest inputs) throws IOException, EncodeException {
        UsePassResponse resp = new UsePassResponse();
        // Check if pass exists
        Pass pass = SQLLinker.getInstance().getPass(inputs.passID);
        if (pass == null) {
            resp.isValid = false;
            resp.message = "That pass does not exist";
            return ResponseEntity.ok(resp);
        }
        // Use the pass
        boolean used = pass.use();
        if (used && pass.getUsageBased()) {
            SQLLinker.getInstance().updatePass(UUID.fromString(inputs.passID), pass);
        }
        // Fill in pass info
        resp.isValid = !pass.isExpired();
        resp.expirationDate = (pass.getExpirationDate() == null) ? 0 : pass.getExpirationDate();
        resp.usageBased = pass.getUsageBased();
        resp.usesLeft = pass.getUsesLeft();
        resp.usesTotal = pass.getUsesTotal();
        resp.message = resp.isValid ? "Successful" : "Pass expired";


        Member passOwner = SQLLinker.getInstance().getMemberByUUID(pass.getUserID().toString());
        UserSettings userSettings = SQLLinker.getInstance().loadUserSettings(passOwner.getUuid().toString());
        Session userSession = SessionManager.getSession(passOwner);
        boolean passOwnerIsLoggedIn = SessionManager.isAuthenticated(userSession.getSessionKey().toString());

        // Send pass used notification
        String title = pass.getFirstName() + " " + pass.getLastName() + " used a pass!";
        Notification notif = new Notification(title, pass.getPassID(), pass.getUserID(), Instant.now().toEpochMilli(), NotificationType.PASS_USED, request.getRemoteAddr());
        SQLLinker.getInstance().addNotification(notif);

        // If the pass's owner is logged in, go ahead and deliver a notification
        // to their web browser to let them know that their pass was used.
        if (passOwnerIsLoggedIn && userSettings.getNotifPassUsage()) {
            WebsocketController.sendMessage(WebsocketTypes.NOTIFICATION, 
                                            new com.capstone.lifesabit.gateguard.login.websocket.object.Notification(notif), 
                                            userSession.getSessionKey());
        }


        // Check if pass expires soon or is expired
        if (userSettings.getNotifPassExpiresSoon() && pass.getUsageBased() && pass.getUsesLeft() == 1) {
            title = pass.getFirstName() + " " + pass.getLastName() + "'s pass is expiring soon!";
            notif = new Notification(title, pass.getPassID(), pass.getUserID(), Instant.now().toEpochMilli(), NotificationType.PASS_EXPIRES_SOON, request.getRemoteAddr());
            SQLLinker.getInstance().addNotification(notif);
            if (passOwnerIsLoggedIn) {
                WebsocketController.sendMessage(WebsocketTypes.NOTIFICATION,
                                                new com.capstone.lifesabit.gateguard.login.websocket.object.Notification(notif), 
                                                userSession.getSessionKey());
            }
            
        } else if(userSettings.getNotifPassExpiration() && pass.isExpired()) { 
            title = pass.getFirstName() + " " + pass.getLastName() + "'s pass is expired!";
            if (SQLLinker.getInstance().getNotificationByTitle(title, userSession.getMember().getUuid().toString()) == null) {
                notif = new Notification(title, pass.getPassID(), pass.getUserID(), Instant.now().toEpochMilli(), NotificationType.PASS_EXPIRED, request.getRemoteAddr());
                SQLLinker.getInstance().addNotification(notif);
                if (passOwnerIsLoggedIn) {
                    WebsocketController.sendMessage(WebsocketTypes.NOTIFICATION,
                                                    new com.capstone.lifesabit.gateguard.login.websocket.object.Notification(notif), 
                                                    userSession.getSessionKey());
                }
            }
        }
            
        
        return ResponseEntity.ok(resp);
    }

    
    
    /** 
     * Processes the frontend request to refresh a pass, which resets the number of uses left for usage-based passes
     * @param request
     * @param inputs the passID of the pass being refreshed
     * @return ResponseEntity<RefreshPassResponse> Returns true and "Pass successfully refreshed!" if the refresh was successful and false with the proper error message if the refresh was unsuccessful
     * @throws ParseException
     */
    @RequestMapping(value = "/refresh-pass", method = RequestMethod.POST)
    ResponseEntity<RefreshPassResponse> refreshPassHandler(HttpServletRequest request, @RequestBody RefreshPassRequest inputs)
            throws ParseException {
        RefreshPassResponse resp = new RefreshPassResponse();
        String sessionKey = SessionManager.getSessionCookie(request.getCookies());
        if (sessionKey == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resp);
        }
        if (sessionKey.isEmpty() || !SessionManager.isAuthenticated(sessionKey)) {
            resp.message = "Error: Invalid session key";
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resp);
        }
        
        Pass pass = SQLLinker.getInstance().getPass(inputs.passID);

        if(!pass.getUsageBased()) {
            resp.success = false;
            resp.message = "This pass is not usage based, so it can't be reset.";
        } else {
            resp.success = SQLLinker.getInstance().refreshPass(pass);
            if (resp.success) {
                resp.message = "Pass successfully refreshed!";
            } else {
                resp.message = "Error refreshing pass";
            }
        }

        if (!resp.success) {
            resp.message = "Error editing pass";
        } else {
            resp.message = "Pass successfully edited";
        }

        return ResponseEntity.ok(resp);
    }


    
    /** 
     * Processes the frontend request to resend the email sent to pass recipients when a pass is created
     * @param request
     * @param inputs the passID of the pass that needs a new email
     * @return ResponseEntity<ResendPassEmailResponse> Returns true and "Email resent" if the resend was successful and false with the proper error message if the edit was unsuccessful
     * @throws ParseException
     */
    @RequestMapping(value = "/resend-pass-email", method = RequestMethod.POST)
    ResponseEntity<ResendPassEmailResponse> editPassHandler(HttpServletRequest request, @RequestBody ResendPassEmailRequest inputs)
            throws ParseException {
        ResendPassEmailResponse resp = new ResendPassEmailResponse();
        String sessionKey = SessionManager.getSessionCookie(request.getCookies());
        if (sessionKey == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resp);
        }
        if (sessionKey == null || sessionKey.isEmpty()) {
            resp.message = "Error: Invalid session key";
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resp);
        }
        if (!SessionManager.isAuthenticated(sessionKey)) {
            resp.message = "Error: Invalid session key";
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resp);
        }

        Session userSession = SessionManager.getSession(UUID.fromString(sessionKey));
        Member member = userSession.getMember();

        Pass pass = SQLLinker.getInstance().getPass(inputs.passID);

        if (pass == null) {
            resp.message = "Error: Could not locate pass";
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resp);
        }

        EmailSender.getInstance().emailUserAboutNewPass(pass);
        resp.success = true;
        resp.message = "Email resent";

        return ResponseEntity.ok(resp);
    }
}