package com.javasampleapproach.security.query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.geolatte.geom.G2D;
import org.geolatte.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.javasampleapproach.security.model.Edge;
import com.javasampleapproach.security.model.EdgeMaps;
import com.javasampleapproach.security.model.MinPath;
import com.javasampleapproach.security.repo.PathRepository;

@Service
public class MongoQuery {
	
	@Autowired
	LineQuery lq;
	@Autowired
	PathRepository pR;
	
	public List<EdgeMaps> getMinPathRoute(double lat1, double lng1, double lat2, double lng2){

		List<MinPath> mpl = new ArrayList<>();
		List<EdgeMaps> edges = new ArrayList<>();
		Map<String, Double> stopsStart = lq.getStopsNearPosition(lat1, lng1);
		Map<String, Double> stopsStart2 = lq.getStopsNearPosition(lat2, lng2);
		
		for (String s1 : stopsStart.keySet()){
			for (String s2 : stopsStart2.keySet()){
				System.out.println("From: " + s1 + " To: " + s2);
				MinPath m = getPath(s1, s2);
				if(m != null){
					System.out.println("idDest: " + m.getIdDestination() + " idSource: " + m.getIdSource());
					m.setTotalCost((int)(m.getTotalCost() + stopsStart.get(s1) + stopsStart2.get(s2)));
					mpl.add(m);
				}
			}
		}
		
		
		Collections.sort(mpl, new Comparator<MinPath>(){
		     public int compare(MinPath mp1, MinPath mp2){
		         if(mp1.getTotalCost() == mp2.getTotalCost())
		             return 0;
		         return mp1.getTotalCost() < mp2.getTotalCost() ? -1 : 1;
		     }
		});
		if(mpl.size()>0){
			MinPath mp = mpl.get(0);
			List<Double> pCoord = lq.pointFromStopid(mp.getEdges().get(0).getIdSource());
			
			for(int i=0;i<mp.getEdges().size();i++){
				Edge e = mp.getEdges().get(i);
				System.out.println("IdSource "+e.getIdSource() +" idDest: "+e.getIdDestination() + " mode : " + e.isMode());
				System.out.println("linea id"+ e.getLineId());
			}
			
			if(pCoord.get(0)!=lat1 || pCoord.get(1)!=lng1){
				EdgeMaps em = new EdgeMaps(lat1, lng1, 
							pCoord.get(0), pCoord.get(1));
				em.setMode(true);
				edges.add(em);
			}
			
			for(int i=0;i<mp.getEdges().size();i++){
				Edge e = mp.getEdges().get(i);
				List<Double> coord = lq.pointFromStopid(e.getIdDestination());
				if(pCoord != null){
					EdgeMaps em = new EdgeMaps(pCoord.get(0), pCoord.get(1), coord.get(0), coord.get(1));
					em.setIdSource(e.getIdSource());
					em.setIdDestination(e.getIdDestination());
					em.setMode(e.isMode());
					em.setNameFrom(lq.nameFromStopid(e.getIdSource()));
					em.setNameTo(lq.nameFromStopid(e.getIdDestination()));
					em.setLineId(e.getLineId());
					edges.add(em);
				}
				pCoord.clear();
				pCoord = coord;	
			}
			
			if(pCoord.get(0)!=lat2 || pCoord.get(1)!=lng2){
				EdgeMaps em = new EdgeMaps(lat2, lng2, 
							pCoord.get(0), pCoord.get(1));
				em.setMode(true);
				edges.add(em);
			}
		}
		
		return edges;
	}
	
	//minPath between source and dest
	public MinPath getPath(String idSource, String idDest){
		return pR.findPathbyIds(idSource, idDest);
	}
}
