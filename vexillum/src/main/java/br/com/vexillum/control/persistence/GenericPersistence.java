package br.com.vexillum.control.persistence;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Restrictions;

import br.com.vexillum.control.manager.ExceptionManager;
import br.com.vexillum.model.CommonEntity;
import br.com.vexillum.model.ICommonEntity;
import br.com.vexillum.util.ReflectionUtils;
import br.com.vexillum.util.Return;

/**
 * Classe com as ações de persistência uasadas em todas as ações do sistema.
 *
 * @param <E> Entidade que herde de {@link ICommonEntity}
 */
public class GenericPersistence<E extends ICommonEntity> implements IGenericPersistence<ICommonEntity> {

	/**
	 * {@link SessionFactory} do hibernate
	 */
	private SessionFactory sessionFactory;
	
	/**
	 * Sessão do hibernate
	 */
	Session session;
	
	/**
	 * Transação usada nas ações.
	 */
	Transaction tx;
	
	public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
	}
	
	public Session getSession(){
		if(session == null || !session.isOpen()){
			session = sessionFactory.openSession();
			session.setFlushMode(FlushMode.MANUAL);
		}
		return session;
	}
	
	public Transaction getTransaction(){
		return getSession().getTransaction();
	}
	
	/**
	 * Inicia uma nova {@link Transaction} caso não esteja ativa.
	 */
	public void beginTransaction(){
		if(tx== null || !getTransaction().isActive()){
			tx = getSession().beginTransaction();
		}		
	}
	
	/**
	 * Realiza o commit em uma {@link Transaction}, caso ainda tenha sido feito
	 */
	public void commitTransaction(){
		if(!getTransaction().wasCommitted()){
			getTransaction().commit();
		}
		flushSession();
	}
	
	/**
	 * Realiza o rollback em uma transação aberta, caso ainda não tenha sido feito
	 */
	public void rollbackTransaction(){
		if(getTransaction() != null && !getTransaction().wasRolledBack()){
			try {
				getTransaction().rollback();
			} catch (Exception e) {
				getSession().clear();
			}
			
		}
		flushSession();
	}
	
	/**
	 * Limpa a sessão aberta.
	 */
	public void flushSession(){
		getSession().flush();
	}
	
	/**
	 * Fecha a sessão aberta.
	 */
	@SuppressWarnings("unused")
	private void closeSession(){
		if(getSession().isOpen()){
			getSession().close();
		}
	}
	
	/* (non-Javadoc)
	 * @see br.com.vexillum.control.persistence.IGenericPersistence#save(java.lang.Object)
	 */
	@Override
	public Return save(ICommonEntity entity) {
		Return ret = null;
		try {
//			beginTransaction();
			ret = new Return(getSession().save(getSession().merge(entity)));
//			commitTransaction();
		} catch (Exception e) {
//			rollbacktTransaction();
			ret = new ExceptionManager(e).treatException();
		}		
		return ret;
	}

	/* (non-Javadoc)
	 * @see br.com.vexillum.control.persistence.IGenericPersistence#searchByCriteria(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Return searchByCriteria(ICommonEntity entity) {
		Return ret = new Return(true);
		List<CommonEntity> lista = new ArrayList<CommonEntity>();
        Criteria crit = getSession().createCriteria(entity.getClass());   
        
        try{
	        List<Field> listFields = ReflectionUtils.getSearchFieldsNotNull(entity);
	        
	        Criterion[] cond = new Criterion[listFields.size()];
	
	        for(int i = 0; i < listFields.size() ; i++){
	        	Field field = listFields.get(i);
        		if(!(field.get(entity) instanceof Long || field.get(entity) instanceof Integer || field.get(entity) instanceof CommonEntity || field.get(entity) instanceof Boolean)){
	        		cond[i] = Restrictions.like(field.getName() , "%" + field.get(entity) + "%");
	        	} else {
	        		cond[i] = Restrictions.eq(field.getName() , field.get(entity));
	        	}
	        }
	        
	        LogicalExpression expr = null;
	        
	        if(cond.length == 0) return ret;
	        
	        if(cond.length > 1){
	            expr = null;
	            for(int i = 1; i < cond.length; i++){
	                if(expr == null){
	                        expr = Restrictions.and(cond[0], cond[1]);
	                } else {
	                        expr = Restrictions.and(expr, cond[i]);
	                }
	            }
	            crit.add(expr);
	        } else {
	            crit.add(cond[0]);
	        }
	        
	        lista.addAll(crit.list());   
	        ret.concat(new Return(true, lista));
        } catch (Exception e){        	
        	ret = new ExceptionManager(e).treatException();
        }
        return ret;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Return listAll(Class classEntity) {
		Return ret = null;
		List<?> list = null;
		try {
			list = getSession().createCriteria(classEntity).list();
			ret = new Return(true, list);
		} catch (Exception e) {
			ret = new ExceptionManager(e).treatException();
		}
		return ret;
	}	

	@Override
	public Return delete(ICommonEntity entity) {
		Return ret = null;
		try {
//			beginTransaction();		
			getSession().delete(entity);
//			commitTransaction();
			ret = new Return(true);
		} catch (Exception e) {
//			rollbacktTransaction();
			ret = new ExceptionManager(e).treatException();
		}
		return ret;
	}

	@Override
	public Return update(ICommonEntity entity) {
		Return ret = null;
		try {
//			beginTransaction();
			getSession().update(getSession().merge(entity));
//			commitTransaction();
			ret = new Return(true);
		} catch (Exception e) {
//			rollbackTransaction();
        	ret = new ExceptionManager(e).treatException();
		}	
		return ret;
	}
	
	@Override
	public Return searchByHQL(ICommonEntity entity) {
		return searchByHQL(entity, null);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Return searchByHQL(ICommonEntity entity, String complement) {
		Return ret = new Return(true);
		List<E> lista = new ArrayList<E>();
        
        try{
	        String hql = "FROM " + entity.getClass().getSimpleName() + " WHERE ";
	        
	        List<String> criterias = setCriterias(entity, null);
	
	        if(criterias.size() == 0) return ret;
	        
	        String groupedCriterias = null;
	        
	        if(criterias.size() > 1){
	            for(int i = 1; i < criterias.size(); i++){
	                if(groupedCriterias == null){
	                	groupedCriterias = criterias.get(0) + " AND " + criterias.get(1);
	                } else {
	                	groupedCriterias += " AND " + criterias.get(i);
	                }
	            }
	            hql = hql.concat(groupedCriterias);
	        } else {
	        	hql = hql.concat(criterias.get(0));
	        }
	        
	        if(complement != null && !complement.isEmpty()){
	        	hql += " " + complement;
	        }
	        
	        lista.addAll(getSession().createQuery(hql).list());   
	        ret.concat(new Return(true, lista));
        } catch (Exception e){        	
        	ret = new ExceptionManager(e).treatException();
        }
        return ret;
	}
	
	/**
	 * Transforma as atributos preeenchidos de uma entidade em condições, via HQL.
	 * @param entity Entidade a ser tomada como referêcnia
	 * @param prefix Prefixo das condições
	 * @return
	 */
	private List<String> setCriterias(ICommonEntity entity, String prefix){
		List<String> criterias = new ArrayList<String>();
		 try {
			List<Field> listFields = ReflectionUtils.getSearchFieldsNotNull(entity);
			
			for (Field field : listFields) {
				Object value = field.get(entity);
				if(value instanceof ICommonEntity){
	        		ICommonEntity searchEntity = (ICommonEntity) value;
	        		if(searchEntity.getId() == null){
	        			criterias.addAll(setCriterias(searchEntity, field.getName()));
	        		} else {
	        			criterias.add(field.getName() + "=" + searchEntity.getId());
	        		}
	        	} else if(!(value instanceof Long || value instanceof Integer || value instanceof Boolean || value instanceof Enum || value instanceof Date)){
	        		criterias.add(field.getName() + " like " + "'%" + value + "%'");
	        	} else if(value instanceof Enum){
	        		criterias.add(field.getName() + "=" + "'" + ((Enum<?>)value).ordinal() + "'");
	        	} else if(value instanceof Boolean){
	        		criterias.add(field.getName() + "=" + value);
	        	}else {
	        		criterias.add(field.getName() + "=" + "'" + value + "'");
	        	}
			}
			addPrefixOnList(prefix, criterias);
		} catch (Exception e) {
			new ExceptionManager(e).treatException();
		}
		return criterias;
	}
	
	/**
	 * Adiciona o prefixo as consições.
	 * @param prefix Prefixo
	 * @param list Lista de condições
	 */
	private void addPrefixOnList(String prefix, List<String> list){
		if(prefix != null){
			for (int i = 0; i < list.size(); i++) {
				String cond = prefix.concat(".".concat(list.get(i)));
				list.set(i, cond);
			}
		}
	}

	@Override
	public Return searchByHQL(String hql) {
		Return ret = null;
		try {
			ret = new Return(true, getSession().createQuery(hql).list());
		} catch (Exception e) {
			ret = new ExceptionManager(e).treatException();
		}
		return ret;
	}

	@Override
	public Return searchByNativeSQL(String sql) {
		Return ret = new Return(true);
		
		ret.setList(getSession().createSQLQuery(sql).list());
		
		return ret;
	}

	@Override
	public Return executeByHQL(String hql) {
		Return ret = null;
		try {
			getSession().createQuery(hql).executeUpdate();
			ret = new Return(true);
		} catch (Exception e) {
			ret = new ExceptionManager(e).treatException();
		}
		return ret;
	}
	
	/**
	 * Busca uam entidade baseada no ID.
	 * @param id ID a ser pesquisado.
	 * @param classz Classe que será pesquisada.
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public E getById(Long id, Class<?> classz){
		try {
			return (E) getSession().get(classz, id);
		} catch (Exception e) {
			new ExceptionManager(e).treatException();
		}
		return null;
	}

}
