package com.javasampleapproach.security.query;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.javasampleapproach.security.model.Segnalazione;
import com.javasampleapproach.security.repo.SegnalationRepository;

@Service
public class SegnalationQuery {

	@Autowired
	SegnalationRepository sR;
	
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
		return sR.findById(id);
	}
	
	//insert new Segnalation
	public String insertSegnalation(String nickname, double lat, double lng, Date dataInizio, String indirizzo, int tipo){
		return sR.insertSegnalazioni(nickname, lat, lng, tipo, dataInizio, indirizzo);
	}
	
	//change data fine of a segnalation
	public String  updateSegnalation(Date dataFine, String id){
		return sR.updateSegnalazione(dataFine, id);
	}
	
	//update Rate
	public String updateRate(double rate, String id){
		return sR.updateRate(rate, id);
	}
}
