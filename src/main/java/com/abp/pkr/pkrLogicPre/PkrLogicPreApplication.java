package com.abp.pkr.pkrLogicPre;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.orm.jpa.vendor.HibernateJpaSessionFactoryBean;

@SpringBootApplication
public class PkrLogicPreApplication {

	public static void main(String[] args) {
		SpringApplication.run(PkrLogicPreApplication.class, args);
	}
	
	/**
	 * Retorna un sessionfactory (ver applicatio.properties) es un metodo de configuracion
	 *@author abpubuntu
	 *@date May 6, 2017
	 * @return
	 */
    @Bean
    public HibernateJpaSessionFactoryBean sessionFactory() {
        return new HibernateJpaSessionFactoryBean();
    }
}
