package com.javasampleapproach.security.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.javasampleapproach.security.model.Autentication;

@Repository
public interface ActivationRepository extends JpaRepository<Autentication, String>{

	@Query(value = "INSERT into activationCode(username, code)"
			+ " VALUES (?1, ?2) "
			+ "RETURNING username", nativeQuery = true)
	String insertCode(String username, String code);
	
	@Query(value = "SELECT username FROM activationCode WHERE code = ?1", nativeQuery = true)
	String getUsernameByCode(String code);
	
	@Query(value = "DELETE FROM activationCode WHERE username = ?1"
			+ " RETURNING username", nativeQuery = true)
	String deleteCode(String username);
}
