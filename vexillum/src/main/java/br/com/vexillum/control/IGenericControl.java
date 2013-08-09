package br.com.vexillum.control;

import br.com.vexillum.util.Return;

public interface IGenericControl<E> {
	public Return save();

	public Return delete();

	public Return update();

	public Return searchByCriteria();

	public Return searchByHQL();

	// TODO Arrumar uma forma de detectar se a pesquisa por entidade ou pela
	// hql
	/*
	 * public Return searchByHQL(){ return persistence.searchByHQL((String)
	 * data.get("hql")); }
	 */

	public Return searchByNativeSQL();

	public Return listAll();
	
	public Return executeByHQL();
}
