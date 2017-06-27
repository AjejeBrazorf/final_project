package com.javasampleapproach.security.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.javasampleapproach.security.model.Segnalazione;

@Repository
public interface SegnalationRepository extends JpaRepository<Segnalazione, Integer>{

	@Query(value = "SELECT * FROM segnalazioni WHERE dataFine >= now()", nativeQuery = true)
	List<Segnalazione> findAll();
	
	@Query(value = "SELECT * FROM segnalazioni WHERE tipo = ?1 AND dataFine >= now()", nativeQuery = true)
	List<Segnalazione> findByType(int tipo);
	
	@Query(value = "SELECT * FROM segnalazioni WHERE id = ?1", nativeQuery = true)
	Segnalazione findById(int id);
	
	@Query(value = "INSERT into segnalazioni(nickname, lat, lng, tipo, rate, count, dataInizio, dataFine, indirizzo)"
			+ " VALUES (?1, ?2, ?3, ?4, 0, 0, ?5, now() + interval '5 minutes', ?6) "
			+ "RETURNING id", nativeQuery = true)
	String insertSegnalazioni(String nickname, double lat, double lng, int tipo, Date dataInizio, String indirizzo);

	@Query(value = "UPDATE segnalazioni SET dataFine = ?1 WHERE id = ?2"
			+ " RETURNING id", nativeQuery = true)
	String updateSegnalazione(Date DataFine, int id);
	
	@Query(value = "UPDATE segnalazioni SET count = ?1, rate = ?2 WHERE id = ?3"
			+ " RETURNING rate", nativeQuery = true)
	Double updateRate(int mode, double rate, int id);
	
}
