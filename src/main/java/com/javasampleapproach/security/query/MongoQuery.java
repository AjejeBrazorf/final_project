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

import com.javasampleapproach.security.model.BusStop;
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
				//System.out.println("From: " + s1 + " To: " + s2);
				MinPath m = getPath(s1, s2);
				if(m != null){
					//System.out.println("idDest: " + m.getIdDestination() + " idSource: " + m.getIdSource());
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
				//System.out.println("IdSource "+e.getIdSource() +" idDest: "+e.getIdDestination() + " mode : " + e.isMode());
				//System.out.println("linea id"+ e.getLineId());
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
					
					//se percorso in pullman prendo le fermate intermedie, su mongo non posso salvarle
					if(!e.isMode()){
						List<BusStop> stops = lq.getIntermediateStops(e.getLineId(), e.getIdSource(), e.getIdDestination());
						if(!stops.isEmpty()){
							//first edge for first stop in the list
							EdgeMaps em = new EdgeMaps(pCoord.get(0), pCoord.get(1), stops.get(0).getLat(), stops.get(0).getLng());
							em.setIdSource(e.getIdSource());
							em.setIdDestination(stops.get(0).getId());
							em.setMode(e.isMode());
							em.setNameFrom(lq.nameFromStopid(e.getIdSource()));
							em.setNameTo(stops.get(0).getName());
							em.setLineId(e.getLineId());
							edges.add(em);
							
							//intermediate stops in the list
							int j;
							for( j= 1; j<stops.size(); j++){
								//first edge for first stop
								EdgeMaps emap = new EdgeMaps(stops.get(j-1).getLat(), stops.get(j-1).getLng(), 
																stops.get(j).getLat(), stops.get(j).getLng());
								emap.setIdSource(stops.get(j-1).getId());
								emap.setIdDestination(stops.get(j).getId());
								emap.setMode(e.isMode());
								emap.setNameFrom(stops.get(j-1).getName());
								emap.setNameTo(stops.get(j).getName());
								emap.setLineId(e.getLineId());
								edges.add(emap);
							}
							//last edge for last stop in the list
							j--;
							EdgeMaps emLast = new EdgeMaps(stops.get(j).getLat(), stops.get(j).getLng(), coord.get(0), coord.get(1));
							emLast.setIdSource(stops.get(0).getId());
							emLast.setIdDestination(e.getIdDestination());
							emLast.setMode(e.isMode());
							emLast.setNameFrom(stops.get(j).getName());
							emLast.setNameTo(lq.nameFromStopid(e.getIdDestination()));
							emLast.setLineId(e.getLineId());
							edges.add(emLast);
						}else{
							EdgeMaps em = new EdgeMaps(pCoord.get(0), pCoord.get(1), coord.get(0), coord.get(1));
							em.setIdSource(e.getIdSource());
							em.setIdDestination(e.getIdDestination());
							em.setMode(e.isMode());
							em.setNameFrom(lq.nameFromStopid(e.getIdSource()));
							em.setNameTo(lq.nameFromStopid(e.getIdDestination()));
							em.setLineId(e.getLineId());
							edges.add(em);
						}
					}else{					
						EdgeMaps em = new EdgeMaps(pCoord.get(0), pCoord.get(1), coord.get(0), coord.get(1));
						em.setIdSource(e.getIdSource());
						em.setIdDestination(e.getIdDestination());
						em.setMode(e.isMode());
						em.setNameFrom(lq.nameFromStopid(e.getIdSource()));
						em.setNameTo(lq.nameFromStopid(e.getIdDestination()));
						em.setLineId(e.getLineId());
						edges.add(em);
					}
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
