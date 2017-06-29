package com.javasampleapproach.security.query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.javasampleapproach.security.repo.ActivationRepository;

@Service
public class ActivationQuery {
	@Autowired
	ActivationRepository acR;

	//select nickname by code
	public String getUsernameByCode(String code){
		return acR.getUsernameByCode(code);
	}

	//delete code
	public void deleteCode(String username){
		acR.deleteCode(username);
	}

	//insert Code
	public void insertCode(String username, String code){
		acR.insertCode(username, code);
	}

}
