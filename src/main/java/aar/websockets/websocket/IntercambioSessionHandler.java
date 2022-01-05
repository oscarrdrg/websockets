package aar.websockets.websocket;

import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.json.JsonObject;
import javax.json.spi.JsonProvider;
import javax.websocket.Session;

import aar.websockets.model.Intercambio;
import aar.websockets.model.ListaIntercambios;
import aar.websockets.model.ListaKpi;

@ApplicationScoped
public class IntercambioSessionHandler {

	private final ListaKpi listaKpi = new ListaKpi();
	private final int time = 5 * 1000;
	private ListaIntercambios listaIntercambios = new ListaIntercambios();
	private int intercambioId = 0;
	private final Set<Session> sessions = new HashSet<>();

	public IntercambioSessionHandler() {

	}

	public void addSession(Session session) {
		sessions.add(session);
		this.listaKpi.rellenarLista();
		System.out.println("Nuevo cliente suscrito: " + session.getId());
		System.out.println("Numero de sesiones: " + sessions.size());
		JsonProvider provider = JsonProvider.provider();
		for (Intercambio intercambio : listaIntercambios.getListaIntercambio()) {
			JsonObject addMessage = provider.createObjectBuilder().add("action", "add").add("id", intercambio.getId())
					.add("kpi1", intercambio.getKpi1()).add("kpi2", intercambio.getKpi2())
					.add("valor", intercambio.getValor()).add("date", intercambio.getDate()).build();
			sendToSession(session, addMessage);

		}

		if (sessions.size() == 1) {
			ThreadActualizarIntercambios tai = new ThreadActualizarIntercambios(listaKpi, listaIntercambios, time);
			tai.start();

			ThreadEnviarActualizaciones tea = new ThreadEnviarActualizaciones(listaIntercambios, sessions, time, this);
			tea.start();

		}
	}

	public void removeSession(Session session) {
		sessions.remove(session);
	}

	public void addIntercambio(Intercambio intercambio) {

		Intercambio aux = new Intercambio();
		aux.setId(intercambioId);
		intercambioId++;

		boolean exists = false;
		int pos = 0;
		for (int i = 0; i < listaIntercambios.getListaIntercambio().size(); i++) {
			if (intercambio.getKpi1().equals(listaIntercambios.getListaIntercambio().get(i).getKpi1())
					&& intercambio.getKpi2().equals(listaIntercambios.getListaIntercambio().get(i).getKpi2())) {
				exists = true;
				pos = i;
			}

		}

		if (exists) {

			aux.setKpi1(listaIntercambios.getListaIntercambio().get(pos).getKpi1());
			aux.setKpi2(listaIntercambios.getListaIntercambio().get(pos).getKpi2());
			aux.setDate(listaIntercambios.getListaIntercambio().get(pos).getDate());
			aux.setValor(listaIntercambios.getListaIntercambio().get(pos).getValor());

		} else {

			int pos1 = 0, pos2 = 0;
			for (int i = 0; i < listaIntercambios.getListaIntercambio().size(); i++) {
				if (intercambio.getKpi1().equals(listaKpi.getListaKpi().get(i).getNombre()))
					pos1 = i;
				if (intercambio.getKpi2().equals(listaKpi.getListaKpi().get(i).getNombre()))
					pos2 = i;
			}

			int valor1Aux = listaKpi.getListaKpi().get(pos1).getValor();
			int valor2Aux = listaKpi.getListaKpi().get(pos2).getValor();
			float valorIntercambio = (float) valor1Aux - (float) valor2Aux;

			aux.setKpi1(intercambio.getKpi1());
			aux.setKpi2(intercambio.getKpi2());
			aux.setValor(Float.toString(valorIntercambio));
			aux.setDate(new Date().toString());
			listaIntercambios.getListaIntercambio().add(aux);

		}

		JsonProvider provider = JsonProvider.provider();
		JsonObject addMessage = provider.createObjectBuilder().add("action", "add").add("id", aux.getId())
				.add("kpi1", aux.getKpi1()).add("kpi2", aux.getKpi2()).add("valor", aux.getValor())
				.add("date", aux.getDate()).build();
		sendToAllConnectedSessions(addMessage);

	}

	public void removeIntercambio(int id) {
		Intercambio intercambio = getIntercambioById(id);
		if (intercambio != null) {
			listaIntercambios.getListaIntercambio().remove(intercambio);
			JsonProvider provider = JsonProvider.provider();
			JsonObject removeMessage = provider.createObjectBuilder().add("action", "remove").add("id", id).build();
			sendToAllConnectedSessions(removeMessage);

		}
	}

	private Intercambio getIntercambioById(int id) {
		for (Intercambio intercambio : listaIntercambios.getListaIntercambio()) {
			if (intercambio.getId() == id) {

				return intercambio;
			}

		}

		return null;
	}

	protected void sendToAllConnectedSessions(JsonObject message) {
		for (Session session : sessions) {
			sendToSession(session, message);
		}
	}

	private void sendToSession(Session session, JsonObject message) {

		try {
			session.getBasicRemote().sendText(message.toString());
		} catch (IOException ex) {
			sessions.remove(session);
			Logger.getLogger(IntercambioSessionHandler.class.getName()).log(Level.SEVERE, null, ex);
			ex.printStackTrace();
		}

	}

}
