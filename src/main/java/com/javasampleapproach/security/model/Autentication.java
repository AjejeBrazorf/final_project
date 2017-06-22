package com.javasampleapproach.security.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "users")
public class Autentication {

	@Id 
	@Column(name = "username")
	private String email; 

	private String password;
	@Column(name = "enabled")
	private boolean state;
	

	public Autentication(){
		
	}
	
	public Autentication(String email, String password, boolean state){
		this.email = email;
		this.password = password;
		this.state = state;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public boolean isState() {
		return state;
	}

	public void setState(boolean state) {
		this.state = state;
	}
	

}
