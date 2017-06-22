package com.javasampleapproach.security.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.springframework.hateoas.ResourceSupport;

@Entity
public class BusLine extends ResourceSupport implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3397045444330283508L;
	
	@Id
	private String line;
	private String description;
	@OneToMany(fetch=FetchType.LAZY, mappedBy="line")
	private List<BusLineStop> stops = new ArrayList<BusLineStop>();
	
	public List<BusLineStop> getStops(){
		return stops;
	}
	
	public String getName() {
		return line;
	}

	public String getDescription() {
		return description;
	}

	public void setLine(String name) {
		this.line = name;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public void setStops(List<BusLineStop> stops) {
		this.stops = stops;
	}

}
