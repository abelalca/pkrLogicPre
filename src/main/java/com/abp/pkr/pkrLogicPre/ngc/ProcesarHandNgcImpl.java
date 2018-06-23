package com.abp.pkr.pkrLogicPre.ngc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.abp.pkr.pkrLogicPre.dao.RangeSttgyRepositoryI;
import com.abp.pkr.pkrLogicPre.db.TbRangeSttgy;
import com.abp.pkr.pkrLogicPre.dto.AccionInfoDto;
import com.abp.pkr.pkrLogicPre.dto.AccionVsPlayer;
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
	public AccionInfoDto procesarHand(HandInfoDto handInfoDto) throws Exception {

		// obtener stacks efectivos - la posicion con valor 0 es la de hero
		Double[] stackEff = handInfoDto.obtenerStackEff();

		// obtenemos la posicion de los otros jugadores
		String[] vsPlayers = obtenerVsPlayer(handInfoDto);

		// consultamos rangos a jugar y armamos el Map que contiene los rangos a jugar
		// vs el rival
		Map<String, List<TbRangeSttgy>> acciones = new HashMap<>();
		int i = 0;
		for (Double stack : stackEff) {
			if (stack > 0) {
				List<TbRangeSttgy> rangos = repositorio
						.findByNbSttgyNumjugAndVrSttgyPosheroAndVrSttgyVsPlayerAndVrSttgyRangeContainingAndNbSttgyStackminLessThanAndNbSttgyStackmaxGreaterThanEqual(
								handInfoDto.getNumjug(), handInfoDto.getPosicionHero(), vsPlayers[i],
								handInfoDto.getHand(), stack, stack);
				// hashmap que tiene como clave la posicion del vsPlayer y los rangos a jugar
				// contra el
				acciones.put(vsPlayers[i], rangos);
			}
			i++;
		}

		// Armamos el objeto a retornar por el servicio
		AccionInfoDto accionInfoDto = obtenerAcciones(handInfoDto, stackEff, acciones);
		
		return accionInfoDto;
	}

	public AccionInfoDto obtenerAcciones(HandInfoDto handInfoDto, Double[] stackEff,
			Map<String, List<TbRangeSttgy>> acciones) throws Exception {
		AccionInfoDto accionInfoDto = new AccionInfoDto();
		accionInfoDto.setHand(handInfoDto.getHand());
		accionInfoDto.setNumJug(handInfoDto.numJugadores() + "max");

		String poshero = handInfoDto.getPosicionHero();
		accionInfoDto.setPosHero(poshero);
		
		int heroposArr = Arrays.asList(stackEff).indexOf((double)0);
		
		if(heroposArr < 0) {
			throw new Exception("Posicion de Hero en el Array no identificada");
		}
		
		accionInfoDto.setStackHero(handInfoDto.getStacksBb()[heroposArr]);

		Map<String, AccionVsPlayer> accVsPly = new HashMap<>();
		String[] tiposPly = obtenerDisctinctTipoPlayers(acciones);

		if (handInfoDto.numJugadores() == 3 && poshero == "BU") {
			accionInfoDto.setSupVsEffStack(stackEff[2]);
			accionInfoDto.setSupVsPlayer("SB");
			accionInfoDto.setInfVsEffStack(stackEff[0]);
			accionInfoDto.setInfVsPlayer("BB");

			for (String tipplayer : tiposPly) {
				List<String> arriba = obtenerAccionVsPlayer(acciones, tipplayer, "SB");
				List<String> abajo = obtenerAccionVsPlayer(acciones, tipplayer, "BB");

				AccionVsPlayer acply = new AccionVsPlayer();
				acply.setSupQA1(arriba.get(0));
				acply.setSupQA2(arriba.get(1));
				acply.setInfQA1(abajo.get(0));
				acply.setInfQA2(abajo.get(1));

				accVsPly.put(tipplayer, acply);
			}

		}
		if (handInfoDto.numJugadores() == 3 && poshero == "SB") {
			accionInfoDto.setSupVsEffStack(stackEff[0]);
			accionInfoDto.setSupVsPlayer("BU");
			accionInfoDto.setInfVsEffStack(stackEff[2]);
			accionInfoDto.setInfVsPlayer("BB");

			for (String tipplayer : tiposPly) {
				List<String> arriba = obtenerAccionVsPlayer(acciones, tipplayer, "BU");
				List<String> abajo = obtenerAccionVsPlayer(acciones, tipplayer, "BB");

				AccionVsPlayer acply = new AccionVsPlayer();
				acply.setSupQA1(arriba.get(0));
				acply.setSupQA2(arriba.get(1));
				acply.setInfQA1(abajo.get(0));
				acply.setInfQA2(abajo.get(1));

				accVsPly.put(tipplayer, acply);
			}
		}
		if (handInfoDto.numJugadores() == 3 && poshero == "BB") {
			accionInfoDto.setSupVsEffStack(stackEff[2]);
			accionInfoDto.setSupVsPlayer("BU");
			accionInfoDto.setInfVsEffStack(stackEff[0]);
			accionInfoDto.setInfVsPlayer("SB");
			
			for (String tipplayer : tiposPly) {
				List<String> arriba = obtenerAccionVsPlayer(acciones, tipplayer, "BU");
				List<String> abajo = obtenerAccionVsPlayer(acciones, tipplayer, "SB");
				
				AccionVsPlayer acply = new AccionVsPlayer();
				acply.setSupQA1(arriba.get(0));
				acply.setSupQA2(arriba.get(1));
				acply.setInfQA1(abajo.get(0));
				acply.setInfQA2(abajo.get(1));
				
				accVsPly.put(tipplayer, acply);
			}
		}

		if (handInfoDto.numJugadores() == 2 && poshero == "SB") {
			accionInfoDto.setSupVsEffStack(Collections.max(Arrays.asList(stackEff)));
			accionInfoDto.setSupVsPlayer("BB");
			accionInfoDto.setInfVsEffStack(null);
			accionInfoDto.setInfVsPlayer(null);
			
			
			for (String tipplayer : tiposPly) {
				List<String> arriba = obtenerAccionVsPlayer(acciones, tipplayer, "BB");
				
				AccionVsPlayer acply = new AccionVsPlayer();
				acply.setSupQA1(arriba.get(0));
				acply.setSupQA2(arriba.get(1));
				
				accVsPly.put(tipplayer, acply);
			}
		}
		if (handInfoDto.numJugadores() == 2 && poshero == "BB") {
			accionInfoDto.setSupVsEffStack(Collections.max(Arrays.asList(stackEff)));
			accionInfoDto.setSupVsPlayer("SB");
			accionInfoDto.setInfVsEffStack(null);
			accionInfoDto.setInfVsPlayer(null);
			
			for (String tipplayer : tiposPly) {
				List<String> arriba = obtenerAccionVsPlayer(acciones, tipplayer, "SB");
				
				AccionVsPlayer acply = new AccionVsPlayer();
				acply.setSupQA1(arriba.get(0));
				acply.setSupQA2(arriba.get(1));
				
				accVsPly.put(tipplayer, acply);
			}
		}
		
		accionInfoDto.setAccionVsPlayer(accVsPly);

		return accionInfoDto;
	}

	private List<String> obtenerAccionVsPlayer(Map<String, List<TbRangeSttgy>> acciones, String tipplayer,
			String posi) {
		List<String> uoPF = new ArrayList<>();
		List<TbRangeSttgy> accs = acciones.get(posi);

		String QA1 = "";
		String QA2 = "";
		for (TbRangeSttgy range : accs) {
			if (range.getNbSttgyPushfold() == 0 && range.getVrSttgyTipjug().equals(tipplayer)) {
				QA1 = QA1 + "\\n" + range.getVrSttgyAccion();
			}
			if (range.getNbSttgyPushfold() == 1 && range.getVrSttgyTipjug().equals(tipplayer)) {
				QA2 = QA2 + "\\n" + range.getVrSttgyAccion();
			}
		}

		uoPF.add(QA1);
		uoPF.add(QA2);

		return uoPF;

	}

	private String[] obtenerDisctinctTipoPlayers(Map<String, List<TbRangeSttgy>> acciones) {
		List<String> tipos = new ArrayList<>();

		for (List<TbRangeSttgy> lista : acciones.values()) {
			for (TbRangeSttgy tbRangeSttgy : lista) {
				if (!tipos.contains(tbRangeSttgy.getVrSttgyTipjug())) {
					tipos.add(tbRangeSttgy.getVrSttgyTipjug());
				}
			}
		}

		String[] arr = new String[tipos.size()];

		return tipos.toArray(arr);
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
