package com.abp.pkr.pkrLogicPre.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
	@PostMapping(value="/procesarHand")
	public AccionInfoDto procesarHand(@RequestBody HandInfoDto handInfoDto) throws Exception{
		return procesarHandNgc.procesarHand(handInfoDto);		
	}	
	
}
