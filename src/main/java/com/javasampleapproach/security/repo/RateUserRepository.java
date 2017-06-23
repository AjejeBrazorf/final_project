package com.javasampleapproach.security.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.javasampleapproach.security.model.Message;
import com.javasampleapproach.security.model.RateUser;

public interface RateUserRepository extends JpaRepository<RateUser, String>{
	@Query(value = "insert into rateUser (username, idsegnalation, rate)"
			+ " VALUES (?1, ?2, ?3)", nativeQuery = true)
	void insertUserRate(String username, String idSegnalation, int rate);

	@Query(value = "UPDATE rateUser SET rate = ?1 WHERE username = ?2 AND idsegnalation = ?3)"
			+ " VALUES (?1, ?2, ?3)", nativeQuery = true)
	void updateUserRate(int rate, String username, String idSegnalation);
	
	@Query(value = "SELECT rate FROM rateUser WHERE username = ?1 AND idsegnalation = ?2)", nativeQuery = true)
	int getUserRate(String username, String idSegnalation);
}
