package com.capstone.lifesabit.gateguard;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.websocket.Session;
import javax.websocket.RemoteEndpoint.Basic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.capstone.lifesabit.gateguard.login.LoginController;
import com.capstone.lifesabit.gateguard.login.websocket.WebsocketController;
import com.capstone.lifesabit.gateguard.login.websocket.WebsocketTypes;
import com.capstone.lifesabit.gateguard.login.websocket.object.Base;
import com.capstone.lifesabit.gateguard.login.websocket.object.Notification;
import com.capstone.lifesabit.gateguard.notifications.NotificationType;

public class WebsocketTests {

  WebsocketController websocketController;
  @Captor
  ArgumentCaptor<String> messageCaptor;

  @BeforeEach
  void setup() {
    this.websocketController = new WebsocketController();
    this.messageCaptor = ArgumentCaptor.forClass(String.class);
  }

  @Test
  public void should_retTrue_whenWebsocketSuccessfullyCreated() throws IOException {
    // given
    String givenSessionKey = UUID.randomUUID().toString();
    UUID givenSessionID = UUID.randomUUID();
    List<String> cookies = Arrays.asList(
      LoginController.SESSION_KEY_COOKIE + "=" + givenSessionKey,
      "other_cookie=value",
      "test=thingy"
    );
    Map<String, Object> userPropertyMap = new HashMap<>();
    userPropertyMap.put("cookie", cookies);
    Session mockSession = mock(Session.class);
    Basic mockBasic = mock(Basic.class);
    when(mockSession.getUserProperties()).thenReturn(userPropertyMap);
    when(mockSession.getBasicRemote()).thenReturn(mockBasic);
    when(mockSession.getId()).thenReturn(givenSessionID.toString());
    Base expectedObj = new Base(givenSessionID.toString(), WebsocketTypes.BLANK, null);;
    String expectedJson = WebsocketController.objectMapper.writeValueAsString(expectedObj);
    
    // when
    Executable executable = () -> websocketController.onOpen(mockSession);
    
    // then
    assertAll(
      // Make sure it doesn't throw an exception
      () -> assertDoesNotThrow(executable),
      // Make sure it calls the sendText method, and capture the arg used
      () -> verify(mockBasic).sendText(messageCaptor.capture()),
      // Make sure it populated all of the maps correctly
      () -> assertEquals(mockSession, websocketController.websocketMap.get(givenSessionID.toString())),
      () -> assertEquals(UUID.fromString(givenSessionKey), websocketController.websocketIDsToLogins.get(givenSessionID.toString())),
      () -> assertEquals(givenSessionID.toString(), websocketController.loginsToWebsocketIDs.get(UUID.fromString(givenSessionKey))),
      // Make sure it sent the expected message to the websocket
      () -> assertEquals(expectedJson, messageCaptor.getValue())
    );
  }

  @Test
  public void should_retTrue_whenWebsocketWithoutCookieSuccessfullyRespondedTo() throws IOException {
    // given
    List<String> cookies = Arrays.asList(
      "other_cookie=value",
      "test=thingy"
    );
    Map<String, Object> userPropertyMap = new HashMap<>();
    userPropertyMap.put("cookie", cookies);
    Session mockSession = mock(Session.class);
    when(mockSession.getUserProperties()).thenReturn(userPropertyMap).thenReturn(new HashMap<String, Object>());
    
    // when
    Executable withCookiesButNoSessionKey = () -> websocketController.onOpen(mockSession);
    Executable withNoCookies = () -> websocketController.onOpen(mockSession);
    
    // then
    assertAll(
      // Make sure it doesn't throw an exception
      () -> assertDoesNotThrow(withCookiesButNoSessionKey),
      () -> assertDoesNotThrow(withNoCookies),
      // Make sure it closes the socket session both times
      () -> verify(mockSession, times(2)).close(),
      // Verify that we didn't fill any of the maps with junk data
      () -> assertTrue(websocketController.websocketMap.isEmpty()),
      () -> assertTrue(websocketController.websocketIDsToLogins.isEmpty()),
      () -> assertTrue(websocketController.loginsToWebsocketIDs.isEmpty())
    );
  }

  @Test
  public void should_retTrue_whenWebsocketSuccessfullyClosed() throws IOException {
    // given
    String givenSessionKey = UUID.randomUUID().toString();
    UUID givenSessionID = UUID.randomUUID();
    List<String> cookies = Arrays.asList(
      LoginController.SESSION_KEY_COOKIE + "=" + givenSessionKey,
      "other_cookie=value",
      "test=thingy"
    );
    Map<String, Object> userPropertyMap = new HashMap<>();
    userPropertyMap.put("cookie", cookies);
    Session mockSession = mock(Session.class);
    Basic mockBasic = mock(Basic.class);
    when(mockSession.getUserProperties()).thenReturn(userPropertyMap);
    when(mockSession.getBasicRemote()).thenReturn(mockBasic);
    when(mockSession.getId()).thenReturn(givenSessionID.toString());
    websocketController.onOpen(mockSession);
    
    // when
    Executable executable = () -> websocketController.onClose(mockSession);
    
    // then
    assertAll(
      // Make sure it doesn't throw an exception
      () -> assertDoesNotThrow(executable),
      // Make sure it de-populated all of the maps correctly
      () -> assertTrue(websocketController.websocketMap.isEmpty()),
      () -> assertTrue(websocketController.websocketIDsToLogins.isEmpty()),
      () -> assertTrue(websocketController.loginsToWebsocketIDs.isEmpty())
    );
  }

  @Test
  public void should_retTrue_whenWebsocketMessageSent() throws IOException {
    // given
    String givenSessionKey = UUID.randomUUID().toString();
    UUID givenSessionID = UUID.randomUUID();
    UUID givenNotifID = UUID.randomUUID();
    UUID givenPassID = UUID.randomUUID();
    UUID givenUserID = UUID.randomUUID();
    NotificationType givenNotifType = NotificationType.PASS_USED;
    String givenTitle = "Yadda yadda";
    long givenTimestamp = System.currentTimeMillis();
    List<String> cookies = Arrays.asList(
      LoginController.SESSION_KEY_COOKIE + "=" + givenSessionKey
    );
    Map<String, Object> userPropertyMap = new HashMap<>();
    userPropertyMap.put("cookie", cookies);
    Session mockSession = mock(Session.class);
    Basic mockBasic = mock(Basic.class);
    when(mockSession.getUserProperties()).thenReturn(userPropertyMap);
    when(mockSession.getBasicRemote()).thenReturn(mockBasic);
    when(mockSession.getId()).thenReturn(givenSessionID.toString());
    Notification notif = new Notification(givenNotifID, givenPassID, givenUserID, givenNotifType, givenTitle, givenTimestamp);
    Base expectedObj = new Base(givenSessionID.toString(), WebsocketTypes.NOTIFICATION, notif);;
    String expectedJson = WebsocketController.objectMapper.writeValueAsString(expectedObj);
    websocketController.onOpen(mockSession);
    
    // when
    Executable executable = () -> WebsocketController.sendMessage(WebsocketTypes.NOTIFICATION, notif, UUID.fromString(givenSessionKey));
    
    // then
    assertAll(
      // Make sure it doesn't throw an exception
      () -> assertDoesNotThrow(executable),
      // Make sure it calls the sendText method, and capture the arg used
      () -> verify(mockBasic, times(2)).sendText(messageCaptor.capture()),
      // Make sure it sent the expected message to the websocket
      () -> assertEquals(expectedJson, messageCaptor.getValue())
    );
  }
}
