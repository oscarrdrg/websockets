package aar.websockets.model;

import java.util.ArrayList;
/**
 * 
 * @author Oscar
 *
 */

public class ListaIntercambios {
	private ArrayList<Intercambio> listaIntercambio;
	
	public ListaIntercambios() {
		this.listaIntercambio = new ArrayList<>();
	}

	public ArrayList<Intercambio> getListaIntercambio() {
		return listaIntercambio;
	}

	public void setListaIntercambio(ArrayList<Intercambio> listaIntercambio) {
		this.listaIntercambio = listaIntercambio;
	}
	
	

}
