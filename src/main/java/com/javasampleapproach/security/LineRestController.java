package com.javasampleapproach.security;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.javasampleapproach.security.query.LineQuery;

import rest.LinesResource;
import rest.StopsResource;

@RestController
public class LineRestController {
	
	@Autowired
	private LineQuery lq;

	@RequestMapping(value="/lines", method=RequestMethod.GET, produces="application/json")
	public HttpEntity<LinesResource> getLines(){
		LinesResource lR = new LinesResource();
		
		lR.setLines(lq.getLines());
		//System.out.println("Linea db :"+lq.getLines().get(6).getDescription());
		//self link
		Link selfLink = linkTo(LineRestController.class).slash("lines").withSelfRel();
		lR.add(selfLink);
		
		return new ResponseEntity<LinesResource>(lR, HttpStatus.OK);
	}
	
	@RequestMapping(value="/{id}/stops", method=RequestMethod.GET, produces="application/json")
	public HttpEntity<StopsResource> getStops(@PathVariable String id){
		StopsResource sR = new StopsResource();
		sR.setStops(lq.getStopsForLine(id));
		//System.out.println(lq.getStopsForLine(id).size());
		
		//self link
		Link selfLink = linkTo(LineRestController.class).slash("lines").withSelfRel();
		sR.add(selfLink);
		
		return new ResponseEntity<StopsResource>(sR, HttpStatus.OK);
	}
	
	@RequestMapping(value="/{id}/lines", method=RequestMethod.GET, produces="application/json")
	public HttpEntity<List<String>> getLinesForStop(@PathVariable String id){
		List<String> stops = lq.getLinesForStop(id);
     
		return new ResponseEntity<List<String>>(stops, HttpStatus.OK);
	}
}
