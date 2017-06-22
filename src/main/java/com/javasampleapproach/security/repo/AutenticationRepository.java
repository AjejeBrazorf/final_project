package com.javasampleapproach.security.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.javasampleapproach.security.model.Autentication;

@Repository
public interface AutenticationRepository extends JpaRepository<Autentication, String>{
	
	@Query(value = "SELECT * FROM users u WHERE u.username = ?1", nativeQuery = true)
	List<Autentication> findByemail(String email);
	
	@Query(value = "INSERT into users(username, password, enabled)"
			+ " VALUES (?1, ?2, ?3) "
			+ "RETURNING username", nativeQuery = true)
	String insertCredenziali(String email, String password, boolean state);

	@Query(value = "SELECT COUNT(*) FROM users WHERE username = ?1", nativeQuery = true)
	int findMail(String email);
	
	@Query(value = "UPDATE users SET password = ?1 WHERE username = ?2 "
			+ " RETURNING username",nativeQuery = true)
	String updateCredentials(String password, String username);
	
	@Query(value = "UPDATE users SET enabled = true WHERE username = ?1"
			+ " RETURNING username",nativeQuery = true)
	String enableUser(String username);
	
	@Query(value = "SELECT password FROM users WHERE username = ?1", nativeQuery = true)
	String getPasswordByName(String username);
}
