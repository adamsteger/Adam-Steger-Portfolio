package com.capstone.lifesabit.gateguard;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.util.UUID;

import javax.servlet.http.Cookie;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import com.capstone.lifesabit.gateguard.login.LoginController;
import com.capstone.lifesabit.gateguard.login.Member;
import com.capstone.lifesabit.gateguard.login.Member.MemberType;
import com.capstone.lifesabit.gateguard.login.Session;
import com.capstone.lifesabit.gateguard.login.SessionManager;

public class SessionTests {
  private SQLLinker sqlLinker;

  @BeforeEach
  void setup() {
    this.sqlLinker = mock(SQLLinker.class);
  }

  @Test
  @DisplayName("Session key creation")
  void should_retTrue_whenSessionKeyCreatedSuccessfully() {
    // given
    String givenUsername = "testuser";
    String givenFirstName = "John";
    String givenLastName = "Doe";
    String givenPhoneNumber = "+15551234567";
    String givenEmail = "test@email.com";
    String givenHashedPassword = "pretendi'mhashed";
    MemberType givenType = MemberType.USER;
    Member givenMember = new Member(givenUsername, givenFirstName, givenLastName,
                                    givenPhoneNumber, givenEmail, givenHashedPassword, givenType);

    // when
    Session userSession;
    try (MockedStatic<SQLLinker> mockedLinker = mockStatic(SQLLinker.class)) {
      mockedLinker.when(() -> SQLLinker.getInstance()).thenReturn(sqlLinker);
      when(sqlLinker.getMember(givenUsername)).thenReturn(givenMember);
      userSession = SessionManager.createNewSession(givenUsername, givenHashedPassword);
    }

    //then
    assertNotNull(userSession);
    assertEquals(userSession.getSessionKey(), SessionManager.getSession(givenUsername, givenHashedPassword).getSessionKey());
    assertTrue(SessionManager.isAuthenticated(userSession.getSessionKey().toString()));
    assertTrue(SessionManager.removeSession(userSession.getSessionKey()));
    assertFalse(SessionManager.isAuthenticated(userSession.getSessionKey().toString()));
  }

  @Test
  void should_retTrue_whenSessionSuccessfullyRemoved() {
    // given
    String givenUsername = "testuser";
    String givenFirstName = "John";
    String givenLastName = "Doe";
    String givenPhoneNumber = "+15551234567";
    String givenEmail = "test@email.com";
    String givenHashedPassword = "pretendi'mhashed";
    MemberType givenType = MemberType.USER;
    Member givenMember = new Member(givenUsername, givenFirstName, givenLastName,
                                    givenPhoneNumber, givenEmail, givenHashedPassword, givenType);

    // when
    Session userSession;
    try (MockedStatic<SQLLinker> mockedLinker = mockStatic(SQLLinker.class)) {
      mockedLinker.when(() -> SQLLinker.getInstance()).thenReturn(sqlLinker);
      when(sqlLinker.getMember(givenUsername)).thenReturn(givenMember);
      userSession = SessionManager.createNewSession(givenUsername, givenHashedPassword);
    }

    //then
    assertAll(
      () -> assertTrue(SessionManager.removeSession(userSession.getSessionKey())),
      () -> assertFalse(SessionManager.isAuthenticated(userSession.getSessionKey().toString()))
    );
  }

  @Test
  void should_retTrue_whenCookieSuccessfullyRetrieved() {
    // given
    String givenSessionKey = UUID.randomUUID().toString();
    Cookie cookie = new Cookie(LoginController.SESSION_KEY_COOKIE, givenSessionKey);
    Cookie[] cookies = {cookie, new Cookie("somethingElse", "yadda"), new Cookie("padding", "forTheCookies")};
    // when
    String returnedSessionKey = SessionManager.getSessionCookie(cookies);
    
    // then
    assertEquals(givenSessionKey, returnedSessionKey);
  }
}
