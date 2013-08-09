package br.com.vexillum.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import br.com.vexillum.model.CommonEntity;

public class EntityUtils {
	
	public static List<Field> getFieldsNotNull(CommonEntity entity) throws Exception{
		Class<?> classEntity = entity.getClass();
		Field[] fields = ReflectionUtils.getFields(classEntity);

		List<Field> listFields = new ArrayList<Field>();		
		
		for (Field field : fields) {
			field.setAccessible(true);
			if(field.get(entity) != null){
				listFields.add(field);
			}
		}
		
		return listFields;
	}
	
}

