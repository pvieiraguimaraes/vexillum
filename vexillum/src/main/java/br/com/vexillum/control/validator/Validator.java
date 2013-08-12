package br.com.vexillum.control.validator;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.util.StringUtils;

import br.com.vexillum.configuration.Properties;
import br.com.vexillum.control.manager.ExceptionManager;
import br.com.vexillum.model.CommonEntity;
import br.com.vexillum.model.annotations.Validate;
import br.com.vexillum.util.Message;
import br.com.vexillum.util.ReflectionUtils;
import br.com.vexillum.util.Return;
import br.com.vexillum.util.SpringFactory;

public class Validator {

	protected Map<String, Object> mapData;
	protected CommonEntity entity;
	protected Class<?> classEntity;
	protected String action;
	
	@SuppressWarnings("rawtypes")
	private Class thisClass;
	
	protected Properties messages;

	public Validator(Map<String, Object> mapData) {
		this.mapData = mapData;
		this.entity = (CommonEntity) mapData.get("entity");
		this.classEntity = this.entity.getClass();
		this.action = (String) mapData.get("action");
		this.thisClass = this.getClass();
		try {
			messages =  SpringFactory.getInstance().getBean("messagesProperties", Properties.class);
		} catch (Exception e) {
			messages = null;
		}
	}

	@SuppressWarnings("unchecked")
	public Return validate() {
		Return ret = new Return(true);
		try {
			String methodName = "validate" + StringUtils.capitalize(action);
			if(ReflectionUtils.haveMethod(methodName, thisClass)){
				Method m = thisClass.getMethod(methodName, new Class[]{ });
				ret.concat((Return) m.invoke(this, new Object[] { }));
			}
		} catch (Exception e) {
			ret = new ExceptionManager(e).treatException();
		}
 		return ret;
	}
	
	protected Return validateModel(){
		Return ret = new Return(true);
		try {
			Field[] campos = ReflectionUtils.getFields(entity.getClass());
			for (Field f : campos) {
				ret.concat(validateAttribute(f));
			}
			return ret;
		} catch (Exception e) {
			ret = new ExceptionManager(e).treatException();
		} 
		return ret;
	}
	
	protected Return validateAttribute(String name){
		Field[] campos = ReflectionUtils.getFields(entity.getClass());
		Return ret = new Return(true);
		try {
			Boolean flag = false;
			for (Field f : campos) {
				if(f.getName().equalsIgnoreCase(name)){
					ret.concat(validateAttribute(f));
					flag = true;
					break;
				}
			}
			if(flag) throw new NoSuchFieldException();
		} catch (Exception e) {
			ret = new ExceptionManager(e).treatException();
		}
		return ret;
	}
	
	protected Return validateAttribute(String fieldName, String validation){
		Field[] campos = ReflectionUtils.getFields(entity.getClass());
		Return ret = new Return(true);
		try {
			Boolean flag = false;
			for (Field f : campos) {
				if(f.getName().equalsIgnoreCase(fieldName)){
					ret.concat(validateAttribute(f, validation));
					flag = true;
					break;
				}
			}
			if(flag) throw new NoSuchFieldException();
		} catch (Exception e) {
			ret = new ExceptionManager(e).treatException();
		}
		return ret;
	}
	
	@SuppressWarnings("unchecked")
	protected Return validateAttribute(Field field){
		Return ret = new Return(true);
		try {
			field.setAccessible(true);
			if (field.isAnnotationPresent(Validate.class)) {
				Validate annot = field.getAnnotation(Validate.class);
				for (Object method : annot.getClass().getDeclaredMethods()) {
					if(ReflectionUtils.haveMethod(((Method)method).getName(), thisClass)){
						Method m = thisClass.getMethod(((Method)method).getName(), new Class[]{ String.class, Object.class, Object.class});
						Object obj = ReflectionUtils.getMethodValue(annot, ((Method)method).getName());
						ret.concat((Return) m.invoke(this, new Object[] { field.getName(), field.get(entity), obj}));
					}
				}
			}
		} catch (Exception e) {
			ret = new ExceptionManager(e).treatException();
		}
		return ret;
	}
	
