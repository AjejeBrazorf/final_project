package com.javasampleapproach.security.query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.javasampleapproach.security.repo.RateUserRepository;

@Service
public class RateUserQuery {

	@Autowired
	RateUserRepository rR;
	
	//insert UserRate
	public void insertUserRate(String username, String idSegnalation, int rate){
		rR.insertUserRate(username, Integer.parseInt(idSegnalation), rate);
	}
	
	//insert UserRate
	public void updateUserRate(String username, String idSegnalation, int rate){
		rR.updateUserRate(rate, username, Integer.parseInt(idSegnalation));
	}

	//insert UserRate
	public Integer isPresentUserRate(String username, int idSegnalation){
		int rate;
		if((rate = rR.getUserRate(username, idSegnalation)) > 0)
			return rate;
		else
			return null;
	}
}
