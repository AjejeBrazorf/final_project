package com.javasampleapproach.security.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.javasampleapproach.security.model.Message;
import com.javasampleapproach.security.model.RateUser;

public interface RateUserRepository extends JpaRepository<RateUser, String>{
	@Query(value = "INSERT into rateUser (username, idsegnalation, rate)"
			+ " VALUES (?1, ?2, ?3)"
			+ " RETURNING rate", nativeQuery = true)
	void insertUserRate(String username, int idSegnalation, int rate);

	@Query(value = "UPDATE rateUser SET rate = ?1 WHERE username = ?2 AND idsegnalation = ?3"
			+ " RETURNING rate", nativeQuery = true)
	int updateUserRate(int rate, String username, int idSegnalation);
	
	@Query(value = "SELECT rate FROM rateUser WHERE username = ?1 AND idsegnalation = ?2", nativeQuery = true)
	Integer getUserRate(String username, int idSegnalation);
}
