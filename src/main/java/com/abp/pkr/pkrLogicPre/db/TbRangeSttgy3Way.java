package com.abp.pkr.pkrLogicPre.db;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tb_spin_range_sttgy3way")
public class TbRangeSttgy3Way {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "nb_sttgy_idn")
	private Integer nbSttgyIdn;

	@Column(name = "nb_sttgy_stackmin")
	private Double nbSttgyStackmin;

	@Column(name = "nb_sttgy_stackmax")
	private Double nbSttgyStackmax;

	@Column(name = "vr_sttgy_range")
	private String vrSttgyRange;

	@Column(name = "vr_sttgy_strategy")
	private String vrSttgyStrategy;

	@Column(name = "vr_aut_usuario")
	private String vrAutUsuario;

	@Column(name = "dt_aut_fechareg")
	private Date dtAutFechareg;

	@Column(name = "vr_sttgy_accion")
	private String vrSttgyAccion;

	@Column(name = "vr_sttgy_tipoaccion")
	private String vrSttgyTipoaccion;

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

	public String getVrSttgyRange() {
		return vrSttgyRange;
	}

	public void setVrSttgyRange(String vrSttgyRange) {
		this.vrSttgyRange = vrSttgyRange;
	}

	public String getVrSttgyStrategy() {
		return vrSttgyStrategy;
	}

	public void setVrSttgyStrategy(String vrSttgyStrategy) {
		this.vrSttgyStrategy = vrSttgyStrategy;
	}

	public String getVrAutUsuario() {
		return vrAutUsuario;
	}

	public void setVrAutUsuario(String vrAutUsuario) {
		this.vrAutUsuario = vrAutUsuario;
	}

	public Date getDtAutFechareg() {
		return dtAutFechareg;
	}

	public void setDtAutFechareg(Date dtAutFechareg) {
		this.dtAutFechareg = dtAutFechareg;
	}

	public String getVrSttgyTipoaccion() {
		return vrSttgyTipoaccion;
	}

	public void setVrSttgyTipoaccion(String vrSttgyTipoaccion) {
		this.vrSttgyTipoaccion = vrSttgyTipoaccion;
	}

}
