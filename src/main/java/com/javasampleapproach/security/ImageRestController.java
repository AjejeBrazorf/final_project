package com.javasampleapproach.security;

import java.security.Principal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import com.javasampleapproach.security.query.ActivationQuery;
import com.javasampleapproach.security.query.UserQuery;
import com.mongodb.util.JSON;

@RestController
public class ImageRestController {
	
	@Autowired
	private  UserQuery uq;

	
	@RequestMapping(value="/image", method=RequestMethod.PUT, produces="application/json")
	public HttpEntity<?> rateSegnalation(
			Principal name, 
			@RequestParam(value = "image", required = true, defaultValue = "null")String image){
		//System.out.println(image);
		uq.updateImageUser(image, name.getName());	
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	
	@RequestMapping(value="/imageUser", method=RequestMethod.GET, produces="text/plain")
	public HttpEntity<?> getUserImage(
			Principal name, 
			@RequestParam(value = "username", required = true, defaultValue = "null")String user){
			String img=uq.getImageByNickname(user);
			HttpEntity<?> r = new ResponseEntity<String>(img, HttpStatus.OK);
			//System.out.println(r);
		return r;
	}
	
}
