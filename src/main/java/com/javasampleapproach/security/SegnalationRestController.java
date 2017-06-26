package com.javasampleapproach.security;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.net.URI;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.javasampleapproach.security.model.SegnalationForClient;
import com.javasampleapproach.security.model.Segnalazione;
import com.javasampleapproach.security.model.TipoSegnalazione;
import com.javasampleapproach.security.query.RateUserQuery;
import com.javasampleapproach.security.query.SegnalationQuery;
import com.javasampleapproach.security.query.UsersQuery;

import rest.ArgumentsResource;

@RestController
public class SegnalationRestController {

	@Autowired
	private SegnalationQuery service;
	@Autowired
	private UsersQuery userService;
	@Autowired
	private RateUserQuery rateService;


	@RequestMapping(value="/segnalations", method=RequestMethod.GET, produces="application/json")
	public HttpEntity<List<SegnalationForClient>> getSegnalations(
			Principal name,
			@RequestParam(value = "type", required = false)String type){

		List<Segnalazione> list;
		List<SegnalationForClient> newList = new ArrayList<>();
		SegnalationForClient sfc = new SegnalationForClient();
		int voto;
		
		if(type == null){
			list = service.getAll();
		}else{
			int intType = (TipoSegnalazione.valueOf(type)).ordinal();
			list = service.getAllforType(intType);
		}
		for(Segnalazione s:list){
			if(name.getName() != null){
				voto = rateService.isPresentUserRate(name.getName(), s.getId());
				sfc.setVoto(voto);
			}
			sfc.setSegnalazione(s);
			newList.add(sfc);
		}
			
		return new ResponseEntity<List<SegnalationForClient>>(newList, HttpStatus.OK);
	}
	
	@RequestMapping(value="/segnalations/{id}", method=RequestMethod.GET, produces="application/json")
	public HttpEntity<Segnalazione> getSegnalation(
			@PathVariable String id){

		Segnalazione s = service.getById(id);
		return new ResponseEntity<Segnalazione>(s, HttpStatus.OK);
	}

	@RequestMapping(value="/segnalations/{id}", method=RequestMethod.PUT, produces="application/json")
	public HttpEntity<?> rateSegnalation(
			@PathVariable String id,
			Principal name,
			@RequestParam(value = "action", required = true)String action,
			@RequestParam(value = "rate", required = false, defaultValue = "null")Integer rate){
		if(action.equals("rate")){
			Integer oldRate;
			if((oldRate = rateService.isPresentUserRate(name.getName(), Integer.parseInt(id)))!= null)
				service.updateRate(1, rate-oldRate, id);
			else{
				//aggiungo anche in tabelle di user-voto
				rateService.insertUserRate(name.getName(), id, rate);
				service.updateRate(0, rate, id);
			}
		}
		else if(action.equals("cancel"))
			service.updateSegnalation(new Date(), id);
		else
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@RequestMapping(value="/segnalations", method=RequestMethod.POST, produces="application/json")
	public HttpEntity<Segnalazione> insertSegnalation(
			Principal name, 
			@RequestParam(value = "lat", required = true)Double lat,
			@RequestParam(value = "lng", required = true)Double lng,
			@RequestParam(value = "address", required = true)String indirizzo,
			@RequestParam(value = "type", required = true)String tipo){

		System.out.println("type : "+ tipo);

		int t = TipoSegnalazione.valueOf(tipo).ordinal();
		String nickname = userService.getUsernameByMail(name.getName());
		Date date = new Date();
		String id = service.insertSegnalation(nickname, lat, lng, date, indirizzo, t);

		Segnalazione s = new Segnalazione(nickname, lat, lng, date, null, indirizzo, TipoSegnalazione.valueOf(tipo), 0d, 0);
		s.setId(Integer.parseInt(id));
		System.out.println("Segnalazione creata");
		return new ResponseEntity<Segnalazione>(s, HttpStatus.CREATED);

	}

}
