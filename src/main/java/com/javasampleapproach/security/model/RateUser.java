package com.javasampleapproach.security.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "rateUser")
public class RateUser implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@ManyToOne
	@JoinColumn(name="username", nullable=false)
	User username;
	
	@Id
	@ManyToOne
	@JoinColumn(name="idsegnalation", nullable=false)
	Segnalazione idSegnalazione;
	int rate;
	
	
	public RateUser(){
		
	}
	

	public RateUser(User username, Segnalazione idSegnalazione, int rate) {
		super();
		this.username = username;
		this.idSegnalazione = idSegnalazione;
		this.rate = rate;
	}
	
	public User getUsername() {
		return username;
	}


	public void setUsername(User username) {
		this.username = username;
	}


	public Segnalazione getIdSegnalazione() {
		return idSegnalazione;
	}


	public void setIdSegnalazione(Segnalazione idSegnalazione) {
		this.idSegnalazione = idSegnalazione;
	}
	public int getRate() {
		return rate;
	}
	public void setRate(int rate) {
		this.rate = rate;
	}
	
}
