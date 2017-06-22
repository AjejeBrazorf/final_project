package com.javasampleapproach.security.model;

import java.util.ArrayList;
import java.util.List;

public class StopForClient {
	
	private String id;
	private String name;
	private Double lat;
	private Double lng;
	private List<String> lines;
	
	public StopForClient(){
		lines = new ArrayList<>();
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public Double getLng() {
		return lng;
	}

	public void setLng(Double lng) {
		this.lng = lng;
	}

	public List<String> getLines() {
		return lines;
	}

	public void setLines(List<String> lines) {
		this.lines = lines;
	}

	

}
