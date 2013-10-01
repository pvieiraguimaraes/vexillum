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
import br.com.vexillum.model.annotations.NotInitialize;

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
			initializeObjectElements(o);
			session.evict(o);
		} catch (Exception e) {
			new ExceptionManager(e).treatException();
		}
	}
	
	public static void initializeObjectElements(Object o) throws Exception{
		initializeObjectElements(o, null);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void initializeObjectElements(Object o, List initialized) throws Exception{
		for(Field f : ReflectionUtils.getFields(o.getClass())){
			f.setAccessible(true);
			if(f.getAnnotation(NotInitialize.class) == null){
				Object obj = f.get(o);
				if(obj instanceof ICommonEntity){
					f.set(o, initialize(obj, initialized));
				} else if(obj instanceof PersistentBag || obj instanceof PersistentList){
					initializeListElements((List) obj, initialized);
				}
			}
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void initializeListElements(List list, List initialized){
		if(list.size() > 0){
			for(Integer i = 0; i < list.size(); i++){
				Object o = list.get(i);
				list.set(i, initialize(o, initialized));
			}
		}
	}
	
	@SuppressWarnings("rawtypes")
	public static Object initialize(Object o, List<Object> initialized){
		if(initialized == null) 
			initialized = new ArrayList<Object>();
		if(!initialized.contains(o)){
			try {
				o = materializeProxy(o);
				initialized.add(o);
				if(o instanceof List) {
					initializeListElements((List) o, initialized);
				} else{
					initializeObjectElements(o, initialized);
				}
			} catch (Exception e) {
				new ExceptionManager(e).treatException();
			}
		}
		return o;
	}
	
	public static Object initialize(Object o){
		return initialize(o, null);
	}
	
}
