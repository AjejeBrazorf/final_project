package com.javasampleapproach.security;

import java.security.Principal;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.javasampleapproach.security.model.Message;
import com.javasampleapproach.security.model.User;
import com.javasampleapproach.security.query.UsersQuery;

import chat.ReceivedMessage;
import chat.SentMessage;

@Controller
public class ChatController implements ApplicationListener<ApplicationEvent>{
	
	@Autowired
	private  UsersQuery pgq;

	
	public void join(MessageHeaders hs) {
		String nickname = "Da riempire con il nickname";
		String sessionId=(String)hs.get("simpSessionId");
	}

	
	@MessageMapping("/bus")
    @SendTo("/topic/bus")
    public ReceivedMessage sendMessageBus(SentMessage message, Principal name) throws Exception {
		String nickname = pgq.getUsernameByMail(name.getName());
		
		pgq.insertMessage(new Date(), message.getText(), nickname, "bus");
        return new ReceivedMessage(nickname, message.getText(), new Date());
    }
	
	@MessageMapping("/traffic")
    @SendTo("/topic/traffic")
    public ReceivedMessage sendMessageTraffic(SentMessage message, Principal name) throws Exception {
		String nickname = pgq.getUsernameByMail(name.getName());
		
		pgq.insertMessage(new Date(), message.getText(), nickname, "traffic");
        return new ReceivedMessage(nickname, message.getText(), new Date());
    }
	
	@MessageMapping("/bike")
    @SendTo("/topic/bike")
    public ReceivedMessage sendMessageBike(SentMessage message, Principal name) throws Exception {
		String nickname = pgq.getUsernameByMail(name.getName());
		
		pgq.insertMessage(new Date(), message.getText(), nickname, "bike");
        return new ReceivedMessage(nickname, message.getText(), new Date());
    }


	@Override
	public void onApplicationEvent(ApplicationEvent e) {
		if (e instanceof SessionDisconnectEvent) {
			SessionDisconnectEvent sde=(SessionDisconnectEvent)e;
			//users.removeUser(sde.getSessionId());
		}
	}

	@RequestMapping("/biketopic")
	public String uploadMessagesBike(Model model, Principal name) {
		List<Message> m = pgq.getMessagebyTopic("bike");
		Collections.reverse(m);
		User user = pgq.getUserbyUsername(name.getName());
		model.addAttribute("messages", m);
		model.addAttribute("user",user);
		model.addAttribute("image", pgq.getImage(name.getName()));
		return "biketopic";
		
	}
	
	@RequestMapping("/transporttopic")
	public String uploadMessagesTransport(Model model, Principal name) {
		List<Message> m = pgq.getMessagebyTopic("bus");
		Collections.reverse(m);
		User user = pgq.getUserbyUsername(name.getName());
		model.addAttribute("messages", m);
		model.addAttribute("user",user);
		model.addAttribute("image", pgq.getImage(name.getName()));
		return "transporttopic";
		
	}
	
	@RequestMapping("/cartopic")
	public String uploadMessagesCar(Model model, Principal name) {
		List<Message> m = pgq.getMessagebyTopic("traffic");
		Collections.reverse(m);
		User user = pgq.getUserbyUsername(name.getName());
		model.addAttribute("messages", m);
		model.addAttribute("user",user);
		model.addAttribute("image", pgq.getImage(name.getName()));
		model.addAttribute("nickname", pgq.getUsernameByMail(name.getName()));
		return "cartopic";
	}
	
	


}