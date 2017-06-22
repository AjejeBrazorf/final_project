package com.javasampleapproach.security.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.javasampleapproach.security.model.MinPath;

public interface PathRepository extends MongoRepository<MinPath, String>{
	
	@Query("{$and:[{idSource:{$eq:'?0'}} , {idDestination:{$eq:'?1'}}]}")
	MinPath findPathbyIds(String idSource, String idDest);

}
