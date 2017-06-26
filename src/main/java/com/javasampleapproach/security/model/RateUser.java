package com.javasampleapproach.security.model;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "rateUser")
public class RateUser implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@EmbeddedId
	RateUserId id;
	int rate;
	
	
	public RateUser(){
		
	}
	
	public RateUser(String username, int idSegnalazione, int rate){
		this.id.setUsername(username);
		this.id.setIdsegnalation(idSegnalazione);
		this.rate = rate;
	}
	
	public int getRate() {
		return rate;
	}
	public void setRate(int rate) {
		this.rate = rate;
	}
	
	public RateUserId getId() {
		return id;
	}

	public void setId(RateUserId id) {
		this.id = id;
	}
}
