package com.abp.pkr.pkrLogicPre.dto;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;

public class HandInfoDto {

	private Integer btnPos;
	private Integer posHero;
	private String posicionHero;

	private int sillaHero;
	private String hand;
	// los arrays deben tener length de los jugadores vivos en la mesa
	private Double[] stacksBb;
	private boolean[] isActivo;
	private Integer numjug;

	// tiempo de respuesta del servicio
	private Long tiempoRest;

	public int getSillaHero() {
		return sillaHero;
	}

	public void setSillaHero(int sillaHero) {
		this.sillaHero = sillaHero;
	}

	public Integer getPosHero() {
		return posHero;
	}

	public void setPosHero(Integer posHero) {
		this.posHero = posHero;
	}

	public Double[] getStacksBb() {
		return stacksBb;
	}

	public void setStacksBb(Double[] stacksBb) {
		this.stacksBb = stacksBb;
	}

	public Integer getBtnPos() {
		return btnPos;
	}

	public void setBtnPos(Integer btnPos) {
		this.btnPos = btnPos;
	}

	public String getHand() {
		return hand;
	}

	public void setHand(String hand) {
		this.hand = hand;
	}

	public int numJugadores() {
		return this.stacksBb.length;
	}

	// Obtiene una copia de la mano pero ordenada con BB en ultima posicion
	public HandInfoDto ordenarInfo() {
		HandInfoDto hand = new HandInfoDto();
		hand.setHand(this.hand);

		// colocamos a BU en la posicion 0 y despues lo hacemos con BB
		if (this.btnPos != 0) {
			// BTN en pos 0
			int tam = this.stacksBb.length;
			Double[] antBu = Arrays.copyOfRange(this.stacksBb, 0, this.btnPos - 1);
			Double[] desBu = Arrays.copyOfRange(this.stacksBb, this.btnPos, tam - 1);
			hand.setStacksBb((Double[]) ArrayUtils.addAll(desBu, antBu));
		} else {
			hand.setStacksBb(this.stacksBb);
		}
		if (this.btnPos <= this.posHero) {
			hand.setPosHero(this.posHero - this.btnPos);
		} else {
			int posH = (this.stacksBb.length - this.btnPos) + this.posHero;
			hand.setPosHero(posH);
		}
		hand.setBtnPos(0);

		// paso BB a la ultima posicion, la pos 0 queda con el primer jugador en
		// hablar
		int bbPosi = 2;
		if (hand.getStacksBb().length > 3) {
			int tam = this.stacksBb.length;
			Double[] antBB = Arrays.copyOfRange(hand.getStacksBb(), 0, bbPosi);
			Double[] desBB = Arrays.copyOfRange(hand.getStacksBb(), bbPosi + 1, tam - 1);
			hand.setStacksBb((Double[]) ArrayUtils.addAll(desBB, antBB));
		}
		if (bbPosi < hand.getPosHero()) {
			hand.setPosHero(hand.getPosHero() - bbPosi - 1);
		} else {
			int posH = (hand.getStacksBb().length - bbPosi - 1) + hand.getPosHero();
			hand.setPosHero(posH);
		}
		if (hand.getStacksBb().length > 2) {
			hand.setBtnPos(hand.getStacksBb().length - 3);
		} else {
			hand.setBtnPos(0);
		}

		return hand;
	}

	public boolean[] getIsActivo() {
		return isActivo;
	}

	public void setIsActivo(boolean[] isActivo) {
		this.isActivo = isActivo;
	}

	public Integer getNumjug() {
		return numjug;
	}

	public void setNumjug(Integer numjug) {
		this.numjug = numjug;
	}

	public String getPosicionHero() {
		if (this.posHero == 0) {
			return "BB";
		} else if (this.posHero == 1) {
			return "SB";
		} else if (this.posHero == 2) {
			return "BU";
		}

		return null;
	}

	public void setPosicionHero(String posicionHero) {
		this.posicionHero = posicionHero;
	}

	public Double[] obtenerStackEff() {

		Double[] stackEff = new Double[this.numjug];

		// if (getPosicionHero().equals("BU") && this.numjug == 3) {
		// Double[] arr = this.stacksBb.clone();
		// arr = (Double[]) ArrayUtils.removeElement(arr, this.sillaHero);
		// List<Double> b = Arrays.asList(arr);
		// Double maximo = Collections.max(b);
		// stackEff[0] = maximo;
		// stackEff[1] = (double) 0;
		// stackEff[2] = maximo;
		// }

		// if (!getPosicionHero().equals("BU") && this.numjug == 3) {
		if (this.numjug == 3) {
			stackEff[1] = (double) 0;

			if (this.stacksBb[0] <= this.stacksBb[1]) {
				stackEff[0] = this.stacksBb[0];
			} else {
				stackEff[0] = this.stacksBb[1];
			}

			if (this.stacksBb[2] <= this.stacksBb[1]) {
				stackEff[2] = this.stacksBb[2];
			} else {
				stackEff[2] = this.stacksBb[1];
			}
		}

		if (this.numjug == 2) {
			if (!this.isActivo[0]) {
				// hero estaria en pos 0 del array
				stackEff[0] = (double) 0;

				stackEff[1] = this.stacksBb[0];
				if (this.stacksBb[1] <= this.stacksBb[0]) {
					stackEff[1] = this.stacksBb[1];
				}
			}

			if (!this.isActivo[2]) {
				// hero estaria en pos 1 del array
				stackEff[1] = (double) 0;

				stackEff[0] = this.stacksBb[1];
				if (this.stacksBb[2] <= this.stacksBb[1]) {
					stackEff[0] = this.stacksBb[2];
				}
			}
		}

		return stackEff;

	}

}
