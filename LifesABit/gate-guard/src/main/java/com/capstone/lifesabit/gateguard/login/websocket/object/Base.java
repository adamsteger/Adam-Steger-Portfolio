package com.capstone.lifesabit.gateguard.login.websocket.object;

import com.capstone.lifesabit.gateguard.login.websocket.WebsocketTypes;

public class Base {
  String sessionID;
  WebsocketTypes category;
  Object value;

  public Base(String sessionID, WebsocketTypes category, Object value) {
    this.sessionID = sessionID;
    this.category = category;
    this.value = value;
  }

  public String getSessionID() {
    return sessionID;
  }

  public void setSessionID(String sessionID) {
    this.sessionID = sessionID;
  }

  public WebsocketTypes getCategory() {
    return category;
  }

  public void setCategory(WebsocketTypes category) {
    this.category = category;
  }

  public Object getValue() {
    return value;
  }

  public void setValue(Object value) {
    this.value = value;
  }
}
