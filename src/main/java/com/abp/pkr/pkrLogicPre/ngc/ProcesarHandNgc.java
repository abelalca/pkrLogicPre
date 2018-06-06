package com.abp.pkr.pkrLogicPre.ngc;

import com.abp.pkr.pkrLogicPre.dto.HandInfoDto;

public interface ProcesarHandNgc {
	
	/**
	 * Metodo llamado por el WS que procesa info de una mano y la almacena en BD o archivo
	 *@author abpubuntu
	 *@date Jun 6, 2018
	 * @param handInfoDto
	 * @throws Exception
	 */
	public void procesarHand(HandInfoDto handInfoDto) throws Exception;	

}
