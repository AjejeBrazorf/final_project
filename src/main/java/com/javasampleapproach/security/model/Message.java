package com.javasampleapproach.security.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Message")
public class Message implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id")
	private int id;
	
	@Column(name = "username")
    private String username;
	
	@Column(name = "message")
	private String message;
	
	
	@Column(name = "data")
	private Date date;
	
	@Column(name = "topic")
	private String topic;

	
	
	public Message(){
		
	}
	
	public Message(String username, String message, Date date, String topic){
		this.username = username;
		this.message = message;
		this.date = date;
		this.topic = topic;
	}
	
	public String getTopic() {
		return topic;
	}


	public void setTopic(String topic) {
		this.topic = topic;
	}
	
	public String getNickname() {
		return username;
	}
	public void setNickname(String username) {
		this.username = username;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}

}
