package com.javasampleapproach.security.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "segnalazioni")
public class RateUser implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	String username;
	int idSegnalazione;
	int rate;
	
	
	public RateUser(){
		
	}
	
	public RateUser(String username, int idSegnalazione, int rate){
		this.username = username;
		this.idSegnalazione = idSegnalazione;
		this.rate = rate;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public int getIdSegnalazione() {
		return idSegnalazione;
	}
	public void setIdSegnalazione(int idSegnalazione) {
		this.idSegnalazione = idSegnalazione;
	}
	public int getRate() {
		return rate;
	}
	public void setRate(int rate) {
		this.rate = rate;
	}
	
}
