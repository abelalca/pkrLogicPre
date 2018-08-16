package com.abp.pkr.pkrLogicPre.ngc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.abp.pkr.pkrLogicPre.dao.RangeSttgyRepositoryI;
import com.abp.pkr.pkrLogicPre.db.TbRangeSttgy;
import com.abp.pkr.pkrLogicPre.dto.AccionInfoDto;
import com.abp.pkr.pkrLogicPre.dto.AccionVsPlayer;
import com.abp.pkr.pkrLogicPre.dto.HandInfoDto;

import ch.qos.logback.classic.Logger;

@Controller
public class ProcesarHandNgcImpl implements ProcesarHandNgc {

	private static final Logger log = (Logger) LoggerFactory.getLogger(ProcesarHandNgcImpl.class);

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
		log.debug("Obteniendo stacks efectivos: " + Arrays.toString(stackEff));

		// obtenemos la posicion de los otros jugadores
		String[] vsPlayers = obtenerVsPlayer(handInfoDto);
		log.debug("Obteniendo posicion jugadores: " + Arrays.toString(vsPlayers));

		String mano = ordenarHand(handInfoDto.getHand());

		// consultamos rangos a jugar y armamos el Map que contiene los rangos a jugar
		// vs el rival
		Map<String, List<TbRangeSttgy>> acciones = new HashMap<>();
		int i = 0;
		for (Double stack : stackEff) {
			if (stack > 0) {
				List<TbRangeSttgy> rangos = repositorio
						.findByNbSttgyNumjugAndVrAutUsuarioAndVrSttgyStrategyAndVrSttgyPosheroAndVrSttgyVsPlayerAndVrSttgyRangeContainingAndNbSttgyStackminLessThanAndNbSttgyStackmaxGreaterThanEqual(
								handInfoDto.getNumjug(), handInfoDto.getUsuario(), handInfoDto.getEstrategia(),
								handInfoDto.getPosicionHero(), vsPlayers[i], mano, stack, stack);
				// hashmap que tiene como clave la posicion del vsPlayer y los rangos a jugar
				// contra el
				acciones.put(vsPlayers[i], rangos);
			}
			i++;
		}
		log.debug("Obteniendo acciones a jugar, numero acciones consultadas: " + acciones.size());


		AccionInfoDto accionInfoDto = obtenerAcciones(handInfoDto, stackEff, acciones);
		log.debug("Retornando Objeto de Acciones");

