package br.com.vexillum.control.persistence;

import br.com.vexillum.util.Return;


public interface IGenericPersistence<E> {
	
	public Return save(E entidade);
	
	public Return delete(E entidade);

	public Return update(E entidade);

	public Return searchByCriteria(E entidade);
	
	public Return searchByHQL(E entidade);
	
	public Return searchByHQL(E entidade, String complement);
	
	public Return searchByHQL(String hql);
	
	public Return searchByNativeSQL(String sql);
	
	public Return executeByHQL(String hql);

	@SuppressWarnings("rawtypes")
	public Return listAll(Class classEntity);
	
}
