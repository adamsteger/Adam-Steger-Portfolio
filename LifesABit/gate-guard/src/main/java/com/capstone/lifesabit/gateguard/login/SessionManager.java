package com.capstone.lifesabit.gateguard.login;

import java.util.HashMap;
import java.util.UUID;

import javax.servlet.http.Cookie;

import com.capstone.lifesabit.gateguard.SQLLinker;

public class SessionManager {
  // The key to this map is the user's name
  private static HashMap<String, Session> sessionMap = new HashMap<>();
  private static HashMap<UUID, Session> sessionMapBySessionkey = new HashMap<>();
  // 1 hour
  private static long SESSION_DURATION = 1000 * 60 * 60;

  public static Session getSession(String username, String hashedPassword) {
    return getSession(username, hashedPassword, false);
  }

  public static Session getSession(String username, String hashedPassword, boolean preSalted) {
    if (sessionMap.containsKey(username)) {
      Session session = sessionMap.get(username);
      if (preSalted) {
        if (session.getMember().saltedHashedPassword.equals(hashedPassword)) {
          return sessionMap.get(username);
        }
      } else {
        if (session.getMember().checkPassword(hashedPassword)) {
          return sessionMap.get(username);
        }
      }
      return null;
    }
    return createNewSession(username, hashedPassword);
  }

  public static Session getSession(Member member) {
    return getSession(member.getUsername(), member.getSaltedHashedPassword(), true);
  }

  public static Session getSession(UUID sessionKey) {
    return sessionMapBySessionkey.get(sessionKey);
  }

  public static boolean isAuthenticated(String sessionKey) {
    if (sessionKey == null || sessionKey.isEmpty()) {
      return false;
    }
    return (getSession(UUID.fromString(sessionKey)) != null);
  }

  public static Session createNewSession(String username, String hashedPassword) {
    Member member = SQLLinker.getInstance().getMember(username);
    if (member == null || !member.checkPassword(hashedPassword)) {
      return null;
    }
    Session newSession = new Session(SESSION_DURATION, member);
    sessionMap.put(username, newSession);
    sessionMapBySessionkey.put(newSession.getSessionKey(), newSession);
    return newSession;
  }

  public static boolean removeSession(UUID sessionKey) {
    Session secondSess = sessionMapBySessionkey.remove(sessionKey);
    Session firstSess = sessionMap.remove(secondSess.getMember().getUsername());
    return (firstSess != null) && (secondSess != null);
  }

  public static String getSessionCookie(Cookie[] cookies) {
    if (cookies == null) {
      return null;
    }
    for (Cookie cookie : cookies) {
      if (LoginController.SESSION_KEY_COOKIE.equals(cookie.getName())) {
        return cookie.getValue();
      }
    }
    return null;
  }
}
