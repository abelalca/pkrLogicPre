package com.abp.pkr.pkrLogicPre.dto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.abp.pkr.pkrLogicPre.ngc.ProcesarHandNgcImpl;

@Controller
public class RangeTransform {

	@Autowired
	ProcesarHandNgcImpl procesarHandNgcImpl;
//	ProcesarHandNgcImpl procesarHandNgcImpl = new ProcesarHandNgcImpl();

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
//	
//	public static void main(String... args) {
//		RangeTransform ran = new RangeTransform();
////		JJ+, 88, 66-33, A6s-A2s, K9s, Q8s, J7s, T5s+, A8o+, KTo-K7o, J7o+, 98o, 87o, 76o, 65o, 54o
//		boolean b= false;
//		b =ran.isHandInRange("JJ+, 88, 66-33, A6s-A2s, K9s, Q8s, J7s, T5s+, A8o+, KTo-K7o, J7o+, 98o, 87o, 76o, 65o, 54o", "AdAs");
//		System.out.println(b);
//		
//		b =ran.isHandInRange("JJ+, 88, 66-33, A6s-A2s, K9s, Q8s, J7s, T5s+, A8o+, KTo-K7o, J7o+, 98o, 87o, 76o, 65o, 54o", "4d4s");
//		System.out.println(b);
//		
//		b =ran.isHandInRange("JJ+, 88, 66-33, A6s-A2s, K9s, Q8s, J7s, T5s+, A8o+, KTo-K7o, J7o+, 98o, 87o, 76o, 65o, 54o", "8d8s");
//		System.out.println(b);
//		
//		b =ran.isHandInRange("JJ+, 88, 66-33, A6s-A2s, K9s, Q8s, J7s, T5s+, A8o+, KTo-K7o, J7o+, 98o, 87o, 76o, 65o, 54o", "JdJs");
//		System.out.println(b);
//		
//		b =ran.isHandInRange("JJ+, 88, 66-33, A6s-A2s, K9s, Q8s, J7s, T5s+, A8o+, KTo-K7o, J7o+, 98o, 87o, 76o, 65o, 54o", "KdKs");
//		System.out.println(b);
//		
//		b =ran.isHandInRange("JJ+, 88, 66-33, A6s-A2s, K9s, Q8s, J7s, T5s+, A8o+, KTo-K7o, J7o+, 98o, 87o, 76o, 65o, 54o", "As6s");
//		System.out.println(b);
//		
//		b =ran.isHandInRange("JJ+, 88, 66-33, A6s-A2s, K9s, Q8s, J7s, T5s+, A8o+, KTo-K7o, J7o+, 98o, 87o, 76o, 65o, 54o", "Kd7s");
//		System.out.println(b);
//		
//		b =ran.isHandInRange("JJ+, 88, 66-33, A6s-A2s, K9s, Q8s, J7s, T5s+, A8o+, KTo-K7o, J7o+, 98o, 87o, 76o, 65o, 54o", "Ad4d");
//		System.out.println(b);
//		
//		b =ran.isHandInRange("JJ+, 88, 66-33, A6s-A2s, K9s, Q8s, J7s, T5s+, A8o+, KTo-K7o, J7o+, 98o, 87o, 76o, 65o, 54o", "Kd9s");
//		System.out.println(b);
//		
//		b =ran.isHandInRange("JJ+, 88, 66-33, A6s-A2s, K9s, Q8s, J7s, T5s+, A8o+, KTo-K7o, J7o+, 98o, 87o, 76o, 65o, 54o", "Jd7d");
//		System.out.println(b);
//		
//		b =ran.isHandInRange("JJ+, 88, 66-33, A6s-A2s, K9s, Q8s, J7s, T5s+, A8o+, KTo-K7o, J7o+, 98o, 87o, 76o, 65o, 54o", "6d5s");
//		System.out.println(b);
//		
//		b =ran.isHandInRange("JJ+, 88, 66-33, A6s-A2s, K9s, Q8s, J7s, T5s+, A8o+, KTo-K7o, J7o+, 98o, 87o, 76o, 65o, 54o", "As8d");
//		System.out.println(b);
//		
//		b =ran.isHandInRange("JJ+, 88, 66-33, A6s-A2s, K9s, Q8s, J7s, T5s+, A8o+, KTo-K7o, J7o+, 98o, 87o, 76o, 65o, 54o", "AsKd");
//		System.out.println(b);
//		
//		b =ran.isHandInRange("JJ+, 88, 66-33, A6s-A2s, K9s, Q8s, J7s, T5s+, A8o+, KTo-K7o, J7o+, 98o, 87o, 76o, 65o, 54o", "Ts6s");
//		System.out.println(b);
//		
//		b =ran.isHandInRange("JJ+, 88, 66-33, A6s-A2s, K9s, Q8s, J7s, T5s+, A8o+, KTo-K7o, J7o+, 98o, 87o, 76o, 65o, 54o", "Ts5s");
//		System.out.println(b);
//		
//		
//
//		
//		System.out.println("--------");
//		b =ran.isHandInRange("JJ+, 88, 66-33, A6s-A2s, K9s, Q8s, J7s, T5s+, A8o+, KTo-K7o, J7o+, 98o, 87o, 76o, 65o, 54o", "9d9s");
//		System.out.println(b);
//	
//		b =ran.isHandInRange("JJ+, 88, 66-33, A6s-A2s, K9s, Q8s, J7s, T5s+, A8o+, KTo-K7o, J7o+, 98o, 87o, 76o, 65o, 54o", "As7s");
//		System.out.println(b);
//		
//		b =ran.isHandInRange("JJ+, 88, 66-33, A6s-A2s, K9s, Q8s, J7s, T5s+, A8o+, KTo-K7o, J7o+, 98o, 87o, 76o, 65o, 54o", "KsTs");
//		System.out.println(b);
//		
//		b =ran.isHandInRange("JJ+, 88, 66-33, A6s-A2s, K9s, Q8s, J7s, T5s+, A8o+, KTo-K7o, J7o+, 98o, 87o, 76o, 65o, 54o", "As7d");
//		System.out.println(b);
//		
//		b =ran.isHandInRange("JJ+, 88, 66-33, A6s-A2s, K9s, Q8s, J7s, T5s+, A8o+, KTo-K7o, J7o+, 98o, 87o, 76o, 65o, 54o", "As7s");
//		System.out.println(b);
//		
//		b =ran.isHandInRange("JJ+, 88, 66-33, A6s-A2s, K9s, Q8s, J7s, T5s+, A8o+, KTo-K7o, J7o+, 98o, 87o, 76o, 65o, 54o", "KsJd");
//		System.out.println(b);
//		
//		b =ran.isHandInRange("JJ+, 88, 66-33, A6s-A2s, K9s, Q8s, J7s, T5s+, A8o+, KTo-K7o, J7o+, 98o, 87o, 76o, 65o, 54o", "Ks6d");
//		System.out.println(b);
//		
//		b =ran.isHandInRange("JJ+, 88, 66-33, A6s-A2s, K9s, Q8s, J7s, T5s+, A8o+, KTo-K7o, J7o+, 98o, 87o, 76o, 65o, 54o", "Js6d");
//		System.out.println(b);
//		
//		b =ran.isHandInRange("JJ+, 88, 66-33, A6s-A2s, K9s, Q8s, J7s, T5s+, A8o+, KTo-K7o, J7o+, 98o, 87o, 76o, 65o, 54o", "2s2d");
//		System.out.println(b);
//		
//		
//		
//	}
//
//	
	
