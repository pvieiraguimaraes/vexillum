package br.com.vexillum.control;

import java.lang.reflect.Field;
import java.util.StringTokenizer;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import br.com.vexillum.model.interfaces.IFilteredSearch;
import br.com.vexillum.util.ReflectionUtils;
import br.com.vexillum.util.Return;

@SuppressWarnings("rawtypes")
@Service
@Scope("prototype")
public class FilteredSearchController extends GenericControl implements IFilteredSearch {

	@SuppressWarnings("unchecked")
	public FilteredSearchController() {
		super(null);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Return searchByFilters() {
		String criterias = (String) data.get("criterias");
		String searchKey = (String) data.get("searchKey");
		
		StringTokenizer token;
		
		String sql = "FROM " + getBindInClass().getSimpleName() + " "
				+ "WHERE "; 
		
		if(criterias != null){
			token = new StringTokenizer(criterias, ",");
			while(token.hasMoreElements()){
				String criteria = token.nextToken();
				sql += criteria + " like '%" + searchKey + "%' OR ";
			}
			sql = sql.substring(0, sql.lastIndexOf(" OR"));
		} else {
			sql += "name like '%" + searchKey + "%'";
		}
		
		data.put("sql", sql);
		
		return searchByHQL();
	}
	
	private Class getBindInClass(){
		String bindIn = (String) data.get("bindIn");
		Class targetClass = null;
		
		for(Field f : ReflectionUtils.getFields(getEntity().getClass())){
			if(f.getName().equals(bindIn)){
				targetClass = f.getType();
			}
		}
		
		if(targetClass == null){
			throw new IllegalArgumentException("BindIn não encontrado no target entity! Possivelmente o nome está incorreto!");
		}
		
		return targetClass;
	}
}
