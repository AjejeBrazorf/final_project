package com.javasampleapproach.security.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.javasampleapproach.security.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, String>{
	
	@Query(value = "SELECT * FROM dettagliouser u WHERE u.email = ?1", nativeQuery = true)
	User findByemail(String email);
	
	@Query(value = "SELECT * FROM dettagliouser u WHERE u.nickname = ?1", nativeQuery = true)
	User findByNickname(String nickaname);
	
	@Query(value = "SELECT nickname FROM dettagliouser u WHERE u.email = ?1", nativeQuery = true)
	String findnicknameByemail(String mail);
	
	@Query(value = "SELECT foto FROM dettagliouser u WHERE u.email = ?1", nativeQuery = true)
	String findImageByemail(String mail);

	@Query(value = "SELECT foto FROM dettagliouser u WHERE u.nickname = ?1", nativeQuery = true)
	String findImageByNickname(String nickname);

	@Query(value = "insert into dettagliouser(nickname, email, gender, eta, istruzione, occupazione, hasCar, annoimmatricolazione, carburante, useCarSharing, fornitoreSharing, useBike, useBikeSharing, useMezziPubblici, tipoViaggio, foto)"
			+ " VALUES (?1, ?2, ?3, ?4, ?5, ?6, ?7, ?8, ?9, ?10, ?11, ?12, ?13, ?14, ?15, ?16)"
			+ " RETURNING nickname", nativeQuery = true)
	String insertUser(String username, String email, int gender, int eta, int istruzione, int occupazione, boolean hasCar, int annoImm, int carburante, boolean useCarSharing, int fornitoreSharing, boolean useBike, boolean useBikeSharing, boolean useMezziPubblici, int tipoViaggio, String foto);

	@Query(value = "UPDATE dettagliouser "
			+ " SET gender = ?2, eta = ?3, istruzione = ?4, occupazione = ?5, hasCar = ?6,"
			+ "annoimmatricolazione = ?7, carburante = ?8, useCarSharing = ?9, fornitoreSharing = ?10,"
			+ "useBike = ?11, useBikeSharing = ?12, useMezziPubblici = ?13, tipoViaggio = ?14, foto =?15 WHERE email = ?1"
			+ " RETURNING nickname", nativeQuery = true)
	String updateUser(String mail, int gender, int eta, int istruzione, int occupazione, boolean hasCar, int annoImm, int carburante, boolean useCarSharing, int fornitoreSharing, boolean useBike, boolean useBikeSharing, boolean useMezziPubblici, int tipoViaggio, String foto);
	
	@Query(value = "UPDATE dettagliouser "
			+ " SET foto =?1 WHERE email = ?2"
			+ " RETURNING nickname", nativeQuery = true)
	String updateImageUser(String foto, String mail);
	
	
	@Query(value = "SELECT * FROM dettagliouser u LIMIT ?1 OFFSET ?2", nativeQuery = true)
	List<User> findUsersByOffset(int limit, int offset);
	
	@Query(value = "SELECT COUNT(*) FROM dettagliouser WHERE nickname = ?1", nativeQuery = true)
	int findNickname(String nickname);

}
