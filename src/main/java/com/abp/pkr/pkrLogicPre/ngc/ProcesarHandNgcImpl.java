package com.abp.pkr.pkrLogicPre.ngc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.abp.pkr.pkrLogicPre.dao.RangeSttgyRepositoryI;
import com.abp.pkr.pkrLogicPre.db.TbRangeSttgy;
import com.abp.pkr.pkrLogicPre.dto.HandInfoDto;

@Controller
public class ProcesarHandNgcImpl implements ProcesarHandNgc {

	@Autowired
	RangeSttgyRepositoryI repositorio;

	/**
	 * Metodo que procesa una mano para asesorar como jugar a HERO,
	 * 
	 * @author abpubuntu
	 * @date Jun 6, 2018
	 * @param handInfoDto
	 * @throws Exception
	 */
	@Override
	public void procesarHand(HandInfoDto handInfoDto) throws Exception {

		// obtener stacks efectivos - la posicion con valor 0 es la de hero
		Double[] stackEff = handInfoDto.obtenerStackEff();

		// obtenemos la posicion de los otros jugadores
		String[] vsPlayers = obtenerVsPlayer(handInfoDto);
		
		
		//consultamos rangos a jugar y armamos el Map que contiene los rangos a jugar vs el rival
		Map<String, List<TbRangeSttgy>> acciones = new HashMap<>();
		int i = 0;		
		for (Double stack : stackEff) {
			if (stack > 0) {
				List<TbRangeSttgy> rangos = repositorio
						.findByNbSttgyNumjugAndVrSttgyPosheroAndVrSttgyVsPlayerAndNbSttgyStackminLessThanAndNbSttgyStackmaxGreaterThanEqual(
								handInfoDto.getNumjug(), handInfoDto.getPosicionHero(), vsPlayers[i], stack, stack);	
				// hasmap que tiene como clave la posicion del vsPlayer y los rangos a jugar contra el
				acciones.put(vsPlayers[i], rangos);
			}
			i++;
		}
		
		
		//Armamos el objeto a retornar por el servicio
		
		

	}
	
	
	public String[] obtenerVsPlayer(HandInfoDto handInfoDto) {
		String[] vsPlayers = new String[handInfoDto.getNumjug()];
		if (vsPlayers.length == 3) {
			if (handInfoDto.getBtnPos() == 0) {
				vsPlayers[0] = "BU";
				vsPlayers[1] = "SB";
				vsPlayers[2] = "BB";
			}
			if (handInfoDto.getBtnPos() == 1) {
				vsPlayers[0] = "BB";
				vsPlayers[1] = "BU";
				vsPlayers[2] = "SB";
			}

			if (handInfoDto.getBtnPos() == 2) {
				vsPlayers[0] = "SB";
				vsPlayers[1] = "BB";
				vsPlayers[2] = "BU";
			}
		}

		if (vsPlayers.length == 2) {
			if (handInfoDto.getBtnPos() == 0) {
				vsPlayers[0] = "BU";
				vsPlayers[1] = "BB";
			}
			if (handInfoDto.getBtnPos() == 1) {
				vsPlayers[0] = "BB";
				vsPlayers[1] = "BU";
			}
		}
		
		return vsPlayers;
		
	}
	

}
