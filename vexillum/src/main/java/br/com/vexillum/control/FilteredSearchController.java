package br.com.vexillum.control;

import java.util.StringTokenizer;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import br.com.vexillum.model.interfaces.IFilteredSearch;
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
		
		String sql = "FROM " + getEntity().getClass().getSimpleName() + " "
				+ "WHERE "; 
		
		if(criterias != null){
			token = new StringTokenizer(criterias, ",");
			while(token.hasMoreElements()){
				String criteria = token.nextToken();
				sql += criteria + " like '%" + searchKey + "%', ";
			}
			sql = sql.substring(0, sql.lastIndexOf(","));
		} else {
			sql += "name like '%" + searchKey + "%'";
		}
		
		data.put("sql", sql);
		
		return searchByHQL();
	}
	
}
