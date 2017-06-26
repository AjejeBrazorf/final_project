package com.javasampleapproach.security.model;

public class SegnalationForClient {

	private Segnalazione segnalazione;
	private int voto;
	
	
	public SegnalationForClient(){
		
	}
	
	public SegnalationForClient(Segnalazione segnalazione, int voto){
		this.segnalazione = segnalazione;
		this.voto = voto;
	}
	
	public Segnalazione getSegnalazione() {
		return segnalazione;
	}
	public void setSegnalazione(Segnalazione segnalazione) {
		this.segnalazione = segnalazione;
	}
	public int getVoto() {
		return voto;
	}
	public void setVoto(int voto) {
		this.voto = voto;
	}
}
