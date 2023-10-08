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
import com.capstone.lifesabit.gateguard.login.Member.MemberType;

@RestController
public class AdminSettingsController {

    public static class LoadAdminSettingsRequest {

    }
    
    public static class LoadAdminSettingsResponse {
        public AdminSettings adminSettings;
        public String message;
    }

    public static class EditAdminSettingsRequest {
        public int maxPassDuration;
        public int maxPassUsage;
        public int maxPassesPerUser;
    }

    public static class EditAdminSettingsResponse {
        public boolean success;
        public String message;
    }


    
    /** 
     * Processes the frontend request to load the admin settings
     * @param request 
     * @param inputs
     * @return ResponseEntity<LoadAdminSettingsResponse> Returns the maxPassDuration, maxPassesPerUser, and maxPassUsage
     */
    @RequestMapping(value = "/load-settings", method = RequestMethod.POST)
    ResponseEntity<LoadAdminSettingsResponse> loadAdminSettingsHandler(HttpServletRequest request, @RequestBody LoadAdminSettingsRequest inputs) {
        LoadAdminSettingsResponse resp = new LoadAdminSettingsResponse();
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

        // If the member isn't an admin, reject the request to view admin settings
        if (userSession.getMember() != null && !userSession.getMember().isAdmin()) {
            resp.message = "You do not have permission to view administrative settings.";
            return ResponseEntity.ok(resp);
        }

        AdminSettings adminSettings = SQLLinker.getInstance().loadAdminSettings();

        if(adminSettings == null) {
            resp.message = "Error loading settings";
        } else {
            resp.message = "Settings loaded successfully";
            resp.adminSettings = adminSettings;
        }

        return ResponseEntity.ok(resp);
    }


    
    /** 
     * Processes the frontend request to edit the admin settings
     * @param request
     * @param inputs An instance of AdminSettings, includes maxPassDuration, maxPassesPerUser, and maxPassUsage
     * @return ResponseEntity<EditAdminSettingsResponse> Returns true and "Settings successfully edited" if the edit was successful and false with the proper error message if the edit was unsuccessful
     */
    @RequestMapping(value = "/edit-settings", method = RequestMethod.POST)
    ResponseEntity<EditAdminSettingsResponse> editAdminSettingsHandler(HttpServletRequest request, @RequestBody EditAdminSettingsRequest inputs) {
        EditAdminSettingsResponse resp = new EditAdminSettingsResponse();
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

        // If the member isn't an admin, reject the request to change admin settings
        if (!member.isAdmin()) {
            resp.success = false;
            resp.message = "You do not have permission to change administrative settings.";
            return ResponseEntity.ok(resp);
        }

        AdminSettings adminSettings = new AdminSettings(inputs.maxPassDuration, inputs.maxPassUsage, inputs.maxPassesPerUser);

        resp.success = SQLLinker.getInstance().editAdminSettings(adminSettings);

        if (!resp.success) {
            resp.message = "Error editing settings";
        } else {
            resp.message = "Settings successfully edited";
        }

        return ResponseEntity.ok(resp);
    }

    


}