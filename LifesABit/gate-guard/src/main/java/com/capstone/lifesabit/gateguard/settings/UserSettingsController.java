package com.capstone.lifesabit.gateguard.settings;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

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

@RestController
public class UserSettingsController {

    public static class LoadUserSettingsRequest {

    }
    
    public static class LoadUserSettingsResponse {
        public UserSettings userSettings;
        public String message;
    }

    public static class EditUserSettingsRequest {
        public boolean notifPassUsage;
        public boolean notifPassExpiration;
        public boolean notifPassExpiresSoon;
        public boolean lightMode;
    }

    public static class EditUserSettingsResponse {
        public boolean success;
        public String message;
    }


    
    /** 
     * Processes the frontend request to load user settings
     * @param request
     * @param inputs 
     * @return ResponseEntity<LoadUserSettingsResponse> Returns the user's notifPassUsage, notifPassExpiration, notifPassExpiresSoon, and lightMode preferences
     */
    @RequestMapping(value = "/load-user-settings", method = RequestMethod.POST)
    ResponseEntity<LoadUserSettingsResponse> loadUserSettingsHandler(HttpServletRequest request, @RequestBody LoadUserSettingsRequest inputs) {
        LoadUserSettingsResponse resp = new LoadUserSettingsResponse();
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

        UserSettings userSettings = SQLLinker.getInstance().loadUserSettings(userSession.getMember().getUuid().toString());

        if(userSettings == null) {
            resp.message = "Error loading settings";
        } else {
            resp.message = "Settings loaded successfully";
            resp.userSettings = userSettings;
        }

        return ResponseEntity.ok(resp);
    }

    
    /** 
     * Processes the frontend request to edit user settings
     * @param request
     * @param inputs notifPassUsage, notifPassExpiration, notifPassExpiresSoon, and lightMode preferences from the User
     * @return ResponseEntity<EditUserSettingsResponse> Returns true and "Settings successfully edited" if the edit was successful and false with the proper error message if the edit was unsuccessful
     */
    @RequestMapping(value = "/edit-user-settings", method = RequestMethod.POST)
    ResponseEntity<EditUserSettingsResponse> editUserSettingsHandler(HttpServletRequest request, @RequestBody EditUserSettingsRequest inputs) {
        EditUserSettingsResponse resp = new EditUserSettingsResponse();
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

        Member member = userSession.getMember();

        UserSettings userSettings = new UserSettings(member.getUuid(), inputs.notifPassUsage, inputs.notifPassExpiration, inputs.notifPassExpiresSoon, inputs.lightMode);

        resp.success = SQLLinker.getInstance().editUserSettings(userSettings);

        if (!resp.success) {
            resp.message = "Error editing settings";
        } else {
            resp.message = "Settings successfully edited";
        }

        return ResponseEntity.ok(resp);
    }

    


}