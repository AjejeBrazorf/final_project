package com.javasampleapproach.security.query;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.javasampleapproach.security.model.Autentication;
import com.javasampleapproach.security.model.FornitoreCarSharing;
import com.javasampleapproach.security.model.Gender;
import com.javasampleapproach.security.model.Istruzione;
import com.javasampleapproach.security.model.Message;
import com.javasampleapproach.security.model.Occupazione;
import com.javasampleapproach.security.model.Role;
import com.javasampleapproach.security.model.Segnalazione;
import com.javasampleapproach.security.model.TipoCarburante;
import com.javasampleapproach.security.model.TipoSegnalazione;
import com.javasampleapproach.security.model.TipoViaggio;
import com.javasampleapproach.security.model.User;
import com.javasampleapproach.security.repo.ActivationRepository;
import com.javasampleapproach.security.repo.AutenticationRepository;
import com.javasampleapproach.security.repo.MessageRepository;
import com.javasampleapproach.security.repo.RateUserRepository;
import com.javasampleapproach.security.repo.RoleRepository;
import com.javasampleapproach.security.repo.SegnalationRepository;
import com.javasampleapproach.security.repo.UserRepository;

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
