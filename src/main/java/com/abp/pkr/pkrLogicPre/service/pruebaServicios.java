package com.abp.pkr.pkrLogicPre.service;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class pruebaServicios {
	
	
	@RequestMapping("/test")
	public String pruebaTest(){
		return "Hola Mundo Servicios";
	}

}
