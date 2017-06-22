package com.javasampleapproach.security.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name = "user_roles")
@IdClass(RoleId.class)
public class Role implements Serializable{

	private static final long serialVersionUID = -3009157732242241606L;
	@Id
	@Column(name = "username")
	private String email;
	@Id
	@Column(name = "role")
	private String role;
	
	
	public Role(){
		
	}
	
	public Role(String email, String role) {
		this.email = email;
		this.role = role;
	}
	
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	
	
}
