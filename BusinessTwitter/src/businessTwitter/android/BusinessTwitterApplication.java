package businessTwitter.android;

import twitter4j.Tweet;
import android.app.Application;

public class BusinessTwitterApplication extends Application {
	
	private String titulo;
	private String ciudad;
	private double latitude;
	private double longitude;
	private String tipo;
	private int numPagina;
	private Tweet tweetSelected;
	private boolean conLinks;

	public BusinessTwitterApplication() {
		super();
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getCiudad() {
		return ciudad;
	}

	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public int getNumPagina() {
		return numPagina;
	}

	public void setNumPagina(int numPagina) {
		this.numPagina = numPagina;
	}

	public Tweet getTweetSelected() {
		return tweetSelected;
	}

	public void setTweetSelected(Tweet tweetSelected) {
		this.tweetSelected = tweetSelected;
	}

	public boolean isConLinks() {
		return conLinks;
	}

	public void setConLinks(boolean conLinks) {
		this.conLinks = conLinks;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
}
