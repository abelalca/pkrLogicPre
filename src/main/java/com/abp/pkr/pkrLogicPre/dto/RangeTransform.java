package com.abp.pkr.pkrLogicPre.dto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.abp.pkr.pkrLogicPre.ngc.ProcesarHandNgcImpl;

import ch.qos.logback.classic.Logger;

@Controller
public class RangeTransform {
	
	private static final Logger log = (Logger) LoggerFactory.getLogger(RangeTransform.class);

	@Autowired
	ProcesarHandNgcImpl procesarHandNgcImpl;
	// ProcesarHandNgcImpl procesarHandNgcImpl = new ProcesarHandNgcImpl();

	private Map<String, Integer> mapaCartas = null;

	public RangeTransform() {
		mapaCartas = new HashMap<>();
		mapaCartas.put("A", 13);
		mapaCartas.put("K", 12);
		mapaCartas.put("Q", 11);
		mapaCartas.put("J", 10);
		mapaCartas.put("T", 9);
		mapaCartas.put("9", 8);
		mapaCartas.put("8", 7);
		mapaCartas.put("7", 6);
		mapaCartas.put("6", 5);
		mapaCartas.put("5", 4);
		mapaCartas.put("4", 3);
		mapaCartas.put("3", 2);
		mapaCartas.put("2", 1);
	}

	public String transformRangeToAllCards(String range) {
		return null;
	}

