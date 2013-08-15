package br.com.vexillum.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import br.com.vexillum.model.ICommonEntity;
import br.com.vexillum.model.annotations.SearchField;
import br.com.vexillum.model.annotations.TransientField;

public class ReflectionUtils {

	public static Field[] getFields(Class<?> classEntity) {
		Field[] fields = classEntity.getDeclaredFields();
		Class<?> superclass = classEntity.getSuperclass();
		while (superclass != null) {
			fields = ArrayUtils.addAll(fields, superclass.getDeclaredFields());
			superclass = superclass.getSuperclass();
		}
		return fields;
	}

	public static Method[] getMethods(Class<?> classEntity) {
		Method[] methods = classEntity.getDeclaredMethods();
		Class<?> superclass = classEntity.getSuperclass();
		while (superclass != Object.class) {
			methods = ArrayUtils.addAll(methods,
					superclass.getDeclaredMethods());
			superclass = superclass.getSuperclass();
		}
		return methods;
	}

	public static Map<String, Method> getMethodsMap(Class<?> classEntity) {
		Map<String, Method> mapMethods = new HashMap<String, Method>();
		Method[] methods = classEntity.getDeclaredMethods();
		Class<?> superclass = classEntity.getSuperclass();
		while (superclass != null) {
			methods = ArrayUtils.addAll(methods,
					superclass.getDeclaredMethods());
			superclass = superclass.getSuperclass();
		}
		for (Method method : methods) {
			mapMethods.put(method.getName(), method);
		}
		return mapMethods;
	}

	@SuppressWarnings({ "rawtypes" })
	public static HashMap<String, Object> prepareDataForPersistence(Object comp) {
		HashMap<String, Object> dados = new HashMap<String, Object>();

		Class classe = comp.getClass();

		Method[] metodos = classe.getMethods();
		Field atributos[] = getFields(classe);
		for (Field f : atributos) {
			try {
				if (!f.isAnnotationPresent(TransientField.class)) {
					String nomeMetodo = "get"
							+ StringUtils.capitalize(f.getName());
					for (Method m : metodos) {
						if ((m.getName()).equals(nomeMetodo)) {
							dados.put(f.getName(),
									m.invoke(comp, new Object[] {}));
							// System.out.println(f.getName() + " = " +
							// dados.get(f.getName()));
							break;
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return dados;
	}

	@SuppressWarnings("rawtypes")
	public static Class getGenericType(Object object) {
		return (Class) ((ParameterizedType) object.getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];
	}

	public static List<Field> getSearchFieldsNotNull(ICommonEntity entity)
			throws Exception {
		ArrayList<Field> resultList = new ArrayList<Field>();
		for (Field field : getSearchFields(entity, false)) {
			field.setAccessible(true);
			if (field.get(entity) != null) {
				resultList.add(field);
			}
		}
		return resultList;
	}

	public static List<Field> getSearchFields(ICommonEntity entity, Boolean instropect)
			throws Exception {
		ArrayList<Field> resultList = new ArrayList<Field>();
		for (Field field : ReflectionUtils.getFields(entity.getClass())) {
			if (field.isAnnotationPresent(SearchField.class)) {
				SearchField annotation = field.getAnnotation(SearchField.class);
				if(annotation.introspect() && instropect){
					field.setAccessible(true);
					resultList.addAll(getSearchFields((ICommonEntity) field.get(entity), instropect));
				} else {
					resultList.add(field);
				}
			}
		}
		return resultList;
	}
	
	public static List<Field> getSearchFields(ICommonEntity entity) throws Exception{
		return getSearchFields(entity, false);
	}
	
	public static Map<Field, Object> getSearchFieldsAndValues(ICommonEntity entity, Boolean instropect)
			throws Exception {
		Map<Field, Object> resultList = new HashMap<Field, Object>();
		for (Field field : ReflectionUtils.getFields(entity.getClass())) {
			if (field.isAnnotationPresent(SearchField.class)) {
				SearchField annotation = field.getAnnotation(SearchField.class);
				field.setAccessible(true);
				if(annotation.introspect() && instropect){
					resultList.putAll(getSearchFieldsAndValues((ICommonEntity) field.get(entity),instropect));
				} else {
					resultList.put(field, field.get(entity));
				}
			}
		}
		return resultList;
	}

	public static List<Field> getNonUpdatableFields(ICommonEntity entity)
			throws Exception {
		ArrayList<Field> resultList = new ArrayList<Field>();
		Field[] fields = ReflectionUtils.getFields(entity.getClass());
		for (Field field : fields) {
			if (field.isAnnotationPresent(Column.class)) {
				Column annotation = field.getAnnotation(Column.class);
				if (!annotation.updatable()) {
					resultList.add(field);
				}
			}
		}
		return resultList;
	}

	@SuppressWarnings("rawtypes")
	public static boolean haveMethod(String methodName, Class searchClass) {
		for (Method method : getMethods(searchClass)) {
			if (method.getName().equalsIgnoreCase(methodName)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * M�todo que servir� para pegar o valor no m�todo no anotation
	 * 
	 * @param entity
	 * @param fieldName
	 * @return valor do m�todo
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Object getMethodValue(Object entity, String fieldName)
			throws Exception {
		Class entityClass = entity.getClass();
		Method method = entityClass.getMethod(fieldName);
		Object retorno = method.invoke(entity);
		return retorno;
	}
}
