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
import com.javasampleapproach.security.query.ActivationQuery;
import com.javasampleapproach.security.query.MessageQuery;
import com.javasampleapproach.security.query.UserQuery;

import chat.ReceivedMessage;
import chat.SentMessage;

@Controller
public class ChatController implements ApplicationListener<ApplicationEvent>{
	
	@Autowired
	private UserQuery userService;
	
	@Autowired
	private MessageQuery messageService;

	
	public void join(MessageHeaders hs) {
		String nickname = "Da riempire con il nickname";
		String sessionId=(String)hs.get("simpSessionId");
	}

	
	@MessageMapping("/bus")
    @SendTo("/topic/bus")
    public ReceivedMessage sendMessageBus(SentMessage message, Principal name) throws Exception {
		
		String nickname = messageService.insertNewMessage(name.getName(), message.getText(), "bus");
		//String nickname = aq.getUsernameByMail(name.getName());
		
		//aq.insertMessage(new Date(), message.getText(), nickname, "bus");
        return new ReceivedMessage(nickname, message.getText(), new Date());
    }
	
	@MessageMapping("/traffic")
    @SendTo("/topic/traffic")
    public ReceivedMessage sendMessageTraffic(SentMessage message, Principal name) throws Exception {
		
		String nickname = messageService.insertNewMessage(name.getName(), message.getText(), "traffic");
		//String nickname = pgq.getUsernameByMail(name.getName());
		//pgq.insertMessage(new Date(), message.getText(), nickname, "traffic");
        return new ReceivedMessage(nickname, message.getText(), new Date());
    }
	
	@MessageMapping("/bike")
    @SendTo("/topic/bike")
    public ReceivedMessage sendMessageBike(SentMessage message, Principal name) throws Exception {
		
		String nickname = messageService.insertNewMessage(name.getName(), message.getText(), "bike");
		//String nickname = pgq.getUsernameByMail(name.getName());
		//pgq.insertMessage(new Date(), message.getText(), nickname, "bike");
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
		List<Message> m = messageService.getMessagebyTopic("bike");
		Collections.reverse(m);
		User user = userService.getUserbyUsername(name.getName());
		model.addAttribute("messages", m);
		model.addAttribute("user",user);
		model.addAttribute("image", userService.getImage(name.getName()));
		model.addAttribute("nickname", userService.getUsernameByMail(name.getName()));
		return "biketopic";
		
	}
	
	@RequestMapping("/transporttopic")
	public String uploadMessagesTransport(Model model, Principal name) {
		List<Message> m = messageService.getMessagebyTopic("bus");
		Collections.reverse(m);
		User user = userService.getUserbyUsername(name.getName());
		model.addAttribute("messages", m);
		model.addAttribute("user",user);
		model.addAttribute("image", userService.getImage(name.getName()));
		model.addAttribute("nickname", userService.getUsernameByMail(name.getName()));
		return "transporttopic";
		
	}
	
	@RequestMapping("/cartopic")
	public String uploadMessagesCar(Model model, Principal name) {
		List<Message> m = messageService.getMessagebyTopic("traffic");
		Collections.reverse(m);
		User user = userService.getUserbyUsername(name.getName());
		model.addAttribute("messages", m);
		model.addAttribute("user",user);
		model.addAttribute("image", userService.getImage(name.getName()));
		model.addAttribute("nickname", userService.getUsernameByMail(name.getName()));
		return "cartopic";
	}
	




}