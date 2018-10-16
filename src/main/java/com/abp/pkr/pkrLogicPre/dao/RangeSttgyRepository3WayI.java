package com.abp.pkr.pkrLogicPre.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.abp.pkr.pkrLogicPre.db.TbRangeSttgy;
import com.abp.pkr.pkrLogicPre.db.TbRangeSttgy3Way;


// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete
public interface RangeSttgyRepository3WayI extends CrudRepository<TbRangeSttgy3Way, Long> {
	
	
	List<TbRangeSttgy3Way> findByAndVrAutUsuarioAndVrSttgyStrategyAndNbSttgyStackminLessThanAndNbSttgyStackmaxGreaterThanEqual(String vrAutUsuario, String vrSttgyStrategy, Double NbSttgyStackmin, Double NbSttgyStackmax);


}