	// public static void main(String... args) {
	// RangeTransform ran = new RangeTransform();
	//// JJ+, 88, 66-33, A6s-A2s, K9s, Q8s, J7s, T5s+, A8o+, KTo-K7o, J7o+, 98o,
	// 87o, 76o, 65o, 54o
	// boolean b= false;
	// int random = 50;
	//
	//
	//
	// b =ran.isHandInRange("JJ+, 88, 66-33, A6s-A2s, K9s, Q8s, J7s, T5s+, A8o+,
	// KTo-K7o, J7o+, 98o, 87o, 76o, 65o, 54o", "AA",random);
	// System.out.println(b);
	//
	// b =ran.isHandInRange("88 [40/60], 99 [50/60], AKo[40/50]", "AKo",random);
	// System.out.println(b);
	// b =ran.isHandInRange("88 [40/90],", "88",random);
	// System.out.println(b);
	//
	// b =ran.isHandInRange("JJ+, 88, 66-33, A6s-A2s, K9s, Q8s, J7s, T5s+, A8o+,
	// KTo-K7o, J7o+, 98o, 87o, 76o, 65o, 54o", "44",random);
	// System.out.println(b);
	//
	// b =ran.isHandInRange("JJ+, 88, 66-33, A6s-A2s, K9s, Q8s, J7s, T5s+, A8o+,
	// KTo-K7o, J7o+, 98o, 87o, 76o, 65o, 54o", "88",random);
	// System.out.println(b);
	//
	// b =ran.isHandInRange("JJ+, 88, 66-33, A6s-A2s, K9s, Q8s, J7s, T5s+, A8o+,
	// KTo-K7o, J7o+, 98o, 87o, 76o, 65o, 54o", "JJ",random);
	// System.out.println(b);
	//
	// b =ran.isHandInRange("JJ+, 88, 66-33, A6s-A2s, K9s, Q8s, J7s, T5s+, A8o+,
	// KTo-K7o, J7o+, 98o, 87o, 76o, 65o, 54o", "KK",random);
	// System.out.println(b);
	//
	// b =ran.isHandInRange("JJ+, 88, 66-33, A6s-A2s, K9s, Q8s, J7s, T5s+, A8o+,
	// KTo-K7o, J7o+, 98o, 87o, 76o, 65o, 54o", "A6s",random);
	// System.out.println(b);
	//
	// b =ran.isHandInRange("JJ+, 88, 66-33, A6s-A2s, K9s, Q8s, J7s, T5s+, A8o+,
	// KTo-K7o, J7o+, 98o, 87o, 76o, 65o, 54o", "K7o",random);
	// System.out.println(b);
	//
	// b =ran.isHandInRange("JJ+, 88, 66-33, A6s-A2s, K9s, Q8s, J7s, T5s+, A8o+,
	// KTo-K7o, J7o+, 98o, 87o, 76o, 65o, 54o", "A4s",random);
	// System.out.println(b);
	//
	// b =ran.isHandInRange("JJ+, 88, 66-33, A6s-A2s, K9s, Q8s, J7s, T5s+, A8o+,
	// KTo-K7o, J7o+, 98o, 87o, 76o, 65o, 54o", "K9o",random);
	// System.out.println(b);
	//
	// b =ran.isHandInRange("JJ+, 88, 66-33, A6s-A2s, K9s, Q8s, J7s, T5s+, A8o+,
	// KTo-K7o, J7o+, 98o, 87o, 76o, 65o, 54o", "J7s",random);
	// System.out.println(b);
	//
	// b =ran.isHandInRange("JJ+, 88, 66-33, A6s-A2s, K9s, Q8s, J7s, T5s+, A8o+,
	// KTo-K7o, J7o+, 98o, 87o, 76o, 65o, 54o", "65o",random);
	// System.out.println(b);
	//
	// b =ran.isHandInRange("JJ+, 88, 66-33, A6s-A2s, K9s, Q8s, J7s, T5s+, A8o+,
	// KTo-K7o, J7o+, 98o, 87o, 76o, 65o, 54o", "A8o",random);
	// System.out.println(b);
	//
	// b =ran.isHandInRange("JJ+, 88, 66-33, A6s-A2s, K9s, Q8s, J7s, T5s+, A8o+,
	// KTo-K7o, J7o+, 98o, 87o, 76o, 65o, 54o", "AKo",random);
	// System.out.println(b);
	//
	// b =ran.isHandInRange("JJ+, 88, 66-33, A6s-A2s, K9s, Q8s, J7s, T5s+, A8o+,
	// KTo-K7o, J7o+, 98o, 87o, 76o, 65o, 54o", "T6s",random);
	// System.out.println(b);
	//
	// b =ran.isHandInRange("JJ+, 88, 66-33, A6s-A2s, K9s, Q8s, J7s, T5s+, A8o+,
	// KTo-K7o, J7o+, 98o, 87o, 76o, 65o, 54o", "T5s",random);
	// System.out.println(b);
	//
	//
	//
	//
	// System.out.println("--------");
	// b =ran.isHandInRange("JJ+, 88, 66-33, A6s-A2s, K9s, Q8s, J7s, T5s+, A8o+,
	// KTo-K7o, J7o+, 98o, 87o, 76o, 65o, 54o", "99",random);
	// System.out.println(b);
	//
	// b =ran.isHandInRange("JJ+, 88, 66-33, A6s-A2s, K9s, Q8s, J7s, T5s+, A8o+,
	// KTo-K7o, J7o+, 98o, 87o, 76o, 65o, 54o", "A7s",random);
	// System.out.println(b);
	//
	// b =ran.isHandInRange("JJ+, 88, 66-33, A6s-A2s, K9s, Q8s, J7s, T5s+, A8o+,
	// KTo-K7o, J7o+, 98o, 87o, 76o, 65o, 54o", "KTs",random);
	// System.out.println(b);
	//
	// b =ran.isHandInRange("JJ+, 88, 66-33, A6s-A2s, K9s, Q8s, J7s, T5s+, A8o+,
	// KTo-K7o, J7o+, 98o, 87o, 76o, 65o, 54o", "A7o",random);
	// System.out.println(b);
	//
	// b =ran.isHandInRange("JJ+, 88, 66-33, A6s-A2s, K9s, Q8s, J7s, T5s+, A8o+,
	// KTo-K7o, J7o+, 98o, 87o, 76o, 65o, 54o", "A7s",random);
	// System.out.println(b);
	//
	// b =ran.isHandInRange("JJ+, 88, 66-33, A6s-A2s, K9s, Q8s, J7s, T5s+, A8o+,
	// KTo-K7o, J7o+, 98o, 87o, 76o, 65o, 54o", "KJo",random);
	// System.out.println(b);
	//
	// b =ran.isHandInRange("JJ+, 88, 66-33, A6s-A2s, K9s, Q8s, J7s, T5s+, A8o+,
	// KTo-K7o, J7o+, 98o, 87o, 76o, 65o, 54o", "K6o",random);
	// System.out.println(b);
	//
	// b =ran.isHandInRange("JJ+, 88, 66-33, A6s-A2s, K9s, Q8s, J7s, T5s+, A8o+,
	// KTo-K7o, J7o+, 98o, 87o, 76o, 65o, 54o", "J6o",random);
	// System.out.println(b);
	//
	// b =ran.isHandInRange("JJ+, 88, 66-33, A6s-A2s, K9s, Q8s, J7s, T5s+, A8o+,
	// KTo-K7o, J7o+, 98o, 87o, 76o, 65o, 54o", "22",random);
	// System.out.println(b);
	//
	// b =ran.isHandInRange("88 [50/60],", "88",random);
	// System.out.println(b);
	// b =ran.isHandInRange("88 [60/90],", "88",random);
	// System.out.println(b);
	// b =ran.isHandInRange("88 [40/60], 99 [50/60], AKo [40/50]", "99",random);
	// System.out.println(b);
	// b =ran.isHandInRange("88 [40/60], 99 [50/60], AKo [40/50]", "87o",random);
	// System.out.println(b);
	//
	// b =ran.isHandInRange("JJ-22, A2s+, A2o+, KTs+, KJo+, QJs", "JTo", random);
	// System.out.println(b);
	//
	// }

