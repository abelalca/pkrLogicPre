package com.abp.pkr.pkrLogicPre.ngc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.abp.pkr.pkrLogicPre.dao.RangeSttgyRepository3WayI;
import com.abp.pkr.pkrLogicPre.dao.RangeSttgyRepositoryI;
import com.abp.pkr.pkrLogicPre.db.TbRangeSttgy;
import com.abp.pkr.pkrLogicPre.db.TbRangeSttgy3Way;
import com.abp.pkr.pkrLogicPre.dto.AccionInfoDto;
import com.abp.pkr.pkrLogicPre.dto.AccionVsPlayer;
import com.abp.pkr.pkrLogicPre.dto.HandInfoDto;
import com.abp.pkr.pkrLogicPre.dto.RangeTransform;

import ch.qos.logback.classic.Logger;

@Controller
public class ProcesarHandNgcImpl implements ProcesarHandNgc {

	private static final Logger log = (Logger) LoggerFactory.getLogger(ProcesarHandNgcImpl.class);

	@Autowired
	RangeSttgyRepositoryI repositorio;

	@Autowired
	RangeSttgyRepository3WayI repositorio3way;

	@Autowired
	RangeTransform rangeTransform;

	protected static List<HandInfoDto> bufferManosAnalizadas = new ArrayList<>();
	protected static List<AccionInfoDto> bufferAccionesAnalizadas = new ArrayList<>();

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

		// almacenar mano leida para no procesarla dos veces
		AccionInfoDto accionLectura = almacenarLectura(handInfoDto);

		// si la mano ya fue analizada anteriormente, retorno el resultado de ese
		// analisis
		if (accionLectura != null) {
			log.debug("Mano actual ya ha sido analizada anteriormente: {} ", accionLectura.getHand());
			return accionLectura;
		} else {
			log.debug("Mano actual NO ha sido procesada anteriormente : {}", handInfoDto.getHand());
		}

		// obtener stacks efectivos - la posicion con valor 0 es la de hero
		Double[] stackEff = handInfoDto.obtenerStackEff();
		log.debug("Obteniendo stacks efectivos: " + Arrays.toString(stackEff));

		// obtenemos la posicion de los otros jugadores
		String[] vsPlayers = obtenerVsPlayer(handInfoDto);
		log.debug("Obteniendo posicion jugadores: " + Arrays.toString(vsPlayers));

		String mano = ordenarHand(handInfoDto.getHand());
		log.debug("Ordenando mano. Antes {} , Despues {} ", handInfoDto.getHand(), mano);

		// generamos numero aleatorio para estrategias mixtas
		int random = (int) Math.round(Math.random() * 100);
		log.debug("generando numero aleatorio para analizar mano {}", random);

		// consultamos rangos a jugar y armamos el Map que contiene los rangos a jugar
		// vs el rival
		Map<String, List<TbRangeSttgy>> acciones = new HashMap<>();
		Map<String, List<TbRangeSttgy>> accionesMix = new HashMap<>();
		int i = 0;
		for (Double stack : stackEff) {
			if (stack > 0) {
				List<TbRangeSttgy> rangosCons = repositorio
						.findByNbSttgyNumjugAndVrAutUsuarioAndVrSttgyStrategyAndVrSttgyPosheroAndVrSttgyVsPlayerAndNbSttgyStackminLessThanEqualAndNbSttgyStackmaxGreaterThan(
								handInfoDto.getNumjug(), handInfoDto.getUsuario(), handInfoDto.getEstrategia(),
								handInfoDto.getPosicionHero(), vsPlayers[i], stack, stack);
				// hashmap que tiene como clave la posicion del vsPlayer y los rangos a jugar
				// contra el
				// buscamos el rango que contenga la mano nuestra
				int estaEnRango = -1;
				List<TbRangeSttgy> rangos = new ArrayList<>();
				List<TbRangeSttgy> rangosMix = new ArrayList<>();
				for (TbRangeSttgy ran : rangosCons) {
					estaEnRango = rangeTransform.isHandInRange(ran.getVrSttgyRange(), mano, random);
					if (estaEnRango == 1) {
						log.debug(
								"Consultando base de datos. La mano {} se encuentra en el rango 2way: {} , con la accion {}",
								mano, ran.getVrSttgyRange(), ran.getVrSttgyAccion());
						rangos.add(ran);
					}
					if (estaEnRango == 0) {
						rangosMix.add(ran);
					}
				}

				acciones.put(vsPlayers[i], rangos);
				accionesMix.put(vsPlayers[i], rangosMix);
			}
			i++;
		}

		log.debug("Obteniendo acciones a jugar, numero acciones consultadas: " + acciones.size());

		AccionInfoDto accionInfoDto = obtenerAcciones(handInfoDto, stackEff, acciones, accionesMix);
		log.debug("Retornando Objeto de Acciones para la mano {}", mano);

