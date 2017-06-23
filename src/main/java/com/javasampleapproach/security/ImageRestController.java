package com.javasampleapproach.security;

import java.security.Principal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.javasampleapproach.security.model.FornitoreCarSharing;
import com.javasampleapproach.security.model.Gender;
import com.javasampleapproach.security.model.Istruzione;
import com.javasampleapproach.security.model.Occupazione;
import com.javasampleapproach.security.model.TipoCarburante;
import com.javasampleapproach.security.model.TipoViaggio;
import com.javasampleapproach.security.query.UsersQuery;

@RestController
public class ImageRestController {
	@Autowired
	private  UsersQuery uq;

	
	@RequestMapping(value="/image", method=RequestMethod.PUT, produces="application/json")
	public HttpEntity<?> rateSegnalation(
			Principal name, 
			@RequestParam(value = "image", required = true, defaultValue = "null")String image){
		System.out.println(image);
		uq.updateImageUser(image, name.getName());	
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
