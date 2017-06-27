package com.javasampleapproach.security.query;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.javasampleapproach.security.model.BusStop;
import com.javasampleapproach.security.model.Edge;
import utility.HibernateUtil;

public class DijkstraQuery {
	
	private SessionFactory sf = HibernateUtil.getSessionFactory();
	private Session session;
	private final double costantePiedi = 1.5;
	private final double costanteMetro = 0.5;
	private final double radius = 250d;
	private Map<String, List<String>> map = new HashMap<String, List<String>>();
	
	public DijkstraQuery() {
		session = sf.getCurrentSession();
		
		JSONParser parser = new JSONParser();

		Object obj;
		try {
			obj = parser.parse(new FileReader( "linee.json"));
			JSONObject jsonObject = (JSONObject) obj;

			JSONArray lines = (JSONArray) jsonObject.get("lines");

			Iterator<JSONObject> iterator = lines.iterator();
			while (iterator.hasNext()) {
				JSONObject line = iterator.next();
				String lineName = (String)line.get("line");
				JSONArray stopsForLIne = (JSONArray)line.get("stops");
				List<String> stopsId = new ArrayList<String>();
				for(int i=0; i<stopsForLIne.size(); i++){					
					String stop = (String)stopsForLIne.get(i);
					stopsId.add(stop);
				}
				map.put(lineName, stopsId);
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	public List<BusStop> getStops(){
		Transaction tx = session.beginTransaction();
		
		String query = "from BusStop";
		Query q = session.createQuery(query);
		List<BusStop> lista = q.list();
		tx.commit();
		//session.close();
		
		return lista;
	}
	
	public List<Edge> getNeighboursWalkForStop(String stopId){
		session = sf.getCurrentSession();
		Transaction tx = session.beginTransaction();

		String query = "select b.id, distance(ST_GeographyFromText('SRID=4326;POINT('||a.lng||' '||a.lat||')'), ST_GeographyFromText('SRID=4326;POINT('||b.lng||' '||b.lat||')') ) "
						+ "from BusStop a, BusStop b "
						+ "where a.id=:n and dwithin(ST_GeographyFromText('SRID=4326;POINT('||a.lng||' '||a.lat||')') , ST_GeographyFromText('SRID=4326;POINT('||b.lng||' '||b.lat||')') , :radius) = true";
		Query q = session.createQuery(query);
		q.setParameter("n", stopId);
		q.setParameter("radius", radius);

		List<Object[]> listResult = q.list();
		List<Edge> lista = new ArrayList<Edge>();

		for (Object[] row : listResult) {
			Edge edge = new Edge();
			edge.setIdSource(stopId);
			edge.setIdDestination((String)row[0]);
			edge.setMode(true);
			edge.setCost((int)((Double)row[1] * costantePiedi));
			lista.add(edge);
		}
		tx.commit();
		//ssession.close();

		return lista;
	}
	
	public Double getDistance(String stop1, String stop2){
		session = sf.getCurrentSession();
		Transaction tx = session.beginTransaction();
		

		String query = "select distance(ST_GeographyFromText('SRID=4326;POINT('||a.lng||' '||a.lat||')'), ST_GeographyFromText('SRID=4326;POINT('||b.lng||' '||b.lat||')') ) "
						+ "from BusStop a, BusStop b "
						+ "where a.id=:n1 and b.id=:n2";
		Query q = session.createQuery(query);
		q.setParameter("n1", stop1);
		q.setParameter("n2", stop2);

		List<Object> listResult = q.list();
		if(listResult.size()!=1) {
			tx.commit();
			session.close();
			return 0d;
		}
		
		tx.commit();
		//session.close();
		return (Double)listResult.get(0);

	}

	public List<Edge> getNeighboursForStop(String stopId){
		/*Transaction tx = session.beginTransaction();
		
		String query = "select b.id, ST_Distance(a.location, b.location) "
				+ "from BusStop a, BusStop b "
				+ "where a.id=:n and b.id in ( select bl.id "
				+ "from BusLineStop bstop inner join bs.line as bl where bl.line in "
				+ "(select bline.line "
				+ "from BusLineStop bs inner join bs.line as bline where bs.stop.id=:n ))";
				
				*
		Query q = session.createQuery(query);
		q.setParameter("n", stopId);

		List<Object[]> listResult = q.list();
		List<Edge> lista = new ArrayList<Edge>();

		for (Object[] row : listResult) {
			Edge edge = new Edge();
			edge.setIdSource(stopId);
			edge.setIdDestination((String)row[1]);
			edge.setMode(false);
			edge.setCost((Integer)row[2]);
			lista.add(edge);
		}
		tx.commit();
		session.close();*/
		
		//data la fermata ci ritorna le linee passanti per essa
		/*String query = "select bstop.sequenceNumber, bs.line"
				+ "from BusLineStop bstop where bstop.line.line in ("
				+ "select distinct b.line from BusLineStop bs inner join bs.line as b where bs.stop.id=:n )";*/
				
		List<Edge> lista = new ArrayList<Edge>();
		
		Iterator<Entry<String, List<String>>> it = map.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String,List<String>> pair = (Map.Entry<String, List<String>>)it.next();
			List<String> value = pair.getValue();
			for(int i=0; i<value.size(); i++){
				if(value.get(i).equals(stopId)){
					if(i!=(value.size()-1)){
						String nextStop = value.get(i+1);
						Edge edge = new Edge();
						edge.setIdSource(stopId);
						edge.setIdDestination(nextStop);
						Double distance = 0d;
						if(!nextStop.equals(stopId))
							distance= getDistance(stopId, nextStop);
						//if line METRO set a lower cost
						if(pair.getKey().equals("METRO"))
							edge.setCost((int)(distance.intValue()*costanteMetro));
						else
							edge.setCost(distance.intValue());
						edge.setMode(false);
						edge.setLineId(pair.getKey());
						lista.add(edge);
					}
				}  
			}

        }
		
		
		return lista;
	}
	
	
	public List<Edge> getNeighboursForLines(){
		List<Edge> lista = new ArrayList<Edge>();
		Iterator<Entry<String, List<String>>> it = map.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String,List<String>> pair = (Map.Entry<String, List<String>>)it.next();
			String linea = pair.getKey();
			List<String> value = pair.getValue();
			for(int i=0; i<value.size()-1; i++){
				
				String startStop = value.get(i);
				
				//get Stop reachable by walk
				lista.addAll(getNeighboursWalkForStop(startStop));
				
				for(int j=(i+1); j<value.size(); j++){
					String nextStop = value.get(j);
					Edge edge = new Edge();
					edge.setIdSource(startStop);
					edge.setIdDestination(nextStop);
					Double distance = 0d;
					if(!nextStop.equals(startStop))
						distance= getDistance(startStop, nextStop);
					//if line METRO set a lower cost
					if(linea.equals("METRO"))
						edge.setCost((int)(distance.intValue()*costanteMetro));
					else
						edge.setCost(distance.intValue()/5);
					edge.setMode(false);
					edge.setLineId(linea);
					lista.add(edge);
					
					if(linea.equals("2") && startStop.equals("101"))
						System.out.println("From : "+startStop+" To: "+nextStop+" Cost :"+distance.intValue()/5);
				}
				System.out.println("Added edges for line "+linea);
				if(linea.equals("2") && startStop.equals("101"))
					return lista;
			}			
		}
		return lista;
	}

}
