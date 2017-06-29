package com.javasampleapproach.security.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.javasampleapproach.security.model.BusLine;

public interface LinesRepository extends JpaRepository<BusLine, String>{

	@Query(value = "SELECT * FROM BusLine", nativeQuery = true)
	List<BusLine> getLines();
	
	@Query(value = "SELECT b.id, b.name, b.lat, b.lng, bs.sequenceNumber"
				+ " FROM BusLineStop bs, BusStop b"
				+ " WHERE bs.lineId = ?1 AND b.id = bs.stopId", nativeQuery = true)
	List<Object[]> getStops(String line);

	@Query(value = "SELECT line"
			+ " FROM BusLine"
			+ " WHERE line IN (SELECT lineId"
			+ "					FROM BusLineStop"
			+ "					WHERE stopId = ?1)", nativeQuery = true)
	List<String> getLinesForStop(String stopId);
	
	@Query(value = "SELECT id, ST_Distance(ST_GeographyFromText('SRID=4326;POINT('||b.lng||' '|| b.lat||')'), ST_GeographyFromText('SRID=4326;POINT('||?1||' '|| ?2 ||')'))"
			+ " FROM BusStop b"
			+ " WHERE ST_DWithin(ST_GeographyFromText('SRID=4326;POINT('||b.lng||' '|| b.lat||')'), ST_GeographyFromText('SRID=4326;POINT('||?1||' '||?2||')'), 250) = true" , nativeQuery = true)
	List<Object[]> getStopsNearPosition(double lng, double lat);
	
	@Query(value = "SELECT lat, lng"
			+ " FROM BusStop b"
			+ " WHERE id = ?1", nativeQuery = true)
	List<Object[]> getStopById(String stopId);
	
	@Query(value = "SELECT name"
			+ " FROM BusStop b"
			+ " WHERE id = ?1", nativeQuery = true)
	String getStopNameById(String stopId);
	
	@Query(value = "SELECT id, name, lat, lng"
			+ " FROM BusStop b"
			+ " WHERE id = ?1", nativeQuery = true)
	List<Object[]> getBusStopById(String stopId);
	
	@Query(value = "SELECT stopid"
			+ " FROM BusLineStop"
			+ " WHERE lineid = ?1 and sequencenumber > ?2 and sequencenumber < ?3", nativeQuery = true)
	List<String> getIntermediateStops(String lineId, int stopFrom, int stopTo);
	
	@Query(value = "SELECT sequencenumber"
			+ " FROM BusLineStop"
			+ " WHERE lineid = ?1 and stopId = ?2 ", nativeQuery = true)
	List<Short> getSequenceNumber(String lineId, String stopId);
	
	
}


