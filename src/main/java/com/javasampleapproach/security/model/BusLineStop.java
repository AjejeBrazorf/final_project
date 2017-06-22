package com.javasampleapproach.security.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name="BusLineStop")
public class BusLineStop implements Serializable {
	
	private static final long serialVersionUID = -8713869938500101499L;
	@Id
	@ManyToOne
	@JoinColumn(name="lineid", nullable=false)
	private BusLine line;
	
	@Id
	@ManyToOne
	@JoinColumn(name="stopid", nullable=false)
	private BusStop stop;
	
	@Id
	private short sequenceNumber;
	
	public BusLineStop(BusLine line, BusStop stop, short sequenceNumber){
		this.line = line;
		this.stop = stop;
		this.sequenceNumber = sequenceNumber;
	}
	
	
	public BusStop getStop(){
		return stop;
	}
	
	public BusLine getLine(){
		return line;
	}
	
	public short getSequenceNumber(){
		return sequenceNumber;
	}


	public void setLine(BusLine line) {
		this.line = line;
	}


	public void setStop(BusStop stop) {
		this.stop = stop;
	}


	public void setSequenceNumber(short sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}
	
	
}



