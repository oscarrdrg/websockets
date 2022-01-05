package aar.websockets.model;

import java.util.ArrayList;

/**
 * 
 * @author Oscar
 *
 */

public class ListaKpi {
	private ArrayList<Kpi> listaKpi;

	public ListaKpi() {
		this.listaKpi = new ArrayList<>();
	}

	public ArrayList<Kpi> getListaKpi() {
		return listaKpi;
	}

	public void setListaKpi(ArrayList<Kpi> listaKpi) {
		this.listaKpi = listaKpi;
	}

	public void rellenarLista() {

		listaKpi.add(new Kpi("Product Sales"));
		listaKpi.add(new Kpi("Product Expenses"));
		listaKpi.add(new Kpi("Return on Assets"));
		listaKpi.add(new Kpi("Product Expenses Budget"));
		listaKpi.add(new Kpi("Product Sales Target"));
		listaKpi.add(new Kpi("Return on Equity"));
		listaKpi.add(new Kpi("Return on Investment"));

	}

}
