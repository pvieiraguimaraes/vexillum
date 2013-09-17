package br.com.vexillum.control;

import java.util.HashMap;
import java.util.Map;

import br.com.vexillum.configuration.Properties;
import br.com.vexillum.control.manager.ExceptionManager;
import br.com.vexillum.control.persistence.GenericPersistence;
import br.com.vexillum.control.validator.Validator;
import br.com.vexillum.model.CommonEntity;
import br.com.vexillum.model.CommonEntityActivated;
import br.com.vexillum.model.ICommonEntity;
import br.com.vexillum.model.annotations.ValidatorClass;
import br.com.vexillum.util.HibernateUtils;
import br.com.vexillum.util.Message;
import br.com.vexillum.util.Return;
import br.com.vexillum.util.SpringFactory;

@SuppressWarnings("unchecked")
public class GenericControl<E extends ICommonEntity> implements IGenericControl<E> {
	
	private GenericPersistence<E> persistence;
	protected HashMap<String, Object> data;
	protected Properties messages;
	
	protected E entity;
	protected Class<E> classEntity;
	@SuppressWarnings("rawtypes")
	protected Class thisClass;
	
//	public GenericControl(HashMap<String, Object> data){
//		persistence  = (IGenericPersistence<E>) SpringFactory.getInstance().getBean("genericPersistence", GenericPersistence.class);
//		thisClass = this.getClass();
//		if(data == null){
//			this.data = new HashMap<String, Object>();
//			entity = null;
//		} else {
//			this.data = data;
//			entity = (E) data.get("entity");
//		}
//		classEntity = ReflectionUtils.getGenericType(this);
//	}
	
	public GenericControl(Class<E> classEntity){
		persistence  = SpringFactory.getInstance().getBean("genericPersistence", GenericPersistence.class);
		thisClass = this.getClass();
//		this.classEntity = ReflectionUtils.getGenericType(this);
		this.classEntity = classEntity;
		try {
			messages =  SpringFactory.getInstance().getBean("messagesProperties", Properties.class);
		} catch (Exception e) {
			messages = null;
		}
		
	}
	
	public void initControl(HashMap<String, Object> data){
		setData(data);
		if(data == null){
			this.data = new HashMap<String, Object>();
			entity = null;
		} else {
			this.data = data;
			entity = (E) data.get("entity");
		}
	}
	
	public GenericPersistence<E> getPersistence() {
		return persistence;
	}

	public void setPersistence(GenericPersistence<E> persistence) {
		this.persistence = persistence;
	}

	public HashMap<String, Object> getData() {
		return data;
	}

	public void setData(HashMap<String, Object> data) {
		this.data = data;
	}
	
	public Return doAction(final String action) {
		return doAction(action, true);
	}		
	
	public Return doAction(final String action, Boolean transactionControlled) {
		Return ret = new Return(true);
		try {
			beginTransaction(transactionControlled);
			data.put("action", action);
//			Method m = rclass.getMethod(action, new Class[]{});
			ret.concat(validateEntity(action));
			if(ret.isValid()){
				ret.concat((Return) (thisClass.getMethod(action, new Class[]{})).invoke(this, new Object[]{}));
			} 
			if(ret.isValid()){
				commitTransaction(transactionControlled);
				ret.addMessage(getActionMessage(ret, action));
			} else {
				rollbackTransaction(transactionControlled);
			}
		} catch (Exception e) {
			ret.concat(new ExceptionManager(e).treatException());
			rollbackTransaction(transactionControlled);
		} 
		return ret;
	}		
		
	@SuppressWarnings("rawtypes")
	private Return validateEntity(String action) {
		Return ret = new Return(true);
		if(entity != null){
			try {
				Class validatorClass = getValidatorClass();
				Validator validator = (Validator) validatorClass.getConstructor(new Class[]{Map.class}).newInstance(data);
				ret.concat(validator.validate());
			} catch (Exception e) {
				ret = new ExceptionManager(e).treatException();
			}
		}
		return ret;
	}


	@SuppressWarnings("rawtypes")
	private Class getValidatorClass() throws ClassNotFoundException{
		ValidatorClass an = entity.getClass().getAnnotation(ValidatorClass.class);
		Class superc = entity.getClass().getSuperclass();
		
		while(an == null && superc != null){
			an = (ValidatorClass) superc.getAnnotation(ValidatorClass.class);
			superc = superc.getSuperclass();
		}
		
		if(an != null){
			return Class.forName(an.validatorClass());
		}
		return Validator.class;
	}
	
	protected Message getActionMessage(Return ret, String action){
		String key = (classEntity.getSimpleName() + "_" + action + "_" + ret.isValid()).toLowerCase();
		String message = messages.getKey(key);
		if(message.isEmpty()){
			key = (classEntity.getSimpleName() + "_" + action + "_" + ret.isValid()).toLowerCase();
			message = messages.getKey(key);
			if(message.isEmpty()){
				key = (action + "_" + ret.isValid()).toLowerCase();
				message = messages.getKey(key);
			}
		}
		return new Message(null, message);
		
		
	}
	
	public Return save(){
		 return persistence.save(entity);
	}
	
	public Return delete(){
		return persistence.delete(entity);
	}

	public Return update(){
		return persistence.update(entity);
	}
	
	public Return deactivate(){
		((CommonEntityActivated)entity).setActive(false);
		return persistence.update(entity);
	}
	
	public Return activate(){
		((CommonEntityActivated)entity).setActive(false);
		return persistence.update(entity);
	}

	public Return searchByCriteria(){
		return persistence.searchByCriteria(entity);
	}
	
	public Return executeByHQL(){
		return persistence.executeByHQL((String) data.get("sql"));
	}
	
	public Return searchByHQL(){
		if(data.get("sql") != null){
			return persistence.searchByHQL((String) data.get("sql"));
		} else {
			return persistence.searchByHQL(entity);
		}
		
	}
	
	//TODO Arrumar uma forma de detectar se é a pesquisa por entidade ou pela hql
	/*public Return searchByHQL(){
		return persistence.searchByHQL((String) data.get("hql"));
	}*/
	
	public Return searchByNativeSQL(){
		return persistence.searchByNativeSQL((String) data.get("sql"));
	}

	public Return listAll(){
		return persistence.listAll((Class<CommonEntity>) classEntity);
	}
	
	private void beginTransaction(Boolean transactionControlled){
		if(transactionControlled){
			persistence.beginTransaction();
		}
	}
	
	private void commitTransaction(Boolean transactionControlled){
		if(transactionControlled){
			persistence.commitTransaction();
		}
	}
	
	private void rollbackTransaction(Boolean transactionControlled){
		
		if(transactionControlled){
			persistence.rollbackTransaction();
		}
	}
	
	public void detachObject(Object o){
		HibernateUtils.detachObject(o, persistence.getSession());
	}
	
}
