package br.com.vexillum.control.persistence;

import br.com.vexillum.util.Return;


public interface IGenericPersistence<E> {
	
	/**
	 * Salva uma entidade.
	 * @param entidade Entidade a ser salva
	 * @return {@link Return}
	 */
	public Return save(E entidade);
	
	/**
	 * Deleta uma entidade.
	 * @param entidade Entidade a ser deletada
	 * @return {@link Return}
	 */
	public Return delete(E entidade);

	/**
	 * Atualiza uma entidade.
	 * @param entidade Entidade a ser atualizada
	 * @return {@link Return}
	 */
	public Return update(E entidade);

	/**
	 * Pesquisa as entidades baseadas em criterios, que são montados a partir dos atributo da entidade.
	 * @param entidade Entidade a tomada como referência.
	 * @return {@link Return}
	 */
	public Return searchByCriteria(E entidade);
	
	/**
	 * Pesquisa as entidades baseadas em criterios, em formato de HQL, que são montados a partir dos atributo da entidade.
	 * @param entidade Entidade a tomada como referência.
	 * @return {@link Return}
	 */
	public Return searchByHQL(E entidade);
	
	/**
	 * Pesquisa as entidades baseadas em criterios, em formato de HQL, que são montados a partir dos atributo da entidade.
	 * Pode possuir um complemente a HQL montada.
	 * 
	 * @param entidade Entidade a tomada como referência.
	 * @return {@link Return}
	 */
	public Return searchByHQL(E entidade, String complement);
	
	/**
	 * Pesquisa as entidades baseadas em uma HQL.
	 * 
	 * @param entidade HQL a ser executada.
	 * @return {@link Return}
	 */
	public Return searchByHQL(String hql);
	
	/**
	 * Pesquisa as entidades baseadas em uma SQL Nativa.
	 * 
	 * @param entidade SQL nativa a ser executada.
	 * @return {@link Return}
	 */
	public Return searchByNativeSQL(String sql);
	
	/**
	 * Executa uma HQL que não seja de pesquisa.
	 * 
	 * @param entidade SQL nativa a ser executada.
	 * @return {@link Return}
	 */
	public Return executeByHQL(String hql);

	/**
	 * Executa todas a entidades de uma determinada classe.
	 * 
	 * @param entidade Tipo das classes.
	 * @return {@link Return}
	 */
	@SuppressWarnings("rawtypes")
	public Return listAll(Class classEntity);
	
}
