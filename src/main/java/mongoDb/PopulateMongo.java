package mongoDb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.map.MultiKeyMap;
import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import utility.GraphDijkstra;

import com.javasampleapproach.security.model.BusStop;
import com.javasampleapproach.security.model.Edge;
import com.javasampleapproach.security.query.DijkstraQuery;

//db.paths_list.createIndex( { idSource: 1, idDestination: 1} )
//db.paths_list.find({$and:[{idSource:{$eq:'100'}} , {idDestination:{$eq:'10'}}]})

public class PopulateMongo {
	private static List<String> nodes ;
	private static MultiKeyMap<String,Edge> edges;
    private static final String ipAddress = "192.168.99.100";
    
	public static void main(String[] args) {
		String myUserName = "user_ai";
		String myPassword = "passAi2017";
		MongoClient mongo = null;
		DijkstraQuery dq;
		
		try {
			
			nodes = new ArrayList<String>();
            edges = new MultiKeyMap<String,Edge>();
            dq = new DijkstraQuery();
            Iterator<BusStop> it = dq.getStops().iterator();
            /*while (it.hasNext()) {
            	BusStop stop = it.next();
                nodes.add(stop.getId());
                
                //find neighbours
                //first on foot
                for(Edge e :dq.getNeighboursWalkForStop(stop.getId())){
                	edges.put(e.getIdSource(), e.getIdDestination(), e);
                }
                //edges.addAll(dq.getNeighboursWalkForStop(stop.getId()));
                
                //then by bus 
                for(Edge e :dq.getNeighboursForStop(stop.getId())){
                	edges.put(e.getIdSource(), e.getIdDestination(), e);
                }
                //edges.addAll(dq.getNeighboursForStop(stop.getId()));
                System.out.println("Added edges for node "+stop.getName());
            }*/
            
            while (it.hasNext()) {
				BusStop stop = it.next();
				nodes.add(stop.getId());
			}

			//then by bus 
			for(Edge e :dq.getNeighboursForLines()){
				edges.put(e.getIdSource(), e.getIdDestination(), e);
			}
            
			mongo = new MongoClient(ipAddress, 27017);
			
			MongoDatabase db = mongo.getDatabase("trasporti");
			System.out.println("Connect to database successfully");

			/*List<ServerAddress> seeds = new ArrayList<ServerAddress>();
			seeds.add( new ServerAddress( "192.168.99.100", 27017));
			List<MongoCredential> credentials = new ArrayList<MongoCredential>();
			credentials.add(
					MongoCredential.createMongoCRCredential(
							myUserName,
							"data",
							myPassword.toCharArray()
							)
					);
			MongoClient mongo2 = new MongoClient( seeds, credentials );*/
			
			MongoCollection<Document> collection = db.getCollection("paths_list");
			
			GraphDijkstra graph = new GraphDijkstra(edges);
			for(String id:nodes){
				System.out.println("Calculating dijkstra for node "+ id);
				graph.dijkstra(id);
				List<Document> minPaths = graph.printAllPaths(id);
				collection.insertMany(minPaths);
			}
			
			Map<String, Integer> index = new HashMap<>();
			index.put("idSource", 1);
			index.put("idDestination", 1);
			
			collection.createIndex(new BasicDBObject(index));
			
			System.out.println("Documents inserted successfully");
		    } catch (MongoException e) {
		    	e.printStackTrace();
		    } catch (Exception e) {
		    	e.printStackTrace();
		    }finally{
		    	mongo.close();   
		    }
			return;
	

	}
}
