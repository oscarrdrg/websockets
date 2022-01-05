package aar.websockets.model;

/**
 * 
 * @author Oscar
 *
 */

public class Intercambio {
	private int id;
	private String kpi1;
	private String kpi2;
	private String valor;
	private String date;

	public Intercambio() {

	}
	

	public int getId() {
		return id;
	}



	public void setId(int id) {
		this.id = id;
	}


	public String getKpi1() {
		return kpi1;
	}

	public void setKpi1(String kpi1) {
		this.kpi1 = kpi1;
	}

	public String getKpi2() {
		return kpi2;
	}

	public void setKpi2(String kpi2) {
		this.kpi2 = kpi2;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

}
