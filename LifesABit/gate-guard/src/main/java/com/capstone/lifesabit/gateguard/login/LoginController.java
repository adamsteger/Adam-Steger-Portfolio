package com.capstone.lifesabit.gateguard.login;

import java.util.ArrayList;
import java.util.UUID;

import javax.print.attribute.standard.Media;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.capstone.lifesabit.gateguard.EmailSender;
import com.capstone.lifesabit.gateguard.SQLLinker;
import com.capstone.lifesabit.gateguard.login.Member.MemberType;
import com.capstone.lifesabit.gateguard.settings.UserSettings;
import com.fasterxml.jackson.annotation.JsonProperty;

@RestController
public class LoginController {

  public static final String SESSION_KEY_COOKIE = "session_key";

    public static class LoginRequest {
        public String username;
        public String hashedPassword;
    }

    public static class LoginResponse {
        public boolean success;
    }

    public static class LogoutRequest {
      
  }

  public static class LogoutResponse {
      public boolean success;
  }

  public static class GetUserInfoRequest {
    
  }

  public static class GetUserInfoResponse {
    public String firstName;
    public String lastName;
    public boolean isAdmin;
    public boolean isOwner;
    public String email;
    public String username;
    public String phone;
  }

  public static class EditUserInfoRequest {
    public String firstName;
    public String lastName;
    public String email;
    public String username;
    public String phone;
  }

  public static class EditUserInfoResponse {
    public boolean success;
    public String message;
  }

  public static class CreateAccountRequest {
    public String username;
    public String firstName;
    public String lastName;
    public String phoneNumber;
    public String emailAddress;
    public String hashedPassword;
    // public String verificationCode;
  }
  
  public static class CreateAccountResponse {
    public String name;
    public String type;
    public boolean success;
    public String message;
  }

  public static class LoadMembersRequest {
    
  }

  public static class LoadMembersResponse {
    public SimpleMember[] memberList;
  }

  public static class MatchAccountRequest {
    public String resetID;
  }
  
  public static class MatchAccountResponse {
    public boolean success;
    public String firstName;
  }
  
  public static class ResetPasswordRequest {
    public String resetID;
    public String newHashedPassword;
  }
  
  public static class ResetPasswordResponse {
    public boolean success;
  }

  public static class RequestPasswordResetRequest {
    public String email;
  }

  public static class EditUserTypeRequest {
    public String userID;
    public String type;
  }

  public static class EditUserTypeResponse {
    public boolean success;
    public String message;
  }

  // No response. We don't want them to know if the email
  // is in our system, in case they're fishing for the emails
  // of our users
  public static class RequestPasswordResetResponse {
    
  }

