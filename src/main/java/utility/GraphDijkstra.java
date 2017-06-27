package utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.TreeSet;

import org.apache.commons.collections4.keyvalue.MultiKey;
import org.apache.commons.collections4.map.MultiKeyMap;
import org.bson.Document;

import com.javasampleapproach.security.model.Edge;

public class GraphDijkstra {
	   private final Map<String, Node> graph; // mapping of vertex names to Vertex objects, built from a set of Edges
	   private static MultiKeyMap<String,Edge> mkm = new MultiKeyMap<String,Edge>();
	   
	   public static List<Document> minPath = new ArrayList<Document>();
	   public static int totCost = 0;
	   
	   
	   public int getCost(){ return totCost;}
	   public List<Document> getPath(){ return minPath;}
	   public void clearCost(){totCost = 0;}
	   public void clearMinPath(){minPath.clear();}
	   
	   
	   
	   public static String sourcePoint = null;
	   
	 
	   /** One vertex of the graph, complete with mappings to neighbouring vertices */
	  public static class Node implements Comparable<Node>{
		public final String name;
		public int dist = Integer.MAX_VALUE; // MAX_VALUE assumed to be infinity
		public Node previous = null;
		public final Map<Node, Integer> neighbours = new HashMap<Node, Integer>();
	 
		public Node(String name)
		{
			this.name = name;
		}
	 
		private void printPath()
		{
			if (this == this.previous)
			{
				//System.out.printf("%s", this.name);
				sourcePoint = this.name;
			}
			else if (this.previous == null)
			{
				System.out.printf("%s(unreached)", this.name);
				
			}
			else
			{
				this.previous.printPath();
				//System.out.print(" -> "+ this.name +"("+this.dist+")");
				totCost = this.dist;
				//minPath.add(this.name);
				
					
				Edge found = mkm.get(sourcePoint, this.name);
				Document doc = new Document("idSource",found.getIdSource())
							       .append("idDestination", found.getIdDestination())
							       .append("mode", found.isMode())
							       .append("cost", found.getCost())
							       .append("lineId", found.getLineId());
				minPath.add(doc);
				sourcePoint = this.name;
					
				
				
			}
		}
	 
		public int compareTo(Node other)
		{
			if (dist == other.dist)
				return name.compareTo(other.name);
	 
			return Integer.compare(dist, other.dist);
		}
	 
		@Override public String toString()
		{
			return "(" + name + ", " + dist + ")";
		}
	}
	 
	   /** Builds a graph from a set of edges */
	   public GraphDijkstra(MultiKeyMap<String,Edge> edges) {
	      graph = new HashMap<String, Node>(edges.size());
	      mkm.putAll(edges);
	      //one pass to find all vertices
	      for (Map.Entry<MultiKey<? extends String>, Edge> e : edges.entrySet()) {
	         if (!graph.containsKey(e.getValue().getIdSource())) graph.put(e.getValue().getIdSource(), new Node(e.getValue().getIdSource()));
	         if (!graph.containsKey(e.getValue().getIdDestination())) graph.put(e.getValue().getIdDestination(), new Node(e.getValue().getIdDestination()));
	      }
	 
	      //another pass to set neighbouring vertices
	      for (Map.Entry<MultiKey<? extends String>, Edge> e : edges.entrySet()) {
	         graph.get(e.getValue().getIdSource()).neighbours.put(graph.get(e.getValue().getIdDestination()), e.getValue().getCost());
	         //graph.get(e.getValue().getIdDestination()).neighbours.put(graph.get(e.getValue().getIdSource()), e.getValue().getCost()); // also do this for an undirected graph
	      }
	   }
	 
	   /** Runs dijkstra using a specified source vertex */ 
	   public void dijkstra(String startName) {
	      if (!graph.containsKey(startName)) {
	         System.err.printf("Graph doesn't contain start vertex \"%s\"\n", startName);
	         return;
	      }
	      
	      
	      
	      final Node source = graph.get(startName);
	      NavigableSet<Node> q = new TreeSet<Node>();
	 
	      // set-up vertices
	      for (Node v : graph.values()) {
	         v.previous = v == source ? source : null;
	         v.dist = v == source ? 0 : Integer.MAX_VALUE;
	         q.add(v);
	      }
	 
	      dijkstra(q);
	   }
	 
	   /** Implementation of dijkstra's algorithm using a binary heap. */
	   private void dijkstra(final NavigableSet<Node> q) {      
	      Node u, v;
	      while (!q.isEmpty()) {
	 
	         u = q.pollFirst(); // vertex with shortest distance (first iteration will return source)
	         if (u.dist == Integer.MAX_VALUE) break; // we can ignore u (and any other remaining vertices) since they are unreachable
	 
	         //look at distances to each neighbour
	         for (Map.Entry<Node, Integer> a : u.neighbours.entrySet()) {
	            v = a.getKey(); //the neighbour in this iteration
	 
	            final int alternateDist = u.dist + a.getValue();
	            if (alternateDist < v.dist) { // shorter path to neighbour found
	               q.remove(v);
	               v.dist = alternateDist;
	               v.previous = u;
	               q.add(v);
	            } 
	         }
	      }
	   }
	 
	   /** Prints a path from the source to the specified vertex */
	   public void printPath(String endName) {
	      if (!graph.containsKey(endName)) {
	         System.err.printf("Graph doesn't contain end vertex \"%s\"\n", endName);
	         return;
	      }
	 
	      graph.get(endName).printPath();
	   }
	   /** Prints the path from the source to every vertex (output order is not guaranteed) */
	   public List<Document> printAllPaths(String src) {
		   
		  List<Document> minPathForSrc = new ArrayList<Document>();
		   
	      for (Node v : graph.values()) {
	         
	    	 
	    	 v.printPath();
	    	 
	    	 List<Document> mP = new ArrayList<Document>(minPath);
	    	 
	    	 Document minPathDoc = new Document("idSource",src)
			           .append("idDestination", sourcePoint)
			           .append("totalCost", totCost)
			           .append("edges", mP);
			  
	    	 
	    	 minPathForSrc.add(minPathDoc);
	    	 totCost = 0;
	    	 minPath.clear();
	    	 
	    	 
	    	 
	    	 //System.out.println();
	         /*System.out.println("Cost " + totCost );
	         for(String s : minPath){
	       	  System.out.println(s);
	         }
	         System.out.println();*/
	      }
	      
	      return minPathForSrc;
	      
	   }
	}