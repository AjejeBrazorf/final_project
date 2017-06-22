package com.javasampleapproach.security.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.javasampleapproach.security.query.UsersQuery;

@Controller
public class WebController {
   
	@Autowired
	private  UsersQuery pgq;
	
	@RequestMapping(value={"/"})
	public String home(Model model, Principal name){
		if(name!= null)
			model.addAttribute("nickname", pgq.getUsernameByMail(name.getName()));
		return "index";
	}

	@RequestMapping(value={"/welcome"})
	public String welcome(){
		return "welcome";
	}
  
    @RequestMapping(value="/admin")
    public String admin(){
        return "admin";
    }
   
    @RequestMapping(value={"/login"})
    public String login(){
        return "login";
    }
   
   
    @RequestMapping(value={"/registration"})
    public String registration(){
        return "registration";
    }
   
    @RequestMapping(value="/403")
    public String Error403(){
        return "403";
    }
}