	public int isHandInRange(String range, String mano, int random) {

		String[] splitRange = range.split(",");
		int isInRange = -1;

		for (int i = 0; i < splitRange.length; i++) {
			splitRange[i] = splitRange[i].trim();
		}

		for (String ran : splitRange) {
			if (ran.startsWith(mano)) {				
				Matcher m = Pattern.compile("\\[(.*?)\\]").matcher(ran);
				while (m.find()) {
					String[] probs = m.group(1).split("/");
					if (random > Integer.valueOf(probs[0].trim()) && random <= Integer.valueOf(probs[1].trim())) {
						return 1;
					} else {
						if (random == 0 && Integer.valueOf(probs[0].trim()) == 0) {
							return 1;
						}
						return 0;
					}
				}

				isInRange = 1;
				return isInRange;
			}

			if (ran.contains("+")) {
				List<String> cartas = new ArrayList<>();
				cartas.add(ran.substring(0, 1));
				cartas.add(ran.substring(1, 2));
				if (ran.length() == 4) {
					cartas.add(ran.substring(2, 3));
				}

				if (cartas.get(0).equals(mano.substring(0, 1))) {
					Integer limiteInf = Integer.valueOf(mapaCartas.get(cartas.get(1)));
					Integer limiteSup = Integer.valueOf(mapaCartas.get(cartas.get(0))) - 1;
					Integer carta2 = Integer.valueOf(mapaCartas.get(mano.substring(1, 2)));

					if (limiteInf <= carta2 && limiteSup >= carta2) {
						if (mano.length() == 3 && mano.substring(2, 3).equals(cartas.get(2))) {
							isInRange = 1;
							return isInRange;
						}
						if (mano.length() == 2) {
							isInRange = 1;
							return isInRange;
						}
					}
				}

				if (ran.length() == 3 && mano.substring(0, 1).equals(mano.substring(1, 2))) {
					cartas.add(ran.substring(0, 1));
					cartas.add(ran.substring(1, 2));
					Integer limiteInf = Integer.valueOf(mapaCartas.get(cartas.get(1)));
					Integer limiteSup = 13;
					Integer carta2 = Integer.valueOf(mapaCartas.get(mano.substring(1, 2)));
					if (limiteInf <= carta2 && limiteSup >= carta2) {
						isInRange = 1;
						return isInRange;
					}

				}

			}

			if (ran.contains("-")) {
				if ((mano.substring(0, 1).equals(ran.substring(0, 1)) && ran.length() == 7)
						|| (ran.length() == 5 && mano.substring(0, 1).equals(mano.substring(1, 2)))) {
					List<String> cartas = new ArrayList<>();
					if (ran.length() == 5) {
						cartas.add(ran.substring(1, 2));
						cartas.add(ran.substring(3, 4));
					}
					if (ran.length() == 7) {
						cartas.add(ran.substring(1, 2));
						cartas.add(ran.substring(5, 6));
						cartas.add(ran.substring(2, 3));
					}

					Integer limiteInf = Integer.valueOf(mapaCartas.get(cartas.get(1)));
					Integer limiteSup = Integer.valueOf(mapaCartas.get(cartas.get(0)));
					Integer carta2 = Integer.valueOf(mapaCartas.get(mano.substring(1, 2)));

					if (limiteInf <= carta2 && limiteSup >= carta2) {
						if (cartas.size() == 3 && cartas.get(2).equals(mano.substring(2, 3))) {
							isInRange = 1;
							return isInRange;
						}
						if (cartas.size() == 2) {
							isInRange = 1;
							return isInRange;
						}
					}

				}

			}

		}

		return isInRange;
	}

}
