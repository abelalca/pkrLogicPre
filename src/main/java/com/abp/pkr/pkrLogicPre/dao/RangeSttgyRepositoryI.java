package com.abp.pkr.pkrLogicPre.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.abp.pkr.pkrLogicPre.db.TbRangeSttgy;


// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete
public interface RangeSttgyRepositoryI extends CrudRepository<TbRangeSttgy, Long> {
	
	
	List<TbRangeSttgy> findByNbSttgyNumjugAndVrAutUsuarioAndVrSttgyStrategyAndVrSttgyPosheroAndVrSttgyVsPlayerAndVrSttgyRangeContainingAndNbSttgyStackminLessThanEqualAndNbSttgyStackmaxGreaterThan(Integer NbSttgyNumjug, String vrAutUsuario, String vrSttgyStrategy, String vrSttgyPoshero, String vrSttgyVsPlayer, String hand, Double NbSttgyStackmin, Double NbSttgyStackmax);
//	
	List<TbRangeSttgy> findByNbSttgyNumjugAndVrAutUsuarioAndVrSttgyStrategyAndVrSttgyPosheroAndVrSttgyVsPlayerAndNbSttgyStackminLessThanEqualAndNbSttgyStackmaxGreaterThan(Integer NbSttgyNumjug, String vrAutUsuario, String vrSttgyStrategy, String vrSttgyPoshero, String vrSttgyVsPlayer, Double NbSttgyStackmin, Double NbSttgyStackmax);
	

}