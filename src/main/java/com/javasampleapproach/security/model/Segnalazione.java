package com.javasampleapproach.security.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "segnalazioni")
public class Segnalazione implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private int id;
    private String nickname;
	private double lat;
	private double lng;
	@Column(name = "datainizio")
	private Date dataInizio;	
	private TipoSegnalazione tipo;
	private double rate;
	private int count;
	@Column(name = "datafine")
	private Date dataFine;
	private String indirizzo;


	public Segnalazione(){
		
	}
	
	public Segnalazione(String nickname, double lat, double lng, Date dataInizio, Date dataFine, String address, TipoSegnalazione tipo, double rate, int count){
		this.nickname = nickname;
		this.lat = lat;
		this.lng = lng;
		this.dataInizio = dataInizio;
		this.dataFine = dataFine;
		this.indirizzo = address;
		this.tipo = tipo;
		this.rate = rate;
		this.count = count;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	public Date getDataInizio() {
		return dataInizio;
	}

	public void setDataInizio(Date dataInizio) {
		this.dataInizio = dataInizio;
	}

	public TipoSegnalazione getTipo() {
		return tipo;
	}

	public void setTipo(TipoSegnalazione tipo) {
		this.tipo = tipo;
	}

	public double getRate() {
		return rate;
	}

	public void setRate(int rate) {
		this.rate = rate;
	}

	public Date getDataFine() {
		return dataFine;
	}

	public void setDataFine(Date dataFine) {
		this.dataFine = dataFine;
	}
	
	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
	public String getIndirizzo() {
		return indirizzo;
	}

	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}


}
