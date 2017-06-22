package com.javasampleapproach.security.model;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity
@Table(name="dettagliouser")
public class User implements Serializable{

	private static final long serialVersionUID = -3009157732242241606L;
	
	private String nickname;
	@Id
	private String email;
	
	private Gender gender;

	private int eta;
	private Istruzione istruzione;
	private Occupazione occupazione;
	@Column(name = "hascar")
	private boolean hascar;
	
	@Column(name = "annoimmatricolazione")
	private int annoimmatricolazione;
	
	@Column(name = "carburante")
	private TipoCarburante tipocarburante;
	private boolean usecarsharing;
	
	@Column(name = "fornitoresharing")
	private FornitoreCarSharing fornitorecarsharing;
	private boolean usebike;
	private boolean usebikesharing;
	private boolean usemezzipubblici;
	private TipoViaggio tipoviaggio;
    private String foto;
    
    
	public User(String username, String email, Gender gender, int eta, Istruzione istruzione, Occupazione occupazione, 
    			boolean hasCar, int annoimmatricolazione, TipoCarburante tipoCarburante, boolean useCarSharing,
    			FornitoreCarSharing fornitoreCarSharing, boolean useBike, boolean useBikeSharing,
    			boolean useMezziPubblici, TipoViaggio tipoViaggio, String foto){
    	this.nickname = username;
    	this.email = email;
    	this.gender = gender;
    	this.eta = eta;
    	this.istruzione = istruzione;
    	this.occupazione = occupazione;
    	this.hascar = hasCar;
    	this.annoimmatricolazione = annoimmatricolazione;
    	this.tipocarburante = tipoCarburante;
    	this.usecarsharing = useCarSharing;
    	this.fornitorecarsharing = fornitoreCarSharing;
    	this.usebike = useBike;
    	this.usebikesharing = useBikeSharing;
    	this.usemezzipubblici = useMezziPubblici;
    	this.tipoviaggio = tipoViaggio;
    	this.foto = foto;
    }
    public User(){
    	
    }
    
    public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
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
	
	public String getFoto() {
		return foto;
	}
	public void setFoto(String foto) {
		this.foto = foto;
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	public boolean isHascar() {
		return hascar;
	}
	public void setHascar(boolean hascar) {
		this.hascar = hascar;
	}
	public int getAnnoimmatricolazione() {
		return annoimmatricolazione;
	}
	public void setAnnoimmatricolazione(int annoimmatricolazione) {
		this.annoimmatricolazione = annoimmatricolazione;
	}
	public TipoCarburante getTipocarburante() {
		return tipocarburante;
	}
	public void setTipocarburante(TipoCarburante tipocarburante) {
		this.tipocarburante = tipocarburante;
	}
	public boolean isUsecarsharing() {
		return usecarsharing;
	}
	public void setUsecarsharing(boolean usecarsharing) {
		this.usecarsharing = usecarsharing;
	}
	public FornitoreCarSharing getFornitorecarsharing() {
		return fornitorecarsharing;
	}
	public void setFornitorecarsharing(FornitoreCarSharing fornitorecarsharing) {
		this.fornitorecarsharing = fornitorecarsharing;
	}
	public boolean isUsebike() {
		return usebike;
	}
	public void setUsebike(boolean usebike) {
		this.usebike = usebike;
	}
	public boolean isUsebikesharing() {
		return usebikesharing;
	}
	public void setUsebikesharing(boolean usebikesharing) {
		this.usebikesharing = usebikesharing;
	}
	public boolean isUsemezzipubblici() {
		return usemezzipubblici;
	}
	public void setUsemezzipubblici(boolean usemezzipubblici) {
		this.usemezzipubblici = usemezzipubblici;
	}
	public TipoViaggio getTipoviaggio() {
		return tipoviaggio;
	}
	public void setTipoviaggio(TipoViaggio tipoviaggio) {
		this.tipoviaggio = tipoviaggio;
	}
}