		if (accionInfoDto == null) {
			log.error("No se encontraron acciones para la mano {}", mano);
			throw new Exception("No se encontraron acciones para la mano " + mano);
		}

		// calculamos las acciones 3way es decir cuando los dos jugadores se involucran
		// en la mano
		log.debug("Encontrando acciones 3Way");
		if (handInfoDto.numJugadores() == 3 && handInfoDto.getPosicionHero().equals("BB")) {
			List<Double> lsStackEff = Arrays.asList(stackEff);
			Double maxStackEff = Collections.max(lsStackEff);

			List<TbRangeSttgy3Way> lsRangos3way = repositorio3way
					.findByAndVrAutUsuarioAndVrSttgyStrategyAndNbSttgyStackminLessThanAndNbSttgyStackmaxGreaterThanEqual(
							handInfoDto.getUsuario(), handInfoDto.getEstrategia(), maxStackEff, maxStackEff);

			int estaEnRango = -1;
			List<TbRangeSttgy3Way> rangos = new ArrayList<>();
			for (TbRangeSttgy3Way ran : lsRangos3way) {
				estaEnRango = rangeTransform.isHandInRange(ran.getVrSttgyRange(), mano, 100);
				if (estaEnRango == 1) {
					log.debug("La mano {} esta en el rango 3way {} con la accion {}", mano, ran.getVrSttgyRange(),
							ran.getVrSttgyAccion());
					rangos.add(ran);
				}
			}
			accionInfoDto.setEff3WayStack(maxStackEff);

			obtenerAcciones3Way(accionInfoDto, rangos);
		}

		// guardamos la mano analizada en el buffer de manos
		bufferAccionesAnalizadas.add(accionInfoDto);
		bufferManosAnalizadas.add(handInfoDto);

