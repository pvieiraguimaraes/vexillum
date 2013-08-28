package br.com.vexillum.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.collection.internal.PersistentBag;
import org.hibernate.collection.internal.PersistentList;
import org.hibernate.proxy.HibernateProxy;

import br.com.vexillum.control.manager.ExceptionManager;
import br.com.vexillum.model.ICommonEntity;

public class HibernateUtils {
	
	@SuppressWarnings("unchecked")
	public static <T> T materializeProxy(T entity) {
	    if (entity == null) {
	        throw new 
	           NullPointerException("Entity passed for initialization is null");
	    }

	    Hibernate.initialize(entity);
	    if (entity instanceof HibernateProxy) {
	        entity = (T) ((HibernateProxy) entity).getHibernateLazyInitializer().getImplementation();
	    }
	    return entity;
	}
	
	@SuppressWarnings("rawtypes")
	public static void tranformBagsOnLists(Object o) throws Exception{
		for(Field f : ReflectionUtils.getFields(o.getClass())){
			f.setAccessible(true);
			if(f.get(o) instanceof PersistentBag || f.get(o) instanceof PersistentList){
				f.set(o, transaformBagInList((List) f.get(o)));
			}
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List transaformBagInList(List bag){
		List list = new ArrayList();
		if(bag instanceof PersistentBag){
			for(Object o : bag){
				list.add(o);
			}
		} else {
			list = bag;
		}
		
		return list;
	}
	
	public static void detachObject(Object o, Session session){
		try {
			o = materializeProxy(o);
			initializeLists(o);
			session.evict(o);
		} catch (Exception e) {
			new ExceptionManager(e).treatException();
		}
	}
	
	@SuppressWarnings("rawtypes")
	public static void initializeLists(Object o) throws Exception{
		for(Field f : ReflectionUtils.getFields(o.getClass())){
			f.setAccessible(true);
			Object obj = f.get(o);
			if(obj instanceof ICommonEntity){
				initializeLists(obj);
			} else if(obj instanceof PersistentBag || obj instanceof PersistentList){
//				Hibernate.initialize(obj);
				initializeListElements((List) obj);
			}
		}
	}
	
	@SuppressWarnings("rawtypes")
	public static void initializeListElements(List list){
		for(Object o : list){
			Hibernate.initialize(o);
			initialize(o);
		}
	}
	
	public static void initialize(Object o){
		try {
			o = materializeProxy(o);
			initializeLists(o);
		} catch (Exception e) {
			new ExceptionManager(e).treatException();
		}
	}
}
