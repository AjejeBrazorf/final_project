package com.javasampleapproach.security.query;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.javasampleapproach.security.model.Segnalazione;
import com.javasampleapproach.security.model.TipoSegnalazione;
import com.javasampleapproach.security.repo.RateUserRepository;
import com.javasampleapproach.security.repo.SegnalationRepository;

@Service 
@Transactional
public class SegnalationQuery {

	@Autowired
	SegnalationRepository sR;

	@Autowired
	UserQuery userService;

	@Autowired
	RateUserRepository ruR;

	public Double segnalationVote(String name, String id, Integer rate) {
		// TODO Auto-generated method stub
		Double newAverage;
		Integer oldRate;

		oldRate = isPresentUserRate(name, Integer.parseInt(id));
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
		String nickname = userService.getUsernameByMail(name);
		int t = TipoSegnalazione.valueOf(tipo).ordinal();

		String id = insertSegnalation(nickname, lat, lng, date, indirizzo, t);
		Segnalazione s = new Segnalazione(nickname, lat, lng, date, null, indirizzo, TipoSegnalazione.valueOf(tipo), 0d, 0);
		s.setId(Integer.parseInt(id));

		return s;
	}

	//insert new Segnalation
	public String insertSegnalation(String nickname, double lat, double lng, Date dataInizio, String indirizzo, int tipo){
		return sR.insertSegnalazioni(nickname, lat, lng, tipo, dataInizio, indirizzo);
	}

	//change data fine of a segnalation
	public String  updateSegnalation(Date dataFine, String id){
		return sR.updateSegnalazione(dataFine, Integer.parseInt(id));
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

	//update Rate
	//mode : 0  new vote
	//mode : 1  update of old vote
	public Double updateRate(int mode, double rate, String id){
		return sR.updateRate(mode, rate, Integer.parseInt(id));
	}

	//insert UserRate
	public void insertUserRate(String username, String idSegnalation, int rate){
		ruR.insertUserRate(username, Integer.parseInt(idSegnalation), rate);
	}

	//update UserRate
	public void updateUserRate(String username, String idSegnalation, int rate){
		ruR.updateUserRate(rate, username, Integer.parseInt(idSegnalation));
	}

	//get vote of User for that segnalation
	public Integer isPresentUserRate(String username, int idSegnalation){
		return ruR.getUserRate(username, idSegnalation);
	}


}
