package com.javasampleapproach.security.model;

public class AnonymousUser {

	private Gender gender;
	private int eta;
	private Istruzione istruzione;
	private Occupazione occupazione;
	private boolean hasCar;
	private int annoImmatricolazione;
	private TipoCarburante tipoCarburante;
	private boolean useCarSharing;
	private FornitoreCarSharing fornitoreCarSharing;
	private boolean useBike;
	private boolean useBikeSharing;
	private boolean useMezziPubblici;
	private TipoViaggio tipoViaggio;
    
	public AnonymousUser() {
	}
	
	public AnonymousUser(User u) {
		this.gender = u.getGender();
		this.eta = u.getEta();
		this.istruzione = u.getIstruzione();
		this.occupazione = u.getOccupazione();
		this.hasCar = u.isHascar();
		this.annoImmatricolazione =u.getAnnoimmatricolazione();
		this.tipoCarburante = u.getTipocarburante();
		this.useCarSharing = u.isUsecarsharing();
		this.fornitoreCarSharing = u.getFornitorecarsharing();
		this.useBike = u.isUsebike();
		this.useBikeSharing = u.isUsebikesharing();
		this.useMezziPubblici = u.isUsemezzipubblici();
		this.tipoViaggio = u.getTipoviaggio();
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public int getEta() {
		return eta;
	}

	public void setEta(int eta) {
		this.eta = eta;
	}
	
	public Istruzione getIstruzione() {
		return istruzione;
	}

	public void setIstruzione(Istruzione istruzione) {
		this.istruzione = istruzione;
	}

	public Occupazione getOccupazione() {
		return occupazione;
	}

	public void setOccupazione(Occupazione occupazione) {
		this.occupazione = occupazione;
	}

	public boolean isHasCar() {
		return hasCar;
	}

	public void setHasCar(boolean hasCar) {
		this.hasCar = hasCar;
	}

	public int getAnnoImmatricolazione() {
		return annoImmatricolazione;
	}

	public void setAnnoImmatricolazione(int annoImmatricolazione) {
		this.annoImmatricolazione = annoImmatricolazione;
	}

	public TipoCarburante getTipoCarburante() {
		return tipoCarburante;
	}

	public void setTipoCarburante(TipoCarburante tipoCarburante) {
		this.tipoCarburante = tipoCarburante;
	}

	public boolean isUseCarSharing() {
		return useCarSharing;
	}

	public void setUseCarSharing(boolean useCarSharing) {
		this.useCarSharing = useCarSharing;
	}

	public FornitoreCarSharing getFornitoreCarSharing() {
		return fornitoreCarSharing;
	}

	public void setFornitoreCarSharing(FornitoreCarSharing fornitoreCarSharing) {
		this.fornitoreCarSharing = fornitoreCarSharing;
	}

	public boolean isUseBike() {
		return useBike;
	}

	public void setUseBike(boolean useBike) {
		this.useBike = useBike;
	}

	public boolean isUseBikeSharing() {
		return useBikeSharing;
	}

	public void setUseBikeSharing(boolean useBikeSharing) {
		this.useBikeSharing = useBikeSharing;
	}

	public boolean isUseMezziPubblici() {
		return useMezziPubblici;
	}

	public void setUseMezziPubblici(boolean useMezziPubblici) {
		this.useMezziPubblici = useMezziPubblici;
	}

	public TipoViaggio getTipoViaggio() {
		return tipoViaggio;
	}

	public void setTipoViaggio(TipoViaggio tipoViaggio) {
		this.tipoViaggio = tipoViaggio;
	}

}
