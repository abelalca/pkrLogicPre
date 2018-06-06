//package com.abp.pkr.pkrLogicPre.dao;
//
//import java.lang.reflect.Field;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//
//import javax.persistence.Id;
//import javax.transaction.Transactional;
//
//import org.hibernate.Criteria;
//import org.hibernate.Session;
//import org.hibernate.SessionFactory;
//import org.hibernate.criterion.Restrictions;
//import org.reflections.Reflections;
//import org.reflections.scanners.FieldAnnotationsScanner;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Repository;
//
//@Repository
//@Transactional
//public class GenericDao {
//
//	@Autowired
//	private SessionFactory sessionFactory;
//
//	public SessionFactory getSessionFactory() {
//		return sessionFactory;
//	}
//
//	public void setSessionFactory(SessionFactory sessionFactory) {
//		this.sessionFactory = sessionFactory;
//	}
//
//	@SuppressWarnings({ "unchecked", "rawtypes" })
//	public List<Object> listAll(Class clazz) throws Exception{
//		Session session = sessionFactory.getCurrentSession();
//		Criteria criteria = session.createCriteria(clazz);
//		List<Object> lista = criteria.list();
//		return lista;
//	}
//
//	@SuppressWarnings("unchecked")
//	public List<Object> listAll(Class clazz, int maxResults) throws Exception{
//		Session session = sessionFactory.getCurrentSession();
//		Criteria criteria = session.createCriteria(clazz);
//		criteria.setMaxResults(maxResults);
//		List<Object> lista = criteria.list();
//		return lista;
//	}
//
//	@SuppressWarnings({ "unchecked", "rawtypes" })
//	public Object find(Class clazz, int id) throws Exception {
//		Object obj = null;
//
//		Reflections r = new Reflections(clazz, new FieldAnnotationsScanner());
//		Set<Field> fields = r.getFieldsAnnotatedWith(Id.class);
//		if (fields.size() == 1) {
//			Iterator iter = fields.iterator();
//			String itercampo= iter.next().toString();
//			String[] arr= itercampo.split("\\.");
//			String campo= arr[arr.length-1];
//			Session session = sessionFactory.getCurrentSession();
//			Criteria criteria = session.createCriteria(clazz);
//			criteria.add(Restrictions.eq(campo, id));
//			obj = criteria.list().get(0);
//		}
//
//		return obj;
//	}
//	
//	@SuppressWarnings("unchecked")
//	public List<Object> findByColumns(Class clazz, Map<String, Object> argsMap) throws Exception{
//		Session session = sessionFactory.getCurrentSession();
//		Criteria criteria = session.createCriteria(clazz);
//		criteria.add(Restrictions.allEq(argsMap));
//		List<Object> lista = criteria.list();
//		return lista;
//	}
//
//}
