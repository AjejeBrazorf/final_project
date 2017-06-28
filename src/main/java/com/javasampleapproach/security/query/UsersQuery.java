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
import com.javasampleapproach.security.model.TipoCarburante;
import com.javasampleapproach.security.model.TipoViaggio;
import com.javasampleapproach.security.model.User;
import com.javasampleapproach.security.repo.AutenticationRepository;
import com.javasampleapproach.security.repo.MessageRepository;
import com.javasampleapproach.security.repo.RoleRepository;
import com.javasampleapproach.security.repo.UserRepository;

@Service
public class UsersQuery{

	@Autowired
	UserRepository uR;
	@Autowired
	MessageRepository mR;
	@Autowired
	AutenticationRepository aR;
	@Autowired
	RoleRepository rR;

	//User us1 = uR.findByUsername(p);
	//List<Message> lm = mR.findByTopic("geografia");

	//utente da mail
	public User getUserbyUsername(String s){
		User n = uR.findByemail(s);
	
		return n;
	}
	
	//utente da mail
	public User getUserbyNickname(String s){
		User n = uR.findByNickname(s);

		return n;
	}
	
	public String getUsernameByMail(String email){
		String s = uR.findnicknameByemail(email);
		
		return s;
	}
	
	public String getImage(String email){
		String s = uR.findImageByemail(email);
		return s;
	}
	
	public String getImageByNickname(String nickname){
		String s = uR.findImageByNickname(nickname);
		return s;
	}
	
	//lista di ultimi 10 messaggi dato un topic
	public List<Message> getMessagebyTopic(String s){
		return mR.findByTopic(s);
	}
	
	//credenziali da mail
	public List<Autentication> getCredentials(String s){
		return aR.findByemail(s);
	}
	
	//insert new User
	public void insertUser(String nickname, String email){
		uR.insertUser(nickname, email, 0, 0, 0, 0, false, 0, 0, false, 0, false, false, false, 0, "");
		
		return;
	}
	
	//insert new Credenziali
	public void insertCredentials(String email, String password, Boolean b){
		aR.insertCredenziali(email, password, b);
		
		return;
	}
	
	//insert new Message
	public void insertMessage(Date d, String text, String username, String type){
		mR.insertMessage(username, text, d, type);
		
		return;
	}
	
	//insert new User-Role
	public void insertRole(String email, String role){
		rR.insertUserRole(email, role);
		
		return;
	}
	
	//Get User Password
	public String getPassword(String username){
		return aR.getPasswordByName(username);
	}
	
	//Update User Credentials
	public void updateUserCred(String username, String password){
		aR.updateCredentials(password, username);
	}
	
	//Enable User
	public void enableUser(String username){
		aR.enableUser(username);
	}
	
	//Update User
	public void updateUser(String mail, Gender g, int eta, Istruzione titolo, Occupazione occ, Boolean hasCar, int annoImmatr, TipoCarburante carb, Boolean carSharing, FornitoreCarSharing fornitoreCarSharing, Boolean useBike, Boolean useBikeSharing, Boolean useMezzi, TipoViaggio tipo, String foto){
		uR.updateUser(mail, g.ordinal(), eta, titolo.ordinal(), occ.ordinal(), hasCar, annoImmatr, carb.ordinal(), carSharing, fornitoreCarSharing.ordinal(), useBike, useBikeSharing, useMezzi, tipo.ordinal(), foto);
	}
	
	//Update Image User
	public void updateImageUser(String foto, String mail){
		uR.updateImageUser(foto, mail);
	}
	
	//Lista di utenti da limit e offset
	public List<User> getUsers(int limit, int offset){
		return uR.findUsersByOffset(limit, offset);
	}
	
	//Lista di utenti da limit e offset
	public List<Message> getMessages(int limit, int offset, String topic){
		return mR.findMessagesByOffset(limit, offset, topic);
	}
	
	public List<Role> getRolesbyUsername(String email){
		return rR.findRolesByemail(email);
	}

	public int mailAlreadyPresent(String email) {
		// TODO Auto-generated method stub
		return aR.findMail(email);
	}

	public int nicknameAlreadyPresent(String nickname) {
		// TODO Auto-generated method stub
		return uR.findNickname(nickname);
	}
	
	
}
