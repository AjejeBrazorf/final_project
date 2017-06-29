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
	@Autowired
	RateUserRepository ruR;
	@Autowired
	UserRepository uR;
	@Autowired
	MessageRepository mR;
	@Autowired
	AutenticationRepository aR;
	@Autowired
	RoleRepository rR;
	@Autowired
	SegnalationRepository sR;
	
	
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
	
	
	//insert UserRate
	public void insertUserRate(String username, String idSegnalation, int rate){
		ruR.insertUserRate(username, Integer.parseInt(idSegnalation), rate);
	}
	
	//insert UserRate
	public void updateUserRate(String username, String idSegnalation, int rate){
		ruR.updateUserRate(rate, username, Integer.parseInt(idSegnalation));
	}

	//insert UserRate
	public Integer isPresentUserRate(String username, int idSegnalation){
		return ruR.getUserRate(username, idSegnalation);
	}

	//insert UserRate
	public Integer isPresentUserRate(String username, String idSegnalation){
		return ruR.getUserRate(username, Integer.parseInt(idSegnalation));
	}

	//select All Segnalation
	public List<Segnalazione> getAll(){
		return sR.findAll();
	}
	
	//select All segnalation of one type
	public List<Segnalazione> getAllforType(int tipo){
		return sR.findByType(tipo);
	}
	
	//select by Id
	public Segnalazione getById(String id){
		return sR.findById(Integer.parseInt(id));
	}
	
	//insert new Segnalation
	public String insertSegnalation(String nickname, double lat, double lng, Date dataInizio, String indirizzo, int tipo){
		return sR.insertSegnalazioni(nickname, lat, lng, tipo, dataInizio, indirizzo);
	}
	
	//change data fine of a segnalation
	public String  updateSegnalation(Date dataFine, String id){
		return sR.updateSegnalazione(dataFine, Integer.parseInt(id));
	}
	

	//update Rate
	//mode : 0  new vote
	//mode : 1  update of old vote
	public Double updateRate(int mode, double rate, String id){
		return sR.updateRate(mode, rate, Integer.parseInt(id));
	}

	//User us1 = uR.findByUsername(p);
	//List<Message> lm = mR.findByTopic("geografia");

	//utente da mail
	public User getUserbyUsername(String s){
		User n = uR.findByemail(s);
	
		return n;
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
	
	//get Roles of one user
	public List<Role> getRolesbyUsername(String email){
		return rR.findRolesByemail(email);
	}

	//test if mail is already present
	public int mailAlreadyPresent(String email) {
		// TODO Auto-generated method stub
		return aR.findMail(email);
	}

	//test if nickname is already present
	public int nicknameAlreadyPresent(String nickname) {
		// TODO Auto-generated method stub
		return uR.findNickname(nickname);
	}

	
	//metodi nuovi per realizzare le transazioni
	
	public void validateUser(String username) {
		// TODO Auto-generated method stub
		enableUser(username);
		deleteCode(username);
	}

	public void insertNewUser(String email, String otp, String nickname, String password, boolean b, String role) {
		// TODO Auto-generated method stub
		insertCode(email, otp);
		insertCredentials(email, password, b);
		insertUser(nickname, email);
		insertRole(email, role);
	}

	public String insertNewMessage(String name, String text, String mode) {
		// TODO Auto-generated method stub
		String nickname = getUsernameByMail(name);
		insertMessage(new Date(), text, nickname, mode);
		
		return nickname;
	}

	public Double segnalationVote(String name, String id, Integer rate) {
		// TODO Auto-generated method stub
		Double newAverage;
		Integer oldRate;
		
		oldRate = isPresentUserRate(name, id);
		//System.out.println("oldrate: " +oldRate);
		if(oldRate != null){
			newAverage = updateRate(1, rate-oldRate, id);
			//System.out.println("Ho aggiornato la media");
			updateUserRate(name, id, rate);
			//System.out.println("Ho aggiornato il voto dello user");
		}
		else{
			//aggiungo anche in tabelle di user-voto
			insertUserRate(name, id, rate);
			//System.out.println("action 2: " +action);
			newAverage = updateRate(0, rate, id);
			//System.out.println("action 3: " +newAverage);
		}
		
		return newAverage;
	}

	public Segnalazione insertNewSegnalation(String name, Double lat, Double lng, Date date, String indirizzo, String tipo) {
		// TODO Auto-generated method stub
		String nickname = getUsernameByMail(name);
		int t = TipoSegnalazione.valueOf(tipo).ordinal();
		
		String id = insertSegnalation(nickname, lat, lng, date, indirizzo, t);
		Segnalazione s = new Segnalazione(nickname, lat, lng, date, null, indirizzo, TipoSegnalazione.valueOf(tipo), 0d, 0);
		s.setId(Integer.parseInt(id));
		
		return s;
	}
	
}
