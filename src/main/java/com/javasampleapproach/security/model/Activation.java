package com.javasampleapproach.security.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "activationCode")
public class Activation {

	@Id
	private String username;
	private String code;
	
	public Activation(){
		
	}
	
	public Activation(String username, String code) {
		super();
		this.username = username;
		this.code = code;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
}
