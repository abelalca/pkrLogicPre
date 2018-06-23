package com.abp.pkr.pkrLogicPre.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tb_range_sttgy")
public class TbRangeSttgy {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "nb_sttgy_idn")
	private Integer nbSttgyIdn;

	@Column(name = "nb_sttgy_numjug")
	private Integer nbSttgyNumjug;

	@Column(name = "vr_sttgy_poshero")
	private String vrSttgyPoshero;

	@Column(name = "nb_sttgy_pushfold")
	private Integer nbSttgyPushfold;

	@Column(name = "nb_sttgy_stackmin")
	private Double nbSttgyStackmin;

	@Column(name = "nb_sttgy_stackmax")
	private Double nbSttgyStackmax;

	@Column(name = "vr_sttgy_tipjug")
	private String vrSttgyTipjug;

	@Column(name = "vr_sttgy_vsplayer")
	private String vrSttgyVsPlayer;

	@Column(name = "vr_sttgy_range")
	private String vrSttgyRange;

	@Column(name = "vr_sttgy_accion")
	private String vrSttgyAccion;

	public String getVrSttgyAccion() {
		return vrSttgyAccion;
	}

	public void setVrSttgyAccion(String vrSttgyAccion) {
		this.vrSttgyAccion = vrSttgyAccion;
	}

	public Integer getNbSttgyIdn() {
		return nbSttgyIdn;
	}

	public void setNbSttgyIdn(Integer nbSttgyIdn) {
		this.nbSttgyIdn = nbSttgyIdn;
	}

	public Integer getNbSttgyNumjug() {
		return nbSttgyNumjug;
	}

	public void setNbSttgyNumjug(Integer nbSttgyNumjug) {
		this.nbSttgyNumjug = nbSttgyNumjug;
	}

	public String getVrSttgyPoshero() {
		return vrSttgyPoshero;
	}

	public void setVrSttgyPoshero(String vrSttgyPoshero) {
		this.vrSttgyPoshero = vrSttgyPoshero;
	}

	public Integer getNbSttgyPushfold() {
		return nbSttgyPushfold;
	}

	public void setNbSttgyPushfold(Integer nbSttgyPushfold) {
		this.nbSttgyPushfold = nbSttgyPushfold;
	}

	public Double getNbSttgyStackmin() {
		return nbSttgyStackmin;
	}

	public void setNbSttgyStackmin(Double nbSttgyStackmin) {
		this.nbSttgyStackmin = nbSttgyStackmin;
	}

	public Double getNbSttgyStackmax() {
		return nbSttgyStackmax;
	}

	public void setNbSttgyStackmax(Double nbSttgyStackmax) {
		this.nbSttgyStackmax = nbSttgyStackmax;
	}

	public String getVrSttgyTipjug() {
		return vrSttgyTipjug;
	}

	public void setVrSttgyTipjug(String vrSttgyTipjug) {
		this.vrSttgyTipjug = vrSttgyTipjug;
	}

	public String getVrSttgyVsPlayer() {
		return vrSttgyVsPlayer;
	}

	public void setVrSttgyVsPlayer(String vrSttgyVsPlayer) {
		this.vrSttgyVsPlayer = vrSttgyVsPlayer;
	}

	public String getVrSttgyRange() {
		return vrSttgyRange;
	}

	public void setVrSttgyRange(String vrSttgyRange) {
		this.vrSttgyRange = vrSttgyRange;
	}

}
