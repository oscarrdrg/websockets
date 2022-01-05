package aar.websockets.websocket;

import java.util.Set;

import javax.json.JsonObject;
import javax.json.spi.JsonProvider;
import javax.websocket.Session;
import aar.websockets.model.ListaIntercambios;
import aar.websockets.model.Intercambio;

/**
 * 
 * @author Oscar
 *
 */

public class ThreadEnviarActualizaciones extends Thread {

	private ListaIntercambios listaIntercambios;
	private Set<Session> sessions;
	private int time = 0;
	private IntercambioSessionHandler ish;

	public ThreadEnviarActualizaciones(ListaIntercambios listaIntercambios, Set<Session> sessions, int time,
			IntercambioSessionHandler ish) {

		this.listaIntercambios = listaIntercambios;
		this.sessions = sessions;
		this.time = time;
		this.ish = ish;

	}

	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(time);
				if (!listaIntercambios.getListaIntercambio().isEmpty()) {
					JsonProvider provider = JsonProvider.provider();
					for (Intercambio intercambio : listaIntercambios.getListaIntercambio()) {
						JsonObject addMessage = provider.createObjectBuilder().add("action", "update")
								.add("id", intercambio.getId()).add("kpi1", intercambio.getKpi1())
								.add("kpi2", intercambio.getKpi2()).add("valor", intercambio.getValor())
								.add("date", intercambio.getDate()).build();
						ish.sendToAllConnectedSessions(addMessage);

					}
				}
			} catch (InterruptedException e) {
				System.err.println("Error: " + e.getMessage());
			}
		}

	}

}
