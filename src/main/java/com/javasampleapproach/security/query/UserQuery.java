package com.javasampleapproach.security.query;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.javasampleapproach.security.model.Autentication;
import com.javasampleapproach.security.model.FornitoreCarSharing;
import com.javasampleapproach.security.model.Gender;
import com.javasampleapproach.security.model.Istruzione;
import com.javasampleapproach.security.model.Occupazione;
import com.javasampleapproach.security.model.Role;
import com.javasampleapproach.security.model.TipoCarburante;
import com.javasampleapproach.security.model.TipoViaggio;
import com.javasampleapproach.security.model.User;
import com.javasampleapproach.security.repo.AutenticationRepository;
import com.javasampleapproach.security.repo.RoleRepository;
import com.javasampleapproach.security.repo.UserRepository;

@Service 
@Transactional
public class UserQuery {

	@Autowired
	AutenticationRepository aR;

	@Autowired
	ActivationQuery aq;

	@Autowired
	RoleRepository rR;

	@Autowired
	UserRepository uR;

	public void validateUser(String username) {
		enableUser(username);
		aq.deleteCode(username);
	}

	public void insertNewUser(String email, String otp, String nickname, String password, boolean b, String role) {
		aq.insertCode(email, otp);
		insertCredentials(email, password, b);
		insertUser(nickname, email);
		insertRole(email, role);
	}

	//Enable User
	public void enableUser(String username){
		aR.enableUser(username);
	}

	//insert new Credenziali
	public void insertCredentials(String email, String password, Boolean b){
		aR.insertCredenziali(email, password, b);

		return;
	}

	//insert new User-Role
	public void insertRole(String email, String role){
		rR.insertUserRole(email, role);

		return;
	}

	//insert new User
	public void insertUser(String nickname, String email){
		uR.insertUser(nickname, email, 0, 0, 0, 0, false, 0, 0, false, 0, false, false, false, 0, "");
		return;
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

	//get nickname by mail
	public String getUsernameByMail(String email){
		String s = uR.findnicknameByemail(email);

		return s;
	}

	//get image by mail
	public String getImage(String email){
		String s = uR.findImageByemail(email);
		return s;
	}

	//get image by nickname
	public String getImageByNickname(String nickname){
		String s = uR.findImageByNickname(nickname);
		return s;
	}

	//test if nickname is already present
	public int nicknameAlreadyPresent(String nickname) {
		return uR.findNickname(nickname);
	}

	//utente da mail
	public User getUserbyUsername(String s){
		User n = uR.findByemail(s);

		return n;
	}

	//credenziali da mail
	public List<Autentication> getCredentials(String s){
		return aR.findByemail(s);
	}

	//Get User Password
	public String getPassword(String username){
		return aR.getPasswordByName(username);
	}

	//Update User Credentials
	public void updateUserCred(String username, String password){
		aR.updateCredentials(password, username);
	}

	//get Roles of one user
	public List<Role> getRolesbyUsername(String email){
		return rR.findRolesByemail(email);
	}

	//test if mail is already present
	public int mailAlreadyPresent(String email) {
		return aR.findMail(email);
	}

	//utente da mail
	public User getUserbyNickname(String s){
		User n = uR.findByNickname(s);

		return n;
	}
}
