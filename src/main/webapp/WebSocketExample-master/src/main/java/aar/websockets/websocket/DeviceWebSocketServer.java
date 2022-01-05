package aar.websockets.websocket;

import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import javax.enterprise.context.ApplicationScoped;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import aar.websockets.model.Device;

@ApplicationScoped
@ServerEndpoint("/actions")
public class DeviceWebSocketServer {
    
    private static DeviceSessionHandler sessionHandler = new DeviceSessionHandler();
    
    public DeviceWebSocketServer() {
        System.out.println("class loaded " + this.getClass());
    }
    
    @OnOpen
    public void onOpen(Session session) {
        sessionHandler.addSession(session);
        System.out.println("cliente suscrito, sesion activa");
    }

    @OnClose
    public void onClose(Session session) {   
        sessionHandler.removeSession(session);
        System.out.println("cliente cierra conexión, sesion eliminada");
    }

    @OnError
    public void onError(Throwable error) {
        Logger.getLogger(DeviceWebSocketServer.class.getName()).
                log(Level.SEVERE, null, error);
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        try (JsonReader reader = Json.createReader(new StringReader(message))) {
            JsonObject jsonMessage = reader.readObject();

            if ("add".equals(jsonMessage.getString("action"))) {
                Device device = new Device();
                device.setName(jsonMessage.getString("name"));
                device.setDescription(jsonMessage.getString("description"));
                device.setType(jsonMessage.getString("type"));
                device.setStatus("Off");
                sessionHandler.addDevice(device);
            }

            if ("remove".equals(jsonMessage.getString("action"))) {
                int id = (int) jsonMessage.getInt("id");
                sessionHandler.removeDevice(id);
            }

            if ("toggle".equals(jsonMessage.getString("action"))) {
                int id = (int) jsonMessage.getInt("id");
                sessionHandler.toggleDevice(id);
            }
        } 
    }
    
    
    
}