		return accionInfoDto;
	}

	private void obtenerAcciones3Way(AccionInfoDto accionInfoDto, List<TbRangeSttgy3Way> rangos) {

		for (TbRangeSttgy3Way ran : rangos) {

			if (ran.getVrSttgyTipoaccion().equals("LL")) {
				accionInfoDto.setLL(ran.getVrSttgyAccion());
			}
			if (ran.getVrSttgyTipoaccion().equals("LR")) {
				accionInfoDto.setLR(ran.getVrSttgyAccion());
			}
			if (ran.getVrSttgyTipoaccion().equals("LS")) {
				accionInfoDto.setLS(ran.getVrSttgyAccion());
			}
			if (ran.getVrSttgyTipoaccion().equals("RC")) {
				accionInfoDto.setRC(ran.getVrSttgyAccion());
			}
			if (ran.getVrSttgyTipoaccion().equals("RS")) {
				accionInfoDto.setRS(ran.getVrSttgyAccion());
			}
			if (ran.getVrSttgyTipoaccion().equals("SS")) {
				accionInfoDto.setSS(ran.getVrSttgyAccion());
			}

		}

	}

	public String ordenarHand(String hand) {
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
			Map<String, List<TbRangeSttgy>> acciones, Map<String, List<TbRangeSttgy>> accionesMix) throws Exception {
		AccionInfoDto accionInfoDto = new AccionInfoDto();
		accionInfoDto.setHand(handInfoDto.getHand());
		accionInfoDto.setNumJug(handInfoDto.numJugadores() + "max");

		String poshero = handInfoDto.getPosicionHero();
		accionInfoDto.setPosHero(poshero);

		int heroposArr = Arrays.asList(stackEff).indexOf((double) 0);

		if (heroposArr < 0) {
			log.debug("Posicion de Hero en el Array no identificada");
			throw new Exception("Posicion de Hero en el Array no identificada");
		}

		accionInfoDto.setStackHero(handInfoDto.getStacksBb()[heroposArr]);

		Map<String, AccionVsPlayer> accVsPly = new HashMap<>();
		String[] tiposPly = obtenerDisctinctTipoPlayers(acciones);
		log.debug("Tipos diferentes de jugadores {}", Arrays.toString(tiposPly));

		// es porque no trajo ninguna accion
		if (tiposPly.length == 0) {
			accionInfoDto.setDefAccion("F");
			return accionInfoDto;
		}

		log.debug("Obteniendo acciones para la situacion, numero jugadores {} , posHero {}, hand {} ",
				handInfoDto.numJugadores(), poshero, handInfoDto.getHand());

		if (handInfoDto.numJugadores() == 3 && poshero == "BU") {
			accionInfoDto.setIzqVsEffStack(stackEff[2]);
			accionInfoDto.setIzqVsPlayer("SB");
			accionInfoDto.setDerVsEffStack(stackEff[0]);
			accionInfoDto.setDerVsPlayer("BB");

			for (String tipplayer : tiposPly) {
				List<String> izq = obtenerAccionVsPlayer(acciones, accionesMix, tipplayer, "SB");
				List<String> der = obtenerAccionVsPlayer(acciones, accionesMix, tipplayer, "BB");

				AccionVsPlayer acply = new AccionVsPlayer();
				acply.setIzqQA1(izq.get(0));
				acply.setIzqQA2(izq.get(1));
				acply.setIzqQA3(izq.get(2));

				acply.setDerQA1(der.get(0));
				acply.setDerQA2(der.get(1));
				acply.setDerQA3(der.get(2));

				accVsPly.put(tipplayer, acply);

			}

			try {
				String accIzqDef = (accVsPly.get("DEF").getIzqQA1() != null
						&& accVsPly.get("DEF").getIzqQA1().isEmpty()) ? accVsPly.get("DEF").getIzqQA3()
								: accVsPly.get("DEF").getIzqQA1();
				String accDerDef = (accVsPly.get("DEF").getDerQA1() != null
						&& accVsPly.get("DEF").getDerQA1().isEmpty()) ? accVsPly.get("DEF").getDerQA3()
								: accVsPly.get("DEF").getDerQA1();

				if (stackEff[2] > stackEff[0]) {
					accionInfoDto.setDefAccion(accDerDef.substring(0, 1));
				} else {
					accionInfoDto.setDefAccion(accIzqDef.substring(0, 1));
				}
			} catch (Exception e) {
			}

		}
		if (handInfoDto.numJugadores() == 3 && poshero == "SB") {
			accionInfoDto.setDerVsEffStack(stackEff[0]);
			accionInfoDto.setDerVsPlayer("BU");
			accionInfoDto.setIzqVsEffStack(stackEff[2]);
			accionInfoDto.setIzqVsPlayer("BB");

			for (String tipplayer : tiposPly) {
				List<String> der = obtenerAccionVsPlayer(acciones, accionesMix, tipplayer, "BU");
				List<String> izq = obtenerAccionVsPlayer(acciones, accionesMix, tipplayer, "BB");

				AccionVsPlayer acply = new AccionVsPlayer();
				acply.setIzqQA1(izq.get(0));
				acply.setIzqQA2(izq.get(1));
				acply.setIzqQA3(izq.get(2));

				acply.setDerQA1(der.get(0));
				acply.setDerQA2(der.get(1));
				acply.setDerQA3(der.get(2));

				accVsPly.put(tipplayer, acply);
			}

			try {
				String accIzqDef = (accVsPly.get("DEF").getIzqQA1() != null
						&& accVsPly.get("DEF").getIzqQA1().isEmpty()) ? accVsPly.get("DEF").getIzqQA3()
								: accVsPly.get("DEF").getIzqQA1();

				accionInfoDto.setDefAccion(accIzqDef.substring(0, 1));
			} catch (Exception e) {
			}
		}
		if (handInfoDto.numJugadores() == 3 && poshero == "BB") {
			accionInfoDto.setIzqVsEffStack(stackEff[2]);
			accionInfoDto.setIzqVsPlayer("BU");
			accionInfoDto.setDerVsEffStack(stackEff[0]);
			accionInfoDto.setDerVsPlayer("SB");

			for (String tipplayer : tiposPly) {
				List<String> izq = obtenerAccionVsPlayer(acciones, accionesMix, tipplayer, "BU");
				List<String> der = obtenerAccionVsPlayer(acciones, accionesMix, tipplayer, "SB");

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
				List<String> lado = obtenerAccionVsPlayer(acciones, accionesMix, tipplayer, "BB");

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

			try {
				String accIzqDef = (accVsPly.get("DEF").getIzqQA1() != null
						&& accVsPly.get("DEF").getIzqQA1().isEmpty()) ? accVsPly.get("DEF").getIzqQA3()
								: accVsPly.get("DEF").getIzqQA1();
				String accDerDef = (accVsPly.get("DEF").getDerQA1() != null
						&& accVsPly.get("DEF").getDerQA1().isEmpty()) ? accVsPly.get("DEF").getDerQA3()
								: accVsPly.get("DEF").getDerQA1();

				if (handInfoDto.getIsActivo()[0]) {
					accionInfoDto.setDefAccion(accDerDef.substring(0, 1));
				} else {
					accionInfoDto.setDefAccion(accIzqDef.substring(0, 1));
				}
			} catch (Exception e) {
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
				List<String> lado = obtenerAccionVsPlayer(acciones, accionesMix, tipplayer, "SB");

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

		}

		accionInfoDto.setAccionVsPlayer(accVsPly);

		return accionInfoDto;
	}

	private List<String> obtenerAccionVsPlayer(Map<String, List<TbRangeSttgy>> acciones,
			Map<String, List<TbRangeSttgy>> accionesMix, String tipplayer, String posi) {
		List<String> uoPF = new ArrayList<>();
		List<TbRangeSttgy> accs = acciones.get(posi);

		String QA1 = "";
		String QA2 = "";
		String QA3 = "";
		for (TbRangeSttgy range : accs) {
			if (range.getVrSttgyTipoaccion().trim().equals("UO") && range.getVrSttgyTipjug().equals(tipplayer)) {
				QA1 = QA1 + range.getVrSttgyAccion();
				if (range.getVrSttgyRange().contains("[")) {
					Matcher m = Pattern.compile("\\[(.*?)\\]").matcher(range.getVrSttgyRange());
					while (m.find()) {
						String[] probs = m.group(1).split("/");
						int min = Integer.valueOf(probs[0].trim());
						int max = Integer.valueOf(probs[1].trim());
						QA1 = QA1 + " (" + (max - min) + ")";
					}
				}
			}
			if (range.getVrSttgyTipoaccion().trim().equals("L") && range.getVrSttgyTipjug().equals(tipplayer)) {
				QA2 = QA2 + range.getVrSttgyAccion();
				if (range.getVrSttgyRange().contains("[")) {
					Matcher m = Pattern.compile("\\[(.*?)\\]").matcher(range.getVrSttgyRange());
					while (m.find()) {
						String[] probs = m.group(1).split("/");
						int min = Integer.valueOf(probs[0].trim());
						int max = Integer.valueOf(probs[1].trim());
						QA2 = QA2 + " (" + (max - min) + ")";
					}
				}
			}
			if (range.getVrSttgyTipoaccion().trim().equals("S") && range.getVrSttgyTipjug().equals(tipplayer)) {
				QA3 = QA3 + range.getVrSttgyAccion();
				if (range.getVrSttgyRange().contains("[")) {
					Matcher m = Pattern.compile("\\[(.*?)\\]").matcher(range.getVrSttgyRange());
					while (m.find()) {
						String[] probs = m.group(1).split("/");
						int min = Integer.valueOf(probs[0].trim());
						int max = Integer.valueOf(probs[1].trim());
						QA3 = QA3 + " (" + (max - min) + ")";
					}
				}
			}

		}

		String QA1mix = "";
		String QA2mix = "";
		String QA3mix = "";
		if (accionesMix.size() > 0) {
			List<TbRangeSttgy> accsMix = accionesMix.get(posi);
			for (TbRangeSttgy range : accsMix) {
				if (range.getVrSttgyTipoaccion().trim().equals("UO") && range.getVrSttgyTipjug().equals(tipplayer)) {
					QA1mix = QA1mix + " | " + range.getVrSttgyAccion();
				}
				if (range.getVrSttgyTipoaccion().trim().equals("L") && range.getVrSttgyTipjug().equals(tipplayer)) {
					QA2mix = QA2mix + " | " + range.getVrSttgyAccion();
				}
				if (range.getVrSttgyTipoaccion().trim().equals("S") && range.getVrSttgyTipjug().equals(tipplayer)) {
					QA3mix = QA3mix + " | " + range.getVrSttgyAccion();
				}
			}
			if (!QA1mix.isEmpty()) {
				QA1 = QA1 + " >>> " + QA1mix;
			}
			if (!QA2mix.isEmpty()) {
				QA2 = QA2 + " >>> " + QA2mix;
			}
			if (!QA3mix.isEmpty()) {
				QA3 = QA3 + " >>> " + QA3mix;
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

	/**
	 * almacena en memoria los ultimos 16 hands analizadas para no volverlas a
	 * analizar nuevamente
	 * 
	 * @param bufferManos
	 * @param handInfoDto
	 * @return
	 * @throws Exception
	 */
	private AccionInfoDto almacenarLectura(HandInfoDto handInfoDto) throws Exception {

		// si la mano esta en el buffer de manos quiere dicir que ya fue analizada y
		// retorno la accion analizada
		for (int i = 0; i < bufferManosAnalizadas.size(); i++) {
			HandInfoDto h = bufferManosAnalizadas.get(i);
			try {
				boolean isIgual = HandInfoDto.equalsHand(handInfoDto, h);
				if (isIgual) {
					return bufferAccionesAnalizadas.get(i);
				}
			} catch (Exception e) {
				log.error("error al tratar de identificar si la mano ha sido analizada previamente");
				throw new Exception("error al tratar de identificar si la mano ha sido analizada previamente");
			}
		}

		// borramos la primera imagen del buffer
		if (bufferManosAnalizadas.size() > 16) {
			bufferManosAnalizadas.remove(0);
			bufferAccionesAnalizadas.remove(0);
		}

		return null;

	}

	public void borrarCache() {
		bufferManosAnalizadas = new ArrayList<>();
		bufferAccionesAnalizadas = new ArrayList<>();		
	}

}
