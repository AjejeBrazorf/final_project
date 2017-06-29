package com.javasampleapproach.security;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.javasampleapproach.security.model.AnonymousUser;
import com.javasampleapproach.security.model.Message;
import com.javasampleapproach.security.model.User;
import com.javasampleapproach.security.query.ActivationQuery;
import com.javasampleapproach.security.query.MessageQuery;
import com.javasampleapproach.security.query.UserQuery;

import chat.ReceivedMessage;
import rest.ArgumentsResource;
import rest.MessagesResource;
import rest.PersonsResource;

@RestController
public class ChatRestController {
	
	@Autowired
	private MessageQuery messageService;
	
	@Autowired
	private UserQuery uq;
	
	private final int pagination = 5;
	private final String paginationString = "5";

	@RequestMapping(value="/users", method=RequestMethod.GET, produces="application/json")
	public  HttpEntity<PersonsResource> getUsers(
			@RequestParam(value = "offset", required = false, defaultValue = "0")int offset, 
			@RequestParam(value = "limit", required = false, defaultValue = paginationString)int limit){
		
		//chiamo servizio in cui da User passo a AnonymouUser e mi ritorna lista, cioè PersonResource
		List<User> list = uq.getUsers(limit, offset);
		List<AnonymousUser> usersList = new ArrayList<>();
		for(User user:list)
			usersList.add(new AnonymousUser(user));
		
		PersonsResource pr = new PersonsResource(usersList);
		
		int nBlocks = usersList.size()/pagination;
		
		//self link
		Link selfLink = linkTo(ChatRestController.class).slash("users").withSelfRel();
        pr.add(selfLink);
        
        if((offset+limit) < usersList.size()){
			Link nextLink = linkTo(methodOn(ChatRestController.class)
							.getUsers(offset+limit, pagination)).withRel("next");
			pr.add(nextLink);
		}
		

		if(offset != 0){
			Link prevLink = linkTo(methodOn(ChatRestController.class)
							.getUsers(offset-1 , pagination)).withRel("prev");
			pr.add(prevLink);
		}
		
        
        Link firstLink = linkTo(methodOn(ChatRestController.class).getUsers(0, pagination)).withRel("first");
        pr.add(firstLink);
        
        int offsetLast = (usersList.size()%pagination) == 0?(nBlocks-1)*pagination:nBlocks*pagination;
		Link lastLink = linkTo(methodOn(ChatRestController.class)
						.getUsers(offsetLast , pagination))
						.withRel("last");
		pr.add(lastLink);
              
		return new ResponseEntity<PersonsResource>(pr, HttpStatus.OK);
	}
	

	@RequestMapping(value="/arguments", method=RequestMethod.GET, produces="application/json")
	public HttpEntity<ArgumentsResource> getArguments(){
		ArgumentsResource ar = new ArgumentsResource();
		//self link
		Link selfLink = linkTo(ChatRestController.class).slash("arguments").withSelfRel();
		ar.add(selfLink);
		
		 
		Link allUsersLink = linkTo(methodOn(ChatRestController.class).getUsers(0, pagination))
									.withRel("allUsers");
        ar.add(allUsersLink);
        
        Link busMessagesLink = linkTo(methodOn(ChatRestController.class).getMessagesByArgument("bus", 0, pagination))
        							.withRel("busAndMetroMessages");
        ar.add(busMessagesLink);
        
        Link trafficLink = linkTo(methodOn(ChatRestController.class).getMessagesByArgument("traffic", 0, pagination))
        							.withRel("trafficMessages");
        ar.add(trafficLink);
        
        Link bikeLink = linkTo(methodOn(ChatRestController.class).getMessagesByArgument("bike", 0, pagination))
        							.withRel("bikeTourMessages");
        ar.add(bikeLink);
        
		return new ResponseEntity<ArgumentsResource>(ar, HttpStatus.OK);
	}
	
	
	@RequestMapping(value="/{id}/messages", method=RequestMethod.GET, produces="application/json")
	public HttpEntity<MessagesResource> getMessagesByArgument(@PathVariable String id,
			@RequestParam(value = "offset", required = false, defaultValue = "0")int offset, 
			@RequestParam(value = "limit", required = false, defaultValue = paginationString)int limit){
		
		List<Message> messages = messageService.getMessages(offset, limit, id);
		List<ReceivedMessage> messagesList = new ArrayList<>();
		for(Message message:messages)
			messagesList.add(new ReceivedMessage(message));
		
		MessagesResource mr = new MessagesResource(messagesList);

		int nBlocks = messagesList.size()/pagination;
		
		//self link
		Link selfLink = linkTo(ChatRestController.class).slash("users").withSelfRel();
		mr.add(selfLink);

		if((offset+limit) < messagesList.size()){
			Link nextLink = linkTo(methodOn(ChatRestController.class)
					.getMessagesByArgument(id, offset+limit, pagination)).withRel("next");
			mr.add(nextLink);
		}
		

		if(offset != 0){
			Link prevLink = linkTo(methodOn(ChatRestController.class)
					.getMessagesByArgument(id, offset-1 , pagination)).withRel("prev");
			mr.add(prevLink);
		}
		
		Link firstLink = linkTo(methodOn(ChatRestController.class).getMessagesByArgument(id, 0, pagination)).withRel("first");
		mr.add(firstLink);

		int offsetLast = (messagesList.size()%pagination) == 0?(nBlocks-1)*pagination:nBlocks*pagination;
		Link lastLink = linkTo(methodOn(ChatRestController.class)
						.getMessagesByArgument(id,offsetLast , pagination))
						.withRel("last");
		mr.add(lastLink);
		        
		return new ResponseEntity<MessagesResource>(mr, HttpStatus.OK);
	}
	
}

