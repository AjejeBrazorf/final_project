package com.javasampleapproach.security;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.javasampleapproach.security.model.SegnalationForClient;
import com.javasampleapproach.security.model.Segnalazione;
import com.javasampleapproach.security.model.TipoSegnalazione;
import com.javasampleapproach.security.query.SegnalationQuery;


@RestController
public class SegnalationRestController {
	
	@Autowired
	private SegnalationQuery service;
	//@Autowired
	//private UsersQuery userService;
	//@Autowired
	//private RateUserQuery rateService;
	
	
	@RequestMapping(value="/segnalations", method=RequestMethod.GET, produces="application/json")
	public HttpEntity<List<SegnalationForClient>> getSegnalations(
			@RequestParam(value = "name", required = false)String name,
			@RequestParam(value = "type", required = false)String type){
		
		List<Segnalazione> list;
		List<SegnalationForClient> newList = new ArrayList<>();
		int voto;
		
		if(type == null){
			list = service.getAll();
		}else{
			int intType = (TipoSegnalazione.valueOf(type)).ordinal();
			list = service.getAllforType(intType);
		}
		for(Segnalazione s:list){
			SegnalationForClient sfc = new SegnalationForClient();
			if(name != null){
				//System.out.println("Name :"+name+" id: "+s.getId());
				voto = service.isPresentUserRate(name, s.getId());
				sfc.setVoto(voto);
			}
			sfc.setSegnalazione(s);
			newList.add(sfc);
		}
		
		return new ResponseEntity<List<SegnalationForClient>>(newList, HttpStatus.OK);
	}
	
	@RequestMapping(value="/segnalations/{id}", method=RequestMethod.GET, produces="application/json")
	public HttpEntity<SegnalationForClient> getSegnalation(
			@RequestParam(value = "name", required = false)String name,
			@PathVariable String id){
		SegnalationForClient sfc = new SegnalationForClient();
		int voto;
		Segnalazione s = service.getById(id);
		if(name != null){
			voto = service.isPresentUserRate(name, s.getId());
			sfc.setVoto(voto);
		}
		sfc.setSegnalazione(s);
		
		return new ResponseEntity<SegnalationForClient>(sfc, HttpStatus.OK);
	}
	
	@RequestMapping(value="/{name}/segnalations/{id}", method=RequestMethod.PUT, produces="application/json")
	public HttpEntity<Double> rateSegnalation(
			@PathVariable String name,
			@PathVariable String id,
			@RequestParam(value = "action", required = true)String action,
			@RequestParam(value = "rate", required = false)Integer rate){
		Double newAverage = 0.0;
		//System.out.println("action : " +action+"id : "+id);
		if(action.equals("rate")){
			newAverage = service.segnalationVote(name, id, rate);
//			Integer oldRate;
//			oldRate = rateService.isPresentUserRate(name.getName(), id);
//			System.out.println("oldrate: " +oldRate);
//			if(oldRate != null){
//				newAverage = service.updateRate(1, rate-oldRate, id);
//				System.out.println("Ho aggiornato la media");
//				rateService.updateUserRate(name.getName(), id, rate);
//				System.out.println("Ho aggiornato il voto dello user");
//			}
//			else{
//				//aggiungo anche in tabelle di user-voto
//				rateService.insertUserRate(name.getName(), id, rate);
//				System.out.println("action 2: " +action);
//				newAverage = service.updateRate(0, rate, id);
//				System.out.println("action 3: " +newAverage);
//			}
		}
		else if(action.equals("cancel"))
			service.updateSegnalation(new Date(), id);
		else
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		
		return new ResponseEntity<Double>(newAverage, HttpStatus.ACCEPTED);
	}
	
	@RequestMapping(value="/{name}/segnalations", method=RequestMethod.POST, produces="application/json")
	public HttpEntity<Segnalazione> insertSegnalation(
			@PathVariable String name,
			@RequestParam(value = "lat", required = true)Double lat,
			@RequestParam(value = "lng", required = true)Double lng,
			@RequestParam(value = "address", required = true)String indirizzo,
			@RequestParam(value = "type", required = true)String tipo){
		
		//System.out.println("type : "+ tipo);
		Date date = new Date();
		Segnalazione s = service.insertNewSegnalation(name, lat, lng, date, indirizzo, tipo);
		
		//String nickname = userService.getUsernameByMail(name.getName());
		//String id = service.insertSegnalation(nickname, lat, lng, date, indirizzo, t);
		
		//Segnalazione s = new Segnalazione(nickname, lat, lng, date, null, indirizzo, TipoSegnalazione.valueOf(tipo), 0d, 0);
		//s.setId(Integer.parseInt(id));
		//System.out.println("Segnalazione creata");
		return new ResponseEntity<Segnalazione>(s, HttpStatus.CREATED);
		
	}
	
}
