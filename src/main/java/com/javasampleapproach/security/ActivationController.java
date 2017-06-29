package com.javasampleapproach.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.javasampleapproach.security.query.ActivationQuery;
import com.javasampleapproach.security.query.UserQuery;

@Controller
public class ActivationController {
	
	@Autowired
	private  UserQuery uq;
	
	@Autowired
	private  ActivationQuery aq;

	@RequestMapping("/signup_final")
	public String goToUserpage( @RequestParam(value="otp", required=true) String code) {
		
		String username = aq.getUsernameByCode(code);
		if(username != null){
			uq.validateUser(username);
		}
		
		return "index";
	}

}
