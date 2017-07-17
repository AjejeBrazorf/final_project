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
import com.javasampleapproach.security.model.BusStop;
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

	//all stops that are between the stop1 and stop2, in the same line
	public List<BusStop> getIntermediateStops(String lineId, String stopFrom, String stopTo){
		List<BusStop> list = new ArrayList<>();
		int seqNumberFrom = 0;
		int seqNumberTo = 0;

		List<Short> seqsFrom = lR.getSequenceNumber(lineId, stopFrom);
		List<Short> seqsTo = lR.getSequenceNumber(lineId, stopTo);

		//divido per casi, perchè posso avere 2 valori di ritorno per ogni stop
		if( seqsFrom.size() == 1 && seqsTo.size() == 1){
			seqNumberFrom = seqsFrom.get(0);
			seqNumberTo = seqsTo.get(0);
		}else if(seqsFrom.size() == 1 && seqsTo.size() == 2){
			seqNumberFrom = seqsFrom.get(0);
			for(int j = seqsTo.size()-1; j >= 0 ; j--){
				if( seqNumberFrom < seqsTo.get(j)){
					seqNumberTo = seqsTo.get(j);
				}
			}
		}else if(seqsFrom.size() == 2 && seqsTo.size() == 1){
			seqNumberTo = seqsTo.get(0);
			for(int i = seqsFrom.size()-1; i >= 0 ; i--){
				if( seqsFrom.get(i) < seqNumberTo){
					seqNumberFrom = seqsFrom.get(i);
				}

			}		
		}else if(seqsFrom.size() == 2 && seqsTo.size() == 2){
			if( seqsFrom.get(1) < seqsTo.get(1)){
				seqNumberFrom = seqsFrom.get(1);
				seqNumberTo = seqsTo.get(1);
			}else{
				seqNumberFrom = seqsFrom.get(0);
				seqNumberTo = seqsTo.get(0);
			}
		}else{
			return list;
		}
		/*for(int i = seqsFrom.size(); i > 0 ; i--){
			for(int j = seqsTo.size(); j > 0 ; j--){
				if( seqsFrom.get(i) < seqsTo.get(i)){
					seqNumberFrom = seqsFrom.get(i);
					seqNumberTo = seqsTo.get(i);
				}
			}

		}*/

		List<String> stops = lR.getIntermediateStops(lineId, seqNumberFrom, seqNumberTo);
		for(String s:stops){
			Object[] row = lR.getBusStopById(s).get(0);
			BusStop bs = new BusStop();
			bs.setId((String)row[0]);
			bs.setName((String)row[1]);
			bs.setLat((double) row[2]);
			bs.setLng((double) row[3]);
			list.add(bs);
		}
		return list;
	}

	//lat,lng from id
	public List<Double> pointFromStopid(String id){
		List<Double> coordinate = new ArrayList<>();
		List<Object[]> queryResult = lR.getStopById(id);

		coordinate.add((Double)queryResult.get(0)[0]);
		coordinate.add((Double)queryResult.get(0)[1]);

		return coordinate;
	}


	//name from id
	public String nameFromStopid(String id){
		return lR.getStopNameById(id);
	}



}
