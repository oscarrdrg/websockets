package aar.websockets.websocket;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import java.util.HashSet;
import java.util.Set;
import javax.json.JsonObject;
import javax.json.spi.JsonProvider;
import javax.websocket.Session;
import aar.websockets.model.Device;

@ApplicationScoped
public class DeviceSessionHandler {
    private int deviceId = 0;
    private final Set<Session> sessions = new HashSet<>();
    private final Set<Device> devices = new HashSet<>();
    
    public void addSession(Session session) {
        sessions.add(session);
        JsonProvider provider = JsonProvider.provider();
        for (Device device : devices) {
            JsonObject addMessage = provider.createObjectBuilder()
                .add("action", "add")
                .add("id", device.getId())
                .add("name", device.getName())
                .add("type", device.getType())
                .add("status", device.getStatus())
                .add("description", device.getDescription())
                .build();
            sendToSession(session, addMessage);
        }
    }

    public void removeSession(Session session) {
        sessions.remove(session);
    }
    
    public void addDevice(Device device) {
        device.setId(deviceId);
        devices.add(device);
        deviceId++;
        JsonProvider provider = JsonProvider.provider();
        JsonObject addMessage = provider.createObjectBuilder()
                .add("action", "add")
                .add("id", device.getId())
                .add("name", device.getName())
                .add("type", device.getType())
                .add("status", device.getStatus())
                .add("description", device.getDescription())
                .build();
        sendToAllConnectedSessions(addMessage);   
    }

    public void removeDevice(int id) {
        Device device = getDeviceById(id);
        if (device != null) {
            devices.remove(device);
            JsonProvider provider = JsonProvider.provider();
            JsonObject removeMessage = provider.createObjectBuilder()
                    .add("action", "remove")
                    .add("id", id)
                    .build();
            sendToAllConnectedSessions(removeMessage);
        }   
    }

    public void toggleDevice(int id) {
        JsonProvider provider = JsonProvider.provider();
        Device device = getDeviceById(id);
        if (device != null) {
            if ("On".equals(device.getStatus())) {
                device.setStatus("Off");
            } else {
                device.setStatus("On");
            }
            JsonObject updateDevMessage = provider.createObjectBuilder()
                    .add("action", "toggle")
                    .add("id", device.getId())
                    .add("status", device.getStatus())
                    .build();
            sendToAllConnectedSessions(updateDevMessage);
        }      
    }

    private Device getDeviceById(int id) {
        for (Device device : devices) {
            if (device.getId() == id) {
                return device;
            }
        }
        return null;
    }

    private void sendToAllConnectedSessions(JsonObject message) {  
        for (Session session : sessions) {
            sendToSession(session, message);
        }
    }

    private void sendToSession(Session session, JsonObject message) {
        try {
            session.getBasicRemote().sendText(message.toString());
        } catch (IOException ex) {
            sessions.remove(session);
            Logger.getLogger(DeviceSessionHandler.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
    }
    
}