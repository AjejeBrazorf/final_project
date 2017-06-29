package com.javasampleapproach.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.javasampleapproach.security.query.ActivationQuery;

@Controller
public class ActivationController {
	
	//@Autowired
	//private  UsersQuery uq;
	@Autowired
	private  ActivationQuery aq;

	@RequestMapping("/signup_final")
	public String goToUserpage( @RequestParam(value="otp", required=true) String code) {
		
		String username = aq.getUsernameByCode(code);
		//System.out.println("username "+username);
		if(username != null){
			aq.validateUser(username);
			//
			//uq.enableUser(username);
			//aq.deleteCode(username);
		}
		
		return "index";
	}

}
