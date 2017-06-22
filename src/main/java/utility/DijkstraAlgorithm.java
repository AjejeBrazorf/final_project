package utility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.javasampleapproach.security.model.Edge;
import com.javasampleapproach.security.model.Graph;
import com.javasampleapproach.security.model.MinPath;
import com.javasampleapproach.security.model.Vertex;


public class DijkstraAlgorithm {

	private final List<Vertex> nodes;
	private final List<Edge> edges;
	private Set<Vertex> settledNodes;
	private Set<Vertex> unSettledNodes;
	private Map<Vertex, Edge> predecessors;
	private Map<Vertex, Integer> distance;

	public DijkstraAlgorithm(Graph graph) {
		// create a copy of the array so that we can operate on this array
		this.nodes = new ArrayList<Vertex>(graph.getVertexes());
		this.edges = new ArrayList<Edge>(graph.getEdges());
	}

	public void execute(Vertex source) {
		settledNodes = new HashSet<Vertex>();
		unSettledNodes = new HashSet<Vertex>();
		distance = new HashMap<Vertex, Integer>();
		predecessors = new HashMap<Vertex, Edge>();
		distance.put(source, 0);
		unSettledNodes.add(source);
		while (unSettledNodes.size() > 0) {
			Vertex node = getMinimum(unSettledNodes);
			settledNodes.add(node);
			unSettledNodes.remove(node);
			findMinimalDistances(node);
		}
	}

	private void findMinimalDistances(Vertex node) {
		List<Vertex> adjacentNodes = getNeighbors(node);
		for (Vertex target : adjacentNodes) {
			Edge new_edge = new Edge();
			new_edge = getDistance(node, target);
			if (getShortestDistance(target) > getShortestDistance(node) + new_edge.getCost()) {
				distance.put(target, getShortestDistance(node)+ new_edge.getCost());
				predecessors.put(target, new_edge);
				unSettledNodes.add(target);
			}
		}

	}

	private Edge getDistance(Vertex node, Vertex target) {
		for (Edge edge : edges) {
			if (edge.getIdSource().equals(node.getId()) && edge.getIdDestination().equals(target.getId())) {
				return edge;
			}
		}
		throw new RuntimeException("Should not happen");
	}

	private List<Vertex> getNeighbors(Vertex node) {
		List<Vertex> neighbors = new ArrayList<Vertex>();
		for (Edge edge : edges) {
			Vertex v = getVertexbyId(edge.getIdDestination());
			if (edge.getIdSource().equals(node.getId()) && !isSettled(v)) {
				neighbors.add(v);
			}
		}
		return neighbors;
	}

	private Vertex getMinimum(Set<Vertex> vertexes) {
		Vertex minimum = null;
		for (Vertex vertex : vertexes) {
			if (minimum == null) {
				minimum = vertex;
			} else {
				if (getShortestDistance(vertex) < getShortestDistance(minimum)) {
					minimum = vertex;
				}
			}
		}
		return minimum;
	}

	private boolean isSettled(Vertex vertex) {
		return settledNodes.contains(vertex);
	}

	private int getShortestDistance(Vertex destination) {
		Integer d = distance.get(destination);
		if (d == null) {
			return Integer.MAX_VALUE;
		} else {
			return d;
		}
	}

	/*
	 * This method returns the path from the source to the selected target and
	 * NULL if no path exists
	 */
	public MinPath getPath(Vertex target) {
		LinkedList<Edge> path = new LinkedList<Edge>();
		
		// check if a path exists
		System.out.println(predecessors.size());
		if (predecessors.get(target) == null) {
			return null;
		}
		//path.add(step);
		Vertex step = target;
		while (predecessors.get(step) != null) {
			Edge e = predecessors.get(step);
			step = new Vertex(e.getIdSource(), "Nome di mappa da fare");
			path.add(e);
		}
		// Put it into the correct order
		Collections.reverse(path);
		
		MinPath minPath = getMinPath(path);
		
		return minPath;
	}

	//ho aggiunto questa funzione per trovare il Vertex dato l'id che abbiamo nel Edge

	private Vertex getVertexbyId(String id){
		for (Vertex v : nodes){
			if(v.getId().equals(id))
				return v;
		}
		return null;
	}

	//funzione per trasformare da List di vertex a MinPath
	public MinPath getMinPath(List<Edge> list){

		MinPath minPath = new MinPath();
		int size = list.size();
		int i = 0;
		int totalCost = 0;

		minPath.setIdSource(list.get(0).getIdSource()); 
		minPath.setIdDestination(list.get(size-1).getIdDestination());

		while(i!=size-1){
			totalCost = list.get(i).getCost();
			i++;
		}

		minPath.setEdges(list);
		minPath.setTotalCost(totalCost);

		return minPath;
	}
	
}
		
		

