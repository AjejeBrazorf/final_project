package com.javasampleapproach.security.model;

import org.geolatte.geom.G2D;
import org.geolatte.geom.Point;

public class EdgeMaps extends Edge{
	private Double lat;
	private Double lng;
	private Double lat2;
	private Double lng2;
	private String nameFrom;
	private String nameTo;
	
	public EdgeMaps(Point<G2D> p1, Point<G2D> p2){
		this(p1.getPosition().getLat(),
				p1.getPosition().getLon(),
				p2.getPosition().getLat(),
				p2.getPosition().getLon());
	}

	public EdgeMaps(Double lat, Double lng, Double lat2, Double lng2){
		super();
		this.lat = lat;
		this.lng = lng;
		this.lat2 = lat2;
		this.lng2 = lng2;
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
	
	public Double getLat2() {
		return lat2;
	}
	public void setLat2(Double lat) {
		this.lat2 = lat;
	}
	public Double getLng2() {
		return lng2;
	}
	public void setLng2(Double lng) {
		this.lng2 = lng;
	}
	
	public String getNameFrom() {
		return nameFrom;
	}

	public void setNameFrom(String nameFrom) {
		this.nameFrom = nameFrom;
	}

	public String getNameTo() {
		return nameTo;
	}

	public void setNameTo(String nameTo) {
		this.nameTo = nameTo;
	}
	
	
}