	@SuppressWarnings("unchecked")
	protected Return validateAttribute(Field field, String validation){
		Return ret = new Return(true);
		try {
			field.setAccessible(true);
			if (field.isAnnotationPresent(Validate.class)) {
				Validate annot = field.getAnnotation(Validate.class);
				for (Object method : annot.getClass().getDeclaredMethods()) {
					if(((String)method).equalsIgnoreCase(validation)){
						if(ReflectionUtils.haveMethod(((Method)method).getName(), thisClass)){
							Method m = thisClass.getMethod(((Method)method).getName(), new Class[]{ String.class, Object.class, Object.class});
							Object obj = ReflectionUtils.getMethodValue(annot, ((Method)method).getName());
							ret.concat((Return) m.invoke(this, new Object[] { field.getName(), field.get(entity), obj}));
							if(!ret.isValid()) continue;
						}
					}
				}
			}
		} catch (Exception e) {
			ret = new ExceptionManager(e).treatException();
		}
		return ret;
	}
	
	//Métodos de validação das ações genéricas
	public Return validateSave(){
		return validateModel();
	}
	
	public Return validateUpdate(){
		return validateModel();
	}

	//INICIO METODOS DAS VALIDA��ES
	public Return notNull(String name, Object valField, Object valAnoted){
		Return ret = new Return(true);		
		if((Boolean) valAnoted){
			if(valField == null || valField.toString().isEmpty())
				ret = creatReturn(name, getValidationMessage(name, "notNull", false));
		}
		return ret;
	}
	
	public Return min(String name, Object valField, Object valAnoted){
		Return ret = new Return(true);
		
		if(isNotNulle(valField) && isNotNulle(valAnoted) && (Integer)valAnoted > 0){
			Integer v1 = valField.toString().length();
			if((v1 < (Integer)valAnoted))
				ret = creatReturn(name, getValidationMessage(name, "min", false) + (Integer)valAnoted);
		}
		return ret;
	}
	
	public Return max(String name, Object valField, Object valAnoted){
		Return ret = new Return(true);
		if(isNotNulle(valField) && isNotNulle(valAnoted) && (Integer)valAnoted > 0){
			Integer v1 = valField.toString().length();
		if(v1 > (Integer)valAnoted)
			ret = creatReturn(name, getValidationMessage(name, "max", false) + (Integer)valAnoted);
		}
		return ret;
	}
	
	public Return past(String name, Object valField, Object valAnoted){
		Return ret = new Return(true);		
		if((Boolean) valAnoted){
			if(DateUtils.round(valField, 0).before(new Date()))
				ret = creatReturn(name, getValidationMessage(name, "past", false));
		} 
		return ret;
	}
	
	public Return future(String name, Object valField, Object valAnoted){
		Return ret = new Return(true);		
		if((Boolean) valAnoted){
			if(valField == null || DateUtils.round(valField, 0).after(new Date()))
				ret = creatReturn(name, getValidationMessage(name, "future", false));
		} 
		return ret;
	}
	
	public Return email(String name, Object valField, Object valAnoted){
		Return ret = new Return(true);
		String regex = "^([0-9a-zA-Z]+([_.-]?[0-9a-zA-Z]+)*@[0-9a-zA-Z]+[0-9,a-z,A-Z,.,-]*(.){1}[a-zA-Z]{2,4})+$";
		if((Boolean) valAnoted){
			if(valField == null || !Pattern.matches(regex, (String) valField))
				ret = creatReturn(name, getValidationMessage(name, "email", false));
		} 
		return ret;
	}
	
	
	public Return equalsFields(String field1, String field2){
		Return ret = new Return(true);	
		if(field1 != null){
			if(!field1.equals(field2)){
				ret.concat(new Return(false));
			}
		}
		return ret;
	}
	
	//FIM METODOS DAS VALIDAÇÕES
	
	public Boolean isNotNulle(Object obj){
		if(obj == null || obj == "")
			return false;
		return true;
	}
	
	public String getValidationMessage(String fieldName, String action, Boolean validate){
		String key = (classEntity.getSimpleName() + "_" + fieldName + "_" + action + "_" + validate).toLowerCase();
		String message = messages.getKey(key);
		if(message.isEmpty()){
			key = (classEntity.getSimpleName() + "_" + action + "_" + validate).toLowerCase();
			message = messages.getKey(key);
			if(message.isEmpty()){
				key = (action + "_" + validate).toLowerCase();
				message = messages.getKey(key);
			}
		}
		return message;
	}
	
	public Return creatReturn(String name, String msg){
		return new Return(false, new Message(name, msg));
	}
}
