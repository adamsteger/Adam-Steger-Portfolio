import React, { useState, useCallback, useEffect } from 'react';
import { toast } from 'react-toastify';
import useWebsocket, {ReadyState} from 'react-use-websocket';

export const WebsocketContext = () => {
  const [socketURL, setSocketUrl] = useState(process.env.NODE_ENV === "development"
                                                                   ? "ws://localhost:8080/websocket"
                                                                   : "wss://www.gate-guard.com:8443/GateGuard-0.0.1-SNAPSHOT/websocket");
  const [messageHistory, setMessageHistory] = useState<MessageEvent[]>([]);
  const {sendMessage, lastMessage, readyState} = useWebsocket(socketURL);

  useEffect(() => {
    if (lastMessage !== null) {
      console.log("Websocket: " + lastMessage.data);
      setMessageHistory((prev) => prev.concat(lastMessage));
      let theData = JSON.parse(lastMessage.data);
      console.log(theData);
      if (theData.category === "NOTIFICATION") {
        toast.info(theData.value.title);
      }
    }
  }, [lastMessage, setMessageHistory]);

  return (<></>);
};