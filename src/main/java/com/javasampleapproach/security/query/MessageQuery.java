package com.javasampleapproach.security.query;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.javasampleapproach.security.model.Message;
import com.javasampleapproach.security.repo.MessageRepository;

@Service
@Transactional
public class MessageQuery {

	@Autowired
	private UserQuery userService;

	@Autowired
	MessageRepository mR;

	public String insertNewMessage(String name, String text, String mode) {
		String nickname = userService.getUsernameByMail(name);
		insertMessage(new Date(), text, nickname, mode);

		return nickname;
	}

	//Lista di utenti da limit e offset
	public List<Message> getMessages(int limit, int offset, String topic){
		return mR.findMessagesByOffset(limit, offset, topic);
	}

	//lista di ultimi 10 messaggi dato un topic
	public List<Message> getMessagebyTopic(String s){
		return mR.findByTopic(s);
	}

	//insert new Message
	public void insertMessage(Date d, String text, String username, String type){
		mR.insertMessage(username, text, d, type);

		return;
	}

}
