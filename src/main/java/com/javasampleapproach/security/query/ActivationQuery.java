package com.javasampleapproach.security.query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.javasampleapproach.security.repo.ActivationRepository;

@Service
public class ActivationQuery {
	@Autowired
	ActivationRepository aR;
	
	//select nickname by code
	public String getUsernameByCode(String code){
		return aR.getUsernameByCode(code);
	}
	
	//delete code
	public void deleteCode(String username){
		aR.deleteCode(username);
	}
	
	//insert Code
	public void insertCode(String username, String code){
		aR.insertCode(username, code);
	}
}
