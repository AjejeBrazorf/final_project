package rest;

import java.util.List;

import org.springframework.hateoas.ResourceSupport;

import chat.ReceivedMessage;

public class MessagesResource extends ResourceSupport{

	private List<ReceivedMessage> messages;
	
	public MessagesResource(List<ReceivedMessage> messages) {
		this.setMessages(messages);
	}

	public List<ReceivedMessage> getMessages() {
		return messages;
	}

	public void setMessages(List<ReceivedMessage> messages) {
		this.messages = messages;
	}

}
