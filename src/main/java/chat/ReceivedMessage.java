package chat;

import java.util.Date;

import com.javasampleapproach.security.model.Message;

public class ReceivedMessage {

	private String user;
	private String message;
	private Date date;

	public ReceivedMessage(String user, String message, Date date) {
		this.setUser(user);
		this.setMessage(message);
		this.setDate(date);
	}
	
	public ReceivedMessage(Message message) {
		this.setUser(message.getNickname());
		this.setMessage(message.getMessage());
		this.setDate(message.getDate());
	}

	public String getMessage() {
		return message;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
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
