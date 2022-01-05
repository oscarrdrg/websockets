package aar.websockets.websocket;

import java.util.Date;
import java.util.Random;

import aar.websockets.model.Intercambio;
import aar.websockets.model.Kpi;
import aar.websockets.model.ListaIntercambios;
import aar.websockets.model.ListaKpi;

/**
 * 
 * @author Oscar
 *
 */

public class ThreadActualizarIntercambios extends Thread {
	private ListaKpi listaKpi;
	private ListaIntercambios listaIntercambios;
	private int time = 0;

	public ThreadActualizarIntercambios(ListaKpi listaKpi, ListaIntercambios listaIntercambios, int time) {
		this.listaKpi = listaKpi;
		this.listaIntercambios = listaIntercambios;
		this.time = time;
	}

	@Override
	public void run() {

		float valorIntercambio;

		while (true) {
			try {
				Thread.sleep(time);
				listaKpi.rellenarLista();
				System.out.println("El tamaño de la lista es: " + listaIntercambios.getListaIntercambio().size());
				if (!listaIntercambios.getListaIntercambio().isEmpty()) {
					for (Intercambio intercambio : listaIntercambios.getListaIntercambio()) {

						valorIntercambio = (float)(Math.random()*100+1);

						System.out.println("El nuevo valor es: " + valorIntercambio);
						intercambio.setValor(Float.toString(valorIntercambio));
						intercambio.setDate(new Date().toString());
						System.out.println("La nueva fecha es: " + intercambio.getDate());
						System.out.println("INTERCAMBIO ACTUALIZADO");

					}
				}

			} catch (InterruptedException e) {
				System.err.println("ERROR EN LA ACTUALIZACION.");
			}

		}

	}

	private int getValorKpiByName(String name) {
		for (Kpi kpi : listaKpi.getListaKpi()) {
			if (kpi.getNombre().equals(name)) {

				return kpi.getValor();
			}

		}
		return 0;
	}

}