		return accionInfoDto;
	}

	private String ordenarHand(String hand) {
		String carta1 = hand.substring(0, 1);
		String carta2 = hand.substring(2, 3);
		String palo1 = hand.substring(1, 2);
		String palo2 = hand.substring(3, 4);

		List<String> mano = new ArrayList<String>();
		mano.add(carta1);
		mano.add(carta2);

		String ORDER = "AKQJT98765432";

		Collections.sort(mano, new Comparator<String>() {

			@Override
			public int compare(String o1, String o2) {
				return ORDER.indexOf(o1) - ORDER.indexOf(o2);
			}
		});

		String suite = null;
		if (palo1.equals(palo2)) {
			suite = "s";
		} else {
			if (carta1.equals(carta2)) {
				suite = "";
			} else {
				suite = "o";
			}
		}

		String ordenada = mano.get(0) + mano.get(1) + suite;

		return ordenada;
	}

	public AccionInfoDto obtenerAcciones(HandInfoDto handInfoDto, Double[] stackEff,
			Map<String, List<TbRangeSttgy>> acciones) throws Exception {
		AccionInfoDto accionInfoDto = new AccionInfoDto();
		accionInfoDto.setHand(handInfoDto.getHand());
		accionInfoDto.setNumJug(handInfoDto.numJugadores() + "max");

		String poshero = handInfoDto.getPosicionHero();
		accionInfoDto.setPosHero(poshero);

		int heroposArr = Arrays.asList(stackEff).indexOf((double) 0);

		if (heroposArr < 0) {
			throw new Exception("Posicion de Hero en el Array no identificada");
		}

		accionInfoDto.setStackHero(handInfoDto.getStacksBb()[heroposArr]);

		Map<String, AccionVsPlayer> accVsPly = new HashMap<>();
		String[] tiposPly = obtenerDisctinctTipoPlayers(acciones);
		
		// es porque no trajo ninguna accion
		if(tiposPly.length == 0) {
			accionInfoDto.setDefAccion("F");
			return accionInfoDto;
		}
		
		

		if (handInfoDto.numJugadores() == 3 && poshero == "BU") {
			accionInfoDto.setIzqVsEffStack(stackEff[2]);
			accionInfoDto.setIzqVsPlayer("SB");
			accionInfoDto.setDerVsEffStack(stackEff[0]);
			accionInfoDto.setDerVsPlayer("BB");

			for (String tipplayer : tiposPly) {
				List<String> izq = obtenerAccionVsPlayer(acciones, tipplayer, "SB");
				List<String> der = obtenerAccionVsPlayer(acciones, tipplayer, "BB");

				AccionVsPlayer acply = new AccionVsPlayer();
				acply.setIzqQA1(izq.get(0));
				acply.setIzqQA2(izq.get(1));
				acply.setIzqQA3(izq.get(2));

				acply.setDerQA1(der.get(0));
				acply.setDerQA2(der.get(1));
				acply.setDerQA3(der.get(2));

				accVsPly.put(tipplayer, acply);

			}

			String accIzqDef = accVsPly.get("DEF").getIzqQA1();
			String accDerDef = accVsPly.get("DEF").getDerQA1();

			if (stackEff[2] > stackEff[0]) {
				accionInfoDto.setDefAccion(accIzqDef.substring(0, 1));
			} else {
				accionInfoDto.setDefAccion(accDerDef.substring(0, 1));
			}

		}
		if (handInfoDto.numJugadores() == 3 && poshero == "SB") {
			accionInfoDto.setDerVsEffStack(stackEff[0]);
			accionInfoDto.setDerVsPlayer("BU");
			accionInfoDto.setIzqVsEffStack(stackEff[2]);
			accionInfoDto.setIzqVsPlayer("BB");

			for (String tipplayer : tiposPly) {
				List<String> der = obtenerAccionVsPlayer(acciones, tipplayer, "BU");
				List<String> izq = obtenerAccionVsPlayer(acciones, tipplayer, "BB");

				AccionVsPlayer acply = new AccionVsPlayer();
				acply.setIzqQA1(izq.get(0));
				acply.setIzqQA2(izq.get(1));
				acply.setIzqQA3(izq.get(2));

				acply.setDerQA1(der.get(0));
				acply.setDerQA2(der.get(1));
				acply.setDerQA3(der.get(2));

				accVsPly.put(tipplayer, acply);
			}

			String accIzqDef = accVsPly.get("DEF").getIzqQA1();
			accionInfoDto.setDefAccion(accIzqDef.substring(0, 1));
		}
		if (handInfoDto.numJugadores() == 3 && poshero == "BB") {
			accionInfoDto.setIzqVsEffStack(stackEff[2]);
			accionInfoDto.setIzqVsPlayer("BU");
			accionInfoDto.setDerVsEffStack(stackEff[0]);
			accionInfoDto.setDerVsPlayer("SB");

			for (String tipplayer : tiposPly) {
				List<String> izq = obtenerAccionVsPlayer(acciones, tipplayer, "BU");
				List<String> der = obtenerAccionVsPlayer(acciones, tipplayer, "SB");

				AccionVsPlayer acply = new AccionVsPlayer();
				acply.setIzqQA1(izq.get(0));
				acply.setIzqQA2(izq.get(1));
				acply.setIzqQA3(izq.get(2));

				acply.setDerQA1(der.get(0));
				acply.setDerQA2(der.get(1));
				acply.setDerQA3(der.get(2));

				accVsPly.put(tipplayer, acply);
			}

		}

		if (handInfoDto.numJugadores() == 2 && poshero == "SB") {
			if (handInfoDto.getIsActivo()[0]) {
				accionInfoDto.setDerVsEffStack(Collections.max(Arrays.asList(stackEff)));
				accionInfoDto.setDerVsPlayer("BB");
			}
			if (handInfoDto.getIsActivo()[2]) {
				accionInfoDto.setIzqVsEffStack(Collections.max(Arrays.asList(stackEff)));
				accionInfoDto.setIzqVsPlayer("BB");
			}

			for (String tipplayer : tiposPly) {
				List<String> lado = obtenerAccionVsPlayer(acciones, tipplayer, "BB");

				AccionVsPlayer acply = new AccionVsPlayer();
				if (handInfoDto.getIsActivo()[0]) {
					acply.setDerQA1(lado.get(0));
					acply.setDerQA2(lado.get(1));
					acply.setDerQA3(lado.get(2));
				}
				if (handInfoDto.getIsActivo()[2]) {
					acply.setIzqQA1(lado.get(0));
					acply.setIzqQA2(lado.get(1));
					acply.setIzqQA3(lado.get(2));
				}

				accVsPly.put(tipplayer, acply);
			}

			String accIzqDef = accVsPly.get("DEF").getIzqQA1();
			String accDerDef = accVsPly.get("DEF").getDerQA1();

			if (handInfoDto.getIsActivo()[0]) {
				accionInfoDto.setDefAccion(accDerDef.substring(0, 1));
			} else {
				accionInfoDto.setDefAccion(accIzqDef.substring(0, 1));
			}
		}

		if (handInfoDto.numJugadores() == 2 && poshero == "BB") {
			if (handInfoDto.getIsActivo()[0]) {
				accionInfoDto.setDerVsEffStack(Collections.max(Arrays.asList(stackEff)));
				accionInfoDto.setDerVsPlayer("SB");
			}
			if (handInfoDto.getIsActivo()[2]) {
				accionInfoDto.setIzqVsEffStack(Collections.max(Arrays.asList(stackEff)));
				accionInfoDto.setIzqVsPlayer("SB");
			}

			for (String tipplayer : tiposPly) {
				List<String> lado = obtenerAccionVsPlayer(acciones, tipplayer, "SB");

				AccionVsPlayer acply = new AccionVsPlayer();
				if (handInfoDto.getIsActivo()[0]) {
					acply.setDerQA1(lado.get(0));
					acply.setDerQA2(lado.get(1));
					acply.setDerQA3(lado.get(2));
				}
				if (handInfoDto.getIsActivo()[2]) {
					acply.setIzqQA1(lado.get(0));
					acply.setIzqQA2(lado.get(1));
					acply.setIzqQA3(lado.get(2));
				}

				accVsPly.put(tipplayer, acply);
			}

			String accIzqDef = accVsPly.get("DEF").getIzqQA1();
			String accDerDef = accVsPly.get("DEF").getDerQA1();

			if (handInfoDto.getIsActivo()[0]) {
				accionInfoDto.setDefAccion(accDerDef.substring(0, 1));
			} else {
				accionInfoDto.setDefAccion(accIzqDef.substring(0, 1));
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
		String QA3 = "";
		for (TbRangeSttgy range : accs) {
			if (range.getVrSttgyTipoaccion().trim().equals("UO") && range.getVrSttgyTipjug().equals(tipplayer)) {
				QA1 = QA1 + range.getVrSttgyAccion();
			}
			if (range.getVrSttgyTipoaccion().trim().equals("L") && range.getVrSttgyTipjug().equals(tipplayer)) {
				QA2 = QA2 + range.getVrSttgyAccion();
			}
			if (range.getVrSttgyTipoaccion().trim().equals("S") && range.getVrSttgyTipjug().equals(tipplayer)) {
				QA3 = QA3 + range.getVrSttgyAccion();
			}
		}

		uoPF.add(QA1);
		uoPF.add(QA2);
		uoPF.add(QA3);

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
				vsPlayers[0] = "SB";
				vsPlayers[1] = "BB";
			}
			if (handInfoDto.getBtnPos() == 1) {
				vsPlayers[0] = "BB";
				vsPlayers[1] = "SB";
			}
		}

		return vsPlayers;

	}

}
