package aar.websockets.websocket;

import java.io.StringReader;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import aar.websockets.model.Intercambio;

@ApplicationScoped
@ServerEndpoint("/actions")

public class IntercambioWebSocketServer {

	private static IntercambioSessionHandler intercambioSessionHandler = new IntercambioSessionHandler();

	public IntercambioWebSocketServer() {
		System.out.println("Clase cargada " + this.getClass());
	}

	@OnOpen
	public void onOpen(Session session) {
		intercambioSessionHandler.addSession(session);
		System.out.println("Cliente suscrito: " + session.getId());
	}

	@OnClose
	public void onClose(Session session) {
		intercambioSessionHandler.removeSession(session);
		System.out.println("Cliente desconectado: " + session.getId());
	}

	@OnError
	public void onError(Throwable error) {
		Logger.getLogger(IntercambioWebSocketServer.class.getName()).log(Level.SEVERE, null, error);
	}

	@OnMessage
	public void onMessage(String message, Session session) {
		try (JsonReader reader = Json.createReader(new StringReader(message))) {
			JsonObject jsonMessage = reader.readObject();

			if ("add".equals(jsonMessage.getString("action"))) {
				System.out.println(jsonMessage.getString("kpi1"));
				System.out.println(jsonMessage.getString("kpi2"));

				Intercambio intercambio = new Intercambio();

				intercambio.setKpi1(jsonMessage.getString("kpi1"));
				intercambio.setKpi2(jsonMessage.getString("kpi2"));
				intercambioSessionHandler.addIntercambio(intercambio);
			}

			if ("remove".equals(jsonMessage.getString("action"))) {
				int id = (int) jsonMessage.getInt("id");
				intercambioSessionHandler.removeIntercambio(id);
			}

		}

	}

}
