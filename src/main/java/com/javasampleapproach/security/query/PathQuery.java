package com.javasampleapproach.security.query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.javasampleapproach.security.model.MinPath;
import com.javasampleapproach.security.repo.PathRepository;

@Service
public class PathQuery {
	@Autowired
	PathRepository pR;
	

	//minPath between source and dest
	public MinPath getPath(String idSource, String idDest){
		return pR.findPathbyIds(idSource, idDest);
	}
}
