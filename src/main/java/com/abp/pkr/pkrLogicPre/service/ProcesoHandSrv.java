package com.abp.pkr.pkrLogicPre.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.abp.pkr.pkrLogicPre.dto.AccionInfoDto;
import com.abp.pkr.pkrLogicPre.dto.HandInfoDto;
import com.abp.pkr.pkrLogicPre.ngc.ProcesarHandNgcImpl;


@RestController
@RequestMapping("/proceso")
public class ProcesoHandSrv {

	@Autowired
	ProcesarHandNgcImpl procesarHandNgc;
	
	/**
	 * Recibe info de una mano, procesa y almacena el resultado en BD o un archivo
	 *@author abpubuntu
	 *@date May 6, 2017
	 * @param handInfoDto
	 * @throws Exception 
	 */
	@GetMapping(value="/procesarHand")
	public AccionInfoDto procesarHand(HandInfoDto handInfoDto) throws Exception{
		return procesarHandNgc.procesarHand(handInfoDto);		
	}
	
	@GetMapping(value="/procesarHandPrueba")
	public AccionInfoDto procesarHandPrueba() throws Exception{
		HandInfoDto h = new HandInfoDto();
		h.setBtnPos(1);
		h.setHand("AA");
		h.setIsActivo(new boolean[]{true,true,true});
		h.setNumjug(3);
		h.setPosHero(2);
		h.setPosicionHero("BU");
		h.setSillaHero(1);
		h.setStacksBb(new Double[] {14.0,15.0,16.0});
		
		
		return procesarHandNgc.procesarHand(h);		
	}
	
}
