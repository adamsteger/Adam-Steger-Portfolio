package com.capstone.lifesabit.gateguard.login.websocket;

import com.capstone.lifesabit.gateguard.login.websocket.object.Base;
import com.capstone.lifesabit.gateguard.login.websocket.object.Notification;

/**
 * This contains a list of "types" that are recognized
 */
public enum WebsocketTypes {
  NOTIFICATION(Notification.class),
  BASE(Base.class),
  BLANK(null);

  Class klass;
  private WebsocketTypes(Class klass) {
    this.klass = klass;
  }
}
