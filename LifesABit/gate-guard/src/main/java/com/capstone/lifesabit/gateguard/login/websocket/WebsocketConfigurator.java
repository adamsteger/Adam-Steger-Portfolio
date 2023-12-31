package com.capstone.lifesabit.gateguard.login.websocket;

import java.util.List;
import java.util.Map;

import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

public class WebsocketConfigurator extends ServerEndpointConfig.Configurator {
  @Override
    public void modifyHandshake(ServerEndpointConfig config, 
                                HandshakeRequest request, 
                                HandshakeResponse response)
    {
        Map<String, List<String>> headers = request.getHeaders();
        if(config != null && config.getUserProperties() != null && headers != null && headers.get("cookie") != null) {
          config.getUserProperties().put("cookie", headers.get("cookie"));
        }
        
    }
}