	public boolean isHandInRange(String range, String mano) {
//		String mano = procesarHandNgcImpl.ordenarHand(hand);

		String[] splitRange = range.split(",");
		boolean isInRange = false;

		for (int i = 0; i < splitRange.length; i++) {
			splitRange[i] = splitRange[i].trim();
		}

		for (String ran : splitRange) {
			if (ran.contains(mano)) {
				isInRange = true;
				return isInRange;
			}

			if (ran.contains("+")) {
				List<String> cartas = new ArrayList<>();
				cartas.add(ran.substring(0, 1));
				cartas.add(ran.substring(1, 2));
				if (ran.length() == 4) {
					cartas.add(ran.substring(2, 3));
				}

				if ( cartas.get(0).equals(mano.substring(0, 1))) {
					Integer limiteInf = Integer.valueOf(mapaCartas.get(cartas.get(1)));
					Integer limiteSup = Integer.valueOf(mapaCartas.get(cartas.get(0))) - 1;
					Integer carta2 = Integer.valueOf(mapaCartas.get(mano.substring(1, 2)));

					if (limiteInf <= carta2 && limiteSup >= carta2) {
						if (mano.length() == 3 && mano.substring(2, 3).equals(cartas.get(2))) {
							isInRange = true;
							return isInRange;
						}
						if (mano.length() == 2) {
							isInRange = true;
							return isInRange;
						}
					}
				}
				
				if (ran.length()==3 && mano.substring(0,1).equals(mano.substring(1,2))) {
					cartas.add(ran.substring(0, 1));
					cartas.add(ran.substring(1, 2));
					Integer limiteInf = Integer.valueOf(mapaCartas.get(cartas.get(1)));
					Integer limiteSup = 13;
					Integer carta2 = Integer.valueOf(mapaCartas.get(mano.substring(1, 2)));
					if (limiteInf <= carta2 && limiteSup >= carta2) {
						isInRange = true;
						return isInRange;						
					}
					
				}

			}

			if (ran.contains("-")) {
				if ( mano.substring(0, 1).equals(ran.substring(0, 1)) || (ran.length()==5 && mano.substring(0,1).equals(mano.substring(1,2)))) {
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
							isInRange = true;
							return isInRange;
						}
						if (cartas.size() == 2) {
							isInRange = true;
							return isInRange;
						}
					}

				}

			}

		}

		return isInRange;
	}

}
