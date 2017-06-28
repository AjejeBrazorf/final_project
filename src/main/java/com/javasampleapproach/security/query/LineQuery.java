package com.javasampleapproach.security.query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.geolatte.geom.G2D;
import org.geolatte.geom.Point;
import org.geolatte.geom.crs.CoordinateReferenceSystems;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.javasampleapproach.security.model.BusLine;
import com.javasampleapproach.security.model.MinPath;
import com.javasampleapproach.security.model.StopForClient;
import com.javasampleapproach.security.repo.LinesRepository;
import com.javasampleapproach.security.repo.PathRepository;


@Service
public class LineQuery {
	
	private final double radius = 250d;
	@Autowired
	LinesRepository lR;
	
	//all Lines
	public List<BusLine> getLines(){
		List<BusLine> l = new ArrayList<>();
		
		List<BusLine> l2 = lR.getLines();
		for (BusLine b : l2){
			BusLine nuova = new BusLine();
			nuova.setLine(b.getName());
			nuova.setDescription(b.getDescription());
			l.add(nuova);
		}
		return l;
	}
	
	//all Stops of one line
	public List<StopForClient> getStopsForLine(String line){
		List<StopForClient> list = new ArrayList<>();
		
		for (Object[] row : lR.getStops(line)) {
			StopForClient s = new StopForClient();
			s.setId((String)row[0]);
			s.setName((String)row[1]);
			s.setLat((double) row[2]);
			s.setLng((double) row[3]);
			
			s.setLines(lR.getLinesForStop(s.getId()));
			
			list.add(s);
		}
		
		return list;
	}
	
	//all Lines passing for stop
	public List<String> getLinesForStop(String stopId){
		return lR.getLinesForStop(stopId);
	}
	
	//all stops near position
	public Map<String, Double> getStopsNearPosition(double lat, double lng){
		Map<String, Double> map = new TreeMap<>();
		List<Object[]> listResult = lR.getStopsNearPosition(lng, lat);
		 
		for (Object[] row : listResult)
			map.put((String)row[0], (Double)row[1]);
		
		return map;
	}
	
	//lat,lng from id
	public List<Double> pointFromStopid(String id){
		List<Double> coordinate = new ArrayList<>();
		List<Object[]> queryResult = lR.getStopById(id);
		
		coordinate.add((Double)queryResult.get(0)[0]);
		coordinate.add((Double)queryResult.get(0)[1]);
		
		return coordinate;
	}
	

	//lat,lng from id
	public String nameFromStopid(String id){
		
		return lR.getStopNameById(id);
	}
	
}
