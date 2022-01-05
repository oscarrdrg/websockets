package aar.websockets.model;

/**
 * 
 * @author Oscar
 *
 */

public class Kpi {

	private int valor;
	private String nombre;

	public Kpi(String nombre) {
		this.nombre = nombre;

	}

	public int getValor() {
		return valor;
	}

	public void setValor(int valor) {
		this.valor = valor;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

}
