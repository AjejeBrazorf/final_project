package com.javasampleapproach.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.javasampleapproach.security.model.EdgeMaps;
import com.javasampleapproach.security.query.MongoQuery;

@RestController
public class PathController {
	
	@Autowired 
	MongoQuery mq;
	
	@RequestMapping(value = "/findPath", method=RequestMethod.GET, produces="application/json")
	public HttpEntity<List<EdgeMaps>> findPath(
			@RequestParam(value="lat1", required=true) double lat1,			
			@RequestParam(value="lng1", required=true) double lng1,
			@RequestParam(value="lat2", required=true) double lat2,
			@RequestParam(value="lng2", required=true) double lng2
			) {
		List<EdgeMaps> list = mq.getMinPathRoute(lat1, lng1, lat2, lng2);
		return new ResponseEntity<List<EdgeMaps>>(list, HttpStatus.OK);
	}
}
