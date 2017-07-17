package com.javasampleapproach.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.javasampleapproach.security.model.User;
import com.javasampleapproach.security.query.UserQuery;

@RestController
public class UserController {
	
	@Autowired
	private UserQuery uq;

	@RequestMapping(value = "/{id}/myInfo", method=RequestMethod.GET, produces="application/json")
	public HttpEntity<User> getUser(@PathVariable String id) {
		//System.out.println(id);
		User user = uq.getUserbyNickname(id);
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}
}
