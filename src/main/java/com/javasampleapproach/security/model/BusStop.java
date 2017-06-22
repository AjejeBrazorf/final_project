package com.javasampleapproach.security.model;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.geolatte.geom.G2D;
import org.geolatte.geom.Point;

@Entity
public class BusStop{
	@Id
	private String id;
	private String name;
	private Double lat;
	@Column(name = "lng")
	private Double lng;
	
	
	@OneToMany(fetch=FetchType.LAZY, mappedBy="stop")
	private List<BusLineStop> lines = new ArrayList<BusLineStop>();
	
	public List<BusLineStop> getLines(){
		return lines;
	}
	
	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}
	
	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setLines(List<BusLineStop> lines) {
		this.lines = lines;
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

}