    @RequestMapping(value = "/log-in", method = RequestMethod.POST)
    ResponseEntity<LoginResponse> loginHandler(HttpServletRequest request, @RequestBody LoginRequest inputs) {
      LoginResponse resp = new LoginResponse();
      Session userSession = SessionManager.getSession(inputs.username, inputs.hashedPassword);
      if (userSession == null) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resp);
      }
      if (userSession.isExpired()) {
        userSession = SessionManager.createNewSession(inputs.username, inputs.hashedPassword);
      }
      resp.success = true;
      ResponseEntity<LoginResponse> finalResp = ResponseEntity.ok(resp);
      HttpHeaders headers = new HttpHeaders();
      headers.addAll(finalResp.getHeaders());
      headers.add(HttpHeaders.SET_COOKIE, SESSION_KEY_COOKIE + "=" + userSession.getSessionKey().toString() + ";HttpOnly;Secure;SameSite=Strict");
      return ResponseEntity.status(HttpStatus.OK).headers(headers).body(resp);
    }

    @RequestMapping(value = "/log-out", method = RequestMethod.POST)
    ResponseEntity<LogoutResponse> logoutHandler(HttpServletRequest request, @RequestBody LogoutRequest inputs) {
      LogoutResponse resp = new LogoutResponse();
      String sessionKey = SessionManager.getSessionCookie(request.getCookies());
      if (sessionKey == null) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resp);
    }
      Session userSession = SessionManager.getSession(UUID.fromString(sessionKey));
      resp.success = false;
      if (userSession == null || userSession.isExpired()) {
        resp.success = true;
        return ResponseEntity.ok(resp);
      }
      resp.success = SessionManager.removeSession(UUID.fromString(sessionKey));
      return ResponseEntity.ok(resp);
    }

    @RequestMapping(value = "/user-info", method = RequestMethod.POST)
    ResponseEntity<GetUserInfoResponse> getUserNameHandler(HttpServletRequest request, @RequestBody GetUserInfoRequest inputs) {
      GetUserInfoResponse resp = new GetUserInfoResponse();
      String sessionKey = SessionManager.getSessionCookie(request.getCookies());
      if (sessionKey == null) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resp);
      }
      Session userSession = SessionManager.getSession(UUID.fromString(sessionKey));
      if (!SessionManager.isAuthenticated(sessionKey)) {
          return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resp);
      }
      Member member = userSession.getMember();
      resp.firstName = member.getFirstName();
      resp.lastName = member.getLastName();
      resp.email = member.getEmail();
      resp.isAdmin = member.isAdmin();
      resp.isOwner = member.isOwner();
      resp.username = member.getUsername();
      resp.phone = member.getPhoneNumber();
      return ResponseEntity.ok(resp);
    }

    @RequestMapping(value = "/edit-user-info", method = RequestMethod.POST)
    ResponseEntity<EditUserInfoResponse> getUserNameHandler(HttpServletRequest request, @RequestBody EditUserInfoRequest inputs) {
      EditUserInfoResponse resp = new EditUserInfoResponse();
      String sessionKey = SessionManager.getSessionCookie(request.getCookies());
      if (sessionKey == null) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resp);
      }
      Session userSession = SessionManager.getSession(UUID.fromString(sessionKey));
      if (!SessionManager.isAuthenticated(sessionKey)) {
          return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resp);
      }
      Member member = userSession.getMember();
      // Check to make sure the email and username are unique
      Member memberWithUsername = SQLLinker.getInstance().getMember(inputs.username);
      Member memberWithEmail = SQLLinker.getInstance().getMemberByEmail(inputs.email);
      if (!member.equals(memberWithEmail) && memberWithEmail != null) {
        resp.message = "This email address is taken. Please choose another.";
        resp.success = false;
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resp);
      }

      if (!member.equals(memberWithUsername) && memberWithUsername != null) {
        resp.message = "This username is taken. Please choose another.";
        resp.success = false;
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resp);
      }
      // Update it in the database
      resp.success = SQLLinker.getInstance().editMember(member, inputs.firstName, inputs.lastName, inputs.email, inputs.username, inputs.phone);
      // Update it in the version cached in our RAM
      member.setFirstName(inputs.firstName);
      member.setLastName(inputs.lastName);
      member.setUsername(inputs.username);
      member.setEmail(inputs.email);
      member.setPhoneNumber(inputs.phone);
      return ResponseEntity.ok(resp);
    }

    @RequestMapping(value = "/new-member", method = RequestMethod.POST)
    ResponseEntity<CreateAccountResponse> createAccountHandler(HttpServletRequest request, @RequestBody CreateAccountRequest inputs) {
      CreateAccountResponse resp = new CreateAccountResponse();
      if (isUsernameTaken(inputs.username)) {
        resp.success = false;
        resp.message = "This username is already taken. Please choose another one.";
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resp);
      }

      if (isEmailTaken(inputs.emailAddress)) {
        resp.success = false;
        resp.message = "There is already an account with this e-mail.";
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resp);
      }

      Member member = new Member(inputs.username, inputs.firstName, inputs.lastName, inputs.phoneNumber, inputs.emailAddress, inputs.hashedPassword, MemberType.USER);

      UserSettings userSettings = new UserSettings(member.getUuid());
      SQLLinker.getInstance().addUserSettings(userSettings);

      member.setHashedPassword(inputs.hashedPassword);
      SQLLinker.getInstance().addUser(member);
      Session userSession = SessionManager.createNewSession(inputs.username, inputs.hashedPassword);
      resp.name = member.getName();
      resp.type = member.getType().toString().toLowerCase();
      resp.message = "Account successfully created.";
      resp.success = true;
      EmailSender.getInstance().emailNewAccount(member);
      return ResponseEntity.ok(resp);
    }

    @RequestMapping(value = "/load-members", method = RequestMethod.POST)
    ResponseEntity<LoadMembersResponse> loadMembersHandler(HttpServletRequest request, @RequestBody LoadMembersRequest inputs) {
      LoadMembersResponse resp = new LoadMembersResponse();
      String sessionKey = SessionManager.getSessionCookie(request.getCookies());
      if (sessionKey == null) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resp);
      }
      Session userSession = SessionManager.getSession(UUID.fromString(sessionKey));
      // If the user's session key is invalid
      if (userSession == null) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resp);
      }
      Member adminAccount = userSession.getMember();
      if (!adminAccount.isAdmin()) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resp);
      }
      SQLLinker linker = SQLLinker.getInstance();
      ArrayList<Member> memberList = linker.getMembers();
      SimpleMember[] simpleMemberList = new SimpleMember[memberList.size()];
      for (int i = 0; i < memberList.size(); i++) {
        SimpleMember simpMem = new SimpleMember();
        simpMem.firstName = memberList.get(i).firstName;
        simpMem.lastName = memberList.get(i).lastName;
        simpMem.email = memberList.get(i).email;
        simpMem.username = memberList.get(i).username;
        simpMem.id = memberList.get(i).getUuid().toString();
        simpMem.memberType = memberList.get(i).getType();
        simpleMemberList[i] = simpMem;
      }
      resp.memberList = simpleMemberList;
      // If we're at this point, the session key is a valid admin's session key
      return ResponseEntity.ok(resp);
    }


    @RequestMapping(value = "/request-reset", method = RequestMethod.POST)
    ResponseEntity<RequestPasswordResetResponse> requestPasswordResetHandler(HttpServletRequest request, @RequestBody RequestPasswordResetRequest inputs) {
      // Check if we have their e-mail in our system
      Member member = SQLLinker.getInstance().getMemberByEmail(inputs.email);
      // If we don't have an email for that member, return early
      // since we don't want to let a potential attacker know this
      if (member == null) {
        return ResponseEntity.ok(null);
      }

      // Map their reset request to an internal map
      UUID resetUUID = PasswordResetManager.requestReset(member.getUuid());
      // Send an email to them with the password reset link
      EmailSender.getInstance().sendPasswordResetEmail(member, resetUUID);

      return ResponseEntity.ok(null);
    }

    /**
     * This web service is used to match a member's account to
     * the ID sent in their password reset request, and prompt the
     * member by name for their new password
     * @param request
     * @param inputs
     * @return
     */
    @RequestMapping(value = "/match-account", method = RequestMethod.POST)
    ResponseEntity<MatchAccountResponse> matchAccountHandler(HttpServletRequest request, @RequestBody MatchAccountRequest inputs) {
      MatchAccountResponse resp = new MatchAccountResponse();
      resp.success = false;

      UUID resetID = UUID.fromString(inputs.resetID);
      if (resetID == null) {
        return ResponseEntity.ok(resp);
      }
      UUID memberID = PasswordResetManager.getMemberUUIDFromResetID(resetID);
      if (memberID == null) {
        return ResponseEntity.ok(resp);
      }
      Member member = SQLLinker.getInstance().getMemberByUUID(memberID.toString());
      resp.success = (member != null);
      
      return ResponseEntity.ok(resp);
    }

    @RequestMapping(value = "/reset-password", method = RequestMethod.POST)
    ResponseEntity<ResetPasswordResponse> resetPasswordHandler(HttpServletRequest request, @RequestBody ResetPasswordRequest inputs) {
      ResetPasswordResponse resp = new ResetPasswordResponse();
      resp.success = false;

      UUID resetID = UUID.fromString(inputs.resetID);
      if (resetID == null) {
        return ResponseEntity.ok(resp);
      }

      if (inputs.newHashedPassword == null || inputs.newHashedPassword.isEmpty()) {
        return ResponseEntity.ok(resp);
      }

      resp.success = PasswordResetManager.resetPassword(resetID, inputs.newHashedPassword);

      return ResponseEntity.ok(resp);
    }

    @RequestMapping(value = "/edit-user-type", method = RequestMethod.POST)
    ResponseEntity<EditUserTypeResponse> editUserTypeRequestHandler(HttpServletRequest request, @RequestBody EditUserTypeRequest inputs) {
        EditUserTypeResponse resp = new EditUserTypeResponse();
        String sessionKey = SessionManager.getSessionCookie(request.getCookies());
      if (sessionKey == null) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resp);
      }
      Session userSession = SessionManager.getSession(UUID.fromString(sessionKey));
      // If the user's session key is invalid
      if (userSession == null) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resp);
      }
      Member ownerAccount = userSession.getMember();
      if (ownerAccount.getType() != MemberType.OWNER) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resp);
      }

      resp.success = SQLLinker.getInstance().editUserType(inputs.userID, MemberType.valueOf(inputs.type));
      resp.message = resp.success ? "Account type successfully updated" : "Error editing account type";

      return ResponseEntity.ok(resp);
    }

    public boolean isUsernameTaken(String username) {
      Member member = SQLLinker.getInstance().getMember(username);
      return member != null;
    }

    public boolean isEmailTaken(String email) {
      Member member = SQLLinker.getInstance().getMemberByEmail(email);
      return member != null;
    }

    public static class SimpleMember {
      @JsonProperty("id")
      String id;
      @JsonProperty("first_name")
      String firstName;
      @JsonProperty("last_name")
      String lastName;
      @JsonProperty("email")
      String email;
      @JsonProperty("member_type")
      MemberType memberType;
      @JsonProperty("username")
      String username;
    }
}