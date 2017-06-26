package com.javasampleapproach.security.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class RateUserId implements Serializable {

    private int idsegnalation;
	private String username;
	
	public int getIdsegnalation() {
		return idsegnalation;
	}
	public void setIdsegnalation(int idsegnalation) {
		this.idsegnalation = idsegnalation;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}

    /** getters and setters **/

}
